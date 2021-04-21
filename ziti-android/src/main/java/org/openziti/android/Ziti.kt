/*
 * Copyright (c) 2018-2021 NetFoundry, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openziti.android

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.v4.content.FileProvider
import android.support.v4.content.LocalBroadcastManager
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.openziti.Ziti
import org.openziti.ZitiContext
import org.openziti.api.MFAType
import org.openziti.net.dns.DNSResolver
import org.openziti.util.Logged
import org.openziti.util.Version
import org.openziti.util.ZitiLog
import java.net.URI
import java.security.KeyStore
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionStage
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import kotlin.coroutines.CoroutineContext

/**
 *
 */
@SuppressLint("StaticFieldLeak")
object Ziti: CoroutineScope, Logged by ZitiLog() {
    const val IDENTITY_ADDED = "ziti.identity.added"
    const val IDENTITY_REMOVED = "ziti.identity.removed"
    const val IDENTITY_MFA = "ziti.identity.mfa"

    private val supervisor = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + supervisor

    internal val Impl = org.openziti.Ziti
    private var enrollmentClass: Class<out Activity> = EnrollmentActivity::class.java

    const val Ziti = "Ziti"
    const val ZitiNotificationChannel = "Ziti"

    lateinit var app: Context

    lateinit var zitiPref: SharedPreferences
    lateinit var keyStore: KeyStore
    var currentActivity: Activity? = null

    object MFAHandler: Ziti.AuthHandler {
        override fun getCode(
            ztx: ZitiContext,
            mfaType: MFAType,
            provider: String
        ): CompletionStage<String> {
            return CompletableFuture.completedFuture("TODO")
        }
    }

    @JvmStatic
    fun getDnsResolver(): DNSResolver = Impl.getDNSResolver()

    @JvmStatic
    fun init(app: Context, seamless: Boolean = true) {
        this.app = app

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            app.getSystemService(NotificationManager::class.java)?.createNotificationChannel(
                NotificationChannel(ZitiNotificationChannel, Ziti, NotificationManager.IMPORTANCE_HIGH)
            )
        }

        if (app is Application) {

            app.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks{
                override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
                override fun onActivityStarted(activity: Activity) {}
                override fun onActivityResumed(activity: Activity) {
                    currentActivity = activity
                }

                override fun onActivityPaused(activity: Activity) {
                    if (activity == currentActivity) currentActivity = null
                }

                override fun onActivityStopped(activity: Activity) {
                    if (activity == currentActivity) currentActivity = null
                }

                override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
                override fun onActivityDestroyed(activity: Activity) {}
            })
        }


        val appId = app.packageName
        val appVer = app.packageManager.getPackageInfo(appId, 0).versionName
        org.openziti.Ziti.setApplicationInfo(appId, appVer)

        keyStore = KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null)

        zitiPref = app.getSharedPreferences("ziti", Context.MODE_PRIVATE)
        val ctxList = Impl.init(keyStore, seamless)

        for (c in ctxList) {
            val enabled = zitiPref.getBoolean("${c.name()}.enabled", true)
            c.setEnabled(enabled)
            launch {
                c.statusUpdates().collect {
                    val on = it != ZitiContext.Status.Disabled
                    zitiPref.edit()
                        .putBoolean("${c.name()}.enabled", on)
                        .apply()
                }
            }
        }

        if (ctxList.isEmpty()) {
            val builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Notification.Builder(app, ZitiNotificationChannel)
            } else {
                Notification.Builder(app)
            }

            val notification = builder
                .setSmallIcon(android.R.drawable.stat_sys_warning)
                .setContentTitle("Ziti Status")
                .setPriority(Notification.PRIORITY_HIGH)
                .setContentText("Application is not enrolled with Ziti Network")
                .setContentIntent(
                    PendingIntent.getActivity(
                    app, 0x7171, getEnrollmentIntent(), PendingIntent.FLAG_CANCEL_CURRENT))
                .build()

            app.getSystemService(NotificationManager::class.java)?.apply {
                notify(null, 0x2171, notification)
            }
        }
    }

    fun deleteIdentity(ctx: ZitiContext) {
        Impl.removeContext(ctx)
        LocalBroadcastManager.getInstance(app).sendBroadcast(
            Intent(IDENTITY_REMOVED).putExtra("id", ctx.name()))

        val ctrl = URI.create(ctx.controller())
        val idAlias = "ziti://${ctrl.host}:${ctrl.port}/${ctx.name()}"
        for(a in keyStore.aliases()) {

            if (keyStore.isKeyEntry(a)) {
                if (a == idAlias) {
                    i("removing key entry $a")
                    keyStore.deleteEntry(a)
                }
            } else if (keyStore.isCertificateEntry(a)) {
                if (a.startsWith("ziti:${ctx.name()}")) {
                    i("removing certificate entry $a")
                    keyStore.deleteEntry(a)
                }
            }
        }
    }

    fun enrollZiti(jwtUri: Uri) {
        val jwt = app.contentResolver.openInputStream(jwtUri)!!.readBytes()
        enrollZiti(jwt)
    }

    fun enrollZiti(jwt: ByteArray) {
        val name = "ziti-sdk"

        Thread {
            try {
                val ctx = Impl.enroll(keyStore, jwt, name)
                showResult("Enrollment Success!!", null)
                LocalBroadcastManager.getInstance(app).sendBroadcast(
                    Intent(IDENTITY_ADDED).putExtra("id", ctx.name()))
            } catch (ex: Exception) {
                e("failed to enroll", ex)
                showResult("Enrollment Failed", ex)
            }
        }.start()
    }

    fun showResult(str: String, ex: Exception?) {
        Handler(app.mainLooper).post {
            if (ex != null) {
                Toast.makeText(app, "${str} : ${ex.localizedMessage}", LENGTH_LONG).show()
            } else {
                Toast.makeText(app, str, LENGTH_LONG).show()
                app.getSystemService(NotificationManager::class.java)?.cancel(null, 0x2171)
            }
        }
    }

    fun sendFeedbackIntent() = Intent(Intent.ACTION_SEND).apply {
        type = "application/zip"
        putExtra(Intent.EXTRA_EMAIL, arrayOf("support@netfoundry.io"))
        putExtra(Intent.EXTRA_SUBJECT, app.getString(R.string.supportEmailSubject))

        val identities = Impl.getContexts()
        val ids = if (identities.isNullOrEmpty()) {
            "no enrollments"
        } else {
            identities.joinToString(separator = "\n") {
                "\t${it} - ${it.getStatus()}"
            }
        }

        val bodyString = """
                    |Device:          ${Build.MODEL} (${Build.MANUFACTURER})
                    |Android Version: ${Build.VERSION.RELEASE}
                    |Android-SDK:     ${Build.VERSION.SDK_INT}
                    |Ziti Version:    ${BuildConfig.ZITI_VERSION}(${Version.revision})
                    |App:             ${app.packageName}
                    |App Version:     ${app.packageManager.getPackageInfo(app.packageName, 0).versionName}
                    |
                    |Enrollments:
                    |${ids}
                    |""".trimMargin()

        putExtra(Intent.EXTRA_TEXT, bodyString)

        val logDir = app.externalCacheDir!!.resolve("logs")
        logDir.mkdirs()
        val log = Runtime.getRuntime().exec("logcat -d -b crash,main").inputStream.bufferedReader().readText()

        val logFile = logDir.resolve("log.zip")
        val zip = ZipOutputStream(logFile.outputStream())
        zip.putNextEntry(ZipEntry("ziti.log"))
        zip.writer().write(log)
        zip.finish()
        zip.flush()
        zip.close()

        putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(app,
            "${app.packageName}.provider", logFile))
    }

    @JvmStatic
    fun getEnrollmentIntent() = Intent(app, enrollmentClass)

    @JvmStatic
    fun setEnrollmentActivity(cls: Class<out Activity>) {
        enrollmentClass = cls
    }

    @JvmStatic
    fun getSocketFactory() = Impl.getSocketFactory()
    @JvmStatic
    fun getSSLSocketFactory() = Impl.getSSLSocketFactory()
}
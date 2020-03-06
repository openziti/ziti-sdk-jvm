package io.netfoundry.ziti.android

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.util.Log
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import java.security.KeyStore

/**
 *
 */
object Ziti {

    val Impl = io.netfoundry.ziti.Ziti

    const val Ziti = "Ziti"
    const val ZitiNotificationChannel = "Ziti"

    lateinit var app: Context
    lateinit var keyStore: KeyStore

    @JvmStatic
    fun init(app: Context, seamless: Boolean = true) {
        this.app = app

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            app.getSystemService(NotificationManager::class.java)?.createNotificationChannel(
                NotificationChannel(ZitiNotificationChannel, Ziti, NotificationManager.IMPORTANCE_HIGH)
            )
        }

        keyStore = KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null)

        val ctxList = Impl.init(keyStore, seamless)
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
                    app, 0x7171, Intent(app, EnrollmentActivity::class.java), PendingIntent.FLAG_CANCEL_CURRENT))
                .build()

            app.getSystemService(NotificationManager::class.java)?.apply {
                notify(null, 0x2171, notification)
            }
        }
    }

    fun enrollZiti(jwtUri: Uri) {
        val jwt = app.contentResolver.openInputStream(jwtUri)!!.readBytes()
        val name = "ziti-sdk"

        Thread {
            try {
                Impl.enroll(keyStore, jwt, name)
                showResult("Enrollment Success!!", null)
            } catch (ex: Exception) {
                Log.w("sample", "exception", ex)
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
}
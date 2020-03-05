package io.netfoundry.ziti.android

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
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
                NotificationChannel(Ziti, "ZITI", NotificationManager.IMPORTANCE_HIGH)
            )
        }

        keyStore = KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null)

        val ctxList = Impl.init(keyStore, seamless)
        if (ctxList.isEmpty()) {
            val notification = NotificationCompat.Builder(app, ZitiNotificationChannel)
                .setSmallIcon(android.R.drawable.stat_sys_warning)
                .setContentTitle("Ziti Status")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentText("Application is not enrolled with Ziti Network")
                .setContentIntent(
                    PendingIntent.getActivity(
                    app, 0x7171, Intent(app, EnrollmentActivity::class.java), PendingIntent.FLAG_CANCEL_CURRENT))
                .build()

            NotificationManagerCompat.from(app).notify(null, 0x2171, notification)
        }
    }

    fun enrollZiti(jwtUri: Uri) {
        val jwt = app.contentResolver.openInputStream(jwtUri)!!.readBytes()
        val name = "Galaxy S8+"

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
                NotificationManagerCompat.from(app).cancel(null, 0x2171)
            }
        }
    }
}
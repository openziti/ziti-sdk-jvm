package io.netfoundry.ziti.android

import android.app.Activity
import android.content.Intent

class EnrollmentActivity : Activity() {

    val SELECT_ENROLLMENT_JWT = 0x1234

    override fun onStart() {
        super.onStart()

        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "*/*"
        }
        startActivityForResult(intent, SELECT_ENROLLMENT_JWT)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            SELECT_ENROLLMENT_JWT -> {
                if (resultCode == Activity.RESULT_OK) {
                    data?.data?.let {
                        Ziti.enrollZiti(it)
                    }
                }
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }

        this.finish()
    }
}

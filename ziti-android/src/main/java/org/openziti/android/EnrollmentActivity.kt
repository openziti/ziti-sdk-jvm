/*
 * Copyright (c) 2018-2020 NetFoundry, Inc.
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

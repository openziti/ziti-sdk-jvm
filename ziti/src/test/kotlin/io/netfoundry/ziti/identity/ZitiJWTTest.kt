/*
 * Copyright (c) 2019 NetFoundry, Inc.
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

package io.netfoundry.ziti.identity

import org.junit.Test

class ZitiJWTTest {

    val jwt = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9." +
            "eyJhcGlCYXNlVXJsIjoiaHR0cHM6Ly9kZW1vLnppdGkubmV0Zm91bmRyeS5pbzoxMDgwLyIsIm" +
            "F1ZCI6ImVuZHBvaW50LWVucm9sbGVyIiwiZW5yb2xsbWVudE1ldGhvZCI6Im90dCIsImVucm9s" +
            "bG1lbnRVcmwiOiJodHRwczovL2RlbW8ueml0aS5uZXRmb3VuZHJ5LmlvOjEwODAvZW5yb2xsP2" +
            "1ldGhvZD1vdHRcdTAwMjZ0b2tlbj1kMzRkZTQ3Ny1kOTU4LTExZTktYWQwMC0wMDBkM2ExYjRi" +
            "MTciLCJleHAiOjE1Njg4MTc1MTMsImZpbmdlcnByaW50cyI6WyI2YWJiYWNlMmFjOWVjYTRjZT" +
            "Y2ODdlODJkMmQ5NDMwYmNmZmUxMmY1MzRlNjYwZDVkZGI4NzBhMDEwZWJjYjk4ZWM3M2I2ODJh" +
            "MmJhYThhZDVkYWE4NzhjYmFkN2NlNjMzZGNhYmExYTBkYmM5ODViYzM3Mzk2NzgxODRjOThmZi" +
            "JdLCJpYXQiOjE1Njg3MzExMTMsImlkZW50aXR5Ijp7InR5cGUiOiJlbmRwb2ludCIsImlkIjoi" +
            "ZDYwYWQ4MmQtOTg0YS00YTYxLWJmYWItNDQyNWJjMWMyYTQ4IiwibmFtZSI6ImRlbW8tZWsxIn" +
            "0sImlzcyI6ImRlbW8ueml0aS5uZXRmb3VuZHJ5LmlvOjEwODAiLCJqdGkiOiIxYmMwMmM5MC1l" +
            "MmM0LTRlZDMtYWI0Yy1kMmE1NDE2NmE1MGQiLCJuYmYiOjE1Njg3MzExMTMsInN1YiI6ImVuZH" +
            "BvaW50LWQ2MGFkODJkLTk4NGEtNGE2MS1iZmFiLTQ0MjViYzFjMmE0OCIsInRva2VuIjoiZDM0" +
            "ZGU0NzctZDk1OC0xMWU5LWFkMDAtMDAwZDNhMWI0YjE3IiwidmVyc2lvbnMiOnsiYXBpIjoiMS" +
            "4wLjAiLCJlbnJvbGxtZW50QXBpIjoiMS4wLjAifX0." +
            "wli56aeN3L39vA2pa-1wvlsygE2aVYyIua860Ok-wauUDb6hn8xs1hYCZ7QTDC_yHtlCovcoNY" +
            "taoUTWISO7oIItLCKEzedzDXNpbTrEBAs_If9Rjg6TkkFif77dAh4_Qz7DQ_jkDLpiBtuQJWvg" +
            "QO1wnESdiVyO5ViY5BnoWJ_DLNxhXR85TLEnZ0t9jK2MwGamH-gZ6RUzPj9fLtaKh_J9DG0nwg" +
            "QQREpX80XMhBvb4t490y12Zlm_GmL-s_MpPqxWdbGoiFrJk0lHmqiCs5nQz27KHcysHL1xXKw3" +
            "uvMeTGIWlJFnb1JxYHgKYH2jQTEzEjvCJUfwTnThKpwi2g"

    @Test
    fun loadJWT() {
        val zjwt = ZitiJWT.fromJWT(jwt)
    }
}
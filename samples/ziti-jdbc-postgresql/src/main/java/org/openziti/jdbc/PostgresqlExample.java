/*
 * Copyright (c) NetFoundry, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.openziti.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

import org.openziti.Ziti;
import org.openziti.ZitiContext;

public class PostgresqlExample {
    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.out.println("Usage: App <ziti identity.json>");
            System.exit(1);
        }

        // Initialize the OpenZiti Java SDK with our identity
        final ZitiContext zitiContext = Ziti.newContext(args[0], "".toCharArray());

        String url = "jdbc:ziti:postgresql://postgres-server.demo/simpledb";

        Properties props = new Properties();
        props.setProperty("user", "postgres");
        props.setProperty("password", "postgres");
        props.setProperty("connectTimeout", "240");

        // Tell the ZDBC driver to wait for up to two minutes for a service named "postgres" to be available
        // in the local SDK before connecting.
        props.setProperty(ZitiDriver.ZITI_WAIT_FOR_SERVICE_NAME, "postgres");
        props.setProperty(ZitiDriver.ZITI_WAIT_FOR_SERVICE_TIMEOUT, "PT120S");

        try (Connection conn = DriverManager.getConnection(url, props)) {
            try (Statement stmt = conn.createStatement()) {
                try (ResultSet rs = stmt.executeQuery("select * from simpletable")) {
                    while (rs.next()) {
                        System.out.println("Result from database is: " + rs.getString(1) + ":" + rs.getInt(2));
                    }
                }
            }
        } finally {
            Ziti.getContexts().forEach(c -> c.destroy());
        }

        System.exit(0);
    }
}
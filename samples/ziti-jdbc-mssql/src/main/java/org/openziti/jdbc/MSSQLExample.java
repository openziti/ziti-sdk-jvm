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

public class MSSQLExample {
    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.out.println("Usage: App <ziti identity.json>");
            System.exit(1);
        }

        // Initialize the OpenZiti Java SDK with our identity
        final ZitiContext zitiContext = Ziti.newContext(args[0], "".toCharArray());

        String url = "zdbc:sqlserver://mssql_host:1433;databaseName=DBNAME";

        Properties props = new Properties();

        // General MSSQL properties
        props.setProperty("user", "zdbc");
        props.setProperty("password", "netfoundry!1");
        props.setProperty("connectTimeout", "240");
        props.setProperty("encrypt", "False");

        // To use MSSQL with zdbc driver we need to set the mssql driver's socketFactoryClass to ZitiSocketFactory
        props.setProperty("socketFactoryClass", "org.openziti.net.ZitiSocketFactory");

        // Ziti specific properties
        props.setProperty("zitiIdentityFile", "network\\DBClient.json");
        props.setProperty("zitiDriverUrlPattern", "^zdbc:sqlserver:.*");
        props.setProperty("zitiDriverClassname", "com.microsoft.sqlserver.jdbc.SQLServerDriver");


        try (Connection conn = DriverManager.getConnection(url, props)) {
            try (Statement stmt = conn.createStatement()) {
                try (ResultSet rs = stmt.executeQuery("select top 10 * from dbo.TABLE")) {
                    while (rs.next()) {
                        System.out.println("Column_1: " + rs.getString(1) + " - Column_2: " + rs.getString(2));
                    }
                }
            }
        } finally {
            Ziti.getContexts().forEach(c -> c.destroy());
        }

        System.exit(0);
    }
}
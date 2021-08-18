/*
  Copyright (c) NetFoundry, Inc.
 
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
 
      https://www.apache.org/licenses/LICENSE-2.0
 
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
*/
package jdbc.postgres;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

import org.openziti.Ziti;

public class App {
	public static void main(String[] args) throws Exception {
		Ziti.init("c:/temp/java-identity.json", "".toCharArray(), false);
		
		String url = "jdbc:postgresql://zitified-postgres/simpledb";
		Properties props = new Properties();
		props.setProperty("user", "postgres");
		props.setProperty("password", "postgres");
		props.setProperty("socketFactory", "org.openziti.net.ZitiSocketFactory");

		try {
			try (Connection conn = DriverManager.getConnection(url, props)) {
				try (Statement stmt = conn.createStatement()) {
					try (ResultSet rs = stmt.executeQuery("select * from simpletable")) {
						while(rs.next()) {
							System.out.println("Result from database is: " + rs.getString(1) + ":" + rs.getInt(2));
						}
					}
				}
			}
		} finally {
			Ziti.getContexts().forEach(c -> c.destroy());
		}
		System.exit(0);
	}
}
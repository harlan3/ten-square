/*
 * Ten Square (OpenGL Decagonal Prism Surface Viewer)
 * Copyright (c) 2022 Harlan Murphy - Orbisoftware
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
 
package searchtool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DatabaseAPI {

	private String connect = "jdbc:derby:directory:dbase/tensquare;create=false";
	//private String connect = "jdbc:derby:memory:tensquare;create=true";
	private static Connection conn = null;

	public static String NULL_UUID = "00000000-0000-0000-0000-000000000000";

	public void initDatabase() {

		try {
			conn = DriverManager.getConnection(connect);
		} catch (Exception except) {
			except.printStackTrace();
		}
	}

	public void createTables(int numberTables) {

		try {
			Statement stmt = conn.createStatement();

			for (int i = 1; i <= numberTables; i++) {

				stmt.execute("CREATE TABLE PageMetaData" + i + " (" + "id VARCHAR(36) PRIMARY KEY, "
						+ "pageNumber INTEGER, " + "text VARCHAR(8192))");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void insertPageMetaData(int tableIndex, int pageIndex, String text) {

		try {
			Statement stmt = conn.createStatement();

			stmt.execute("INSERT INTO PageMetaData" + tableIndex + " VALUES ('" + UUID.randomUUID().toString() + "',"
					+ pageIndex + ",'" + text + "')");

		} catch (SQLException sqlExcept) {
			sqlExcept.printStackTrace();
		}
	}

	public String retrievePageText(int tableIndex, int pageIndex) {

		String returnStr = "";

		try {
			Statement stmt;
			stmt = conn.createStatement();

			String selectStatement = "SELECT text FROM PageMetaData" + tableIndex + " where pageNumber = " + pageIndex;
			ResultSet results = stmt.executeQuery(selectStatement);

			if (results.next()) {
				returnStr = results.getString("text");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return returnStr;
	}

	public List<Integer> textSearch(int tableIndex, String text) {

		List<Integer> list = new ArrayList<Integer>();

		try {
			Statement stmt;
			stmt = conn.createStatement();

			String selectStatement = "SELECT pageNumber FROM PageMetaData" + tableIndex + " WHERE text LIKE '%" + text
					+ "%'";

			ResultSet results = stmt.executeQuery(selectStatement);

			while (results.next()) {

				int pageNumber = Integer.parseInt(results.getString("pageNumber"));
				list.add(pageNumber);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return list;
	}
}

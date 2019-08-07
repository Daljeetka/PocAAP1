package com.jdbc.neo.knowledgebase;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.poc.dto.ChartDTO;

public class ReadSQL {

	static Connection connection = null;
	static DatabaseMetaData metadata = null;

	// Static block for initialization
	static {
		try {
			connection = SqlDBConnection.getConnection();
		} catch (SQLException e) {
			System.err.println("There was an error getting the connection: " + e.getMessage());
		}

		try {
			metadata = connection.getMetaData();
		} catch (SQLException e) {
			System.err.println("There was an error getting the metadata: " + e.getMessage());
		}
	}

	/**
	 * Prints in the console the columns metadata, based in the Arraylist of tables
	 * passed as parameter.
	 * 
	 * @param tables
	 * @throws SQLException
	 */
	public List<List> getSqlData(String sqlQuery) throws SQLException {
		new ArrayList<String>();
		new ArrayList<String>();
		new ArrayList<String>();

		List<List> rows = new ArrayList<>();

		ResultSet rs = null;
		Statement st = connection.createStatement();
		rs = st.executeQuery(sqlQuery);
		ResultSetMetaData rsmd = rs.getMetaData();

		int columnsNumber = rsmd.getColumnCount();
		while (rs.next()) {
			List<String> columns = new ArrayList<>();
			for (int i = 1; i <= columnsNumber; i++)

			{

				columns.add(rs.getString(i));
			}

			rows.add(columns);
		}

		return rows;
	}

	/**
	 * Prints in the console the columns metadata, based in the Arraylist of tables
	 * passed as parameter.
	 * 
	 * @param tables
	 * @throws SQLException
	 */
	public ChartDTO extractData(String sqlQuery) throws SQLException {
		new ArrayList<String>();
		new ArrayList<String>();
		new ArrayList<String>();

		List<List> rows = new ArrayList<>();
		ChartDTO chartDTO = new ChartDTO();
		ResultSet rs = null;
		Statement st = connection.createStatement();
		rs = st.executeQuery(sqlQuery);
		ResultSetMetaData rsmd = rs.getMetaData();

		int columnsNumber = rsmd.getColumnCount();
		while (rs.next()) {
			chartDTO.addXaxisValue(rs.getString(1));
			 
            chartDTO.addYaxisValue(rs.getDouble(2));
		}

		return chartDTO;
	}
	
}

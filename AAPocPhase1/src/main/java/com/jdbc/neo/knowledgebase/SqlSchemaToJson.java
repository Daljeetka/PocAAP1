package com.jdbc.neo.knowledgebase;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mysql.cj.util.StringUtils;

public class SqlSchemaToJson {

	static Connection connection = null;
	static DatabaseMetaData metadata = null;

	// Static block for initialization
	static {
		try {
			connection = SqlDBConnection.getConnection();
		} catch (SQLException e) {
			System.err.println("There was an error getting the connection: "
					+ e.getMessage());
		}

		try {
			metadata = connection.getMetaData();
		} catch (SQLException e) {
			System.err.println("There was an error getting the metadata: "
					+ e.getMessage());
		}
	}

	/**
	 * Prints in the console the general metadata.
	 * 
	 * @throws SQLException
	 */
	public static void printGeneralMetadata() throws SQLException {
		System.out.println("Database Product Name: "
				+ metadata.getDatabaseProductName());
		System.out.println("Database Product Version: "
				+ metadata.getDatabaseProductVersion());
		System.out.println("Logged User: " + metadata.getUserName());
		System.out.println("JDBC Driver: " + metadata.getDriverName());
		System.out.println("Driver Version: " + metadata.getDriverVersion());
		System.out.println("\n");
	}

	/**
	 * 
	 * @return Arraylist with the table's name
	 * @throws SQLException
	 */
	public static List getTablesMetadata() throws SQLException {
		String table[] = { "TABLE" };
		ResultSet rs = null;
		List<String> tables = new ArrayList<String>();
		// receive the Type of the object in a String array.
		rs = metadata.getTables(null, null, null, table);
		tables = new ArrayList<String>();
		while (rs.next()) {
			tables.add(rs.getString("TABLE_NAME"));
		}
		return tables;
	}

	/**
	 * Prints in the console the columns metadata, based in the Arraylist of
	 * tables passed as parameter.
	 * 
	 * @param tables
	 * @throws SQLException
	 */
	public String getColumnsMetadata(List<String> tables)
			throws SQLException {
		ResultSet rs = null;
		ResultSet rskeys = null;
		
		List<String> listJsonString = new ArrayList<>();
		String jsonString = "{\"Tables\":\n[";
		tables = tables.subList(1, tables.size());
		// Print the columns properties of the actual table
		int tabInd = 1;
		for (String actualTable : tables) {
			int colInd = 1;
			rs = metadata.getColumns(null, null, actualTable, null);
			rskeys = metadata.getImportedKeys(connection.getCatalog(), "world", actualTable);
			jsonString += "\n{\"Table\":\""+actualTable.toUpperCase() + "\",\n\"TabInd\":\""+  tabInd  +"\",\n\"columns\":\n[\n{" ;
			while (rs.next()) {

				jsonString = jsonString + "\""+ "Key" + "\":\""
						+ rs.getString("COLUMN_NAME") + "\"," ;
				jsonString = jsonString + "\""+ "ColInd" + "\":\""
						+ colInd + "\"," ;
				
				jsonString = jsonString + "\""+ "Value" + "\":\""
						+ rs.getString("TYPE_NAME") + "\"},{" ;
				colInd++;
			}
			jsonString = jsonString.substring(0, jsonString.length()-2);
			jsonString = jsonString+"],\n\"hasRelations\":\n[\n{" ;
			
			if(rskeys.next())
			{
				jsonString = jsonString + "\"Table\":\""
						+ rskeys.getString("PKTABLE_NAME") + "\"," ;
				jsonString = jsonString + "\"Column\":\""
						+ rskeys.getString("PKCOLUMN_NAME") + "\"," ;
			}
			else
			{
				jsonString = jsonString + "\"Table\":\""
					+ "\"," ;
				jsonString = jsonString + "\"Column\":\""
				    + "\"," ;
			}
			jsonString = jsonString.substring(0, jsonString.length()-1);
			jsonString = jsonString+ "}]},\n" ;
			System.out.println("\n");
			listJsonString.add(jsonString);
			tabInd++;
		}
		jsonString = jsonString.substring(0, jsonString.length()-2)+ "]}";
		System.out.println(jsonString);
        return jsonString;
	}

	/**
	 * 
	 * @param args
	 */
	//public static void main(String[] args) {
	public String getSchema() {
		String jsonSchema= "";
		try {
			printGeneralMetadata();
			// Print all the tables of the database scheme, with their names and
			// structure
			 jsonSchema = getColumnsMetadata(getTablesMetadata());
		} catch (SQLException e) {
			System.err
					.println("There was an error retrieving the metadata properties: "
							+ e.getMessage());
		}
		return jsonSchema;
	}
}

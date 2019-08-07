package com.jdbc.neo.knowledgebase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Value;

public class ReadNeo implements AutoCloseable {
	private final Driver driver;
	final List<String> agg_ops = new ArrayList<String>(Arrays.asList("","GROUP BY"));
	final List<String> sql_funcs = new ArrayList<String>(Arrays.asList("","date","count"));
	final List<String> cond_ops = new ArrayList<String>(Arrays.asList("=", ">", "<", "OP"));
	final List<String> syms = new ArrayList<String>(Arrays.asList("SELECT", "WHERE", "AND", "COL", "TABLE", "CAPTION",
			"PAGE", "SECTION", "OP", "COND", "QUESTION", "AGG", "AGGOPS", "CONDOPS"));

	public ReadNeo(String uri, String user, String password) throws IOException {
		driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));

	}

	@Override
	public void close() throws Exception {
		driver.close();
	}

	public String getTableName(String tableId) {
		try (Session session = driver.session()) {

			StatementResult result = session.run("MATCH (n {tabind: '" + tableId + "'}) RETURN n, n.name");

			Record record = result.next();
			System.out.println("\n----------------------------\nTable Name: " + record.get(1) + "\n");
			return (record.toString());
		}
	}

	public List<Record> getSelRecord(String tableId, Map<String, List> sqlMap) {
		new ArrayList<>();
		List<Record> record = new ArrayList<>();
		String inClauseSel = "";
		List<Integer> selList = new ArrayList<>();
		for (Map.Entry<String, List> entry : sqlMap.entrySet()) {
			//System.out.println("\n----------------------------\n" + entry.getKey() + "=" + entry.getValue() + "\n");
			if (entry.getKey().equals("sel")) {
				selList = entry.getValue();
				for (Integer integer : selList) {
					inClauseSel = inClauseSel + "'" + integer + "',";
				}
				inClauseSel = inClauseSel.substring(0, inClauseSel.length() - 1);

				try (Session session = driver.session()) {

					StatementResult result = session.run("MATCH (p:TableName{tabind:'" + tableId + "'})\r\n"
							+ "OPTIONAL MATCH (p)-[:CHILD]->(c:ColumnName) where c.colind IN [" + inClauseSel + "] \r\n"
							+
							
							"RETURN {TableName : p.name, ColumnName : c.column}");
					record = result.list();
				}
			}

		}
		return record;

	}
	
	public List<Integer> getSelFunc(String tableId, Map<String, List> sqlMap) {
		new ArrayList<>();
		List<Integer> functions = new ArrayList<>();
		String inClauseSel = "";
		List<Integer> funcList = new ArrayList<>();
		for (Map.Entry<String, List> entry : sqlMap.entrySet()) {
			//System.out.println("\n----------------------------\n" + entry.getKey() + "=" + entry.getValue() + "\n");
			if (entry.getKey().equals("func")) {
				funcList = entry.getValue();
				for (Integer integer : funcList) {
					functions.add(integer);
				}
				
			}

		}
		return functions;

	}
	
	public List<Integer> getAgg(String tableId, Map<String, List> sqlMap) {
		new ArrayList<>();
		List<Integer> aggregations = new ArrayList<>();
		String inClauseSel = "";
		List<Integer> aggList = new ArrayList<>();
		for (Map.Entry<String, List> entry : sqlMap.entrySet()) {
		//	System.out.println("\n----------------------------\n" + entry.getKey() + "=" + entry.getValue() + "\n");
			if (entry.getKey().equals("agg")) {
				aggList = entry.getValue();
				for (Integer integer : aggList) {
					aggregations.add(integer);
				}
				
			}

		}
		return aggregations;

	}

	public String getCondRecord(String tableId, Map<String, List> sqlMap) {
		List<List> condList = new ArrayList<>();
		List<Record> record = new ArrayList<>();
		String whereCondSel = "";
		String condOper = "";
		String whereVal = "";
		String whereString = "";
		String sqlColNames = "";
		new ArrayList<>();
		for (Map.Entry<String, List> entry : sqlMap.entrySet()) {
		//	System.out.println("\n----------------------------\n" + entry.getKey() + "=" + entry.getValue() + "\n");
			if (entry.getKey().equals("conds")) {
				condList = entry.getValue();
				for (int i = 0; i < entry.getValue().size(); i++) {
					List<Integer> condColList = new ArrayList<>();
					// condColList.add((Integer) condList.get(i).get(0));
					whereCondSel = whereCondSel + "'" + condList.get(i).get(0) + "',";
					condOper = cond_ops.get((int) condList.get(i).get(1));
					whereVal = String.valueOf(condList.get(i).get(2));
					condColList.add((Integer) condList.get(i).get(1));
					condColList.add((Integer) condList.get(i).get(2));
				}
				whereCondSel = whereCondSel.substring(0, whereCondSel.length() - 1);
  
				if(!whereCondSel.equals("'0'")) {
				
				try (Session session = driver.session()) {

					StatementResult result1 = session.run("MATCH (p:TableName{tabind:'" + tableId + "'})\r\n"
							+ "OPTIONAL MATCH (p)-[:CHILD]->(c:ColumnName) where c.colind IN [" + whereCondSel
							+ "] \r\n" +
							// "RETURN {TableName : p.name, ColumnName : {name :collect( c.column)}}");
							"RETURN {TableName : p.name, ColumnName : c.column}");
					record = result1.list();

					for (Record recordVal : record) {

						List<Value> recordVals = recordVal.values();
						Map recordMap = recordVals.get(0).asMap();
						// sqlTableName = (String) recordMap.get("TableName");
						sqlColNames = sqlColNames + ((String) recordMap.get("ColumnName")) + ",";

					}
					sqlColNames = sqlColNames.substring(0, sqlColNames.length() - 1);

				}
				}

			}

		}
		if(!sqlColNames.equals("")) {
		whereString = "where " + sqlColNames + " " + condOper + " " + whereVal;}
		
		return whereString;

	}

	public String generateSqlQuery(String json) throws Exception {
		String sqlQuery = "";
		try (ReadNeo greeter = new ReadNeo("bolt://localhost:7687", "neo4j", "satnam12")) {
			ParseSqlJson jsonNodeDemo = new ParseSqlJson(json);
			jsonNodeDemo.readJsonWithJsonNode();

			String tableId = jsonNodeDemo.readTableIdNode();
			greeter.getTableName(tableId);

			Map<String, List> sqlMap = jsonNodeDemo.sqlInformation();

			List<Record> resultRecord = greeter.getSelRecord(tableId, sqlMap);
			List<Integer> functions = greeter.getSelFunc(tableId, sqlMap);
			List<Integer> aggregations = greeter.getAgg(tableId, sqlMap);
			String whereString = greeter.getCondRecord(tableId, sqlMap);
			String sqlTableName = "";
			String aggClause = "";
			String sqlColNames = "";
			int i=0;
			for (Record record : resultRecord) {

				List<Value> recordVals = record.values();
				Map recordMap = recordVals.get(0).asMap();
				sqlTableName = (String) recordMap.get("TableName");
				sqlColNames =sqlColNames + sql_funcs.get(functions.get(i))+"(" + ((String) recordMap.get("ColumnName")) + "),";
				if(aggregations.get(i) !=0) {
				aggClause = aggClause+ agg_ops.get(aggregations.get(i)) + " " + sql_funcs.get(functions.get(i))+"(" + ((String) recordMap.get("ColumnName")) + ")";
				}
                i++;
			}
			sqlColNames = sqlColNames.substring(0, sqlColNames.length() - 1);

			sqlQuery = "Select " + sqlColNames + " from " + sqlTableName + " " + whereString + aggClause;
			System.out.println("SqlQuery " + sqlQuery);

		}
		return sqlQuery;
	}
}
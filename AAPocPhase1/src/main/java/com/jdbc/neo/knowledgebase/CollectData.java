package com.jdbc.neo.knowledgebase;

import java.util.List;

public class CollectData {
	public static void main(String[] args) throws Exception {
		JsonStore js = new JsonStore();
		String json = js.fetchJson(3);
		ReadNeo rn = new ReadNeo("bolt://localhost:7687", "neo4j", "satnam12");
		String sqlQuery = rn.generateSqlQuery(json);
		ReadSQL rs = new ReadSQL();
		rs.extractData(sqlQuery);
		List<List> rowsData = rs.getSqlData(sqlQuery);
		System.out.println(rowsData.toString());
	}
}

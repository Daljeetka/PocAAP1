package com.jdbc.neo.knowledgebase;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ParseSqlJson {

	JsonNode rootNode;
	ObjectMapper objectMapper;
	String jsonSqlString;

	public ParseSqlJson(String jsonSqlString) throws IOException {
		objectMapper = new ObjectMapper();
		this.jsonSqlString = jsonSqlString;
		rootNode = objectMapper.readTree(jsonSqlString);
	}

	public JsonNode readJsonWithJsonNode() throws JsonProcessingException {
		String prettyPrintEmployee = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
		System.out.println(prettyPrintEmployee + "\n");
		return rootNode;
	}

	public String readTableIdNode() {
		JsonNode nameNode = rootNode.path("table_id");
		String tabInd = nameNode.asText();
		return tabInd;
	}

	public Map<String, List> sqlInformation() throws JsonProcessingException {
		JsonNode sqlInformationNode = rootNode.get("sql");
		Map<String, List> sqlInformationMap = objectMapper.convertValue(sqlInformationNode, Map.class);

		return sqlInformationMap;
	}

}
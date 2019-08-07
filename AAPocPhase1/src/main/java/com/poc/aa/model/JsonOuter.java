package com.poc.aa.model;

public class JsonOuter {
	private Long id;
	private String question;
	private String table_id;
	private JsonInner jsonInner;
	
	public JsonOuter(){}
	
	public JsonOuter(Long id, String question, String table_id, JsonInner jsonInner){
		this.id = id;
		this.question = question;
		this.table_id = table_id;
		this.jsonInner = jsonInner;
	}
 
	
	@Override
	public String toString() {
		return "JsonOuter {question:" + question + ", table_id:" + table_id + ", sql:" + jsonInner + "}";
	}

}
package com.poc.aa.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jdbc.neo.knowledgebase.JsonStore;
import com.jdbc.neo.knowledgebase.ReadNeo;
import com.jdbc.neo.knowledgebase.ReadSQL;
import com.poc.aa.model.Address;
import com.poc.aa.model.Customer;



@RestController
@RequestMapping("/api/customer")
public class RestAPIs {
	
	Map<Long, Customer> custStores = new HashMap<Long, Customer>();
	
	@PostConstruct
    public void initIt() throws Exception {
        custStores.put(Long.valueOf(1), new Customer(new Long(1), "Show the product name and quantity per unit.", 25, new Address("NANTERRE CT", "77471")));
        custStores.put(Long.valueOf(2), new Customer(new Long(2), "Show the discontinued product list.", 37, new Address("W NORMA ST", "77009")));
        custStores.put(Long.valueOf(3), new Customer(new Long(3), "Show me the Sales Data/ Count of invoices datewise", 18, new Address("S NUGENT AVE", "77571")));
        custStores.put(Long.valueOf(4), new Customer(new Long(4), "Show the report showing the title and the first and last name of all sales representatives.", 23, new Address("E NAVAHO TRL", "77449")));
        custStores.put(Long.valueOf(5), new Customer(new Long(5), "Create a report that shows the order id, freight cost, freight cost with this tax for all orders of $500 or more", 45, new Address("AVE N", "77587")));
    }
	 
	@GetMapping(value = "/all")
	public List<Customer> getResource() {
		
		List<Customer> custList = custStores.entrySet().stream()
		        .map(entry -> entry.getValue())
		        .collect(Collectors.toList());
		
		return custList;
	}
	
	
	
	@GetMapping(value = "/json")
	public String getJson(@RequestParam int id) throws Exception {
		System.out.println("asdasdad " + id);
		JsonStore js = new JsonStore();
		String json = js.fetchJson(id);
		
		return json;
	}
	
	@GetMapping(value = "/sql")
	public String getSQL() throws Exception {
		JsonStore js = new JsonStore();
		String json = js.fetchJson(2);
		
		ReadNeo rn = new ReadNeo("bolt://localhost:7687", "neo4j", "satnam12");
		String sqlQuery = rn.generateSqlQuery(json);
		
		return sqlQuery;
	}
	
	@GetMapping(value = "/data1")
	public String getData1() throws Exception {
		JsonStore js = new JsonStore();
		String json = js.fetchJson(1);
		ReadNeo rn = new ReadNeo("bolt://localhost:7687", "neo4j", "satnam12");
		String sqlQuery = rn.generateSqlQuery(json);
		ReadSQL rs = new ReadSQL();
		List<List> rowsData = rs.getSqlData(sqlQuery);
		System.out.println(rowsData.toString());
		return rowsData.toString();
	}
	
	  @RequestMapping(value = "/data", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	    public ResponseEntity<Object> getData() throws Exception {
		  
		  JsonStore js = new JsonStore();
			String json = js.fetchJson(3);
			ReadNeo rn = new ReadNeo("bolt://localhost:7687", "neo4j", "satnam12");
			String sqlQuery = rn.generateSqlQuery(json);
			ReadSQL rs = new ReadSQL();
	        return ResponseEntity.ok(rs.extractData(sqlQuery));
	    }
	
}
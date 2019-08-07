package com.jdbc.neo.knowledgebase;

import java.util.List;

public class JsonStore {

	
	public String fetchJson(int i)
	{
		String Json = "";
		
		if(i==1)
		{
			Json = "{\r\n" + 
					"   \"question\":\"show Product name and quantity/unit\",\r\n" + 
					"   \"sql\":{	\r\n" + 
					"      \"conds\":[\r\n" + 
					"         [\r\n" + 
					"            0,\r\n" + 
					"            0,\r\n" + 
					"            0\r\n" + 
					"         ]\r\n" + 
					"      ],\r\n" + 
					"      \"sel\":[2,10],\r\n" + 
					"      \"agg\":[0,0],\r\n" + 
					"	  \"func\":[0,0]\r\n" + 
					"   },\r\n" + 
					"   \"table_id\":12\r\n" + 
					"}";
			
		}else if (i==2) {
			Json = "{\r\n" + 
					"   \"question\":\"show discontinued product list\",\r\n" + 
					"   \"sql\":{	\r\n" + 
					"      \"conds\":[\r\n" + 
					"         [\r\n" + 
					"            11,\r\n" + 
					"            1,\r\n" + 
					"            1\r\n" + 
					"         ]\r\n" + 
					"      ],\r\n" + 
					"      \"sel\":[2,10],\r\n" + 
					"      \"agg\":[0,0],\r\n" + 
					"	  \"func\":[0,0]\r\n" + 
					"   },\r\n" + 
					"   \"table_id\":12\r\n" + 
					"}";
		}
		else if (i==3) {
			
			//SELECT COUNT(id), date(invoice_date) FROM invoices GROUP BY date(invoice_date)
					Json = "{\r\n" + 
							"   \"question\":\"Show me the Sales Data/ Count of invoices datewise\",\r\n" + 
							"   \"sql\":{	\r\n" + 
							"      \"conds\":[\r\n" + 
							"         [\r\n" + 
							"            0,\r\n" + 
							"            0,\r\n" + 
							"            0\r\n" + 
							"         ]\r\n" + 
							"      ],\r\n" + 
							"      \"sel\":[1,3],\r\n" + 
							"      \"agg\":[1,0],\r\n" + 
							"	  \"func\":[1,2]\r\n" + 
							"   },\r\n" + 
							"   \"table_id\":5\r\n" + 
							"}";
				}
		else if (i==4) {
			
		}
		else if (i==5) {
			
		}
		return Json;
	}
}

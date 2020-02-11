# PocAAP1
Phase1
Augmented Analytics -- an approach that automates insights using machine learning and natural-language generation, marks the next wave of disruption in the data and analytics market.

However, to go from raw data to insights, you need to go through many technical steps, including:
1) Collect data from multiple sources
2) Clean data so it is ready for analysis
3) Conduct the analysis
4) Generate insights, and
5) Communicate those insights with the organization and convert them into action plans

What augmented analytics does is to relieve a company’s dependence on data scientists by automating insight generation in a company through the use of advanced machine learning and artificial intelligence algorithms.

This project will:

Use Speech to identify data and prepare it.

Use Speech to validate whether it would be useful by using standard statistical tests.

Use Speech to determine kind of report/chart on top of this to further visualize it to enable decision.

We have divided the project in 3 phases:


1) Knowledge Base Creation, SQL generation.


2) Natural Language Processing (TextToSQL) ,Speech to Text conversion.


3) Generate Reports/Charts from SQL created in Part 1.


Below class creates the Knowledge Graph in Neo4J db from Mysql DB.
WriteNeo.java

1)	Connect to Sql database.
2)	getColumnsMetaData and create JSON.
3)	Connect to Neo4J 
4)	Call apoc.convert.fromJsonMap and pass Json. It will create Nodes and Child in graph Db of Neo4j.


Assuming that I have the Seq2Sql ready with output in the form of Json:

{
   "question":"Display the Yearly Sale",
   "sql":{	
      "conds":[
         [
            0,
            0,
            0
         ]
      ],
      "sel":[2,1],
      "agg":[1,0],  
      "func":[2,1]
   },
   "table_id":4
}


The fields represent the following:

question: the natural language question written by the worker.
table_id: the ID of the table to which this question is addressed.
sql: the SQL query corresponding to the question. This has the following subfields:

sel: the numerical index of the column that is being selected. You can find the actual column from the table.
agg: the numerical index of the aggregation operator that is being used. E.g. Group by
func: the numerical index of the aggregation operator that is being used. E.g. date(), month(), year()
conds: a list of triplets (column_index, operator_index, condition) where:
column_index: the numerical index of the condition column that is being used. You can find the actual column from the table.
operator_index: the numerical index of the condition operator that is being used. Eg. >,<,=
condition: the comparison value for the condition, in either string or float type.


Another class ReadNeo.java

1)	Read above json to fetch the table_id, Condition columns, selection columns etc.
2)	Each node in neo4j has its label index. E.g Product table node has tabInd = 12. So it is mapped to fetch the table name from neo4j. Further graph is traversed to get the column names. 
3)	The above sql(with tableid and columned ) is converted to actual sql query that can be executed.




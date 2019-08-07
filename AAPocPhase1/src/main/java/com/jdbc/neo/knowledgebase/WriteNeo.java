package com.jdbc.neo.knowledgebase;

import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Transaction;
import org.neo4j.driver.v1.TransactionWork;

import static org.neo4j.driver.v1.Values.parameters;

public class WriteNeo implements AutoCloseable
{
    private final Driver driver;
    private final String jsonString;
    public final SqlSchemaToJson  md = new SqlSchemaToJson();

    public WriteNeo( String uri, String user, String password )
    {   
        driver = GraphDatabase.driver( uri, AuthTokens.basic( user, password ) );
        jsonString = md.getSchema();
    }

    @Override
    public void close() throws Exception
    {
        driver.close();
    }

    public void printGreeting( final String message )
    {
        try ( Session session = driver.session() )
        {
            String greeting = session.writeTransaction( new TransactionWork<String>()
            {
                @Override
                public String execute( Transaction tx )
                {

                	
                    StatementResult result = tx.run( "WITH apoc.convert.fromJsonMap('" +
                            jsonString +
                            "') as document\r\n" + 
                            "UNWIND document.Tables AS table\r\n" + 
                            "UNWIND table.columns AS column\r\n" + 
                            "MERGE (tn:TableName {name: table.Table, tabind:table.TabInd})\r\n" + 
                            "MERGE (cn:ColumnName {column: column.Key, colind: column.ColInd})\r\n" + 
                            "ON CREATE SET cn.Value = column.Value\r\n" + 
                            "MERGE (tn)-[:CHILD]->(cn)\r\n" + 
                            "return *");
return result.toString();
                	
                }
            } );
            System.out.println( greeting );
        }
    }

    public static void main( String... args ) throws Exception
    {
        try ( WriteNeo greeter = new WriteNeo( "bolt://localhost:7687", "neo4j", "satnam12" ) )
        {
            greeter.printGreeting( "hello, world" );
        }
    }
}
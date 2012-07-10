package hu.bme.inf.mit.neo4jmapping;

import java.io.IOException;


public class Neo4jMapping {
	
	public static void main(String[] args) {
		final String databasePath = "c:/testdb";
		
		Neo4jTestBed neo4jTestBed;		
		try {
			neo4jTestBed = new Neo4jTestBed(databasePath, true);
			neo4jTestBed.runTest();			
		} catch (IOException e) {			
			e.printStackTrace();
		}
	}
	
}

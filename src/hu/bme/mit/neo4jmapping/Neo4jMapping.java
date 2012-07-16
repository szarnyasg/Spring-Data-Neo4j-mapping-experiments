package hu.bme.mit.neo4jmapping;

public class Neo4jMapping {
	
	public static void main(String[] args) {
		// set database path here
		final String databasePath = "h:/testdb"; 
		
		Neo4jTestBed neo4jTestBed;		
		try {
			neo4jTestBed = new Neo4jTestBed(databasePath, true);
			neo4jTestBed.runTest();			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

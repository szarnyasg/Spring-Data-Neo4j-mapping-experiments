package hu.bme.inf.mit.neo4jmapping;


public class Neo4jMapping {
	
	public static void main(String[] args) {
		final String databasePath = "h:/testdb";
		Neo4jTestBed neo4jTestBed = new Neo4jTestBed(databasePath, true);
		neo4jTestBed.runTest();
	}
	
}

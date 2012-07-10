package hu.bme.inf.mit.neo4jmapping;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;

import org.neo4j.graphdb.Transaction;
import org.neo4j.kernel.EmbeddedGraphDatabase;
import org.neo4j.kernel.GraphDatabaseAPI;
import org.neo4j.kernel.impl.transaction.SpringTransactionManager;
import org.neo4j.kernel.impl.util.FileUtils;
import org.springframework.data.neo4j.core.GraphDatabase;
import org.springframework.data.neo4j.support.DelegatingGraphDatabase;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.jta.JtaTransactionManager;

public class Neo4jTestBed {

	private String databasePath;

	public Neo4jTestBed(String databasePath, boolean deletePrevious) throws IOException {
		this.databasePath = databasePath;

		// deleting test database directory
		if (new File(databasePath).exists() && deletePrevious) {
			try {
				FileUtils.deleteRecursively(new File(databasePath));
				System.out.println("Database directory deleted.");
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Cannot delete database directory.");
				throw e;
			}
		}	
	}
	
	private GraphDatabaseAPI graphDatabaseAPI = null;
	private PlatformTransactionManager transactionManager;
	private Neo4jTemplate template;
	
	public void runTest() {
		try {
			System.out.println("Starting database.");
		    graphDatabaseAPI = new EmbeddedGraphDatabase(databasePath);
		    System.out.println("Database started.");
		    GraphDatabase graphDatabase = new DelegatingGraphDatabase(graphDatabaseAPI);
		    transactionManager = new JtaTransactionManager(new SpringTransactionManager(graphDatabaseAPI));
		    template = new Neo4jTemplate(graphDatabase, transactionManager);		    
		    
		    System.out.println("Starting transaction.");
		    loadData();
		    System.out.println("Transaction finished.");
		    
		    System.out.println("Query started.");
		    Neo4jQueryExecutor neo4jQueryExecutor = new Neo4jQueryExecutor(template);
		    neo4jQueryExecutor.executeQuery();
		    System.out.println("Query finished.");	
		    
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		    if (graphDatabaseAPI != null) {
		    	graphDatabaseAPI.shutdown();
		    }
		    System.out.println("Database shutdown.");
		}
	}

	private void loadData() {
	    Transaction tx = graphDatabaseAPI.beginTx();
	    
	    template.repositoryFor(Movie.class);
	    
	    Movie m1 = new Movie("Node 1", "Test 1");
	    Movie m2 = new Movie("Node 2", "Test 2");
	    Movie m3 = new Movie("Node 3", "Test 3");
	    Movie m4 = new Movie("Node 4", "Test 4");
	    
	    HashSet<Movie> movies = new HashSet<Movie>();
	    movies.add(m2);
	    movies.add(m3);
	    movies.add(m4);
	    
	    m1.m = movies;
	    
	    template.save(m1);
	    
	    tx.success();
	    tx.finish();		
	}
}

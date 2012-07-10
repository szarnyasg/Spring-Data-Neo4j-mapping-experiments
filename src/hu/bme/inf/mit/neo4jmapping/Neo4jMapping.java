package hu.bme.inf.mit.neo4jmapping;

import java.io.File;
import java.util.HashSet;
import java.util.Iterator;

import org.neo4j.graphdb.Transaction;
import org.neo4j.kernel.EmbeddedGraphDatabase;
import org.neo4j.kernel.GraphDatabaseAPI;
import org.neo4j.kernel.impl.transaction.SpringTransactionManager;
import org.neo4j.kernel.impl.util.FileUtils;
import org.springframework.data.neo4j.conversion.EndResult;
import org.springframework.data.neo4j.core.GraphDatabase;
import org.springframework.data.neo4j.support.DelegatingGraphDatabase;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.jta.JtaTransactionManager;


public class Neo4jMapping {


	public static void main(String[] args) {
		boolean deletePrevious = true;
		
		final String databasePath = "h:/testdb";
		
		// deleting test database directory
		if (deletePrevious) {
			try {
				FileUtils.deleteRecursively(new File(databasePath));
				System.out.println("testdb directory deleted.");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		GraphDatabaseAPI graphDatabaseAPI = null;
		try {
			System.out.println("Starting database.");    // ----------------------------------------
		    graphDatabaseAPI = new EmbeddedGraphDatabase(databasePath);
		    GraphDatabase graphDatabase = new DelegatingGraphDatabase(graphDatabaseAPI);
		    PlatformTransactionManager transactionManager = new JtaTransactionManager(new SpringTransactionManager(graphDatabaseAPI));
		    Neo4jTemplate template = new Neo4jTemplate(graphDatabase, transactionManager);
		    
		    
		    System.out.println("Starting transaction."); // ----------------------------------------
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
		    System.out.println("Transaction finished."); // ----------------------------------------
		    
		    System.out.println("Query started.");        // ----------------------------------------
		    Neo4jQueryExecutor neo4jQueryExecutor = new Neo4jQueryExecutor(template);
		    
		    neo4jQueryExecutor.executeQuery();
		    System.out.println("Query finished.");       // ----------------------------------------			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		    if (graphDatabaseAPI != null) {
		    	graphDatabaseAPI.shutdown();
		    }
		    System.out.println("Database shutdown.");    // ----------------------------------------
		}
	}

}

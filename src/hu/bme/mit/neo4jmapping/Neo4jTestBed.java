package hu.bme.mit.neo4jmapping;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;

import org.neo4j.graphdb.Transaction;
import org.neo4j.kernel.EmbeddedGraphDatabase;
import org.neo4j.kernel.GraphDatabaseAPI;
import org.neo4j.kernel.impl.transaction.SpringTransactionManager;
import org.neo4j.kernel.impl.util.FileUtils;
import org.springframework.data.neo4j.core.GraphDatabase;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.neo4j.support.DelegatingGraphDatabase;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.jta.JtaTransactionManager;

public class Neo4jTestBed {

	private String databasePath;

	public Neo4jTestBed(String databasePath, boolean deletePrevious) {
		this.databasePath = databasePath;

		try {
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
		} catch (Exception e) {
			// TODO: handle exception
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
		} catch (Throwable e) {
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

		GraphRepository<MyNode> graphRepository = template.repositoryFor(MyNode.class);
		
		MyNode m0 = new MyNode("Node 0", 0);
		m0.nodes = new HashSet<MyNode>();
	    graphRepository.save(m0);
		
		int linkedListLength = 5;
		//int starLeafCount = 1000;

		// star graph
		/*
		if (starLeafCount > 0) {
			for (int i = 1; i < starLeafCount; i++) {
				MyNode m = new MyNode("Star " + i, linkedListLength + i);
				m0.nodes.add(m);
				graphRepository.save(m);
				graphRepository.save(m0);
			}
		}*/
		
	    // linked list
	    if (linkedListLength > 0) {
	        MyNode lastNode = m0;
	        for (int i = 1; i < linkedListLength; i++) {
	            MyNode m = new MyNode("Linked list " + i, i);
	            lastNode.nextNode = m;
	            graphRepository.save(m);
	            graphRepository.save(lastNode);
	            lastNode = m;
	        }
	    }
	    //graphRepository.save(m0);

		tx.success();
		tx.finish();
	}
}

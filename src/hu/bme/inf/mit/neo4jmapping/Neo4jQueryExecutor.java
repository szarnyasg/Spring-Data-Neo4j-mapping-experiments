package hu.bme.inf.mit.neo4jmapping;

import java.util.Iterator;

import org.springframework.data.neo4j.conversion.EndResult;
import org.springframework.data.neo4j.support.Neo4jTemplate;

public class Neo4jQueryExecutor {

	String queryTextPosLength =
			"START n=node:__types__(className=\"hu.bme.mit.train.cc.distributed.NeoSegmentImpl\") " +
			"WHERE n.segment_length < 0 " +
			"RETURN COUNT(n) AS count"; 
	
	String queryTextRouteSensor =
			"START sensor=node:__types__(className=\"hu.bme.mit.train.cc.distributed.NeoSensorImpl\") " +
			"MATCH sensor-[:SENSOR_TRACKELEMENT]->switch-[:SWITCH_SWITCHPOSITION]->switchPosition-[:SWITCHPOSITION_ROUTE]->route-[r?:ROUTE_SENSOR]->sensor " +
			"WHERE r IS NULL " +
			"RETURN COUNT(sensor) AS count";
	
	String queryTextSwitchSensor =
			"START n=node:__types__(className=\"hu.bme.mit.train.cc.distributed.NeoSwitchImpl\") " +
			"MATCH n-[r?:TRACKELEMENT_SENSOR]->m " +
			"WHERE r IS NULL " +
			"RETURN COUNT(n) AS count";
	
	String queryTextSwitchSet =
			"START signal=node:__types__(className=\"hu.bme.mit.train.cc.distributed.NeoSignalImpl\") " +
			"MATCH signal<-[:ROUTE_ENTRY]-route<-[:ROUTE_SWITCHPOSITION]-switchposition-[:SWITCHPOSITION_SWITCH]->switch " +
			"WHERE 1=1 " +
			"  AND signal.Signal_actualState=\"SIGNAL_STATE_KIND_GO\" " +
			"  AND switchposition.switchPosition_switchState <> switch.switch_actualState " +
			"RETURN COUNT(DISTINCT(switch)) AS count";
	
	String queryTextSignalNeighbor = ""; // TODO: implement missing query

	private Neo4jTemplate template;

	public Neo4jQueryExecutor(Neo4jTemplate template) {
		this.template = template;
	}

	public void executeQuery() {
	    String queryText = "START n=node:__types__(className=\"Movie\") RETURN n";
		EndResult<Movie> endResult = template.query(queryText, null).to(Movie.class);
	    
		Iterator<Movie> iter = endResult.iterator();
		
		while (iter.hasNext()) {
			System.out.println(iter.next());
		}
	}
	
}

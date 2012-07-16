package hu.bme.mit.neo4jmapping;

import java.util.Set;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

@NodeEntity
public class MyNode {
    @GraphId
    private Long id;
    
    public MyNode() { }

    public MyNode(String a, int x) {
    	this.a = a;
    	this.x = x;
    }

    public String a;
    public int x;
    
    public String toString() { return a + " // " + x; }
    
    @Fetch
    @RelatedTo(type = "NODES", direction = Direction.BOTH)
    public Set<MyNode> nodes;    

    @Fetch
    @RelatedTo(type = "NEXT_NODE", direction = Direction.BOTH)
    MyNode nextNode;
    
	@Override
	public int hashCode() {
        return (id == null) ? x : id.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		MyNode other = (MyNode) obj;
		if (id == null) return other.id == null;
        return id.equals(other.id);
	}
}

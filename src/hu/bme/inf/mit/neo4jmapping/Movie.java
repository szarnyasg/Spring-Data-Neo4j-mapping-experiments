package hu.bme.inf.mit.neo4jmapping;

import java.util.Set;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

@NodeEntity
public class Movie {
    @GraphId
    private Long id;
    
    @Indexed
    private String name;
    
    public Movie() { }

    public Movie(String a, String b) {
    	this.a = a;
    	this.b = b;
    }

    public String a;
    public String b;
    
    @Fetch
    @RelatedTo(type = "MOVIE_REF", direction = Direction.BOTH)
    public Set<Movie> m;    
    
	@Override
	public int hashCode() {
        return (id == null) ? 0 : id.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Movie other = (Movie) obj;
		if (id == null) return other.id == null;
        return id.equals(other.id);
    }
}

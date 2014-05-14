import java.util.ArrayList;
public class Vertex implements Comparable<Vertex> {
	
	int id;
	Vertex leader;
	int rank = 0;
	int distToFringe = Integer.MAX_VALUE;
	
	boolean visited = false;
	ArrayList<Vertex> connections;
	ArrayList<Edge> edgelist;
	boolean inMST = false;
	
	public Vertex(int id){
		
		this.id = id;
		connections = new ArrayList<Vertex>();
		edgelist = new ArrayList<Edge>();
	}
	
	public static Vertex find(Vertex v){
		
		if(v.leader != null){
			//using the path compression heuristic
			v.leader = find(v.leader);
		}
		return v;
	}
	
	public static boolean checkCycle(Vertex v1, Vertex v2){
		
		if(find(v1).id == find(v2).id){
			return true;
		}else{
			return false;
		}
	}
	
	public static void union(Vertex v1, Vertex v2){
		
		if(v1.rank > v2.rank){
			v2.leader = v1;
		}else{
			v1.leader = v2;
			if(v1.rank == v2.rank){
				v2.rank = v1.rank+1;
			}
		}
	}

	public boolean isNeighbor(Vertex v){
		
		for(int i = 0; i<connections.size(); i++){
			if (connections.get(i).id == v.id){
				return true;
			}
		}
		
		return false;
	}
	
	public int compareFringeDistance(Vertex v){
		
		if (this.distToFringe > v.distToFringe ){
			return 1;
		}else if (this.distToFringe < v.distToFringe){
		   return -1;
		}else{
		   return 0;
		}
		
	}
	
	@Override
	public int compareTo(Vertex v) {
		
		if (this.id > v.id ){
			return 1;
		}else if (this.id < v.id){
		   return -1;
		}else{
		   return 0;
		}
	}
		
}


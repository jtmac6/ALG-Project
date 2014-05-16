

public class Edge implements Comparable<Edge> {
	
	int id;
	Vertex src;
	Vertex dest;
	int weight;
	String name;
	
	public Edge(Vertex v1, Vertex v2, int w){
		
		this.src = v1;
		this.dest = v2;
		this.weight = w;
		this.name = src.id + "--"+weight+"--"+dest.id;
		
	}
	
	@Override
	public int compareTo(Edge e) {
		
		
		if(this.weight>e.weight){
			return 1;
		}else if(this.weight<e.weight){
			return -1;
		}else{
			
			
			if (this.src.id > e.src.id ){
				return 1;
			}else if (this.src.id < e.src.id){
			   return -1;
			}else{
				
				if (this.dest.id > e.dest.id ){
					return 1;
				}else if (this.dest.id < e.dest.id){
				   return -1;
				}else{
				   return 0;
				}  
			}
		}
		
	}
	
	
	public String ToString(){
		return src.id + " " + dest.id + " weight = "+ weight;
	}
	
}

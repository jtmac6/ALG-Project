

public class Edge implements Comparable<Edge> {
	
	Vertex left;
	Vertex right;
	int weight;
	
	public Edge(Vertex v1, Vertex v2, int w){
		
		this.left = v1;
		this.right = v2;
		this.weight = w;
	}
	
	@Override
	public int compareTo(Edge e) {
		
		
		if(this.weight>e.weight){
			return 1;
		}else if(this.weight<e.weight){
			return -1;
		}else{
			
			
			if (this.left.id > e.left.id ){
				return 1;
			}else if (this.left.id < e.left.id){
			   return -1;
			}else{
				
				if (this.right.id > e.right.id ){
					return 1;
				}else if (this.right.id < e.right.id){
				   return -1;
				}else{
				   return 0;
				}  
			}
		}
		
	}
	
	
	public String ToString(){
		return left.id + " " + right.id + " weight = "+ weight;
	}
	
}

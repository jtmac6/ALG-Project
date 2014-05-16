
public class VEdge {

	public Vertex dest;
	public int weight;
	
	public VEdge (Vertex d, int w){
		this.dest = d;
		this.weight = w;
	}

	public int compareTo(VEdge e) {
	
		if(this.weight>e.weight){
			return 1;
		}else if(this.weight<e.weight){
			return -1;
		}else{
			return 0;
		}
	}	
	
}

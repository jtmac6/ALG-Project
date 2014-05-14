import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Stack;
public class Graph {
	
	//The adjacency matrix
	public static ArrayList<ArrayList<Integer>> matrix;
	//The adjacency list
	public static ArrayList<Vertex> vertices  = new ArrayList<Vertex>();
	
	//The path taken by the depth first search
	public ArrayList<Integer> dfsPath = new ArrayList<Integer>();
	
	//The sorted list of edges
	public static ArrayList<Edge> edgeList = new ArrayList<Edge>();
	
	//graph parameters
	public static int n;
	public int seed;
	public double p;
	public int secondseed = 2 * seed;
	public Random rand1;
	public Random rand2;
	public Graph(int n, int seed, double p){
		
		this.seed = seed;
		this.n = n;
		this.p = p;
		this.secondseed = 2*seed;
		this.rand1 = new Random(seed);
		this.rand2 = new Random(secondseed);
		
	}
	
	//generates a matrix candidate
	public ArrayList<ArrayList<Integer>> generateMatrix(){
		//create a blank matrix
		ArrayList<ArrayList<Integer>> matrix = new ArrayList<ArrayList<Integer>>();
		for (int r = 0; r<n;r++){
			ArrayList<Integer> row = new ArrayList<Integer>();
			for (int c = 0; c<n;c++){
				row.add(0);
			}
			matrix.add(row);
		}
		//Add weighted connections
		for (int r = 0; r<matrix.size();r++){
			ArrayList<Integer> row = matrix.get(r);
			for (int c=r+1; c<row.size();c++){
				double rnum = rand1.nextDouble();
				if(rnum<=p){
					int weight = rand2.nextInt((n-1)+1) +1;
					//set the connection weight for the current node
					row.set(c, weight);
					//set the connection weight for the connected node
					matrix.get(c).set(r, weight);
				}
			}
		}
		return matrix;
	}
	
	//generate a graph
	public ArrayList<Vertex> generateGraph(){
	
		long start = System.currentTimeMillis();
		System.out.println("TEST: n="+n+", seed="+seed+", p="+p);
		//generate a matrix candidate
		matrix = generateMatrix();
		//create n nodes
		for(int i = 0; i<n; i++){
			Vertex v = new Vertex(i);
			vertices.add(v);
		}
		//using the matrix candidate construct the adjacency list candidate
		for(int r=0;r<matrix.size();r++){
			ArrayList<Integer> row = matrix.get(r);
			for(int c=0; c<row.size();c++){
				if(row.get(c)>0){
					//TODO incorporate weights into the adjacency list
					int weight = row.get(c);
					Vertex rv = vertices.get(r);
					Vertex cv = vertices.get(c);
					rv.connections.add(cv);
					Edge e = new Edge(rv, cv, weight);
					rv.edgelist.add(e);
				}
			}
		}
		//sort each vertice's connection list by id number for the DFS
		for(int i = 0; i<vertices.size();i++){
			Collections.sort(vertices.get(i).connections);
		}
		//Run the DFS on the adjacency list candidate
		DFS(vertices.get(0));
		
		//Check if the graph is connected
		if(IsConnected()){
			long end = System.currentTimeMillis();
			long time = end-start;
			System.out.println("Time to generate the graph: "+time+" milliseconds");
			//print the output if the graph is small enough
			if(vertices.size()<10){
				printOutput();
			}
			return vertices;
			
		}else{
			//generate a new graph if the candidate wasn't connected
			clearGraph();
			generateGraph();
		}
		return vertices;
	}
	
	//Create a list of edges using the adjacency matrix
	public static ArrayList<Edge> assembleEdgeListWithMatrix(){
		for(int r=0; r<matrix.size(); r++){
			ArrayList<Integer> row = matrix.get(r);
			for(int c=r+1; c<row.size();c++){
				int weight = row.get(c);
				if(weight!=0){
					Edge e = new Edge(vertices.get(c), vertices.get(r), weight);
					//System.out.println("Adding: " + e.ToString());
					edgeList.add(e);
				}
			}
		}
		return edgeList;
	}
	
	
	public static boolean duplicateEdge(ArrayList<Edge> edgeList, Edge e){
		
		for(int i = 0; i<edgeList.size();i++){
			Edge temp = edgeList.get(i);
			if(temp.left.id == e.right.id){
				return true;
			}
		}
		return false;
	}
	
	public static ArrayList<Edge> assembleEdgeListWithList(){
		
		ArrayList<Edge> edgeList = new ArrayList<Edge>();
		
		for (int i = 0; i < vertices.size(); i++){
			Vertex v1 = vertices.get(i);
			for (int j = 0; j < v1.connections.size(); j++){
				
				Vertex v2 = v1.connections.get(j);
				int weight = matrix.get(i).get(v2.id);
				Edge e = new Edge(v1, v2, weight);
				
				if(!duplicateEdge(edgeList, e)){
					edgeList.add(e);
				}
			}
		}

		return edgeList;		
	}
	
		
	//Sorting Methods	
	public static ArrayList<Edge> countSort(ArrayList<Edge> edges, int R){
		int N = edges.size();
		int[] count = new int[R+1];
		Edge[] aux = new Edge[N];
		for(int i = 0; i<N; i++){
			count[edges.get(i).weight+1]++;
		}
		for (int r = 0; r<R; r++){
			count[r+1] += count[r];
		}
		for(int i = 0; i<N; i++){
			aux[count[edges.get(i).weight]++] = edges.get(i);
		}
		for(int i = 0; i<N; i++){
			
			edges.set(i, aux[i]);
		}
		return edges;
	}
	
	public static void insertionSort(ArrayList<Edge> edges){
		for(int i = 1; i<edges.size(); i++){
			Edge temp = edges.get(i);
			int j;
			for(j=i-1; j>=0 && temp.compareTo(edges.get(j))==-1; j--){
				edges.set(j+1, edges.get(j));
			}
			edges.set(j+1, temp);
		}
	}
	
	public static void swap(ArrayList<Edge> edges, int i, int j){
		Edge temp = edges.get(i);
		edges.set(i, edges.get(j));
		edges.set(j, temp);
	}
	
	public static int partition(ArrayList<Edge> edges, int lo, int hi){
		int i = lo, j = hi+1;
		while(true){
			while(edges.get(++i).compareTo(edges.get(lo))==-1){
				if(i==hi){ break;}
			}
			while(edges.get(lo).compareTo(edges.get(--j))==-1){
				if(j==lo){ break;}
			}
			if(i>=j){break;}
			swap(edges, i, j);
		}
		swap(edges,lo,j);
		return j;
	}
	
	public static void quickSort(ArrayList<Edge> edges){
		Collections.shuffle(edges);
		qSort(edges, 0, edges.size()-1);
	}
	
	public static void qSort(ArrayList<Edge> edges, int lo, int hi){
		if (hi <= lo){
			return;
		}
		int j = partition(edges, lo, hi);
		qSort(edges, lo, j-1);
		qSort(edges,j+1, hi);
	}
	
	
	//Algorithms
	
	public static ArrayList<Edge> Kruskal(ArrayList<Edge> edgeList){
		
		ArrayList<Edge> treeEdges = new ArrayList<Edge>();
		
		int edgeCount = 0;
		while(edgeCount < (n-1)){
			
			Edge e = edgeList.remove(0);
			Vertex v1 = e.left;
			Vertex v2 = e.right;
			if(!Vertex.checkCycle(v1, v2)){
				Vertex.union(v1, v2);
				edgeCount++;
				treeEdges.add(e);
			}
		}
		
		return treeEdges;
	}

	
	public static ArrayList<Edge> Prim(ArrayList<Edge> edgeList){
		
		printEdgeList(edgeList);
		ArrayList<Edge> MST = new ArrayList<Edge>();
		Heap h = new Heap();
		int mstSize = 1;
		Vertex curr = vertices.get(0);
		curr.inMST = true;
		
		//loop until the MST contains all the nodes
		while(mstSize!=vertices.size()){
			
			//the time complexity is a bit sketchy here
			//go through the nodes adjacent to the current node
			for(int i = 0; i<curr.connections.size(); i++){
				
				Vertex adjv = curr.connections.get(i);
				if(!adjv.inMST){
					//get the edges to that node.
					for(int j = 0; j<edgeList.size();j++){
						Edge e = edgeList.get(j);
						if(e.right.id == curr.id && e.left.id == adjv.id){
							System.out.println("Adding: "+ e.ToString());
							h.insert(e);
						}
					}
				}
				
			}
			
			//get the smallest edge out of the heap
			Edge smallest = h.remove();
			//if the adjacent node is not in the MST, add it
			if(smallest!=null && smallest.left!=null){
				
				if(!smallest.left.inMST){
					
					smallest.left.inMST=true;
					curr = smallest.left;
					MST.add(smallest);
					mstSize++;	
				}

			}
		}
		return MST;
	}
	
	//Utility Methods
	//Prints the edges in the edge list using the ToString method in the edge class
	public static void printEdgeList( ArrayList<Edge> edges){
		for(int i = 0; i<edges.size(); i++){
			System.out.println(edges.get(i).ToString());
		}
	}
	
	//Calculates the sum of all the weights in the edge list
	public static int sumWeights(ArrayList<Edge> edges){
		int sum = 0;
		for(int i = 0; i<edges.size(); i++){
			sum+=edges.get(i).weight;
		}
		return sum;
	}
	
	//Finds the largest weight in the edge list, this is used to calculate an R value for count sort
	public static int findMaxWeight(ArrayList<Edge> edgeList){
		int max = 0;
		for(int i = 0; i<edgeList.size(); i++){
			int weight = edgeList.get(i).weight;
			if(weight>max){max = weight;}
		}
		return max;
	}
	
	//Goes through the vertices and check if all of them have been marked as visited by the DFS
	//If this method returns true, the graph is connected.
	public boolean IsConnected(){
		int count = 0;
		//int unvisited = 0;
		for(int i = 0; i<vertices.size(); i++){
			if(vertices.get(i).visited){
				count++;
			}else{
				//unvisited++;
			}
		}
		if(count == vertices.size()){
			return true;
		}
		return false;
	}
	
	//Resets the graph, used when a candidate graph is not
	//connected and a new graph needs to be generated
	public void clearGraph(){
		vertices = new ArrayList<Vertex>();
	}
	
	//Resets the visited values for all the nodes in the graph
	public static void resetVisited(){
		for(int i = 0; i<vertices.size(); i++){
			vertices.get(i).visited = false;
		}
	}
	
	//Perform a Depth First Search on the graph
	//Used to check if the graph is connected
	public void DFS(Vertex start){
		Stack<Vertex> s = new Stack<Vertex>();
		s.push(start);
		while(!s.isEmpty()){
			Vertex v = (Vertex)s.pop();
			if(v.visited==false){
				v.visited=true;
				dfsPath.add(v.id);
			}
			for(int i = 0; i<v.connections.size();i++){
				
				if(!v.connections.get(i).visited){
					s.push(v.connections.get(i));
				}	
			}	
		}		
	}

	//Printing Methods
	
	//Prints all output to the terminal
	public void printOutput(){
		
		System.out.println();
		printAdjMatrix(matrix);
		System.out.println();
		printAdjList();
		System.out.println();
		printDFSPath();
		//printSortingOutput();
		printKruskalOutput();
		System.out.println();
		printPrimOutput();
	}
	
	//Prints the output for the sorting routines
	public static void printSortingOutput(){
		
		System.out.println("===================================");
		System.out.println("SORTED EDGES WITH MATRIX USING INSERTION SORT");
		long start1 = System.currentTimeMillis();
		edgeList = assembleEdgeListWithMatrix();
		insertionSort(edgeList);
		//edge printout
		printEdgeList(edgeList);
		System.out.println();
		//total weight
		System.out.println("Total weight = "+ sumWeights(edgeList));
		//runtime
		long end1 = System.currentTimeMillis();
		long runtime1 = end1-start1;
		System.out.println("Runtime: "+runtime1+" milliseconds");
		System.out.println();
		
		edgeList = new ArrayList<Edge>();
		
		System.out.println("===================================");
		System.out.println("SORTED EDGES WITH MATRIX USING COUNT SORT");
		long start2 = System.currentTimeMillis();
		edgeList = assembleEdgeListWithMatrix();
		int R = findMaxWeight(edgeList)+1;
		countSort(edgeList, R);
		//edge printout
		printEdgeList(edgeList);
		//total weight
		System.out.println("Total weight = "+ sumWeights(edgeList));
		//runtime
		long end2 = System.currentTimeMillis();
		long runtime2 = end2-start2;
		System.out.println("Runtime: "+runtime2+" milliseconds");
		System.out.println();
		
		edgeList = new ArrayList<Edge>();
		
		System.out.println("===================================");
		System.out.println("SORTED EDGES WITH MATRIX USING QUICKSORT");
		long start3 = System.currentTimeMillis();
		edgeList = assembleEdgeListWithMatrix();
		quickSort(edgeList);
		//edge printout
		printEdgeList(edgeList);
		System.out.println();
		//total weight
		System.out.println("Total weight = "+ sumWeights(edgeList));
		//runtime
		long end3 = System.currentTimeMillis();
		long runtime3 = end3-start3;
		System.out.println("Runtime: "+runtime3+" milliseconds");
		System.out.println();
		
		edgeList = new ArrayList<Edge>();
		
		System.out.println("===================================");
		System.out.println("SORTED EDGES WITH LIST USING INSERTION SORT");
		long start4 = System.currentTimeMillis();
		edgeList = assembleEdgeListWithList();
		insertionSort(edgeList);
		//edge printout
		printEdgeList(edgeList);
		System.out.println();
		//total weight
		System.out.println("Total weight = "+ sumWeights(edgeList));
		//runtime
		long end4 = System.currentTimeMillis();
		long runtime4 = end4-start4;
		System.out.println("Runtime: "+runtime4+" milliseconds");
		System.out.println();
		
		edgeList = new ArrayList<Edge>();
		
		System.out.println("===================================");
		System.out.println("SORTED EDGES WITH LIST USING COUNT SORT");
		long start5 = System.currentTimeMillis();
		edgeList = assembleEdgeListWithList();
		countSort(edgeList, R);
		//edge printout
		printEdgeList(edgeList);
		System.out.println();
		//total weight
		System.out.println("Total weight = "+ sumWeights(edgeList));
		//runtime
		long end5 = System.currentTimeMillis();
		long runtime5 = end5-start5;
		System.out.println("Runtime: "+runtime5+" milliseconds");
		System.out.println();
		
		edgeList = new ArrayList<Edge>();
		
		System.out.println("===================================");
		System.out.println("SORTED EDGES WITH LIST USING QUICKSORT");
		long start6 = System.currentTimeMillis();
		edgeList = assembleEdgeListWithList();
		quickSort(edgeList);
		//edge printout
		printEdgeList(edgeList);
		System.out.println();
		//total weight
		System.out.println("Total weight = "+ sumWeights(edgeList));
		//runtime
		long end6 = System.currentTimeMillis();
		long runtime6 = end6-start6;
		System.out.println("Runtime: "+runtime6+" milliseconds");
		System.out.println();
		
	}
	
	public void printKruskalOutput(){
		
		System.out.println("===================================");
		System.out.println("KRUSKAL WITH MATRIX USING INSERTION SORT");
		long start1 = System.currentTimeMillis();
		edgeList = assembleEdgeListWithMatrix();
		insertionSort(edgeList);
		ArrayList<Edge> mstEdges1 = Kruskal(edgeList);
		//edge printout
		printEdgeList(mstEdges1);
		System.out.println();
		//total weight
		System.out.println("Total weight of MST using Kruskal: "+ sumWeights(mstEdges1));
		//System.out.println("Total weight = "+ sumWeights(edgeList));
		//runtime
		long end1 = System.currentTimeMillis();
		long runtime1 = end1-start1;
		System.out.println("Runtime: "+runtime1+" milliseconds");
		System.out.println();
		
		edgeList = new ArrayList<Edge>();
		
		System.out.println("===================================");
		System.out.println("KRUSKAL WITH MATRIX USING COUNT SORT");
		long start2 = System.currentTimeMillis();
		edgeList = assembleEdgeListWithMatrix();
		int R = findMaxWeight(edgeList)+1;
		countSort(edgeList, R);
		ArrayList<Edge> mstEdges2 = Kruskal(edgeList);
		//edge printout
		printEdgeList(mstEdges2);
		//total weight
		System.out.println("Total weight of MST using Kruskal: "+ sumWeights(mstEdges2));
		//System.out.println("Total weight = "+ sumWeights(edgeList));
		//runtime
		long end2 = System.currentTimeMillis();
		long runtime2 = end2-start2;
		System.out.println("Runtime: "+runtime2+" milliseconds");
		System.out.println();
		
		edgeList = new ArrayList<Edge>();
		
		System.out.println("===================================");
		System.out.println("KRUSKAL WITH MATRIX USING QUICKSORT");
		long start3 = System.currentTimeMillis();
		edgeList = assembleEdgeListWithMatrix();
		quickSort(edgeList);
		ArrayList<Edge> mstEdges3 = Kruskal(edgeList);
		//edge printout
		printEdgeList(mstEdges3);
		System.out.println();
		//total weight
		System.out.println("Total weight of MST using Kruskal: "+ sumWeights(mstEdges3));
		//System.out.println("Total weight = "+ sumWeights(edgeList));
		//runtime
		long end3 = System.currentTimeMillis();
		long runtime3 = end3-start3;
		System.out.println("Runtime: "+runtime3+" milliseconds");
		System.out.println();
		
		edgeList = new ArrayList<Edge>();
		
		System.out.println("===================================");
		System.out.println("KRUSKAL WITH LIST USING INSERTION SORT");
		long start4 = System.currentTimeMillis();
		edgeList = assembleEdgeListWithList();
		//printEdgeList(edgeList);
		insertionSort(edgeList);
		ArrayList<Edge> mstEdges4 = Kruskal(edgeList);
		//edge printout
		printEdgeList(mstEdges4);
		System.out.println();
		//total weight
		System.out.println("Total weight of MST using Kruskal: "+ sumWeights(mstEdges4));
		//System.out.println("Total weight = "+ sumWeights(edgeList));
		//runtime
		long end4 = System.currentTimeMillis();
		long runtime4 = end4-start4;
		System.out.println("Runtime: "+runtime4+" milliseconds");
		System.out.println();
		
		edgeList = new ArrayList<Edge>();
		
		System.out.println("===================================");
		System.out.println("KRUSKAL WITH LIST USING COUNT SORT");
		long start5 = System.currentTimeMillis();
		edgeList = assembleEdgeListWithList();
		countSort(edgeList, R);
		ArrayList<Edge> mstEdges5 = Kruskal(edgeList);
		//edge printout
		printEdgeList(mstEdges5);
		System.out.println();
		//total weight
		System.out.println("Total weight of MST using Kruskal: "+ sumWeights(mstEdges5));
		//System.out.println("Total weight = "+ sumWeights(edgeList));
		//runtime
		long end5 = System.currentTimeMillis();
		long runtime5 = end5-start5;
		System.out.println("Runtime: "+runtime5+" milliseconds");
		System.out.println();
		
		edgeList = new ArrayList<Edge>();
		
		System.out.println("===================================");
		System.out.println("KRUSKAL WITH LIST USING QUICKSORT");
		long start6 = System.currentTimeMillis();
		edgeList = assembleEdgeListWithList();
		quickSort(edgeList);
		ArrayList<Edge> mstEdges6 = Kruskal(edgeList);
		//edge printout
		printEdgeList(mstEdges6);
		System.out.println();
		//total weight
		System.out.println("Total weight of MST using Kruskal: "+ sumWeights(mstEdges1));
		//System.out.println("Total weight = "+ sumWeights(edgeList));
		//runtime
		long end6 = System.currentTimeMillis();
		long runtime6 = end6-start6;
		System.out.println("Runtime: "+runtime6+" milliseconds");
		System.out.println();
		edgeList = new ArrayList<Edge>();
	}

	
	public void printPrimOutput(){
		
		System.out.println("===================================");
		System.out.println("PRIM WITH ADJACENCY MATRIX");
		long start1 = System.currentTimeMillis();
		edgeList = assembleEdgeListWithMatrix();
		//printEdgeList(edgeList);
		ArrayList<Edge> mstEdges1 = Prim(edgeList);
		//edge printout
		printEdgeList(mstEdges1);
		System.out.println();
		//total weight
		System.out.println("Total weight of MST using Prim: "+ sumWeights(mstEdges1));
		//System.out.println("Total weight = "+ sumWeights(edgeList));
		//runtime
		long end1 = System.currentTimeMillis();
		long runtime1 = end1-start1;
		System.out.println("Runtime: "+runtime1+" milliseconds");
		System.out.println();
		edgeList = new ArrayList<Edge>();
		
		System.out.println("===================================");
		System.out.println("PRIM WITH ADJACENCY LIST");
		long start4 = System.currentTimeMillis();
		edgeList = assembleEdgeListWithList();
		//printEdgeList(edgeList);
		ArrayList<Edge> mstEdges4 = Prim(edgeList);
		//edge printout
		printEdgeList(mstEdges4);
		System.out.println();
		//total weight
		System.out.println("Total weight of MST using Prim: "+ sumWeights(mstEdges4));
		//System.out.println("Total weight = "+ sumWeights(edgeList));
		//runtime
		long end4 = System.currentTimeMillis();
		long runtime4 = end4-start4;
		System.out.println("Runtime: "+runtime4+" milliseconds");
		System.out.println();
		edgeList = new ArrayList<Edge>();
	}
	
	//Prints out the adjacency matrix
	public void printAdjMatrix(ArrayList<ArrayList<Integer>> matrix){
		
		System.out.println("The graph as an adjacency matrix:");
		System.out.println();
		//print out the matrix
		for (int r = 0; r<matrix.size();r++){
			ArrayList<Integer> row = matrix.get(r);
			StringBuilder sb = new StringBuilder();
			sb.append(" ");
			for (int c = 0; c<row.size();c++){
				sb.append(row.get(c));
				sb.append("   ");
			}
			String line = sb.toString();
			System.out.println(line);
			System.out.println();
		}
		
	}

	//prints out the adjacency list
	public void printAdjList(){
		
		System.out.println("The graph as an adjacency list:");
		for(int i = 0; i<vertices.size(); i++){
			
			Vertex current = vertices.get(i);
			StringBuilder sb = new StringBuilder();
			sb.append(current.id);
			sb.append("-> ");
			ArrayList<Vertex> neighbors = current.connections;
			for(int j = 0; j<neighbors.size(); j++){
				int vertexid = neighbors.get(j).id;
				sb.append(vertexid);
				sb.append("(");
				sb.append(matrix.get(vertexid).get(current.id));
				sb.append(") ");
			}
			System.out.println(sb.toString());
		}
	}

	//prints out the DFS path
	public void printDFSPath(){
		
		System.out.println("Depth-First Search:");
		System.out.println("Vertices:");
		StringBuilder sb = new StringBuilder();
		for(int i = 0;i<vertices.size(); i++){
			
			sb.append(" ");
			sb.append(vertices.get(i).id);
		}
		System.out.println(sb.toString());
		System.out.println("Predecessors:");
		System.out.println("Not really sure how the predecessor values are computed");
		
	}
	
	

}

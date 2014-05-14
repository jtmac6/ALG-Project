import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
public class MST {
	
	
	public static void main(String[] args){
		
		//Get the file name from the command line arguments
		String filename = args[0];
		Graph g;
		
		try {
			//Read in the file
			BufferedReader in = new BufferedReader(new FileReader(filename));
			try {
				//Get the data from the file line by line
				String sn = in.readLine();
				String sseed = in.readLine();
				String sp = in.readLine();
				
				//convert the strings into integers and a double
				//catch errors when the integers do not meet the requirements
				Integer n = null;
				try {
					n = Integer.parseInt(sn);
					if(n<2){System.out.println("n must be greater than 1."); in.close(); return;}
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					System.out.println("n and seed must be integers");
					e.printStackTrace();
				}
				
				Integer seed = null;
				try {
					seed = Integer.parseInt(sseed);
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					System.out.println("n and seed must be integers");
					e.printStackTrace();
				}
			
				Double p = null;
				try {
					p = Double.parseDouble(sp);
					if(p<0 || p>1){System.out.println("p must be between 0 and 1");in.close(); return;}
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					System.out.println("p must be a real number");
					e.printStackTrace();
				}
				
				if(n!=null && seed!=null && p!=null){
					
					//Generate the graph
					//g.generateMatrix(n, seed, p);
					g = new Graph(n,seed,p);
					ArrayList<Vertex> graph = g.generateGraph();
				}
				
			in.close();	
			} catch (IOException e) {
				
				e.printStackTrace();
			}
			
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		}
		
	}

}

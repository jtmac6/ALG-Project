import java.util.ArrayList;
import java.util.List;

public class Heap {
	
    public List<Edge> edges;
    public int size;
    
    public Heap()
    {
        edges = new ArrayList<Edge>();
        edges.add(null);           
        size = 0;
    } 

    public void insert(Edge x)
    {
        edges.add(x);
        size++;
        int k = size;
        while (k > 1 && edges.get(k/2).compareTo(x) > 0) {
            edges.set(k,edges.get(k/2));
            k = k/2;
        }
        edges.set(k,x);
    } 


    public Edge remove() 
    {
        if (!isEmpty()) {
            Edge min = peek();
            edges.set(1,edges.get(size));
            edges.remove(edges.get(size));
            size--;
            if (size > 1) {
                heapify(1);
            }
            return min;
        }
        return null;
    }

    public Edge peek() 
    {
        return edges.get(1);
    }

    public boolean isEmpty()
    {
        if(size==0){return true;}
        return false;
    }
    
    private void heapify(int vroot)
    {
        Edge current = edges.get(vroot);
        int child;
        int k=vroot;

        while (2*k <= size) {
            child = 2*k;
            if (child < size &&    // right child exists
                // check if right < left
                 edges.get(child+1).compareTo(edges.get(child)) < 0) {
                child++;
            }

            // if item to be added <= child, stop and add the item
            
            if (current.compareTo(edges.get(child)) <= 0) {
                break;
            }
            else {
                edges.set(k,edges.get(child));
                k = child;
            }
        }
        edges.set(k,current);
    }
}

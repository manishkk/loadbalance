import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

//without orphan nodes
public class Random_Edges_Graph
{
    private Map<Integer, List<Integer>> adjacencyList;
    static List<Float> Load = new LinkedList<Float>();
    public static int v;
    static int neighbors = 0;
    static float AverageLoad = 0;

    /**
     * @param v
     */
    public Random_Edges_Graph(int v) {
        adjacencyList = new HashMap<Integer, List<Integer>>();
        for (int i = 1; i <= v; i++)
            adjacencyList.put(i, new LinkedList<Integer>());
    }
    
    //Add Random Edges
    public boolean setEdge(int to, int from) {
        if (to > adjacencyList.size() || from > adjacencyList.size())
            System.out.println("The vertices does not exists");
        List<Integer> sls = adjacencyList.get(to);
        List<Integer> dls = adjacencyList.get(from);
        if (sls.contains(from) || dls.contains(to)) return false;
        sls.add(from);
        dls.add(to);
        System.out.println("Edge exist from "+ from + " to "+ to);
        return true;
    }
    public List<Integer> getEdge(int to) {
        if (to > adjacencyList.size()) {
            System.out.println("The vertices does not exists");
            return null;
        }
        return adjacencyList.get(to);
    }
    
    // return average load on vertex
    int neighbourCount;
    public float getAverageLoadOnVertex(int vertex, List<Integer> neighbours){
        neighbourCount = neighbours.size();
        //System.out.println("Vertex " + vertex + " have "+neighbourCount + " neighbors " );
        float totalLoad = Load.get(vertex-1);
        for (int i = 0; i < neighbourCount; i++) {
            totalLoad += Load.get(neighbours.get(i)-1);
        }
        // return (int) Math.round(totalLoad / (neighbourCount + 1)); // TODO: Remove Round
        System.out.println("Average Load on vertex "+ vertex + " is " +  totalLoad / (neighbourCount + 1));
        return totalLoad / (neighbourCount + 1); // TODO: Remove Round
    }
    
    // return vertices with less load than average
    public List<Integer> getRecievers(List<Integer> neighbours, float averageLoad) {
        int neighbourCount = neighbours.size();
        List<Integer> recievers = new LinkedList<Integer>();
        for (int i = 0; i < neighbourCount; i++) {
            if (Load.get(neighbours.get(i)-1) < averageLoad) 
            	recievers.add(neighbours.get(i));
        }
        return recievers;
    }
    
 // return vertices with more load than average
    public List<Integer> getGivers(List<Integer> neighbours, float averageLoad) {
        int neighbourCount = neighbours.size();
        List<Integer> Givers = new LinkedList<Integer>();
        for (int i = 0; i < neighbourCount; i++) {
            if (Load.get(neighbours.get(i)-1) > averageLoad) 
            	Givers.add(neighbours.get(i));
        }
        return Givers;
    }
    
    // share load to neighbours who have less load than average
    public void shareLoadToRecievers(int vertex, List<Integer> recievers, float load) {
        int recieversCount = recievers.size();
     
        for (int i = 0; i < recieversCount; i++) {
            Load.set(recievers.get(i) - 1, Load.get(recievers.get(i) - 1) + load);
        }
    }
    
    // check if graph is stabilized
    public boolean validateStabilization() {
        int vertexCount = Load.size();
        int firstVertexLoad = (int)Math.floor(Load.get(0));
        for (int vertex = 1; vertex < vertexCount; vertex++) {
            if ((int)Math.floor(Load.get(vertex)) != firstVertexLoad) {
                System.out.println("First not matching load on vertex: " + (vertex + 1));
                return false;
            }
        }
        return true;
    }
    // Generate Random Number
    public static float getRandomIntegerBetweenRange(float min, float max){
//    	float randomValue = ((Math.random()*((max-min)+1))+min);
//    	float roundOff = BigDecimal.valueOf(randomValue).setScale(3,RoundingMode.HALF_UP).doubleValue();
//    	return roundOff;
    	return (float) ((Math.random()*((max-min)+1))+min);
    }

    public static void main(String args[]) {
        System.out.println("Enter the number of edges: ");
        Scanner sc = new Scanner(System.in);
        int e = sc.nextInt();
        try {
            int minV = (int) Math.ceil((Math.sqrt(1 + 2 * e)));
            int maxV = e + 1;
            Random random = new Random();
            
            v = Math.abs(random.nextInt(maxV - minV) + minV);
            System.out.println("Random graph has "+ v +" vertices");
            Random_Edges_Graph reg = new Random_Edges_Graph(v);

            // Generate random edges
            int count = 1, to, from;
            for (; count < v; count++) {
                reg.setEdge(count, count + 1);
            }
            while (count <= e) {
                to = random.nextInt(v) + 1;
                from = random.nextInt(v) + 1;
                if (to == from) continue;
                if (reg.setEdge(to, from)) count++;
            }

            // Generate Random Loads for each vertex
            for (int i = 1; i <= v; i++) {
                Load.add(getRandomIntegerBetweenRange(2,100));
            }
                        
            System.out.println("Initial loads on the nodes: "+ Load+ "\n");
            // System.out.println("The Adjacency List Representation of the random graph is as below: ");
            System.out.println("Maximum Value: " + Collections.max(Load));
            System.out.println("Minimum Value: " + Collections.min(Load));
            
            int iterationCount = 1;
            while (iterationCount <= 1000) {
                for (int vertex = 1; vertex <= v; vertex++) {
                	float vertexLoad = Load.get(vertex - 1);
                	List<Integer> MaxEdgeList = null;
                	List<Integer> MinEdgeList = null;
                    List<Integer> edgeList = reg.getEdge(vertex);
                    System.out.println("Individual load at vertex " + vertex + " is: " + vertexLoad + " :neighbours: " + edgeList);
                    float averageLoad = reg.getAverageLoadOnVertex(vertex, edgeList);
                    
                	if (averageLoad < vertexLoad) {
                		MaxEdgeList = reg.getEdge(vertex);
                		
                	}
                	
                	if (averageLoad > vertexLoad) {
                		MinEdgeList = reg.getEdge(vertex);
                		
                	}
                	
                    //System.out.println("averageLoad " + averageLoad);
                    if (averageLoad < vertexLoad) {
                    	float loadToBeShared = vertexLoad - averageLoad;
                        //System.out.println("loadToBeShared " + loadToBeShared);
                        List<Integer> recievers = reg.getRecievers(MaxEdgeList, averageLoad);
                        //recievers.add(vertex);
                       // System.out.println("recievers other than me " + recievers);
                       
	                        if (recievers.size() > 0) {
	                        	float loadToBeSharedPerReciever = loadToBeShared / (recievers.size()+1);
	                        	

	                            //System.out.println("loadToBeSharedPerReciever " + loadToBeSharedPerReciever + "  on each node of S-min");
	                        	
	                           
	                            //call this function when all propo
	                            reg.shareLoadToRecievers(vertex, recievers, loadToBeSharedPerReciever);
	                            Load.set(vertex - 1, (vertexLoad-(loadToBeSharedPerReciever*recievers.size())));
	                            
	                            //System.out.println("Node " + vertex + " can give " + loadToBeSharedPerReciever + " load to " + recievers);
	                            for (int i = 0; i < recievers.size(); i++) {
	                            	 System.out.println("Node " + vertex + " can give " + loadToBeSharedPerReciever + " load to node " + recievers.get(i));
	                            }
	                            
	                            //Load.set(vertex - 1, vertexLoad - loadToBeSharedPerReciever * recievers.size());
	                            //System.out.println("Current load of this node " + (vertexLoad-(loadToBeSharedPerReciever*recievers.size())));
	                        }
                    }
                    
                    if (averageLoad > vertexLoad) {
                    //System.out.println("Now I will give Proposals to neighbor about my capacity");
                    float loadToBeTaken = averageLoad - vertexLoad;
                    //System.out.println("Total Load to be taken from neighbours "+ loadToBeTaken);	
                    	
                    List<Integer> Givers = reg.getGivers(MinEdgeList, averageLoad);
                    //System.out.println("Nodes who can give me load " + Givers);	
                    	
                    	if(Givers.size() > 0) {
                    		float loadToBeGivenToNode = loadToBeTaken/Givers.size();
                    		//System.out.println("loadToBeGivenToNode " + loadToBeGivenToNode + "  from each node of S-max");
                    		//System.out.println("Node " + vertex + " can take " + loadToBeGivenToNode + " load from " + Givers);
                    		for (int i = 0; i < Givers.size(); i++) {
                    			System.out.println("Node " + vertex + " can take " + loadToBeGivenToNode + " load from node " + Givers.get(i));
                           }
                    		
                    	}
                    	
                    }
                    
                }
                System.out.println("=====================================");
                if (reg.validateStabilization()) break;
                System.out.println("Load after iteration " + iterationCount + ": " + Load);
                iterationCount++;
                System.out.println("Maximum Value: " + Collections.max(Load));
                System.out.println("Minimum Value: " + Collections.min(Load));
            }

            System.out.println("=====================================");
            System.out.println("========== Graph Stabilized =========");
            System.out.println("Edge Count: " + e);
            System.out.println("Vertex Count: " + v);
            System.out.println("Graph stabilized on iteration: " + iterationCount);
            System.out.println("Load: " + Load);
            System.out.println("=====================================");
            System.out.println("Maximum Value: " + Collections.max(Load));
            System.out.println("Minimum Value: " + Collections.min(Load));
        } catch (Exception E) {
            System.out.println("Something went wrong: " + E);
        }
        sc.close();
    }
}
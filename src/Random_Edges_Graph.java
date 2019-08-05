import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
//With Potential Function = Individual maximum Load - Individual minimum load
//without orphan nodes
public class Random_Edges_Graph
{
    private Map<Integer, List<Integer>> adjacencyList;
    static List<Float> Load = new LinkedList<Float>();
    static List<Float> PotentialLoad = new LinkedList<Float>();
    static List<Float> PotentialSum = new LinkedList<Float>();
    static List<Float> NewPotentialSum = new LinkedList<Float>();
    public static int v;
    static int neighbors = 0;
    float AvgLoad;
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
    static float potential;
    
    public float getAverageLoadOnVertex(int vertex, List<Integer> neighbours){
    	
        neighbourCount = neighbours.size();
        //System.out.println("Vertex " + vertex + " have "+neighbourCount + " neighbors " );
        float totalLoad = Load.get(vertex-1);
        for (int i = 0; i < neighbourCount; i++) {
            totalLoad += Load.get(neighbours.get(i)-1);
           
        }
        AvgLoad = totalLoad / (neighbourCount + 1);
        // return (int) Math.round(totalLoad / (neighbourCount + 1)); // TODO: Remove Round
        System.out.println("Average Load on vertex "+ vertex + " is " +  AvgLoad);
		/*
		 * potential = Math.abs(Load.get(vertex-1) - AvgLoad);
		 * System.out.println("Individual Potential VALUE "+ potential);
		 * PotentialLoad.add(potential);
		 */
        return AvgLoad;
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
    
	/*
	 * public boolean checkFlag(List<Integer> flags) { //
	 * System.out.println("I am here"); int numberOfVertices = flags.size(); for(int
	 * vertex = 0; vertex < numberOfVertices; vertex++) { if (flags.get(vertex) !=
	 * 1) return false; } return true; }
	 */
    // Generate Random Number
    public static float getRandomIntegerBetweenRange(float min, float max){
//     float randomValue = ((Math.random()*((max-min)+1))+min);
//     float roundOff = BigDecimal.valueOf(randomValue).setScale(3,RoundingMode.HALF_UP).doubleValue();
//     return roundOff;
    return (int) ((Math.random()*((max-min)+1))+min);
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
                Load.add(getRandomIntegerBetweenRange(2,10000));
               
            }
                       
            System.out.println("Initial loads on the nodes: "+ Load+ "\n");
            // System.out.println("The Adjacency List Representation of the random graph is as below: ");
            System.out.println("Maximum Value: " + Collections.max(Load));
            System.out.println("Minimum Value: " + Collections.min(Load));
            PotentialSum.add(Collections.max(Load) - Collections.min(Load));
            
            int iterationCount = 1;
            //List<Integer> flags;
            while (iterationCount <= 1000) {
            Map<String, List<Float>> nodePair=new HashMap<String, List<Float>>();
            	//flags = new LinkedList<Integer>();
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
                
//**************Case 1*******************                
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
                            //add some function when new proposal comes // TODO
                            //reg.shareLoadToRecievers(vertex, recievers, loadToBeSharedPerReciever);
                            //Load.set(vertex - 1, (vertexLoad-(loadToBeSharedPerReciever*recievers.size())));
                           
                           
                           
                            //System.out.println("Node " + vertex + " can give " + loadToBeSharedPerReciever + " load to " + recievers);
                            for (int i = 0; i < recievers.size(); i++) {
                            System.out.println("Node " + vertex + " can give " + loadToBeSharedPerReciever + " load to node " + recievers.get(i));
                            String key=vertex+"_"+recievers.get(i);
                            if(!nodePair.containsKey(key)) {
                            List<Float> loadList=new ArrayList<Float>();
                            loadList.add(loadToBeSharedPerReciever);
                            nodePair.put(key,loadList);
                            }else {
                            List<Float> list = nodePair.get(key);
                            list.add(loadToBeSharedPerReciever);
                            nodePair.put(key,list);
                            }
                            //IterativeLoad.set(vertex-1, IterativeLoad.get(vertex-1)- loadToBeSharedPerReciever);
                            //IterativeLoad.set(recievers.get(i)-1, IterativeLoad.get(vertex-1) + loadToBeSharedPerReciever);
                         
                            }
                           
                            //Load.set(vertex - 1, vertexLoad - loadToBeSharedPerReciever * recievers.size());
                            //System.out.println("Current load of this node " + (vertexLoad-(loadToBeSharedPerReciever*recievers.size())));
                        }
                       // flags.add(0);
                   
                    }
   
//**************Case 2*******************                    
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
                    String key=Givers.get(i)+"_"+vertex;
                            if(!nodePair.containsKey(key)) {
                            List<Float> loadList=new ArrayList<Float>();
                        loadList.add(loadToBeGivenToNode);
                        nodePair.put(key,loadList);
                            }else {
                        List<Float> list = nodePair.get(key);
                        list.add(loadToBeGivenToNode);
                        nodePair.put(key,list);
                        }
                           }
                   
                    }
                   
                   
                    }
                    
                	
                	 if (averageLoad == vertexLoad) {
                		//Updte code here
                		 System.out.println("case 3 occured");
                		 Float localM=0F;
                		 Integer posLocalM;
                		 //calculate LocalM from Smax
                		 for(Integer vertLocalM:MaxEdgeList) {
                			 List<Integer> vertEdgeList = reg.getEdge(vertLocalM);
                			 float avgLoadLocalM = reg.getAverageLoadOnVertex(vertLocalM, vertEdgeList);
                			 if(localM<averageLoad) {
                				 localM=avgLoadLocalM;
                				 posLocalM=vertLocalM;
                			 }
                			 
                		 }
                		 //Chose Alpha from s.t < Local M.
                		 //For time being asssume Alpha as 0.1
                		 Float alpha=0.1F;
                		 Float SumGiversToVertex=0.0F;
                		 for (Integer k: MaxEdgeList) {
                			 float vertexLoadLocalM = Load.get(k - 1);
                			 float averageLoadLocalM = reg.getAverageLoadOnVertex(k, reg.getEdge(k));
                			 float loadToBeShared = vertexLoadLocalM - averageLoadLocalM;
                			 List<Integer> recievers = reg.getRecievers(MaxEdgeList, averageLoad);
                			 float loadToBeSharedPerReciever = loadToBeShared / (recievers.size()+1);
                			 if (averageLoad < vertexLoad) {
                                 
                                     //System.out.println("loadToBeShared " + loadToBeShared);
                                     
                                     //recievers.add(vertex);
                                    // System.out.println("recievers other than me " + recievers);
                                     if (recievers.size() > 0) {
                                     
                                     System.out.println("Node " + vertex + " can take " + (alpha-loadToBeSharedPerReciever) + " load from node " + k);
                                     SumGiversToVertex+=loadToBeSharedPerReciever;
                                     }
                                     String key=vertex+"_"+k;
                                     if(!nodePair.containsKey(key)) {
                                     List<Float> loadList=new ArrayList<Float>();
                                 loadList.add((alpha-loadToBeSharedPerReciever));
                                 nodePair.put(key,loadList);
                                     }else {
                                 List<Float> list = nodePair.get(key);
                                 list.add((alpha-loadToBeSharedPerReciever));
                                 nodePair.put(key,list);
                                 }
                			 }
                			 
                			 
                		 }
						 if((SumGiversToVertex*alpha) < localM) {
							 System.out.println("Its correct its less than localM");
						 }
                	 }
                   
                   
                }
                
                //Iterate over the nodePair to distribute the minimum load to particular Node or Vertex! (Change according to urs)
                Set<Map.Entry<String, List<Float>>> st= nodePair.entrySet();
                for(Map.Entry<String, List<Float>> entry:st) {
                System.out.println("------------------------");
                	//Debugging
                
                System.out.println(entry.getKey()+entry.getValue());
                String key=entry.getKey();
                String nodeList[] =(key.split("_"));
                Integer fromNode=Integer.parseInt(nodeList[0]);
                Integer toNode=Integer.parseInt(nodeList[1]);
                Float minLoad=Collections.min(entry.getValue());
               
                System.out.println("Minimum transfer Load is "+ minLoad);
                //System.out.println("("+(fromNode-1)+")"+Load.get(fromNode-1));
                //System.out.println("("+(toNode-1)+")"+Load.get(toNode-1));
                
            	System.out.println("Old values from node "+(fromNode)+" with load "+Load.get(fromNode-1)+" to node "+(toNode)+" with load "+Load.get(toNode-1));
                Float newValueFromNode=Load.get(fromNode-1)-minLoad;
                Float newValueToNode=Load.get(toNode-1)+minLoad;
                //Update the Load value of vertices
                Load.set(fromNode-1, newValueFromNode);
                Load.set(toNode-1, newValueToNode);
                
                System.out.println("New values from node "+(fromNode)+" with load "+Load.get(fromNode-1)+" to node "+(toNode)+ " with load "+Load.get(toNode-1));
                
                }
               
                System.out.println("==========================================================================");
                if (reg.validateStabilization()) break;
                //if (reg.checkFlag(flags)) break;
                System.out.println("Load after iteration " + iterationCount + ": " + Load);
                iterationCount++;
                System.out.println("Maximum Value: " + Collections.max(Load));
                System.out.println("Minimum Value: " + Collections.min(Load));
                PotentialSum.add(Collections.max(Load) - Collections.min(Load));
				/*
				 * System.out.println("PotentialLoad: " + PotentialLoad); float PotSum =0;
				 * for(int i=0 ; i < PotentialLoad.size(); i++) { PotSum +=
				 * PotentialLoad.get(i); } PotentialSum.add(PotSum);
				 */
            }

            System.out.println("=====================================");
            System.out.println("========== Graph Stabilized =========");
            System.out.println("Edge Count: " + e);
            System.out.println("Vertex Count: " + v);
            System.out.println("Graph stabilized on iteration: " + iterationCount);
            System.out.println("Load: " + Load);
			/*
			 * System.out.println("Sum of PotentialLoad: " + PotentialSum); for(int i=0 ; i
			 * < PotentialSum.size(); i++) { if ( i== 0) NewPotentialSum.add(i,
			 * PotentialSum.get(i)); else NewPotentialSum.add(i,
			 * PotentialSum.get(i)-PotentialSum.get(i-1)); }
			 * System.out.println("New Sum of PotentialLoad: " + NewPotentialSum);
			 */
            System.out.println("=====================================");
            System.out.println("Maximum Value: " + Collections.max(Load));
            System.out.println("Minimum Value: " + Collections.min(Load));
            PotentialSum.add(Collections.max(Load) - Collections.min(Load));
            System.out.println("Vaule of Extreme Potential Load in Each Iteration: " + PotentialSum);
        } catch (Exception E) {
            System.out.println("Something went wrong: " + E);
        }
        sc.close();
    }
}

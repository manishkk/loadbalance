import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
 
import static java.util.stream.Collectors.*;

import java.util.Comparator;

import static java.util.Map.Entry.*;

public class MinMax {
private Map<Integer, List<Integer>> adjacencyList;
static List<Float> Load = new LinkedList<Float>();
public static int v;
static int neighbors = 0;


public MinMax(int v) {
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
   public static float getRandomIntegerBetweenRange(float min, float max){
//      float randomValue = ((Math.random()*((max-min)+1))+min);
//      float roundOff = BigDecimal.valueOf(randomValue).setScale(3,RoundingMode.HALF_UP).doubleValue();
//      return roundOff;
    return (int) ((Math.random()*((max-min)+1))+min);
    }
   
   public static void main(String[] args) {
System.out.println("Enter the number of edges: ");
Scanner sc = new Scanner(System.in);
int e = sc.nextInt();
int minV = (int) Math.ceil((Math.sqrt(1 + 2 * e)));
int maxV = e + 1;
Random random = new Random();

v = Math.abs(random.nextInt(maxV - minV) + minV);
System.out.println("Random graph has " + v + " vertices");
MinMax minMaxGraph= new MinMax(v);
// Generate random edges
        int count = 1, to, from;
        for (; count < v; count++) {
        minMaxGraph.setEdge(count, count + 1);
        }
        while (count <= e) {
            to = random.nextInt(v) + 1;
            from = random.nextInt(v) + 1;
            if (to == from) continue;
            if (minMaxGraph.setEdge(to, from)) count++;
        }

        // Generate Random Loads for each vertex
        for (int i = 1; i <= v; i++) {
            Load.add(getRandomIntegerBetweenRange(2,10000));
           
        }
        System.out.println(""+minMaxGraph.adjacencyList.toString());
        System.out.println("Load:"+minMaxGraph.Load);
        int iterationCount = 1;
        while(!checkForBalance(minMaxGraph.adjacencyList,minMaxGraph.Load)) {
        //System.out.println("Not balanced");
       
        while (iterationCount <= 10) {
         System.out.println("Iteration_______________________________________________BEGIN"+iterationCount);
for (int i = 0; i < minMaxGraph.adjacencyList.size(); i++) {
 System.out.println("Node with number:" +i);
// Min Transfer
Map<Integer, Float> sMin = new HashMap<>();
// List<Integer> sMax=new LinkedList<>();
Map<Integer, Float> sMax = new HashMap<>();


sMin = findSmin(minMaxGraph.adjacencyList, minMaxGraph.Load, minMaxGraph.Load.get(i).intValue());
//System.out.println("SMin list" + sMin);

int loadNode = (int) (minMaxGraph.Load.get(i).floatValue());
int roundRobin = 0;
if (!sMin.isEmpty()) {

Map.Entry<Integer, Float> entry = sMin.entrySet().iterator().next();
int minLoad = (int) (entry.getValue().floatValue());

int loadToTransfer = loadNode - minLoad + 1;

System.out.println("Load to Transfer:" + loadToTransfer);
// Transfer load to S_min in RoundRobin

while (loadToTransfer >= 0) {
Set<Entry<Integer, Float>> entrySet = sMin.entrySet();
Map.Entry<Integer, Float> element = (Map.Entry<Integer, Float>) entrySet
.toArray()[roundRobin];
Integer key = element.getKey();

minMaxGraph.Load.set((element.getKey().intValue()) - 1,
minMaxGraph.Load.get(key.intValue() - 1) + 1);
minMaxGraph.Load.set(i, minMaxGraph.Load.get(i) - 1);
if (roundRobin < sMin.size())
roundRobin = roundRobin + 1;
if (roundRobin == sMin.size()) {
// reset to start again
roundRobin = 0;
}

loadToTransfer = loadToTransfer - 1;
}
}
System.out.println("Load after Min Transfer ******* " + minMaxGraph.Load);
//minMaxGraph.Load.sort((o1,o2)->o1.compareTo(o2));  
// SMAX
sMax = findSmax(minMaxGraph.adjacencyList, minMaxGraph.Load, minMaxGraph.Load.get(i).intValue());
//System.out.println("SMax list" + sMax);

if (!sMax.isEmpty()) {

Map.Entry<Integer, Float> entryMax = sMax.entrySet().iterator().next();
int maxLoad = (int) (entryMax.getValue().floatValue());
// int loadNode=(int)(minMaxGraph.Load.get(i).floatValue());
int maxloadToTransfer = maxLoad - loadNode - 1;
// max Load Transfer
System.out.println("Load to Transfer:" + maxloadToTransfer);
roundRobin = 0;
while (maxloadToTransfer >= 0) {

Set<Entry<Integer, Float>> entrySet = sMax.entrySet();
Map.Entry<Integer, Float> element = (Map.Entry<Integer, Float>) entrySet
.toArray()[roundRobin];

Integer key = element.getKey();

// System.out.println(key);


minMaxGraph.Load.set((element.getKey().intValue()) - 1,
minMaxGraph.Load.get(key.intValue() - 1) - 1);
minMaxGraph.Load.set(i, minMaxGraph.Load.get(i) + 1);

if (roundRobin < sMax.size())
roundRobin = roundRobin + 1;
if (roundRobin == sMax.size()) {
// reset to start again
roundRobin = 0;
}

maxloadToTransfer = maxloadToTransfer - 1;

}
}
System.out.println("Load after max Transfer ******* " + minMaxGraph.Load);
//list.sort((o1, o2) -> o1.compareToIgnoreCase(o2));
//Sortng after each node

minMaxGraph.Load.sort((o1,o2)->o1.compareTo(o2));
System.out.println("After Iteration "+i+"  "+minMaxGraph.Load);




}

System.out.println("Iteration_______________________________________________END"+iterationCount);
iterationCount++;
if(checkForBalance(minMaxGraph.adjacencyList,minMaxGraph.Load)) break;
        }
       
        break;
        }
        System.out.println("**************************************");
        System.out.println("Balanced after:"+iterationCount);
        System.out.println(minMaxGraph.Load);

}
private static Map<Integer, Float> findSmax(Map<Integer, List<Integer>> adjacencyList2, List<Float> load2, int node) {
Map<Integer, Float> sminMap= new HashMap<>();
// System.out.println(load2.size());
// for(int i=0;i<load2.size();i++) {
// sminMap.put(i+1, load2.get(i));
// }

for(int i=0;i<load2.size();i++) {
if(load2.get(i)>node) {
sminMap.put(i+1, load2.get(i));
}
}

Map<Integer, Float> sorted = sminMap.entrySet()
.stream()
.sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                       (oldValue, newValue) -> oldValue, LinkedHashMap::new));

//System.out.println(sminMap);
//System.out.println(sorted);
return sorted;
}
private static Map<Integer, Float> findSmin(Map<Integer, List<Integer>> adjacencyList2, List<Float> load2,int node) {
List<Integer> sMin=new LinkedList<>();
Map<Integer, Float> sminMap= new HashMap<>();
// System.out.println(load2.size());

for(int i=0;i<load2.size();i++) {
if(load2.get(i)<node) {
sminMap.put(i+1, load2.get(i));
}
}

// for(int i=0;i<load2.size();i++) {
// sminMap.put(i+1, load2.get(i));
// }
Map<Integer, Float> sorted = sminMap.entrySet()
.stream()
.sorted(Map.Entry.comparingByValue())
.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                       (oldValue, newValue) -> oldValue, LinkedHashMap::new));

//System.out.println(sminMap);
//System.out.println(sorted);
return sorted;
}
private static boolean checkForBalance(Map<Integer, List<Integer>> adjacencyList2, List<Float> load2) {
int vertexCount = load2.size();
List<Float> sorted = load2.stream().sorted().collect(Collectors.toList());
       int firstVertexLoad = (int)Math.floor(sorted.get(0));
       List<Integer> differenceList= new LinkedList<>();
       boolean absoluteMatch=true;
       boolean finalResult=false;
       for (int vertex = 1; vertex < vertexCount; vertex++) {
           if ((int)Math.floor(sorted.get(vertex)) != firstVertexLoad) {
               //System.out.println("First not matching load on vertex: " + (vertex + 1));
               absoluteMatch=false;
           }
           int diff=(int) (sorted.get(vertex)-firstVertexLoad);
           differenceList.add(diff);
       }
       HashSet<Integer> hashSet = new HashSet<>(differenceList);
      // System.out.println("Size of the hashSet "+size);
       if(hashSet.size()<=1&&hashSet.iterator().next()==1||absoluteMatch) {
        finalResult=true;
        System.out.println("size:: "+hashSet.size()+"::::  Absloute Match"+absoluteMatch);
        System.out.println("Graph is balanced now ");
       }
       
      return finalResult;
}
 

}
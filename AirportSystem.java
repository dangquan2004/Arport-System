import java.util.*;

import static org.junit.Assert.assertEquals;

/**
 * A class that simulate an airport system
 * @author Quan Tran
 * @references CSDS 233 Lecture Slides
 */
public class AirportSystem {
    /** A nested class representing the connection in the graph */
    static class Edges{

        /** The starting location of this edge as a string */
         private String source;
        /** The end destination of this edge as a string. */
         private String destination;
        /** The distance between start and destination. */
        private int distance;

        public Edges(String source, String destination, int distance){
            this.destination = destination;
            this.source = source;
            this.distance = distance;
        }

        @Override
        public String toString(){
            return "[" + source + ", " + destination + "]";
        }
    }

    /** A nested class representing cities in a graph */
    private static class Vertex{
        /** The city name */
        private String id;
        /** The cities that are connected to this city by the airport system */
        private List<Edges> edges;
        /** A flag to check if the cities been visited or not for the traversal */
        private boolean encountered = false;
        /** Parent of the current city */
        private Vertex parent;

        /**
         * Initialize a vertex, which is a city and its connection to other cities
         * @param id the city name
         */
        public Vertex(String id){
            this.id = id;
            edges = new ArrayList<>();
        }

        /** Override to return the city name as a vertex */
        @Override
        public String toString(){
            return id;
        }
    }

    /** The adjacency list of cities. Each node is a city, and each connecting line indicates a flight between two cities. */
    private List<Vertex> connections;

    /**
     * Initialize the airport system by instantiate the list of cities
     */
    public AirportSystem(){
        this.connections = new ArrayList<>();
    }

    /**
     * Retrieve the list of cities for this airport
     * @return the list of cities for this airport
     */
    private List<Vertex> getConnections(){
        return this.connections;
    }

    /**
     * A helper method to check if two city has already a connection or not
     * @param source the starting city
     * @param destination the final city
     * @return true if two city has already established a connection
     */
    private boolean checkHasEdge(String source, String destination){
        // Traverse through the connection list
        for (int indexCon = 0; indexCon < getConnections().size(); indexCon++){
            // Get the current city, which is the source
            Vertex currentCity = getConnections().get(indexCon);
            // If the city is in the list, traverse through cities that are connected to this city
            if (currentCity.toString().equals(source)){
                for (int indexVertex = 0; indexVertex < currentCity.edges.size(); indexVertex++){
                    // Get the connection
                    Edges connection = currentCity.edges.get(indexVertex);
                    // Check if the connection existed in the list by checking if the arrivals matches the input
                    if (connection.destination.equals(destination)) return true;
                }
            }
        }
        // Otherwise return false
        return false;
    }
    /**
     * A method to get the cities type "Vertex" in the connections list based on their input name
     * @param source the starting city
     * @return the staring city in the connections list
     */
    private Vertex getVertex(String source){
        // If the vertex has already exists in the connection list, retrieve it
        for (int indexConnection = 0; indexConnection < getConnections().size(); indexConnection++){
            Vertex sourceCity = getConnections().get(indexConnection);
            if (sourceCity.toString().equals(source)) return sourceCity;
        }
        // If not, create a new one and add it to the connections list
        Vertex sourceCity = new Vertex(source);
        getConnections().add(sourceCity);
        return sourceCity;
    }
    /**
     * A method to add the edges to vertex in the case that weight is positive and no connection has existed before
     * @param source the starting city
     * @param destination the final city
     * @param weight the distance between two city
     * @return true if the connection is established successfully
     * @references CSDS 233 Lecture Notes
     */
    public boolean addEdge(String source, String destination, int weight){
        // If the weight is negative or there has been a connection between two cities, return false
        if ((weight < 0) || (checkHasEdge(source, destination)))  return false;
        // If no connection established before, create the connection by adding new edge
        else{
            // Get the vertex of source and destination
            Vertex startDestination = getVertex(source);
            Vertex endDestination = getVertex(destination);
            // Create a new edges to the source
            Edges newEdge = new Edges(source, destination, weight);
            startDestination.edges.add(newEdge);
            // Add the same edges from the destination back to the source
            newEdge = new Edges(destination, source, weight);
            endDestination.edges.add(newEdge);
            return true;
        }
    }

    /**
     * A method that traverse through the graph using BFS style
     * @param start the original start city
     * @return the BFS traversal starting from the input city
     * @references CSDS 233 Lectures Note
     */
    public List<String> breadthFirstSearch(String start) {
        // An output list of cities name
        List<String> output = new ArrayList<>();
        // Retrieve the city as Vertex type from its name
        Vertex startVertex = getVertex(start);
        // Set the parent of the start vertex as null because we start from there
        startVertex.parent = null;
        // Mark the start vertex as encountered
        startVertex.encountered = true;
        // Initialize a queue as linked list
        LinkedList<Vertex> queue = new LinkedList<>();
        // Add the start city into the queue
        queue.add(startVertex);
        while (!queue.isEmpty()) {
            // Remove first city inside the queue and set it as the source city for later execution
            Vertex currentVertex = queue.remove(0);
            // Traverse through those cities that have the connection with the source city.
            for (int index = 0; index < currentVertex.edges.size(); index++) {
                // Retrieve the connection of the current city
                Edges EdgesInCurrentVertex = currentVertex.edges.get(index);
                // Retrieve the neighbors of the source city by getting the vertex of its destination
                // The destinations of the source city are its neighbors
                Vertex neighbors = getVertex(EdgesInCurrentVertex.destination);
                // If the neighbor has never been encountered/visited, marked it as visited
                if (!neighbors.encountered) {
                    neighbors.encountered = true;
                    // Set the neighbor parent as the source city
                    neighbors.parent = currentVertex;
                    // Add the neighbor into the queue
                    queue.add(neighbors);
                }
            }
            // Once the inner loop is completed, adding the recently removed city from the queue into the output list
            output.add(currentVertex.toString());
        }
        // Return the output.
        return output;
    }

    /**
     * A method to print graph
     */
    public void printGraph(){
        // Looping through the vertex
        for (int index = 0; index < getConnections().size(); index++){
            // Retrieve the city
            Vertex city = getConnections().get(index);
            System.out.println("");
            // Print the city
            System.out.print("V: " + city + " | E: ");
            // Looping through the connection of the city
            for (int index2 = 0; index2 < city.edges.size(); index2++){
                // Get the connection of the city
                Edges cityEdges = city.edges.get(index2);
                // Print the cities that have connection to the vertex
                System.out.print(cityEdges.toString() + ", ");
            }
        }
    }

    /**
     * A method to find the shortest distance between two cities two Dijkstras' algorithm
     * @param cityA the source city
     * @param cityB the destination city
     * @return the shortest distance bewteen two cities
     * @references CSDS 233 Lecture 23 Dijkstra's algorithm pseudocode
     */
    public int shortestDistance(String cityA, String cityB){
        // STEP 1: INITIALIZATION
        // An array list to store the city that has been visited
        List<Vertex> visitedCity = new ArrayList<>();
        // An array to keep up with the updated distance of a city to a source when a city is added into the visited list
        List<Integer> DistanceTracking = new ArrayList<>();
        // Retrieve the staring city
        Vertex startingCity = getVertex(cityA);
        // Add the startingCity into the visited list
        visitedCity.add(startingCity);
        // Once the source is added, update the distance of other city to the source
        for (int cityTracking = 0; cityTracking < getConnections().size(); cityTracking++){
            // Calculate the current distance between the source city to other cities
            int distance = getDistanceBetweenCities(startingCity, getConnections().get(cityTracking));
            // Add the updated distance into the distance tracking list:
            DistanceTracking.add(distance);
        }
        // Once all the distance is updated, find the city that has the smallest distance to the source
        Vertex smallestDistanceToSourceCity = findSmallestDistanceAmongList(visitedCity, DistanceTracking);
        // Once that city is retrieved, start step 2:
        //STEP 2: Looping and Updating until the visited city list consists with all the city
        while (visitedCity.size() < getConnections().size()){
            // Add the city having the smallest distance to the visited list
            visitedCity.add(smallestDistanceToSourceCity);
            // After adding, updating the new distance of other cities to the starting city (source)
            for (int smallestCityTracking = 0; smallestCityTracking < smallestDistanceToSourceCity.edges.size(); smallestCityTracking++){
                // Retrieve the connection between the smallest city to other cities
                Edges smallestCityConnection = smallestDistanceToSourceCity.edges.get(smallestCityTracking);
                // Retrieve the neighbors
                Vertex neighbors = getVertex(smallestCityConnection.destination);
                // Retrieve the current distance of the neighbors to the source (before adding to the visited list)
                int currentDistance = DistanceTracking.get(indexInsideList(getConnections(), neighbors));
                // Calculate the updated distance of the neighbors after adding the smallest city into the visited list
                int updatedDistance = DistanceTracking.get(indexInsideList(getConnections(), smallestDistanceToSourceCity)) + smallestCityConnection.distance;

                // Make comparison on what distances to retain of that neighbors
                if (updatedDistance < currentDistance){
                    DistanceTracking.set(indexInsideList(getConnections(),neighbors), updatedDistance);
                }
            }
            // Once all the distance of cities is updated, again finding the cities that has the shortest distance to the source
            Vertex newSmallestDistanceToSourceCity = findSmallestDistanceAmongList(visitedCity, DistanceTracking);
            // If the new smallest-distance-to-source city is not visited, make it the smallest-distance-to-source city, and return to the
            // beginning of the while loop
            if (!visitedCity.contains(newSmallestDistanceToSourceCity)) smallestDistanceToSourceCity = newSmallestDistanceToSourceCity;
        }

        // STEP 3: After we have the shortest distance of each cities to the source, retrieving the shortest distance of cityB
        // Get the "Vertex" type of cityB
        Vertex destinationCity = getVertex(cityB);
        // Retrieve the shortest distance of the destination city that is stored inside the distance tracking list
        int destinationCityDistance = DistanceTracking.get(indexInsideList(getConnections(), destinationCity));
        return destinationCityDistance;
    }

    /**
     * A helper method for shortestDistance() method to find the index of an element inside a list
     * @param list the list that contains the element
     * @param city the city which its index is retrieved
     * @return the index of the city inside the list
     */
    private int indexInsideList(List<Vertex> list, Vertex city){
        return list.indexOf(city);
    }

    /**
     * A helper method for shortestDistance() to find the smallest distance, an integer, inside a list containing full of integer
     * @param minKnownPath the list of visited city. Helper checking if the city has been visited or not
     * @param distanceBetweenCities a list that keep track the distance between the source and other cities
     * @return the city that has the smallest distance to the source among other cities
     */
    private Vertex findSmallestDistanceAmongList(List<Vertex> minKnownPath, List<Integer> distanceBetweenCities) {
        // Initialize the minimum distance as infinity
        int minDistance = Integer.MAX_VALUE;
        // Initialize the city that has the minimum distance
        Vertex minDistanceCity = null;
        // Looping inside the list containing the distance between cities and the source
        for (int index = 0; index < distanceBetweenCities.size(); index++) {
            // Retrieve the city
            Vertex currentCity = getConnections().get(index);
            // Check if the city is not in the known set and has the smallest distance
            if (!minKnownPath.contains(currentCity) && distanceBetweenCities.get(index) < minDistance) {
                minDistance = distanceBetweenCities.get(index);
                minDistanceCity = currentCity;
            }
        }
        return minDistanceCity;
    }


    /**
     * A helper method for shortestDistance() to get the distance between two cities
     * @param source the source city
     * @param destination the destination city
     * @return the distance between two cities if they are neighbors or infinity if they have no connection
     */
    private int getDistanceBetweenCities(Vertex source, Vertex destination){
        // Looping through the list of cities
        for (int index = 0; index < getConnections().size(); index++){
            // Retrieve the current city
            Vertex currentCity = getConnections().get(index);
            // Check if two cities, the source city and current city have connection or not.
            if (checkHasEdge(source.toString(), currentCity.toString())){
                // If they have connection, looping through the connection of the source city
                for (int index1 = 0; index1 < source.edges.size(); index1++){
                    if (source.edges.get(index1).destination.equals(destination.toString())){
                        return source.edges.get(index1).distance;
                    }
                }
            }
        }
        // If there's no connection, return infinity
        return Integer.MAX_VALUE;
    }

    /**
     * A method to create a minimum spanning tree using Prims's algorithm
     * @return a list that contains the edges of the tree
     * @references CSDS 233 Lecture 24 Prims's algorithm pseudocode
     */
    public List<Edges> minimumSpanningTree(){
        // Choose the second city in the list and start traverse from it
        Vertex startCity = getConnections().get(1);
        // A list to keep track the edges
        List<Edges> edgesTracking = new ArrayList<>();
        // A list to keep track of the vertex
        List<Vertex> vertexTracing = new ArrayList<>();
        // An output list that contains edges
        List<Edges> output = new ArrayList<>();
        // Looping through the connection of start city and add them into the edges tracking list
        appendEdgesFromCity(edgesTracking, startCity);
        // Add the vertex into the tracing list
        vertexTracing.add(startCity);
        startCity.encountered = true;
        // Once we have the edges, find the one that has the smallest distance among each
        Edges smallestDistanceEdge = findMinConnectionInList(edgesTracking);
        // Add the edges into the output
        output.add(smallestDistanceEdge);
        // Once we add the edges to the output, we remove it to the edges tracking list
        edgesTracking.remove(smallestDistanceEdge);
        // Retrieve the vertex that has the shortest distance to the MST
        Vertex smallestDistanceVertex = getVertex(smallestDistanceEdge.destination);
        while (vertexTracing.size() <= getConnections().size()){
            // Add the new vertex into the vertex tracing list
            vertexTracing.add(smallestDistanceVertex);
            smallestDistanceVertex.encountered = true;
            // Once we add the vertex into the tracing list, looping through its connection
            appendEdgesFromCity(edgesTracking, smallestDistanceVertex);
            // Once have the edges in the list, find the one that has the smallest distance among each
            Edges newSmallestDistanceEdges = findMinConnectionInList(edgesTracking);

            if (newSmallestDistanceEdges != null && !output.contains(newSmallestDistanceEdges)
                                                 && !getVertex(newSmallestDistanceEdges.destination).encountered) {
                // Add the edges into the output
                output.add(newSmallestDistanceEdges);
            }
            // Then remove it in the edge tracking list
            edgesTracking.remove(newSmallestDistanceEdges);

            // Initialize the new smallest distance vertex as empty. As long as it is empty, the vertex tracing list does not increase in size
            Vertex newSmallestDistanceVertex = null;
            // If the new edges does not null, get its destination aka the new smallest distance vertex
            if (newSmallestDistanceEdges != null) {
                newSmallestDistanceVertex = getVertex(newSmallestDistanceEdges.destination);
            }
            // Check and add the new shortest distance vertex into the vertex tracking list
            if (newSmallestDistanceVertex!= null && !newSmallestDistanceVertex.encountered) smallestDistanceVertex = newSmallestDistanceVertex;
            // Remove unecessary edges from list
            removeEdgesFromList(edgesTracking);
        }
        return output;
    }

    /**
     * A helper method for minimumSpanningTree() that traverse through all the connection of a vertex and
     * remove the connection if its destination has already been visited.
     * @param edgesListTracking the list that of connection of a vertex
     */
    private void removeEdgesFromList(List<Edges> edgesListTracking){
        // Looping inside the list that contains edges
        for (int index = 0; index < edgesListTracking.size(); index++){
            // If the destination of the edge has already been encountered
            if (getVertex(edgesListTracking.get(index).destination).encountered){
                // Remove it from the list
                edgesListTracking.remove(edgesListTracking.get(index));
            }
        }
    }

    /**
     * A helper method for minimumSpanningTree() that append the connection of a city into a list that contains connections
     * Condition: The vertex has not already been visited.
     * @param edgesList the list that contains connections
     * @param city the city in which its connections is retrieved under the above condition
     * @return the list that contains connections passed the condition
     */
    private List<Edges> appendEdgesFromCity(List<Edges> edgesList, Vertex city){

        for (int edgesIndex = 0; edgesIndex < city.edges.size(); edgesIndex++){
            // Retrieve the edges
            Edges connection = city.edges.get(edgesIndex);
            // Check if the vertex is visited
            if (!getVertex(connection.destination).encountered){
                edgesList.add(connection);
            }
        }
        return edgesList;
    }

    /**
     * A helper method for minimumSpanningTree() that return the smallest distance edges inside a list of edges
     * @param edgesList the list contains edges
     * @return the edge that has the smallest distance
     */
    private Edges findMinConnectionInList(List<Edges> edgesList){
        // If the list is empty, return nothing
        if (edgesList.isEmpty()) {
            return null;
        }
        // Initialize a minEdges variable
        Edges minEdge = null;
        // Set the initial distance as infinity
        int initialEdgeDistance = Integer.MAX_VALUE;
        // Looping through the list containing edges
        for (int index = 0; index < edgesList.size(); index++){
            // The distance of the edge is less than the initial distance
            if (edgesList.get(index).distance < initialEdgeDistance){
                // Set the initial distance the distance of the edges
                initialEdgeDistance = edgesList.get(index).distance;
                // Retrieve the edge
                minEdge = edgesList.get(index);
            }
        }
        // Return the edge
        return minEdge;
    }

    public static void main(String[] args) {

        AirportSystem airportSystem = new AirportSystem();
        // Add cities and flights with random weights and create a disconnected graph
        airportSystem.addEdge("1", "5", 4);
        airportSystem.addEdge("1", "4", 1);
        airportSystem.addEdge("1", "2", 2);
        airportSystem.addEdge("4", "5", 9);
        airportSystem.addEdge("2", "4", 3);
        System.out.println(airportSystem.minimumSpanningTree());

    }
}

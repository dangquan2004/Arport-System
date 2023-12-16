import java.util.List;

import static org.junit.Assert.*;

/**
 * A JUnit class to test airport system
 */
public class AirportSystemTest {

    @org.junit.Test
    public void shortestDistance() {
        AirportSystem airportSystem = new AirportSystem();

        airportSystem.addEdge("1", "5", 4);
        airportSystem.addEdge("1", "4", 1);
        airportSystem.addEdge("1", "2", 2);
        airportSystem.addEdge("4", "5", 9);
        airportSystem.addEdge("2", "4", 3);
        airportSystem.addEdge("2", "3", 3);
        airportSystem.addEdge("2", "6", 7);
        airportSystem.addEdge("3", "4", 5);
        airportSystem.addEdge("3", "6", 8);

        // Case 1: The shortest distance is 1-edge connection with nearby neighbors
        int distance  = airportSystem.shortestDistance("1", "4");
        assertEquals(1, distance);
        int distance1 = airportSystem.shortestDistance("2", "4");
        assertEquals(3, distance1);

        // Case 2: The shortest distance is 2-edge vertex, which is shorter than 1-edge connection
        // In this case, 2-edge connection is shorter than 1-edge connection
        int distance2 = airportSystem.shortestDistance("4", "5");
        // 2-edge connection
        assertEquals(5, distance2);
        // 1 edge connection
        assertNotEquals(9, distance2);

        // Case 3: The shortest distance is 2-edge vertex, which is shorter than another 2-edge connection
        int distance3 = airportSystem.shortestDistance("2", "5");
        assertEquals(6, distance3);
        assertNotEquals(12, distance3);

        // Case 4: The shortest distance is a 3-edge connection.
        int distance4 = airportSystem.shortestDistance("3", "5");
        assertEquals(9, distance4);
        // Another example in the same case
        int distance5 = airportSystem.shortestDistance("6", "5");
        assertEquals(13, distance5);

    }

    @org.junit.Test
    public void minimumSpanningTree() {

        AirportSystem airportSystem = new AirportSystem();
        airportSystem.addEdge("1", "5", 4);
        airportSystem.addEdge("1", "4", 1);
        airportSystem.addEdge("1", "2", 2);
        airportSystem.addEdge("4", "5", 9);
        airportSystem.addEdge("2", "4", 3);
        airportSystem.addEdge("2", "3", 3);
        airportSystem.addEdge("2", "6", 7);
        airportSystem.addEdge("3", "4", 5);
        airportSystem.addEdge("3", "6", 8);

        // Case 1: Multiple-Edge Tree
        String edgesListExpected = "[[5, 1], [1, 4], [1, 2], [2, 3], [2, 6]]";
        assertEquals(edgesListExpected, airportSystem.minimumSpanningTree().toString());

        // Case2: 3-edge Tree
        AirportSystem airportSystem1 = new AirportSystem();
        airportSystem1.addEdge("1", "5", 4);
        airportSystem1.addEdge("1", "4", 1);
        airportSystem1.addEdge("1", "2", 2);
        String edgesListExpected1 = "[[5, 1], [1, 4], [1, 2]]";
        assertEquals(edgesListExpected1, airportSystem1.minimumSpanningTree().toString());

        // Case3: 2-edge Tree
        AirportSystem airportSystem2 = new AirportSystem();
        airportSystem2.addEdge("1", "5", 4);
        airportSystem2.addEdge("1", "4", 1);
        String edgesListExpected2 = "[[5, 1], [1, 4]]";
        assertEquals(edgesListExpected2, airportSystem2.minimumSpanningTree().toString());

        // Case 4: 1-edge Tree
        AirportSystem airportSystem3 = new AirportSystem();
        airportSystem3.addEdge("1", "5", 4);
        String edgesListExpected3 = "[[5, 1]]";
        assertEquals(edgesListExpected3, airportSystem3.minimumSpanningTree().toString());
    }
}
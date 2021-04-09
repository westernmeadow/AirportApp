//
//  Name:       Kwan, Wesley
//  Project:    3
//  Due:        05/13/20
//  Course:     cs-2400-02-sp20
//
//  Description:
//              An app that creates a graph of airports and allows a user
//              to manipulate and observe certain graph data.
//
import java.util.*;
import java.io.*;

public class AirportApp
{
    public static void main(String[] args)
    {
        GraphInterface<String> graph = new DirectedGraph<>();
        Map<String, String> airports = new HashMap<>();
        String[] data = new String[0];
        Scanner csvReader = null;
        
        try
        {
            csvReader = new Scanner(new FileReader("airports.csv"));
            while (csvReader.hasNextLine())
            {
                String row = csvReader.nextLine();
                data = row.split(",");
                graph.addVertex(data[0].trim());
                airports.put(data[0].trim(), data[1].trim());
            }
            csvReader.close();
        }
        catch (FileNotFoundException e)
        {
            System.out.println(e.getMessage());
        }
        
        try
        {
            csvReader = new Scanner(new FileReader("distances.csv"));
            while (csvReader.hasNextLine())
            {
                String row = csvReader.nextLine();
                data = row.split(",");
                graph.addEdge(data[0].trim(), data[1].trim(), Double.parseDouble(data[2]));
            }
            csvReader.close();
        }
        catch (FileNotFoundException e)
        {
            System.out.println(e.getMessage());
        }
        
        Scanner inputReader = new Scanner(System.in);
        boolean done = false;
        while (!done)
        {
            System.out.print("Command? ");
            String userInput = inputReader.nextLine();
            switch (userInput)
            {
                case "Q":
                    System.out.print("Airport code: ");
                    userInput = inputReader.nextLine();
                    if (airports.containsKey(userInput))
                        System.out.println(airports.get(userInput));
                    else
                        System.out.println("Airport code does not exist.");
                    break;
                case "D":
                    System.out.print("Airport codes: ");
                    userInput = inputReader.nextLine();
                    data = userInput.split(" ");
                    if (data.length == 2)
                    {
                        String begin = data[0];
                        String end = data[1];
                        if (airports.containsKey(begin) && airports.containsKey(end))
                        {
                            try
                            {
                                Stack<String> path = new Stack<>();
                                int distance = (int)graph.getCheapestPath(begin, end, path);
                                System.out.println("The minimum distance between " +
                                                    airports.get(begin) + " and " +
                                                    airports.get(end) + " is " +
                                                    distance + " through the route:");
                                while (!path.isEmpty()) 
                                    System.out.println(airports.get(path.pop()));
                            }
                            catch (RuntimeException e)
                            {
                                System.out.println(e.getMessage());
                            }
                        }
                        else
                            System.out.println("Airport code(s) does not exist.");
                    }
                    else
                        System.out.println("Invalid input.");
                    break;
                case "I":
                    System.out.print("Airport codes and distance: ");
                    userInput = inputReader.nextLine();
                    data = userInput.split(" ");
                    if (data.length == 3)
                    {
                        try
                        {
                            String begin = data[0];
                            String end = data[1];
                            double distance = Double.parseDouble(data[2]);
                            if (graph.hasEdge(begin, end))
                                    System.out.println("Connection already exists.");
                            else
                            {
                                if (graph.addEdge(begin, end, distance))
                                    System.out.println("You have inserted a connection from " +
                                                        airports.get(begin) + " to " + airports.get(end) + 
                                                       " with a distance of " + (int)distance + ".");
                                else
                                    System.out.println("Airport code(s) does not exist.");
                            }
                        }
                        catch (IllegalArgumentException e)
                        {
                            System.out.println("Input distance is not a number.");
                        }
                    }
                    else
                        System.out.println("Invalid input.");
                    break;
                case "R":
                    System.out.print("Airport codes: ");
                    userInput = inputReader.nextLine();
                    data = userInput.split(" ");
                    if (data.length == 2)
                    {
                        String begin = data[0];
                        String end = data[1];
                        if (airports.containsKey(begin) && airports.containsKey(end))
                        {
                            if (graph.removeEdge(begin, end))
                                System.out.println("The connection from " + airports.get(begin) +
                                                   " and " + airports.get(end) + " removed.");
                            else
                                System.out.println("Connection does not exist.");
                        }
                        else
                        {
                            System.out.println("Airport code(s) does not exist.");
                        }
                    }
                    else
                        System.out.println("Invalid input.");
                    break;
                case "H":
                    System.out.println("Q Query the airport information by entering the airport code.");
                    System.out.println("D Find the minimum distance between two airports.");
                    System.out.println("I Insert a connection by entering two airport codes and distance.");
                    System.out.println("R Remove an existing connection by entering two airport codes.");
                    System.out.println("H Display this message.");
                    System.out.println("E Exit.");
                    break;
                case "E":
                    done = true;
                    break;
                default:
                    System.out.println("Unrecognized command.");
            }
        }
    }
}

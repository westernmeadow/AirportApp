//
//  Name:       Kwan, Wesley
//  Project:    3
//  Due:        05/13/20
//  Course:     cs-2400-02-sp20
//
//  Description:
//              A class that implements the ADT directed graph.
//
import java.util.Map;
import java.util.HashMap;
import java.util.Queue;
import java.util.PriorityQueue;
import java.util.Stack;
import java.util.Iterator;

public class DirectedGraph<T> implements GraphInterface<T>
{
    private Map<T, VertexInterface<T>> vertices;
    private int edgeCount;
    
    public DirectedGraph()
    {
        vertices = new HashMap<>();
        edgeCount = 0;
    }
    
    public boolean addVertex(T vertexLabel)
    {
        VertexInterface<T> addOutcome = vertices.put(vertexLabel, new Vertex<>(vertexLabel));
        return addOutcome == null;
    }
    
    public boolean addEdge(T begin, T end)
    {
        return addEdge(begin, end, 0);
    }

    public boolean addEdge(T begin, T end, double edgeWeight)
    {
        boolean result = false;
        VertexInterface<T> beginVertex = vertices.get(begin);
        VertexInterface<T> endVertex = vertices.get(end);
        if ( (beginVertex != null) && (endVertex != null) )
            result = beginVertex.connect(endVertex, edgeWeight);
        if (result)
            edgeCount++;
        return result;
    }
    
    public boolean removeEdge(T begin, T end)
    {
        boolean result = false;
        VertexInterface<T> beginVertex = vertices.get(begin);
        VertexInterface<T> endVertex = vertices.get(end);
        if ( (beginVertex != null) && (endVertex != null) )
            result = beginVertex.disconnect(endVertex);
        if (result)
            edgeCount--;
        return result;
    }
    
    public boolean hasEdge(T begin, T end)
    {
        boolean found = false;
        VertexInterface<T> beginVertex = vertices.get(begin);
        VertexInterface<T> endVertex = vertices.get(end);
        if ( (beginVertex != null) && (endVertex != null) )
        {
            Iterator<VertexInterface<T>> neighbors = beginVertex.getNeighborIterator();
            while (!found && neighbors.hasNext())
            {
                VertexInterface<T> nextNeighbor = neighbors.next();
                if (endVertex.equals(nextNeighbor))
                    found = true;
            }
        }
        return found;
    }
    
    public boolean isEmpty()
    {
        return vertices.isEmpty();
    }
    
    public int getNumberOfVertices()
    {
        return vertices.size();
    }

    public int getNumberOfEdges()
    {
        return edgeCount;
    }
    
    public void clear()
    {
        vertices.clear();
        edgeCount = 0;
    }
    
    private void resetVertices()
    {
        Iterator<VertexInterface<T>> vertexIterator = vertices.values().iterator();
        while (vertexIterator.hasNext())
        {
            VertexInterface<T> vertex = vertexIterator.next();
            vertex.unvisit();
            vertex.setCost(0);
            vertex.setPredecessor(null);
        }
    }
    
    public Queue<T> getBreadthFirstTraversal(T origin)
    {
        throw new UnsupportedOperationException();
    }
    
    public Queue<T> getDepthFirstTraversal(T origin)
    {
        throw new UnsupportedOperationException();
    }

    public Stack<T> getTopologicalOrder()
    {
        throw new UnsupportedOperationException();
    }
    
    public int getShortestPath(T begin, T end, Stack<T> path)
    {
        throw new UnsupportedOperationException();
    }

    public double getCheapestPath(T begin, T end, Stack<T> path)
    {
        resetVertices();
        boolean done = false;
        Queue<EntryPQ> priorityQueue = new PriorityQueue<>();
        VertexInterface<T> originVertex = vertices.get(begin);
        VertexInterface<T> endVertex = vertices.get(end);
        priorityQueue.add(new EntryPQ(originVertex));
        while (!done && !priorityQueue.isEmpty())
        {
            EntryPQ frontEntry = priorityQueue.remove();
            VertexInterface<T> frontVertex = frontEntry.vertex;
            if (!frontVertex.isVisited())
            {
                frontVertex.visit();
                frontVertex.setCost(frontEntry.cost);
                frontVertex.setPredecessor(frontEntry.previousVertex);
                if (frontVertex.equals(endVertex))
                    done = true;
                else
                {
                    Iterator<VertexInterface<T>> neighbors = frontVertex.getNeighborIterator();
                    Iterator<Double> weights = frontVertex.getWeightIterator();
                    while (!done && neighbors.hasNext())
                    {
                        VertexInterface<T> nextNeighbor = neighbors.next();
                        Double nextWeight = weights.next();
                        if (!nextNeighbor.isVisited())
                        {
                            double nextCost = nextWeight + frontVertex.getCost();
                            priorityQueue.add(new EntryPQ(nextNeighbor, nextCost, frontVertex));
                        }
                    }
                }
            }
        }
        if (!done)
            throw new RuntimeException("Path does not exist.");
            
        double pathLength = endVertex.getCost();
        path.push(endVertex.getLabel());
        VertexInterface<T> vertex = endVertex;
        while(vertex.hasPredecessor())
        {
            vertex = vertex.getPredecessor();
            path.push(vertex.getLabel());
        }
        return pathLength;
    }
    
    private class EntryPQ implements Comparable<EntryPQ>
    {
        private VertexInterface<T> vertex;
        private double cost;
        private VertexInterface<T> previousVertex;
        
        public EntryPQ(VertexInterface<T> aVertex)
        {
            this(aVertex, 0, null);
        }
        
        public EntryPQ(VertexInterface<T> aVertex, double aCost, VertexInterface<T> predecessor)
        {
            vertex = aVertex;
            cost = aCost;
            previousVertex = predecessor;
        }
        
        public int compareTo(EntryPQ other)
        {
            double result = cost - other.cost;
            if (result < 0)
                return -1;
            else if (result == 0)
                return 0;
            else
                return 1;
        }
    }
}
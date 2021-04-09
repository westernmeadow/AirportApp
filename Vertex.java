//
//  Name:       Kwan, Wesley
//  Project:    3
//  Due:        05/13/20
//  Course:     cs-2400-02-sp20
//
//  Description:
//              A class of vertices for a graph.
//
import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;

public class Vertex<T> implements VertexInterface<T>
{
    private T label;
    private List<Edge> edgeList;
    private boolean visited;
    private VertexInterface<T> previousVertex;
    private double cost;

    public Vertex(T vertexLabel)
    {
        this(vertexLabel, 0, null);
    }
    
    public Vertex(T vertexLabel, double aCost, VertexInterface<T> predecessor)
    {
        label = vertexLabel;
        edgeList = new LinkedList<>();
        visited = false;
        previousVertex = predecessor;
        cost = aCost;
    }
    
    public T getLabel()
    {
        return label;
    }
    
    public void visit()
    {
        visited = true;
    }
    
    public void unvisit()
    {
        visited = false;
    }
    
    public boolean isVisited()
    {
        return visited;
    }
    
    public boolean connect(VertexInterface<T> endVertex)
    {
        return connect(endVertex, 0);
    } // end connect
    
    public boolean connect(VertexInterface<T> endVertex, double edgeWeight)
    {
        boolean result = false;
        
        if (!this.equals(endVertex))
        {
            Iterator<VertexInterface<T>> neighbors = getNeighborIterator();
            boolean duplicateEdge = false;
            
            while (!duplicateEdge && neighbors.hasNext())
            {
                VertexInterface<T> nextNeighbor = neighbors.next();
                if (endVertex.equals(nextNeighbor))
                    duplicateEdge = true;
            }
            
            if (!duplicateEdge)
            {
                edgeList.add(new Edge(endVertex, edgeWeight));
                result = true;
            }
        }
        
        return result;
    }
    
    public boolean disconnect(VertexInterface<T> endVertex)
    {   
        if (!this.equals(endVertex))
            return edgeList.remove(new Edge(endVertex));
        return false;
    }
    
    public Iterator<VertexInterface<T>> getNeighborIterator()
    {
        return new NeighborIterator();
    }
    
    public Iterator<Double> getWeightIterator()
    {
        return new WeightIterator();
    }
    
    public boolean hasNeighbor()
    {
        return !edgeList.isEmpty();
    }
    
    public VertexInterface<T> getUnvisitedNeighbor()
    {
        VertexInterface<T> result = null;
        
        Iterator<VertexInterface<T>> neighbors = getNeighborIterator();
        while ( neighbors.hasNext() && (result == null) )
        {
            VertexInterface<T> nextNeighbor = neighbors.next();
            if (!nextNeighbor.isVisited())
                result = nextNeighbor;
        }
        
        return result;
    }
    
    public void setPredecessor(VertexInterface<T> predecessor)
    {
        previousVertex = predecessor;
    }
    
    public VertexInterface<T> getPredecessor()
    {
        return previousVertex;
    }
    
    public boolean hasPredecessor()
    {
        return previousVertex != null;
    }
    
    public void setCost(double newCost)
    {
        cost = newCost;
    }
    
    public double getCost()
    {
        return cost;
    }
    
    public boolean equals(Object other)
    {
        boolean result;
        
        if ((other == null) || (getClass() != other.getClass()))
            result = false;
        else
        {
            // The cast is safe within this else clause
            @SuppressWarnings("unchecked")
            Vertex<T> otherVertex = (Vertex<T>)other;
            result = label.equals(otherVertex.label);
        }
        
        return result;
    }
    
    private class NeighborIterator implements Iterator<VertexInterface<T>>
    {
        private Iterator<Edge> edges;
        
        private NeighborIterator()
        {
            edges = edgeList.iterator();
        }
        
        public boolean hasNext()
        {
            return edges.hasNext();
        }
        
        public VertexInterface<T> next()
        {
            VertexInterface<T> nextNeighbor = null;
            
            if (edges.hasNext())
            {
                Edge edgeToNextNeighbor = edges.next();
                nextNeighbor = edgeToNextNeighbor.getEndVertex();
            }
            else
                throw new RuntimeException("No such element");
            
            return nextNeighbor;
        }
    }
    
    private class WeightIterator implements Iterator<Double>
    {
        private Iterator<Edge> edges;
        
        private WeightIterator()
        {
            edges = edgeList.iterator();
        }
        
        public boolean hasNext()
        {
            return edges.hasNext();
        }
        
        public Double next()
        {
            Double nextWeight = null;
            
            if (edges.hasNext())
            {
                Edge edgeToNextNeighbor = edges.next();
                nextWeight = edgeToNextNeighbor.getWeight();
            }
            else
                throw new RuntimeException("No such element");
            
            return nextWeight;
        }
    }
    
    private class Edge
    {
        private VertexInterface<T> vertex;
        private double weight;
        
        public Edge(VertexInterface<T> endVertex, double edgeWeight)
        {
            vertex = endVertex; weight = edgeWeight;
        }
        
        public Edge(VertexInterface<T> endVertex)
        {
            vertex = endVertex; weight = 0;
        }
        
        public VertexInterface<T> getEndVertex()
        {
            return vertex;
        }
        
        public double getWeight() 
        {
            return weight;
        }
        
        public boolean equals(Object other)
        {
            boolean result;
        
            if ((other == null) || (getClass() != other.getClass()))
                result = false;
            else
            {
                // The cast is safe within this else clause
                @SuppressWarnings("unchecked")
                Edge otherEdge = (Edge)other;
                result = vertex.getLabel().equals(otherEdge.vertex.getLabel());
            }
            
            return result;
        }
    } 
}
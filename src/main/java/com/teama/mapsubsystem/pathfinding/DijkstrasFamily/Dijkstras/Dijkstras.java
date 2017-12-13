package com.teama.mapsubsystem.pathfinding.DijkstrasFamily.Dijkstras;


import com.teama.mapsubsystem.data.MapNode;
import com.teama.mapsubsystem.data.MapEdge;
import com.teama.mapsubsystem.pathfinding.DijkstrasFamily.DijkstrasTemplate;
import com.teama.mapsubsystem.pathfinding.Path;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

import static java.lang.Math.abs;
import static java.lang.Math.sqrt;

public class Dijkstras extends DijkstrasTemplate {

    protected HashMap<String,KnownPointD> checkedPoints;
    protected PriorityQueue<KnownPointD> queue;
    protected MapNode start, end;
    protected HashMap<String, MapNode> disableNodes  ;



    /**
     * This is the a star itself.
     * @param start the start MapNode
     * @param end the end MapNode
     * @return the path generated by a star
     */
    @Override
    public Path generatePath(MapNode start, MapNode end) {

        this.start=start;
        this.end=end;
        checkedPoints= new HashMap<>();
        queue=new PriorityQueue<>();
        if(disableNodes==null) disableNodes= new HashMap<String, MapNode>();

        KnownPointD checking ; // create a temp variable to keep track of which node are we on.

        //Generate Path
        for(checking = new KnownPointD(start,null,0);
            !checking.getNode().getId().equals(end.getId());   // reached end
            checking=queue.poll() // move forward one step
                )
        {
            putNodesIntoQueue(checking); // put adjacent node into queue.
            checkedPoints.put(checking.getNode().getId(),checking);
            if(queue.peek()==null) {
                throw new java.lang.RuntimeException("Cannot generate a route from the given start and end.");
            }
            // @TODO double check if this is good enough for errors.
        }
        // Done generating, output the path
        // make it into the format of outputting.
        return formatOutput(collectPath(checking));
    }

    //TODO fill this function
    @Override
    public Path generatePath(MapNode start, MapNode end, ArrayList<MapNode> disableNodes){
        this.disableNodes=grabDisableNodes(disableNodes);
        return generatePath(start, end);
    }


    ////////////////////// helper ///////////////////////

    /**
     * This helper function is to convert the disabled MapNode ArrayList to HashMap with ID as key
     * @param nodes is the ArrayList needed to convert
     */
    protected HashMap<String,MapNode> grabDisableNodes(ArrayList<MapNode> nodes){
        HashMap<String,MapNode> temp = new HashMap<>();
        for(int i = 0; i < nodes.size(); i++){
            temp.put(nodes.get(i).getId(), nodes.get(i));
        }
        return temp;
    }

    /**
     * This helper function is to use the abs value of coordinates difference to calculate difference.
     * @param n1 is the start node.
     * @param n2 is the end node.
     * @return returns the sum of x coord diff and y coord diff.
     */
    private int calDistance(MapNode n1, MapNode n2)
    {
        double x = (double) abs(n1.getCoordinate().getxCoord() - n2.getCoordinate().getxCoord());
        double y = (double) abs(n1.getCoordinate().getyCoord() - n2.getCoordinate().getxCoord());
        return (int) sqrt ( x*x+y*y ) ;
    }

    /**
     * This helper function is to get the other node which links to the given edge.
     * @param e the edge being checked
     * @param n the node that's known
     * @return the node on the other end of this edge
     */
    protected MapNode adjacentNode(MapEdge e, MapNode n)
    {
        if(e.getStart().getId().equals(n.getId())) return e.getEnd();
        else return e.getStart();
    }


    /**
     * This helper function is to put all the nodes that are linked to checking into the queue.
     * @param checking is the node currently under examining.
     */
    protected void putNodesIntoQueue (KnownPointD checking)
    {
        MapNode nextNode;
        KnownPointD nextPoint;
        for(MapEdge e : checking.getEdge()) // putting the adjacentNodes into queue
        {
            nextNode = adjacentNode(e,checking.getNode());  // get the node to be calculated
            if(disableNodes.containsKey(nextNode.getId())) continue; //skip this node if it is in the disabled list
            if( !checkedPoints.containsKey(nextNode.getId())) {  // prevent from going to points already been at
                int newPastCost = checking.getPastCost() + (int) e.getWeight();

                nextPoint = new KnownPointD(nextNode, checking, newPastCost); // Generate a new Point from checking point to add into queue.
                queue.add(nextPoint); // add into queue
            }
        }
    }

    /**
     * This helper function is to get the edge between two given nodes.
     * @param a MapNode a
     * @param b MapNode b
     * @return the edge connecting MapNode a and MapNode b. If not found, returns null.
     */
    private MapEdge getEdgeBetweenNodes(MapNode a, MapNode b)
    {
        for (MapEdge mapEdge : a.getEdges()) {
            if(mapEdge.getStart().getId().equals(b.getId()) || mapEdge.getEnd().getId().equals(b.getId())) {
                return mapEdge;
            }
        }
        return null;
    }

    /**
     * This helper function is to generate the Path from end point and put them in a list.
     * @param lastPoint the end point the Path
     * @return  the reversed Path
     */
    protected ArrayList<MapNode> collectPath(KnownPointD lastPoint)
    {
        ArrayList<MapNode> finalPath = new ArrayList<>();
        for (;lastPoint.getLastNode()!=null;)
        {
            finalPath.add(lastPoint.getNode());
            lastPoint=lastPoint.getLastNode();
        }
        finalPath.add(lastPoint.getNode());
        return finalPath;
    }

    /**
     * This helper function is to change the output path into the format of the Path Class.
     * @param finalPath the reversed path generated from ending location.
     * @return the formatted Path object.
     */
    protected Path formatOutput(ArrayList<MapNode> finalPath)
    {
        Path output = new Path();
        MapNode currentNode = finalPath.get(finalPath.size()-1); // extract the first Node of the list.
        output.addNode(currentNode); // put the start node into it.
        MapNode nextNode;
        for(int i =finalPath.size()-2;i>-1;--i)
        {
            nextNode=finalPath.get(i); // extract the second node
            output.addNode(nextNode);             //store the next node
            output.addEdge(getEdgeBetweenNodes(nextNode,currentNode)); // store the edge between them.
            currentNode=nextNode;   // move forward one step.
        }
        return output;
    }
}

package business.managers;

import core.entities.Node;
import core.interfaces.DatabaseI;
import edu.princeton.cs.algs4.BST;

import java.util.ArrayList;

public class NodeManager {

    private static DatabaseI<Node> database;
    private static BST<Integer, Node> listById;
    private static BST<String, Node> listByName;


    public ArrayList<Node> GetAll() {
        ArrayList<Node> list = new ArrayList<>();

        for(var nodeId : listById.keys()){
            list.add(listById.get(nodeId));
        }

        return list;
    }

    public Node GetNode(Integer id) {
        return listById.get(id);
    }

}

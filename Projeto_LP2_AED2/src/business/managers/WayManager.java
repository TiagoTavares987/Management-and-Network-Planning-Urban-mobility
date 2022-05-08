package business.managers;

import core.entities.Way;
import core.interfaces.DatabaseI;
import edu.princeton.cs.algs4.BST;

import java.util.ArrayList;

public class WayManager {

    private static DatabaseI<Way> database;
    private static BST<Integer, Way> listById;
    private static BST<String, Way> listByUsername;

    /**
     * @return a lista de ways
     */
    public ArrayList<Way> GetAll() {
        ArrayList<Way> list = new ArrayList<>();

        for(var wayId : listById.keys()){
            list.add(listById.get(wayId));
        }

        return list;
    }

    /**
     * @param id
     * @return o way com aquele id
     */
    public Way GetWay(Integer id) {
        return listById.get(id);
    }
}

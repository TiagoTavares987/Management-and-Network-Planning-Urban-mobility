package business.managers;

import core.entities.*;
import core.interfaces.DatabaseI;
import database.PoiDatabase;
import edu.princeton.cs.algs4.BST;

import java.util.ArrayList;

public class PoiManager {

    private static DatabaseI<Poi> database;
    private static BST<Integer, Poi> listById;
    private static BST<String, Poi> listByName;

    public PoiManager() {

        if (database == null) {
            database = new PoiDatabase();
            listById = new BST<>();
            listByName = new BST<>();

            for (var poi : database.GetTable()) {
                listById.put(poi.getId(), poi);
                listByName.put(poi.Name, poi);
            }
        }
    }

    /**
     * @return a lista de pois
     */
    public ArrayList<Poi> GetAll() {
        ArrayList<Poi> list = new ArrayList<>();

        for (var poiId : listById.keys()) {
            list.add(listById.get(poiId));
        }

        return list;
    }

    /**
     * @param id
     * @return
     */
    public Poi GetPoi(Integer id) {
        return listById.get(id);
    }
}

package business.managers;

import core.entities.Tag;
import core.interfaces.DatabaseI;
import edu.princeton.cs.algs4.BST;

import java.util.ArrayList;

public class TagManager {

    private DatabaseI<Tag> database;

    private static BST<Integer, Tag> listById;
    private static BST<String, Tag> listByUsername;


    public ArrayList<Tag> GetAll() {
        ArrayList<Tag> list = new ArrayList<>();

        for(var tagId : listById.keys()){
            list.add(listById.get(tagId));
        }

        return list;
    }

    public Tag GetTag(Integer id) {
        return listById.get(id);
    }
}

package business.managers;

import core.entities.Localization;
import core.entities.Poi;
import core.entities.Tag;
import core.entities.Way;
import core.interfaces.DatabaseI;
import core.utils.Test;
import database.WayDatabase;
import edu.princeton.cs.algs4.BST;
import edu.princeton.cs.algs4.ST;

import java.io.IOException;
import java.util.ArrayList;

public class WayManager {

    private static TagManager tagManager;
    private static PoiManager poiManager;
    private static DatabaseI<Way> database;
    private static BST<Integer, Way> listById;
    private static BST<String, Way> listByName;

    public WayManager(TagManager tag_Manager, PoiManager poi_Manager) {

        if (database == null) {
            database = new WayDatabase();
            listById = new BST<>();
            listByName = new BST<>();

            for (Way way : database.GetTable()) {
                listById.put(way.getId(), way);
                listByName.put(way.Name, way);
            }
        }

        if (tag_Manager != null && tagManager == null)
            tagManager = tag_Manager;

        if (poi_Manager != null && poiManager == null)
            poiManager = poi_Manager;
    }

    /**
     * @return a lista de ways
     */
    public ArrayList<Way> GetAll() {
        ArrayList<Way> list = new ArrayList<>();

        for(Integer wayId : listById.keys()){
            list.add(listById.get(wayId));
        }

        return list;
    }

    /**
     * @param id id do way
     * @return o way
     */
    public Way GetWay(Integer id) {
        return listById.get(id);
    }

    /**
     *
     * @param name Tipo String nome da way
     * @param start Tipo integer recebe o inicio de uma way (onde ela comeca)
     * @param end Tipo integer recebe o final de uma way (onde ela termina)
     * @return retorna o way com o respetivo id
     * @throws Exception
     */
    public int newWay(String name, Integer start, Integer end) throws Exception {

        Way way = new Way();
        way.Name = name;
        way.Start = start;
        way.End = end;

        Way newWay = SaveWay(way);

        return newWay.getId();
    }

    /**
     *
     * @param way_id Integer recebe o id do way
     * @param silent Tipo boolean recebe true ou false (serve para mandar excecoes)
     * @throws Exception
     */
    public void deleteWay(Integer way_id, boolean silent) throws Exception {

        //apagar este object

        Way way = GetWay(way_id);

        if (!database.Delete(way_id)){
            if(silent)
                return;
            else
                throw new Exception("Node nao apagado");
        }

        //apagar todos os objectos contidos neste

        ST<Integer, Tag> wayTags = way.getTags();
        for(Integer tagId : wayTags.keys())
            tagManager.deleteTag(wayTags.get(tagId).getId(), true);

        ST<Integer, Poi> wayPois = way.getPois();
        for(Integer poiId : wayPois.keys())
            poiManager.deletePoi(wayPois.get(poiId).getId(), true);
    }

    /**
     *
     * @param way Recebe o Way
     * @return Returna o way
     * @throws Exception
     */
    public Way SaveWay(Way way) throws Exception {

        validateWay(way.Name, way.Start, way.End);

        if (way.getId() == 0) {
            Way newWay = database.Insert(way);
            if (newWay != null) {
                listById.put(newWay.getId(), newWay);
                listByName.put(newWay.Name, newWay);
                return newWay;
            }
        } else {
            if (listById.contains(way.getId())) {
                Way newWay = database.Update(way);
                if (newWay != null) {
                    String oldWayname = listById.get(way.getId()).Name;
                    listById.put(newWay.getId(), newWay);
                    listByName.delete(oldWayname);
                    listByName.put(newWay.Name, newWay);
                }
            }
        }

        return null;
    }

    /**
     *
     * @param way_id Integer recebe o id do way
     * @param name String recebe o nome do way
     * @param start Integer incio da way
     * @param end Integer fim da way
     * @throws Exception
     */
    //editWay
    public void editWay(Integer way_id, String name, Integer start, Integer end) throws Exception {

        Way way = listById.get(way_id);
        if (way == null)
            throw new Exception("Id do way invalido");

        way = new Way();
        way.setId(way_id);
        way.Name = name;
        way.Start = start;
        way.End = end;

        SaveWay(way);
    }
    /**
     *
     * @param name String recebe o nome do way
     * @param start Integer incio da way
     * @param end Integer fim da way
     * @throws Exception
     */
    public void validateWay(String name, Integer start, Integer end) throws Exception {
        if (Test.isNullOrEmpty(name))
            throw new Exception("Nome invalido");
        if (start == 0)
            throw new Exception("Start invalido");
        if (end == 0)
            throw new Exception("End invalido");

    }

    /**
     *
     * @param way_id Integer recebe o id do way
     * @param descricao String recebe a descricao
     * @throws Exception
     */
    public void addTag(Integer way_id, String descricao) throws Exception {

        Way way = database.GetEntity(way_id);
        if (way == null)
            throw new Exception("Way invalido");

        int tag = tagManager.newTag(descricao);
        way.getTags().put(tag, tagManager.GetTag(tag));

        Way updatedWay = database.Update(way);
        if (updatedWay == null)
            throw new Exception("Tag nao associado ao way");
    }

    //deleteTag

    /**
     *
     * @param tag_id Integer recebe o id da tag
     * @param way_id Integer recebe o id do way
     * @throws Exception
     */
    public void deleteTag(Integer tag_id, Integer way_id) throws Exception {

        Way way = GetWay(way_id);
        ST<Integer, Tag> wayTags = way.getTags();

        if(wayTags.contains(tag_id)){
            wayTags.remove(tag_id);
            SaveWay(way);
        }

    }

    // addPoi

    /**
     *
     * @param way_id Integer recebe o id do way
     * @param Name String recebe o nome
     * @param descricao String recebe a descricao
     * @param localization Localization recebe a log e lan
     * @throws Exception
     */
    public void addPoi(Integer way_id, String Name, String descricao, Localization localization) throws Exception {

        Way way = database.GetEntity(way_id);
        if (way == null)
            throw new Exception("Way invalido");

        int poi = poiManager.newPoi(Name, descricao, localization);
        way.getPois().put(poi, poiManager.GetPoi(poi));

        Way updatedNode = database.Update(way);
        if (updatedNode == null)
            throw new Exception("Poi nao associado ao way");
    }

    //deletePoi

    /**
     *
     * @param poi_id Integer recebe o id do poi
     * @param way_id Integer recebe o id do way
     * @throws Exception
     */
    public void deletePoi(Integer poi_id, Integer way_id) throws Exception {

        Way way = GetWay(way_id);
        ST<Integer, Poi> wayPois = way.getPois();

        if(wayPois.contains(poi_id)){
            wayPois.remove(poi_id);
            SaveWay(way);
        }
    }

    /**
     *
     * @throws IOException
     */
    public void snapShot() throws IOException {
        database.SaveToFile();
    }

}

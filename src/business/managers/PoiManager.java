package business.managers;

import core.baseEntities.Entity;
import core.entities.*;
import core.interfaces.DatabaseI;
import core.utils.Test;
import database.PoiDatabase;
import edu.princeton.cs.algs4.BST;
import edu.princeton.cs.algs4.ST;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

public class PoiManager {

    private static NodeManager nodeManager;
    private static WayManager wayManager;
    private static TagManager tagManager;
    private static DatabaseI<Poi> database;
    private static BST<Integer, Poi> listById;
    private static BST<String, Poi> listByName;

    /**
     * construtor do PoiManager
     */
    public PoiManager(NodeManager node_Manager, WayManager way_Manager, TagManager tag_Manager) {

        if (database == null) {
            database = new PoiDatabase();
            listById = new BST<>();
            listByName = new BST<>();

            for (Poi poi : database.GetTable()) {
                listById.put(poi.getId(), poi);
                listByName.put(poi.Name, poi);
            }
        }

        if (node_Manager != null && nodeManager == null)
            nodeManager = node_Manager;

        if (way_Manager != null && wayManager == null)
            wayManager = way_Manager;

        if (tag_Manager != null && tagManager == null)
            tagManager = tag_Manager;
    }

    /**
     * @return a lista de pois
     */
    public ArrayList<Poi> GetAll() {
        ArrayList<Poi> list = new ArrayList<>();

        for (Integer poiId : listById.keys()) {
            list.add(listById.get(poiId));
        }

        return list;
    }

    /**
     * @param id id do poi
     * @return um poi
     */
    public Poi GetPoi(Integer id) {
        return listById.get(id);
    }

    /**
     *
     * @param Name String recebe o nome do poi
     * @param descricao String recebe a descricao do poi
     * @param longitude  longitude do poi
     * @param latitude  latitude do poi
     * @return Returna o poi com o respetivo id
     * @throws Exception
     */
    public int newPoi(String Name, String descricao, float longitude, float latitude) throws Exception {

        Poi poi = new Poi();
        poi.Name = Name;
        poi.Description = descricao;
        poi.getLocalization().Longitude = longitude;
        poi.getLocalization().Latitude = latitude;

        Poi newPoi = SavePoi(poi);

        return newPoi.getId();
    }

    /**
     *
     * @param poi_id Integer recebe o id de poi
     * @param silent Tipo boolean recebe true ou false (serve para mandar excecçoes)
     * @throws Exception
     */
    public void deletePoi(Integer poi_id, boolean silent) throws Exception {

        ArrayList<Way> ways = wayManager.GetAll();
        for(Way way : ways){
            ST<Integer, Poi> pois = way.getPois();
            for(Integer poiId : pois.keys()){
                if(poiId == poi_id)
                    if(silent)
                        return;
                    else
                        throw new Exception("Poi nao apagado, em uso num way");
            }
        }

        ArrayList<Node> nodes = nodeManager.GetAll();
        for(Node node : nodes){
            ST<Integer, Poi> pois = node.getPois();
            for(Integer poiId : pois.keys()){
                if(poiId == poi_id)
                    if(silent)
                        return;
                    else
                        throw new Exception("Poi nao apagado, em uso num node");
            }
        }

        ArrayList<Tag> tags = tagManager.GetAll();
        for (Tag tag : tags) {
            ArrayList<Entity> extra_info = tag.get_extra_info();
            for (Entity extraInfo : extra_info) {
                if (extraInfo.getClass() == Poi.class && extraInfo.getId() == poi_id)
                    if(silent)
                        return;
                    else
                        throw new Exception("Poi nao apagado, em uso num extra info");
            }
        }

        Poi poi = database.GetEntity(poi_id);
        if (!database.Delete(poi_id)){
            if(silent)
                return;
            else
                throw new Exception("Poi nao apagado");
        }

        listById.delete(poi_id);
        listByName.delete(poi.Name);
    }

    /**
     *
     * @param poi Recebe o Poi
     * @return Returna o Poi
     * @throws Exception
     */
    public Poi SavePoi(Poi poi) throws Exception {
        validatePoi(poi.Name, poi.Description, poi.getLocalization());

        if (poi.getId() == 0) {
            Poi newPoi = database.Insert(poi);
            if (newPoi != null) {
                listById.put(newPoi.getId(), newPoi);
                listByName.put(newPoi.Name, newPoi);
                return newPoi;
            }
        } else {
            if (listById.contains(poi.getId())) {
                Poi newPoi = database.Update(poi);
                if (newPoi != null) {
                    String oldPoiname = listById.get(poi.getId()).Name;
                    listById.put(newPoi.getId(), newPoi);
                    listByName.delete(oldPoiname);
                    listByName.put(newPoi.Name, newPoi);
                    return newPoi;
                }
            }
        }

        return null;
    }

    /**
     *
     * @param poi_id Integer recebe o id do poi
     * @param Name String que recebe o nome do poi
     * @param descricao String recebe a descricao do poi
     * @param localization Localizacao que recebe a latitude e longitude do poi
     * @throws Exception
     */
    public void editPoi(Integer poi_id, String Name, String descricao, Localization localization) throws Exception {

        Poi poi = listById.get(poi_id);
        if (poi == null)
            throw new Exception("Id do Poi invalido");

        poi = new Poi();
        poi.setId(poi_id);
        poi.Name = Name;
        poi.Description = descricao;
        poi.getLocalization().Longitude = localization.Longitude;
        poi.getLocalization().Latitude = localization.Latitude;

        SavePoi(poi);
    }

    /**
     *
     * @param Name String recebe o nome do poi
     * @param descricao String recebe descricao do poi
     * @param localization Localizacao que recebe a latitude e longitude do poi
     * @throws Exception
     */
    public void validatePoi(String Name, String descricao, Localization localization) throws Exception {
        if (Test.isNullOrEmpty(Name))
            throw new Exception("Nome vazio");
        if (Test.isNullOrEmpty(descricao))
            throw new Exception("Descricao vazia");
        if (localization == null)
            throw new Exception("Localização invalida");
    }

    /**
     *
     * @throws IOException
     */
    public void snapShot() throws IOException {
        database.SaveToFile();
    }

    public void binSnapShot() throws IOException {
        database.SaveToBinFile();
    }

    public void loadTextFile(String path) throws IOException, ParseException {

        database.ReadFromFile(path);

        for(int poiId : listById.keys()){
            listById.delete(poiId);
        }
        for(String poiName : listByName.keys()){
            listByName.delete(poiName);
        }

        for(Poi poi : database.GetTable()) {
            listById.put(poi.getId(), poi);
            listByName.put(poi.Name, poi);
        }
    }

    public void loadBinFile(String path) throws IOException {

        database.ReadFromBinFile(path);

        for(int poiId : listById.keys()){
            listById.delete(poiId);
        }
        for(String poiName : listByName.keys()){
            listByName.delete(poiName);
        }

        for(Poi poi : database.GetTable()) {
            listById.put(poi.getId(), poi);
            listByName.put(poi.Name, poi);
        }
    }
}

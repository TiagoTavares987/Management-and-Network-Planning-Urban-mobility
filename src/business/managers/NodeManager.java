package business.managers;

import business.utils.Utils;
import core.entities.*;
import core.interfaces.DatabaseI;
import core.utils.Test;
import database.NodeDatabase;
import edu.princeton.cs.algs4.BST;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.ST;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

public class NodeManager {

    private static WayManager wayManager;
    private static TagManager tagManager;
    private static PoiManager poiManager;
    private static DatabaseI<Node> database;
    private static BST<Integer, Node> listById;
    private static BST<String, Node> listByName;

    public NodeManager(WayManager way_Manager, TagManager tag_Manager, PoiManager poi_Manager) {

        if (database == null) {
            database = new NodeDatabase();
            listById = new BST<>();
            listByName = new BST<>();

            for (Node node : database.GetTable()) {
                listById.put(node.getId(), node);
                listByName.put(node.Name, node);
            }
        }

        if (way_Manager != null && wayManager == null)
            wayManager = way_Manager;

        if (tag_Manager != null && tagManager == null)
            tagManager = tag_Manager;

        if (poi_Manager != null && poiManager == null)
            poiManager = poi_Manager;
    }

    /**
     * @return list de pois
     */
    public ArrayList<Node> GetAll() {
        ArrayList<Node> list = new ArrayList<>();

        for(Integer nodeId : listById.keys()){
            list.add(listById.get(nodeId));
        }

        return list;
    }

    /**
     * @param id id do node
     * @return um node
     */
    public Node GetNode(Integer id) {
        return listById.get(id);
    }

    /**
     *
     * @param name String recebe o nome do node
     * @param localization Localization recebe o local do node
     * @return novo node com o respetivo id
     * @throws Exception
     */
    public int newNode(String name, Localization localization) throws Exception {

        Node node = new Node();
        node.Name = name;
        node.getLocalization().Latitude = localization.Latitude;
        node.getLocalization().Longitude = localization.Longitude;

        Node newNode= SaveNode(node); // guardar o node

        return newNode.getId(); // retorna o id do novo node
    }

    /**
     *
     * @param node_id Integer recebe o valor do id do node
     * @param silent Tipo boolean recebe true ou false (serve para mandar exceçoes)
     * @throws Exception
     */
    public void deleteNode(Integer node_id, boolean silent) throws Exception {
        //verificar se algum objeto depende deste

        ArrayList<Way> ways = wayManager.GetAll(); // recebe para o array todas as ways
        for(Way way : ways){
            if(way.Start == node_id || way.End == node_id){
                if(silent)
                    return;
                else
                    throw new Exception("Node nao apagado, em uso num way");
            }
        }

        //apagar este object
        Node node = GetNode(node_id);

        if (!database.Delete(node_id)){
            if(silent)
                return;
            else
                throw new Exception("Node nao apagado");
        }

        listById.delete(node_id);
        listByName.delete(node.Name);

        //apagar todos os objectos contidos neste
        ST<Integer, Tag> nodeTags = node.getTags(); // buscar todas as tags daquele node
        for(Integer tagId : nodeTags.keys())
            tagManager.deleteTag(nodeTags.get(tagId).getId(), true);

        ST<Integer, Poi> nodePois = node.getPois();
        for(Integer poiId : nodePois.keys())
            poiManager.deletePoi(nodePois.get(poiId).getId(), true);
    }

    /**
     *
     * @param node Recebe node com todos os seus atributos
     * @return Returna o Node
     * @throws Exception
     */
    public Node SaveNode(Node node) throws Exception {
        validateNode(node.Name, node.getLocalization());

        if (node.getId() == 0) {
            Node newNode = database.Insert(node);
            if (newNode != null) {
                listById.put(newNode.getId(), newNode);
                listByName.put(newNode.Name, newNode);
                return newNode;
            }
        } else {
            if (listById.contains(node.getId())) {
                Node newNode = database.Update(node);
                if (newNode != null) {
                    String oldNodename = listById.get(node.getId()).Name; // buscar o nome do node com aquele id
                    listById.put(newNode.getId(), newNode);
                    listByName.delete(oldNodename);
                    listByName.put(newNode.Name, newNode);
                    return newNode;
                }
            }
        }

        return null;
    }

    /**
     *
     * @param node_id Integer que recebe o id do node
     * @param Name String que recebe o nome do node
     * @param localization Localization recebe a localizacao do node
     * @throws Exception
     */
    //editNode
    public void editNode(Integer node_id, String Name, Localization localization) throws Exception {

        Node node = listById.get(node_id);
        if(node == null)
            throw new Exception("Id do Node invalido");

        Node newNode = new Node();
        newNode.setId(node_id);
        newNode.Name = Name;
        newNode.getLocalization().Longitude = localization.Longitude;
        newNode.getLocalization().Latitude = localization.Latitude;


        // temos que guardar os pois e tags do node pois ao editarmos sem isto perdiamos a referencia a eles
        ST<Integer, Poi> oldPois = node.getPois();
        ST<Integer, Poi> newPois = newNode.getPois();
        for(Integer id : oldPois)
            newPois.put(oldPois.get(id).getId(), oldPois.get(id));

        ST<Integer, Tag> oldTags = node.getTags();
        ST<Integer, Tag> newTags = newNode.getTags();
        for(Integer id : oldTags)
            newTags.put(oldTags.get(id).getId(), oldTags.get(id));

        SaveNode(newNode);
    }

    /**
     *
     * @param Name String que recebe o nome do node
     * @param localization Localization que recebe a localizacao do node
     * @throws Exception
     */

    public void validateNode(String Name, Localization localization) throws Exception {
        if (Test.isNullOrEmpty(Name))
            throw new Exception("Nome invalido");

        if (localization == null)
            throw new Exception("Localização invalida");
    }

    /**
     *
     * @param node_id Integer que recebe o id do node
     * @param tag_id Integer que recebe o id da tag
     * @throws Exception
     */
    public void addTag(Integer node_id, Integer tag_id) throws Exception {

        Node node = database.GetEntity(node_id);
        if (node == null)
            throw new Exception("Node invalido");

        Tag tag = tagManager.GetTag(tag_id);
        if (tag == null)
            throw new Exception("Tag invalida");

        node.getTags().put(tag_id, tag);

        Node updatedNode = database.Update(node);
        if (updatedNode == null)
            throw new Exception("Tag nao associado ao node");

        listById.put(updatedNode.getId(), updatedNode);
        listByName.put(updatedNode.Name, updatedNode);
    }

    /**
     *
     * @param tag_id Integer que recebe o id da tag
     * @param node_id Integer que recebe o id do node
     * @throws Exception
     */
    //deleteTag
    public void deleteTag(Integer node_id, Integer tag_id) throws Exception {

        Node node = database.GetEntity(node_id);
        if (node == null)
            throw new Exception("Node invalido");

        Tag tag = tagManager.GetTag(tag_id);
        if (tag == null)
            throw new Exception("Tag invalida");

        node.getTags().remove(tag_id);

        Node updatedNode = database.Update(node);
        if (updatedNode == null)
            throw new Exception("Tag nao associado ao node");

        listById.put(updatedNode.getId(), updatedNode);
        listByName.put(updatedNode.Name, updatedNode);
    }

    /**
     *
     * @param node_id Integer que receberá o id do node
     * @param poi_id id do poi
     * @throws Exception
     */
    public void addPoi(Integer node_id, Integer poi_id) throws Exception {

        Node node = database.GetEntity(node_id);
        if (node == null)
            throw new Exception("Node invalido");

        Poi poi = poiManager.GetPoi(poi_id);
        if (poi == null)
            throw new Exception("Poi invalido");

        node.getPois().put(poi_id, poi);

        Node updatedNode = database.Update(node);
        if (updatedNode == null)
            throw new Exception("Poi nao associado ao node");

        listById.put(updatedNode.getId(), updatedNode);
        listByName.put(updatedNode.Name, updatedNode);
    }

    //deletePoi

    /**
     *
     * @param poi_id Integer que receberá o id do ponto de interesse
     * @param node_id Integer que receberá o id do node
     * @throws Exception
     */
    public void deletePoi(Integer node_id, Integer poi_id) throws Exception {

        Node node = database.GetEntity(node_id);
        if (node == null)
            throw new Exception("Node invalido");

        Poi poi = poiManager.GetPoi(poi_id);
        if (poi == null)
            throw new Exception("Poi invalido");

        node.getPois().remove(poi_id);

        Node updatedNode = database.Update(node);
        if (updatedNode == null)
            throw new Exception("Poi nao associado ao node");

        listById.put(updatedNode.getId(), updatedNode);
        listByName.put(updatedNode.Name, updatedNode);
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

    /**
     *
     * @throws IOException
     */
    public void loadTextFile(String path) throws IOException, ParseException {

        database.ReadFromFile(path);

        for(int nodeId : listById.keys()){
            listById.delete(nodeId);
        }
        for(String nodeName : listByName.keys()){
            listByName.delete(nodeName);
        }

        for(Node node : database.GetTable()) {
            listById.put(node.getId(), node);
            listByName.put(node.Name, node);
        }
    }

    public void loadBinFile(String path) throws IOException {

        database.ReadFromBinFile(path);

        for(int nodeId : listById.keys()){
            listById.delete(nodeId);
        }
        for(String nodeName : listByName.keys()){
            listByName.delete(nodeName);
        }

        for(Node node : database.GetTable()) {
            listById.put(node.getId(), node);
            listByName.put(node.Name, node);
        }
    }
}

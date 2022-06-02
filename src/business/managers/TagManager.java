package business.managers;

import core.baseEntities.Entity;
import core.entities.*;
import core.interfaces.DatabaseI;
import core.utils.Test;
import database.TagDatabase;
import edu.princeton.cs.algs4.BST;
import edu.princeton.cs.algs4.ST;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

public class TagManager {

    private static NodeManager nodeManager;
    private static WayManager wayManager;
    private static PoiManager poiManager;
    private DatabaseI<Tag> database;
    private static BST<Integer, Tag> listById;
    private static BST<String, Tag> listByDescricao;


    public TagManager(NodeManager node_Manager, WayManager way_Manager, PoiManager poi_Manager) {

        if (database == null) {
            database = new TagDatabase();
            listById = new BST<>();
            listByDescricao = new BST<>();

            for (Tag tag : database.GetTable()) {
                listById.put(tag.getId(), tag);
                listByDescricao.put(tag.Description, tag);
            }
        }

        if (node_Manager != null && nodeManager == null)
            nodeManager = node_Manager;

        if (way_Manager != null && wayManager == null)
            wayManager = way_Manager;

        if (poi_Manager != null && poiManager == null)
            poiManager = poi_Manager;
    }


    /**
     * @return a lista de tags
     */
    public ArrayList<Tag> GetAll() {
        ArrayList<Tag> list = new ArrayList<>();

        for (Integer tagId : listById.keys()) {
            list.add(listById.get(tagId));
        }

        return list;
    }

    /**
     * @param id id da tag
     * @return a tag
     */
    public Tag GetTag(Integer id) {
        return listById.get(id);
    }

    /**
     * @param descricao String recebe a descricao
     * @return Returna a tag com o respetivo id
     * @throws Exception
     */
    public int newTag(String descricao) throws Exception {

        Tag tag = new Tag();
        tag.Description = descricao;

        Tag newTag = SaveTag(tag);

        return newTag.getId();
    }

    /**
     * @param tag_id     Integer recebe o id da tag
     * @param extra_info Entity recebe a informacao extra
     * @throws Exception
     */
    public void addExtraInfo(Integer tag_id, Entity extra_info) throws Exception {

        Tag tag = GetTag(tag_id);
        if (tag == null)
            throw new Exception("Tag invalida");

        ArrayList<Entity> tagExtraInfo = tag.get_extra_info();

        if (extra_info.getClass() == Poi.class) {
            Poi poi = poiManager.SavePoi((Poi) extra_info);
            if (poi != null) {
                tagExtraInfo.add(poi);
                SaveTag(tag);
            }
        } else if (extra_info.getClass() == Tag.class) {
            Tag tagExtra = SaveTag((Tag) extra_info);
            if (tagExtra != null) {
                tagExtraInfo.add(tagExtra);
                SaveTag(tag);
            }
        } else {
            throw new Exception("Tipo de informação extra invalida");
        }
    }

    /**
     * @param tag_id     Integer recebe o id da tag
     * @param extra_info Entity recebe a informacao extra
     * @throws Exception
     */
    public void deleteExtraInfo(Integer tag_id, Entity extra_info) throws Exception {

        Tag tag = GetTag(tag_id);
        if (tag == null)
            throw new Exception("Tag invalida");

        ArrayList<Entity> tagExtraInfo = tag.get_extra_info();

        if (extra_info.getClass() == Poi.class) {
            Poi poi = poiManager.SavePoi((Poi) extra_info);
            if (poi != null) {
                Poi extraPoi = null;
                for (Entity extraInfo : tagExtraInfo) {
                    if (extraInfo.getClass() == Poi.class && extraInfo.getId() == poi.getId()) {
                        extraPoi = (Poi) extraInfo;
                        break;
                    }
                }
                if (extraPoi != null)
                    tagExtraInfo.remove(extraPoi);
            }
        } else if (extra_info.getClass() == Tag.class) {
            Tag tagExtra = SaveTag((Tag) extra_info);
            if (tagExtra != null) {
                Tag extraTag = null;
                for (Entity extraInfo : tagExtraInfo) {
                    if (extraInfo.getClass() == Tag.class && extraInfo.getId() == tagExtra.getId()) {
                        extraTag = (Tag) extraInfo;
                        break;
                    }
                }
                if (extraTag != null)
                    tagExtraInfo.remove(extraTag);
            }
        } else {
            throw new Exception("Tipo de informação extra invalida");
        }
    }

    /**
     * @param tag_id Integer recebe o id da tag
     * @param silent Tipo boolean recebe true ou false (serve para mandar throws)
     * @throws Exception
     */
    public void deleteTag(Integer tag_id, boolean silent) throws Exception {

        ArrayList<Way> ways = wayManager.GetAll();
        for (Way way : ways) {
            ST<Integer, Poi> tags = way.getPois();
            for (Integer tagId : tags.keys()) {
                if (tagId == tag_id)
                    if (silent)
                        return;
                    else
                        throw new Exception("Tag nao apagado, em uso num way");
            }
        }

        ArrayList<Node> nodes = nodeManager.GetAll();
        for (Node node : nodes) {
            ST<Integer, Poi> tags = node.getPois();
            for (Integer tagId : tags.keys()) {
                if (tagId == tag_id)
                    if (silent)
                        return;
                    else
                        throw new Exception("Tag nao apagado, em uso num node");
            }
        }

        ArrayList<Tag> tags = GetAll();
        for (Tag tag : tags) {
            if (tag.getId() != tag_id) {
                ArrayList<Entity> extra_info = tag.get_extra_info();
                for (Entity extraInfo : extra_info) {
                    if (extraInfo.getClass() == Tag.class && extraInfo.getId() == tag_id)
                        if (silent)
                            return;
                        else
                            throw new Exception("Tag nao apagado, em uso num extra info");
                }
            }
        }

        Tag tag = GetTag(tag_id);
        if (!database.Delete(tag_id)) {
            if (silent)
                return;
            else
                throw new Exception("Tag nao apagada");
        }

        listById.delete(tag_id);
        listByDescricao.delete(tag.Description);

        //apagar todos os objectos contidos neste
        ArrayList<Entity> tagExtraInfo = tag.get_extra_info();
        for (Entity extraInfo : tagExtraInfo) {
            if (extraInfo.getClass() == Poi.class) {
                poiManager.deletePoi(extraInfo.getId(), true);
            } else if (extraInfo.getClass() == Tag.class) {
                deleteTag(extraInfo.getId(), true);
            }
        }
    }

    /**
     * @param tag Recebe a Tag
     * @return Returna a Tag
     * @throws Exception
     */
    public Tag SaveTag(Tag tag) throws Exception {
        validateTag(tag.Description);

        if (tag.getId() == 0) {
            Tag newTag = database.Insert(tag);
            if (newTag != null) {
                listById.put(newTag.getId(), newTag);
                listByDescricao.put(newTag.Description, newTag);
                return newTag;
            }
        } else {
            if (listById.contains(tag.getId())) {
                Tag newTag = database.Update(tag);
                if (newTag != null) {
                    String oldTagDescription = listById.get(tag.getId()).Description;
                    listById.put(newTag.getId(), newTag);
                    listByDescricao.delete(oldTagDescription);
                    listByDescricao.put(newTag.Description, newTag);
                    return newTag;
                }
            }
        }

        return null;
    }

    /**
     * @param tag_id    Integer recebe o id da tag
     * @param descricao String que recebe a descricao
     * @throws Exception
     */
    public void editTag(Integer tag_id, String descricao) throws Exception {

        Tag tag = listById.get(tag_id);
        if (tag == null)
            throw new Exception("Id da Tag invalido");

        tag = new Tag();
        tag.setId(tag_id);
        tag.Description = descricao;

        SaveTag(tag);
    }

    /**
     * @param descricao String que recebe a descricao
     * @throws Exception
     */
    public void validateTag(String descricao) throws Exception {

        if (Test.isNullOrEmpty(descricao))
            throw new Exception("Descricao vazio");
    }

    /**
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

        for(int tagId : listById.keys()){
            listById.delete(tagId);
        }
        for(String descricao : listByDescricao.keys()){
            listByDescricao.delete(descricao);
        }

        for(Tag tag : database.GetTable()) {
            listById.put(tag.getId(), tag);
            listByDescricao.put(tag.Description, tag);
        }
    }

    public void loadBinFile(String path) throws IOException {

        database.ReadFromBinFile(path);

        for(int tagId : listById.keys()){
            listById.delete(tagId);
        }
        for(String descricao : listByDescricao.keys()){
            listByDescricao.delete(descricao);
        }

        for(Tag tag : database.GetTable()) {
            listById.put(tag.getId(), tag);
            listByDescricao.put(tag.Description, tag);
        }
    }
}


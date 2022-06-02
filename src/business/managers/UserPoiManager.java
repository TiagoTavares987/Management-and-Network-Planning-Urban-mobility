package business.managers;

import core.baseEntities.Entity;
import core.entities.*;
import core.enums.UserType;
import edu.princeton.cs.algs4.ST;

import java.util.ArrayList;
import java.util.Date;


public class UserPoiManager {

    private static UserManager userManager;
    private static PoiManager poiManager;
    private static WayManager wayManager;
    private static NodeManager nodeManager;

    public UserPoiManager(UserManager user_Manager, PoiManager poi_Manager, WayManager way_Manager, NodeManager node_Manager) {

        if (user_Manager != null && userManager == null)
            userManager = user_Manager;

        if (poi_Manager != null && poiManager == null)
            poiManager = poi_Manager;

        if (way_Manager != null && wayManager == null)
            wayManager = way_Manager;

        if (node_Manager != null && nodeManager == null)
            nodeManager = node_Manager;
    }
    /**
     * @param userId id o user
     * @param ini data inicio
     * @param fim data fim
     * @return a lista de pois
     */
    public ArrayList<Poi> poisVisitadosUser(Integer userId, Date ini, Date fim) {
        ArrayList<Poi> list = new ArrayList<>();

        User user = userManager.GetUser(userId);
        if (user != null) {
            ST<Integer, UserPoi> userPois = user.getPois();
            for (Integer poiId : userPois.keys()) {
                UserPoi userPoi = userPois.get(poiId); // buscar o objeto ao dicionario com o respetiva chave
                if (userPoi.date.after(ini) && userPoi.date.before(fim))
                    list.add(poiManager.GetPoi(userPoi.poi));
            }
        }
        return list;
    }

    /**
     * @param userId id o user
     * @param ini data inicio
     * @param fim data fim
     * @return a lista de pois
     */
    public ArrayList<Poi> poisNaoVisitadosUser(Integer userId, Date ini, Date fim) {
        ArrayList<Poi> list = new ArrayList<>();

        User user = userManager.GetUser(userId);

        if (user != null) {
            ST<Integer, UserPoi> userPoisMatch = user.getPois();
            ST<Integer, UserPoi> userPois = new ST<>();
            for (Integer userPoi : userPoisMatch.keys()){
                UserPoi poi = userPoisMatch.get(userPoi); // buscar o objeto ao dicionario com o respetiva chave
                if(!userPois.contains(poi.poi))
                    userPois.put(poi.poi, poi);
            }

            ArrayList<Poi> allPois = poiManager.GetAll();
            for (Poi poi : allPois) {
                if (!userPois.contains(poi.getId()) || userPois.get(poi.getId()).date.before(ini) || userPois.get(poi.getId()).date.after(fim))
                    list.add(poi);
            }
        }

        return list;
    }

    /**
     * @param poiId id o poi
     * @param ini data inicio
     * @param fim data fim
     * @return a lista de users
     */
    public ArrayList<User> usersQueVisitraramUmPoi(Integer poiId, Date ini, Date fim) {
        ArrayList<User> list = new ArrayList<>();

        ArrayList<User> users = userManager.GetAll();
        for (User user : users) {
            ST<Integer, UserPoi> userPois = user.getPois();
            for(Integer userPoiId : userPois.keys()) {
                UserPoi userPoi = userPois.get(userPoiId);
                if (userPoi.poi == poiId && userPoi.date.after(ini) && userPoi.date.before(fim))
                    list.add(user);
            }
        }

        return list;
    }

    /**
     * @param ini data inicio
     * @param fim data fim
     * @return a lista de users
     */
    public ArrayList<Poi> poisNaoVisitados(Date ini, Date fim) {
        ArrayList<Poi> list = new ArrayList<>();

        ST<Integer, Integer> poisVisitados = new ST<>();

        for(User user : userManager.GetAll()) {

            ST<Integer, UserPoi> userPois = user.getPois();
            for (Integer poiId : userPois.keys()) {
                UserPoi userPoi = userPois.get(poiId); // buscar o userpoi com aquele id de poi
                if (userPoi.date.after(ini) && userPoi.date.before(fim))
                    poisVisitados.put(userPoi.poi, 0);
            }
        }

        for(Poi poi : poiManager.GetAll()){
            if(!poisVisitados.contains(poi.getId()))
                list.add(poi);
        }

        return list;
    }
    /**
     * @param ini data inicio
     * @param fim data fim
     * @return a lista de users
     */
    public ArrayList<User> top5Users(Date ini, Date fim) {
        ArrayList<User> list = new ArrayList<>();

        ST<Integer, Integer> count = new ST<>();
        ArrayList<User> users = userManager.GetAll();
        for (User user : users) {
            ST<Integer, UserPoi> userPois = user.getPois();
            for (Integer poiId : userPois.keys()) {
                if (userPois.get(poiId).date.after(ini) && userPois.get(poiId).date.before(fim)) {
                    int c = 1;
                    int userId = user.getId();
                    if (count.contains(userId))
                        c += count.get(userId);

                    count.put(userId, c);
                }
            }
        }

        int[] top5user = top5(count);

        for(int i = 0; i < 5; i++)
            list.add(userManager.GetUser(top5user[i]));

        return list;
    }

    /**
     * @param ini data inicio
     * @param fim data fim
     * @return lista de pois
     */
    public ArrayList<Poi> top5Pois(Date ini, Date fim) {
        ArrayList<Poi> list = new ArrayList<>();

        ST<Integer, Integer> poisCount = new ST<>();

        ArrayList<User> users = userManager.GetAll();
        for (User user : users) {
            ST<Integer, UserPoi> userPois = user.getPois();
            for (Integer poiId : userPois.keys()) {
                UserPoi poi = userPois.get(poiId);
                if (poi.date.after(ini) && poi.date.before(fim)) {
                    if(!poisCount.contains(poi.poi))
                        poisCount.put(poi.poi, 0);

                    poisCount.put(poi.poi, poisCount.get(poi.poi) + 1);
                }
            }
        }

        int[] top5poi = top5(poisCount);

        for(int i = 0; i < 5; i++)
            list.add(poiManager.GetPoi(top5poi[i]));

        return list;
    }
    private int[] top5 (ST<Integer, Integer> list) {
        int[] top5id = new int[] {0, 0, 0, 0, 0}; // userID
        int[] top5list = new int[] {0, 0, 0, 0, 0}; // contagem dos pois
        for (Integer userId : list.keys()) {
            Integer value = list.get(userId);
            for(int i = 0; i < 5; i++){
                if(value > top5list[i]) {
                    //shitf e organizar pelo maior
                    for(int j = 4; j > i; j--){
                        top5list[j] = top5list[j-1];
                        top5id[j] = top5id[j-1];
                    }
                    top5list[i] = value;
                    top5id[i] = userId;
                    break;
                }
            }
        }
        return top5id;
    }

    /**
     * @param tagId id da tag
     * @return lista de nodes e ways
     */
    public ArrayList<Entity> linhasVerticesArestas(Integer tagId) {
        ArrayList<Entity> list = new ArrayList<>();

        ArrayList<Node> nodes = nodeManager.GetAll();
        for (Node node : nodes) {
            ST<Integer, Tag> tags = node.getTags();
            for(Integer tag_Id : tags.keys()){
                if(tags.get(tag_Id).getId() == tagId){
                    list.add(node);
                    break;
                }
            }
        }

        ArrayList<Way> ways = wayManager.GetAll();
        for (Way way : ways) {
            ST<Integer, Tag> tags = way.getTags();
            for(Integer tag_Id : tags.keys()){
                if(tags.get(tag_Id).getId() == tagId){
                    list.add(way);
                    break;
                }
            }
        }

        return list;
    }

    /**
     * primeiro validar o user
     * segundo ver se ja contem o poi se nao adiciona
     * e no fim retorna
     * @param user_id
     * @param Name
     * @param descricao
     * @param longitude
     * @param latitude
     * @return
     */
    public int newPoi(Integer user_id, String Name, String descricao, float longitude, float latitude) throws Exception {

        User user = userManager.GetUser(user_id);
        if (user == null)
            throw new Exception("Utilizador invalido");

        int poi = poiManager.newPoi(Name, descricao, longitude, latitude);

        if (!userManager.addPoi(user_id, poi))
            throw new Exception("Poi nao associado ao utilizador");

        return poi;
    }

    /**
     *
     * @param user_id Integer recebe o id do user
     * @param poi_id Integer recebe o id do poi
     * @throws Exception
     */
    public void deletePoi(Integer user_id, Integer poi_id) throws Exception {

        if (UserManager.getCurrentUser().UserType != UserType.Admin)
            throw new Exception("SÃ³ o admin tem permissao para apagar");

        User user = userManager.GetUser(user_id);
        if (user == null)
            throw new Exception("Utilizador invalido");

        poiManager.deletePoi(poi_id, true);

        if (!userManager.deletePoi(user_id, poi_id))
            throw new Exception("Poi nao associado ao utilizador");

    }

    /**
     *
     * @param poi_id Integer recebe o id do poi
     * @param Name String recebe o nome do poi
     * @param descricao String recebe o nome da descricao
     * @param localization Localizatio recebe log e lan do poi
     * @throws Exception
     */
    public void editPoi(Integer poi_id, String Name, String descricao, Localization localization) throws Exception {
        poiManager.editPoi(poi_id, Name, descricao, localization);
    }

}

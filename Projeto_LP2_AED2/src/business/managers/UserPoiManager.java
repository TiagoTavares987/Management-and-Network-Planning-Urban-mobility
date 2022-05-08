package business.managers;

import core.baseEntities.Entity;
import core.entities.Poi;
import core.entities.User;
import edu.princeton.cs.algs4.ST;

import java.util.ArrayList;
import java.util.Date;

public class UserPoiManager {

    private final UserManager userManager = new UserManager();
    private final PoiManager poiManager = new PoiManager();
    private final WayManager wayManager = new WayManager();
    private final NodeManager nodeManager = new NodeManager();


    /**
     * @param userId id o user
     * @param ini data inicio
     * @param fim data fim
     * @return a lista de pois
     */
    public ArrayList<Poi> poisVisitadosUser(Integer userId, Date ini, Date fim) {
        ArrayList<Poi> list = new ArrayList<>();

        var user = userManager.GetUser(userId);
        if (user != null) {
            var userPois = user.getPois();
            for (var poiId : userPois.keys()) {
                var userPoi = userPois.get(poiId);
                if (userPoi.date.after(ini) && userPoi.date.before(fim))
                    list.add(poiManager.GetPoi(poiId));
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

        var user = userManager.GetUser(userId);

        if (user != null) {
            var userPois = user.getPois();
            var allPois = poiManager.GetAll();
            for (var poi : allPois) {
                var poiUser = userPois.get(userId);
                if (!userPois.contains(poi.getId())) {
                    if (poiUser.date.after(ini) && poiUser.date.before(fim))
                        list.add(poi);
                }
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

        var users = userManager.GetAll();
        for (var user : users) {
            var userPois = user.getPois();
            if (userPois.contains(poiId)) {
                var userPoi = userPois.get(poiId);
                if (userPoi.date.after(ini) && userPoi.date.before(fim))
                    list.add(user);
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
    public ArrayList<User> poisNaoVisitados(Integer poiId, Date ini, Date fim) {
        return null;
    }

    public ArrayList<User> top5Users(Date ini, Date fim) {
        ArrayList<User> list = new ArrayList<>();

        ST<Integer, Integer> count = new ST<>();
        var users = userManager.GetAll();
        for (var user : users) {
            var userPois = user.getPois();
            for (var poiId : userPois.keys()) {
                if (userPois.get(poiId).date.after(ini) && userPois.get(poiId).date.before(fim)) {
                    int c = 1;
                    int userId = user.getId();
                    if (count.contains(userId))
                        c += count.get(userId);

                    count.put(userId, c);
                }
            }
        }

        for (var userId : count.keys()) {
            var value = count.get(userId);
            list.add(userManager.GetUser(userId));
        }

        return list;
    }

    /**
     * @param ini data inicio
     * @param fim data fim
     * @return lista de pois
     */
    public ArrayList<Poi> top5Pois(Date ini, Date fim) {
        ArrayList<Poi> list = new ArrayList<>();
        var pois = poiManager.GetAll();
        for (var poi : pois) {
        }
        return list;
    }

    /**
     * @param tagId id da tag
     * @return lista de nodes e ways
     */
    public ArrayList<Entity> linhasVerticesArestas(Integer tagId) {
        ArrayList<Entity> list = new ArrayList<>();

        var nodes = nodeManager.GetAll();
        for (var node : nodes) {
            var tags = node.getTags();
            if (tags.contains(tagId)) {
                list.add(node);
                break;
            }
        }

        var ways = wayManager.GetAll();
        for (var way : ways) {
            var tags = way.getTags();
            if (tags.contains(tagId)) {
                list.add(way);
                break;
            }
        }

        return list;
    }
}

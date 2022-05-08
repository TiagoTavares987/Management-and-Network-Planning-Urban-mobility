package database;

import core.entities.User;
import core.entities.UserPoi;
import core.interfaces.DatabaseI;
import edu.princeton.cs.algs4.ST;
import java.util.ArrayList;


public class UserDatabase implements DatabaseI<User> {

    private final ST<Integer, User> userDatabaseST = new ST<>();

    /**
     * @param id
     * @return a entidade
     */
    @Override
    public User GetEntity(Integer id) {

        if(userDatabaseST.contains(id))
            return userDatabaseST.get(id).Clone();
        else
            return null;
    }

    /**
     * @return as entidades
     */
    @Override
    public ArrayList<User> GetTable() {

        var entities = new ArrayList<User>();
        for (var id : userDatabaseST)
            entities.add(userDatabaseST.get(id).Clone());

        return entities;
    }

    /**
     * @param entity
     * @return a entidade inserida
     */
    @Override
    public User Insert(User entity) {

        if (entity == null || entity.getId() != 0)
            return null;

        var id = 1;
        if(userDatabaseST.size() > 0)
            id += userDatabaseST.max();

        entity.setId(id);
        userDatabaseST.put(id, entity.Clone());

        return entity;
    }

    /**
     * @param entity
     * @return a entidade atualizada ou null se nao existir
     */
    @Override
    public User Update(User entity) {

        if (entity == null || !userDatabaseST.contains(entity.getId()))
            return null;

        userDatabaseST.put(entity.getId(), entity.Clone());
        return entity;
    }

    /**
     * @param id
     * @return verdadeira se por apagada com sucesso ou falsa se não tiver na database
     */
    @Override
    public boolean Delete(Integer id) {

        if (!userDatabaseST.contains(id))
            return false;

        userDatabaseST.remove(id);
        return true;
    }

    /**
     * @param user_id
     * @param poi_id
     * @return a entidade atualizada com o novo POI
     */
    public boolean addPoi(Integer user_id, Integer poi_id) {

        var entity = (User) GetEntity(user_id);
        if (entity == null)
            return false;

        var id = 1;
        var pois = entity.getPois();
        if(pois.size() > 0)
            id += pois.max();

        var user_poi = new UserPoi();
        user_poi.setId(id);
        user_poi.user_id = user_id;
        user_poi.poi = poi_id;
        user_poi.date = new java.util.Date();
        pois.put(id, user_poi);

        return Update(entity) != null;
    }

    /**
     * @param user_id
     * @param poi_id
     * @return falsa se nao existir entidade ou nao tiver poi e retorna a entidade atualizada
     */
    public boolean deletePoi(Integer user_id, Integer poi_id) {

        var entity = (User) GetEntity(user_id);
        if (entity == null)
            return false;

        var pois = entity.getPois();
        if(pois.contains(poi_id)){
            pois.remove(1);
            return Update(entity) != null;
        }

        return false;
    }
}
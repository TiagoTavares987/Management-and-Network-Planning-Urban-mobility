package database;

import core.entities.Way;
import core.interfaces.DatabaseI;
import edu.princeton.cs.algs4.ST;

import java.util.ArrayList;

public class WayDatabase implements DatabaseI<Way> {

    private final ST<Integer, Way> wayDatabaseST = new ST<>();

    /**
     * @param id
     * @return a entidade
     */
    @Override
    public Way GetEntity(Integer id) {

        if(wayDatabaseST.contains(id))
            return wayDatabaseST.get(id).Clone();
        else
            return null;
    }

    /**
     * @return as entidades
     */
    @Override
    public ArrayList<Way> GetTable() {

        var entities = new ArrayList<Way>();
        for (var id : wayDatabaseST)
            entities.add(wayDatabaseST.get(id).Clone());

        return entities;
    }

    /**
     * @param entity
     * @return a entidade inserida
     */
    @Override
    public Way Insert(Way entity) {

        if (entity == null || entity.getId() != 0)
            return null;

        var id = 1;
        if(wayDatabaseST.size() > 0)
            id += wayDatabaseST.max();

        entity.setId(id);
        wayDatabaseST.put(id, entity.Clone());

        return entity;
    }

    /**
     * @param entity
     * @return a entidade atualizada ou null se nao existir
     */
    @Override
    public Way Update(Way entity) {

        if (entity == null || !wayDatabaseST.contains(entity.getId()))
            return null;

        wayDatabaseST.put(entity.getId(), entity.Clone());
        return entity;
    }

    /**
     * @param id
     * @return verdadeira se por apagada com sucesso ou falsa se não tiver na database
     */
    @Override
    public boolean Delete(Integer id) {

        if (!wayDatabaseST.contains(id))
            return false;

        wayDatabaseST.remove(id);
        return true;
    }
}

package database;

import core.entities.Poi;
import core.interfaces.DatabaseI;
import edu.princeton.cs.algs4.ST;

import java.util.ArrayList;

public class PoiDatabase implements DatabaseI<Poi> {

    private final ST<Integer, Poi> poiDatabaseST = new ST<>();

    /**
     * @param id
     * @return a entidade
     */
    @Override
    public Poi GetEntity(Integer id) {
        if(poiDatabaseST.contains(id))
            return poiDatabaseST.get(id).Clone();
        else
            return null;
    }

    /**
     * @return as entidades
     */
    @Override
    public ArrayList<Poi> GetTable() {

        var entities = new ArrayList<Poi>();
        for (var id : poiDatabaseST)
            entities.add(poiDatabaseST.get(id).Clone());

        return entities;
    }

    /**
     * @param entity
     * @return a entidade inserida
     */
    @Override
    public Poi Insert(Poi entity) {

        if (entity == null || entity.getId() != 0)
            return null;

        var id = 1;
        if(poiDatabaseST.size() > 0)
            id += poiDatabaseST.max();

        entity.setId(id);
        poiDatabaseST.put(id, entity.Clone());

        return entity;
    }

    /**
     * @param entity
     * @return a entidade atualizada ou null se nao existir
     */
    @Override
    public Poi Update(Poi entity) {

        if (entity == null || !poiDatabaseST.contains(entity.getId()))
            return null;

        poiDatabaseST.put(entity.getId(), entity.Clone());
        return entity;
    }

    /**
     * @param id
     * @return verdadeira se por apagada com sucesso ou falsa se não tiver na database
     */
    @Override
    public boolean Delete(Integer id) {

        if (!poiDatabaseST.contains(id))
            return false;

        poiDatabaseST.remove(id);
        return true;
    }
}


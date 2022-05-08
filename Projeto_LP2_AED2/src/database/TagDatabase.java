package database;

import core.entities.Tag;
import core.interfaces.DatabaseI;
import edu.princeton.cs.algs4.ST;
import java.util.ArrayList;


public class TagDatabase implements DatabaseI<Tag> {

    private final ST<Integer, Tag> tagDatabaseST = new ST<>();

    /**
     * @param id
     * @return a entidade
     */
    @Override
    public Tag GetEntity(Integer id) {

        if(tagDatabaseST.contains(id))
            return tagDatabaseST.get(id).Clone();
        else
            return null;
    }

    /**
     * @return as entidades
     */
    @Override
    public ArrayList<Tag> GetTable() {

        var entities = new ArrayList<Tag>();
        for (var id : tagDatabaseST)
            entities.add(tagDatabaseST.get(id).Clone());

        return entities;
    }

    /**
     * @param entity
     * @return a entidade inserida
     */
    @Override
    public Tag Insert(Tag entity) {

        if (entity == null || entity.getId() != 0)
            return null;

        var id = 1;
        if(tagDatabaseST.size() > 0)
            id += tagDatabaseST.max();

        entity.setId(id);
        tagDatabaseST.put(id, entity.Clone());

        return entity;
    }

    /**
     * @param entity
     * @return a entidade atualizada ou null se nao existir
     */
    @Override
    public Tag Update(Tag entity) {

        if (entity == null || !tagDatabaseST.contains(entity.getId()))
            return null;

        tagDatabaseST.put(entity.getId(), entity.Clone());
        return entity;
    }

    /**
     * @param id
     * @return verdadeira se por apagada com sucesso ou falsa se não tiver na database
     */
    @Override
    public boolean Delete(Integer id) {

        if (!tagDatabaseST.contains(id))
            return false;

        tagDatabaseST.remove(id);
        return true;
    }
}
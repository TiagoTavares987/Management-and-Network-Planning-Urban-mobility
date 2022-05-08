package database;

import core.entities.Node;
import core.interfaces.DatabaseI;
import edu.princeton.cs.algs4.ST;

import java.util.ArrayList;

public class NodeDatabase implements DatabaseI<Node> {

    private final ST<Integer, Node> nodeDatabaseST = new ST<>();

    /**
     * @param id
     * @return
     */
    @Override
    public Node GetEntity(Integer id) {

        if(nodeDatabaseST.contains(id))
            return nodeDatabaseST.get(id).Clone();
        else
            return null;
    }

    /**
     * @return as entitades
     */
    @Override
    public ArrayList<Node> GetTable() {

        var entities = new ArrayList<Node>();
        for (var id : nodeDatabaseST)
            entities.add(nodeDatabaseST.get(id).Clone());

        return entities;
    }

    /**
     * @param entity
     * @return a entidade inserida
     */
    @Override
    public Node Insert(Node entity) {

        if (entity == null || entity.getId() != 0)
            return null;

        var id = 1;
        if(nodeDatabaseST.size() > 0)
            id += nodeDatabaseST.max();

        entity.setId(id);
        nodeDatabaseST.put(id, entity.Clone());

        return entity;
    }

    /**
     * @param entity
     * @return a entidade atualizada ou null se nao existir
     */
    @Override
    public Node Update(Node entity) {

        if (entity == null || !nodeDatabaseST.contains(entity.getId()))
            return null;

        nodeDatabaseST.put(entity.getId(), entity.Clone());
        return entity;
    }

    /**
     * @param id
     * @return verdadeira se por apagada com sucesso ou falsa se não tiver na database
     */
    @Override
    public boolean Delete(Integer id) {

        if (!nodeDatabaseST.contains(id))
            return false;

        nodeDatabaseST.remove(id);
        return true;
    }
}
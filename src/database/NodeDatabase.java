package database;

import core.baseEntities.Const;
import core.entities.Node;
import core.entities.Poi;
import core.entities.Tag;
import core.entities.Way;
import core.interfaces.DatabaseI;
import core.utils.Test;
import edu.princeton.cs.algs4.ST;

import java.io.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class NodeDatabase extends Database implements DatabaseI<Node> {

    private static final ST<Integer, Node> nodeDatabaseST = new ST<>();
    private final TagDatabase tagDatabase = new TagDatabase();
    private final PoiDatabase poiDatabase = new PoiDatabase();

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

        ArrayList<Node> entities = new ArrayList<Node>();
        for (Integer id : nodeDatabaseST)
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

        int id = 1;
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

    public void SaveToFile() throws IOException {

        try {
            FileWriter nfw = new FileWriter(new File(Const.outputPath, Const.nodesFile)); // wrapp do file
            PrintWriter npw = new PrintWriter(nfw);
            FileWriter tfw = new FileWriter(new File(Const.outputPath, Const.nodesTagsFile)); // wrapp do file
            PrintWriter tpw = new PrintWriter(tfw);
            FileWriter pfw = new FileWriter(new File(Const.outputPath, Const.nodesPoisFile)); // wrapp do file
            PrintWriter ppw = new PrintWriter(pfw);

            String line;
            for(Integer nodeId : nodeDatabaseST.keys()){
                Node node = nodeDatabaseST.get(nodeId);
                line = node.toString();
                if (!Test.isNullOrEmpty(line))
                    npw.println(line);
                line = node.tagsToString();
                if (!Test.isNullOrEmpty(line))
                    tpw.println(line);
                line = node.poisToString();
                if (!Test.isNullOrEmpty(line))
                    ppw.println(line);
            }

            npw.flush();
            npw.close();
            nfw.close();

            tpw.flush();
            tpw.close();
            tfw.close();

            ppw.flush();
            ppw.close();
            pfw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void ReadFromFile(String path) throws IOException, ParseException {

        if(Test.isNullOrEmpty(path))
            path = Const.inputPath;

        FileReader fr = new FileReader(new File(path, Const.nodesFile));
        BufferedReader br = new BufferedReader(fr);

        String line;

        while ((line=br.readLine()) != null) {
            Node node = new Node();

            int propCount = 0;
            StringTokenizer st=new StringTokenizer(line, ",");

            while (st.hasMoreTokens()) {
                String token = st.nextToken();
                if(propCount == 0)
                    node.setId(Integer.parseInt(token));
                else if(propCount == 1)
                    node.Name = token;
                else if(propCount == 2)
                    node.getLocalization().Longitude = Float.parseFloat(token);
                else if(propCount == 3)
                    node.getLocalization().Latitude = Float.parseFloat(token);

                propCount++;
            }

            nodeDatabaseST.put(node.getId(), node);
        }

        br.close();
        fr.close();

        fr = new FileReader(new File(Const.inputPath, Const.nodesTagsFile));
        br = new BufferedReader(fr);

        while ((line=br.readLine()) != null) {
            Node node = null;

            int propCount = 0;
            StringTokenizer st=new StringTokenizer(line, ",");

            while (st.hasMoreTokens()) {
                String token = st.nextToken();
                if(propCount == 0){
                    int nodeId = Integer.parseInt(token);
                    if(nodeDatabaseST.contains(nodeId))
                        node = GetEntity(nodeId);
                    else
                        node = null;
                }
                else if(node != null) {
                    int tagId = Integer.parseInt(token);
                    Tag tag = tagDatabase.GetEntity(tagId);
                    if(tag != null){
                        ST<Integer, Tag> nodeTags = node.getTags();
                        if(!nodeTags.contains(tagId))
                            nodeTags.put(tagId, tag);
                    }
                }

                propCount++;
            }

            Update(node);
        }

        br.close();
        fr.close();

        fr = new FileReader(new File(Const.inputPath, Const.nodesPoisFile));
        br = new BufferedReader(fr);

        while ((line=br.readLine()) != null) {
            Node node = null;

            int propCount = 0;
            StringTokenizer st=new StringTokenizer(line, ",");

            while (st.hasMoreTokens()) {
                String token = st.nextToken();
                if(propCount == 0){
                    int nodeId = Integer.parseInt(token);
                    if(nodeDatabaseST.contains(nodeId))
                        node = GetEntity(nodeId);
                    else
                        node = null;
                }
                else if(node != null) {
                    int poiId = Integer.parseInt(token);
                    Poi poi = poiDatabase.GetEntity(poiId);
                    if(poi != null){
                        ST<Integer, Poi> nodePois = node.getPois();
                        if(!nodePois.contains(poiId))
                            nodePois.put(poiId, poi);
                    }
                }

                propCount++;
            }

            Update(node);
        }

        br.close();
        fr.close();
    }

    @Override
    public void SaveToBinFile() {

        try {
            FileOutputStream nfile = new FileOutputStream(Const.nodesBinOutputFile);
            DataOutputStream ndos = new DataOutputStream(new BufferedOutputStream(nfile));
            FileOutputStream tfile = new FileOutputStream(Const.nodesTagsBinOutputFile);
            DataOutputStream tdos = new DataOutputStream(new BufferedOutputStream(tfile));
            FileOutputStream pfile = new FileOutputStream(Const.nodesPoisBinOutputFile);
            DataOutputStream pdos = new DataOutputStream(new BufferedOutputStream(pfile));


            for(Integer nodeId : nodeDatabaseST.keys()){
                Node node = nodeDatabaseST.get(nodeId);
                ndos.writeUTF(node.toString());
                tdos.writeUTF(node.tagsToString());
                pdos.writeUTF(node.poisToString());
            }

            ndos.flush();
            ndos.close();
            nfile.close();

            tdos.flush();
            tdos.close();
            tfile.close();

            pdos.flush();
            pdos.close();
            pfile.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void ReadFromBinFile(String path) throws IOException {

        if(Test.isNullOrEmpty(path))
            path = Const.nodesBinInputFile;

        FileInputStream file = new FileInputStream(path);
        DataInputStream dos = new DataInputStream(new BufferedInputStream(file));

        Node node = new Node();
        int n = dos.readInt();
        for (int i = 0; i < n; i++) {
            node.setId(i);
            node.getLocalization().Longitude = dos.readFloat();
            node.getLocalization().Latitude = dos.readFloat();
            nodeDatabaseST.put(node.getId(), node);
        }

        Update(node);

        dos.close();
        file.close();

        file = new FileInputStream(Const.nodesTagsBinInputFile);
        dos = new DataInputStream(new BufferedInputStream(file));

        node = null;
        n = dos.readInt();
        for (int i = 0; i < n; i++) {
            int nodeId = i;
            if (nodeDatabaseST.contains(nodeId))
                node = GetEntity(nodeId);
            else
                node = null;
            if (node != null) {
                int tagId = i;
                Tag tag = tagDatabase.GetEntity(tagId);
                if(tag != null) {
                    ST<Integer, Tag> nodeTags = node.getTags();
                    if(!nodeTags.contains(tagId))
                        nodeTags.put(tagId, tag);
                }
            }
        }
        Update(node);

        dos.close();
        file.close();

        file = new FileInputStream(Const.nodesPoisBinInputFile);
        dos = new DataInputStream(new BufferedInputStream(file));

        node = null;
        n = dos.readInt();
        for (int i = 0; i < n; i++) {
            int nodeId = i;
            if (nodeDatabaseST.contains(nodeId))
                node = GetEntity(nodeId);
            else
                node = null;
            if (node != null) {
                int poiId = i;
                Poi poi = poiDatabase.GetEntity(poiId);
                if(poi != null) {
                    ST<Integer, Poi> nodePois = node.getPois();
                    if(!nodePois.contains(poiId))
                        nodePois.put(poiId, poi);
                }
            }
        }
        Update(node);

        dos.close();
        file.close();
    }
}

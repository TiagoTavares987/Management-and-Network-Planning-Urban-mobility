package database;

import core.baseEntities.Const;
import core.entities.Node;
import core.entities.Poi;
import core.entities.Tag;
import core.entities.Way;
import core.interfaces.DatabaseI;
import edu.princeton.cs.algs4.ST;

import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class WayDatabase implements DatabaseI<Way> {

    private final ST<Integer, Way> wayDatabaseST = new ST<>();
    private final TagDatabase tagDatabase = new TagDatabase();
    private final PoiDatabase poiDatabase = new PoiDatabase();

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

        ArrayList<Way> entities = new ArrayList<Way>();
        for (Integer id : wayDatabaseST)
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

        int id = 1;
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

    public void SaveToFile() throws IOException {

        try {
            FileWriter nfw = new FileWriter(new File(Const.outputPath, Const.waysFile)); // wrapp do file
            PrintWriter npw = new PrintWriter(nfw);
            FileWriter tfw = new FileWriter(new File(Const.outputPath, Const.waysTagsFile)); // wrapp do file
            PrintWriter tpw = new PrintWriter(tfw);
            FileWriter pfw = new FileWriter(new File(Const.outputPath, Const.waysPoisFile)); // wrapp do file
            PrintWriter ppw = new PrintWriter(tfw);

            for(Integer wayId : wayDatabaseST.keys()){
                Way way = wayDatabaseST.get(wayId);
                npw.println(way.toString());
                tpw.println(way.tagsToString());
                ppw.println(way.poisToString());
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
    public void ReadFromFile() throws IOException {

        FileReader fr = new FileReader(new File(Const.inputPath, Const.waysFile));
        BufferedReader br = new BufferedReader(fr);

        String line;

        while ((line=br.readLine()) != null) {
            Way way = new Way();

            int propCount = 0;
            StringTokenizer st=new StringTokenizer(line, ",");

            while (st.hasMoreTokens()) {
                String token = st.nextToken();
                if(propCount == 0)
                    way.setId(Integer.parseInt(token));
                else if(propCount == 1)
                    way.Name = token;
                else if(propCount == 2)
                    way.Start = Integer.parseInt(token);
                else if(propCount == 3)
                    way.End = Integer.parseInt(token);

                propCount++;
            }

            wayDatabaseST.put(way.getId(), way);
        }

        br.close();
        fr.close();

        fr = new FileReader(new File(Const.inputPath, Const.waysTagsFile));
        br = new BufferedReader(fr);

        while ((line=br.readLine()) != null) {
            Way way = null;

            int propCount = 0;
            StringTokenizer st=new StringTokenizer(line, ",");

            while (st.hasMoreTokens()) {
                String token = st.nextToken();
                if(propCount == 0){
                    int wayId = Integer.parseInt(token);
                    if(wayDatabaseST.contains(wayId))
                        way = GetEntity(wayId);
                    else
                        way = null;
                }
                else if(way != null) {
                    int tagId = Integer.parseInt(token);
                    Tag tag = tagDatabase.GetEntity(tagId);
                    if(tag != null){
                        ST<Integer, Tag> wayTags = way.getTags();
                        if(!wayTags.contains(tagId))
                            wayTags.put(tagId, tag);
                    }
                }

                propCount++;
            }

            Update(way);
        }

        br.close();
        fr.close();

        fr = new FileReader(new File(Const.inputPath, Const.waysPoisFile));
        br = new BufferedReader(fr);

        while ((line=br.readLine()) != null) {
            Way way = null;

            int propCount = 0;
            StringTokenizer st=new StringTokenizer(line, ",");

            while (st.hasMoreTokens()) {
                String token = st.nextToken();
                if(propCount == 0){
                    int wayId = Integer.parseInt(token);
                    if(wayDatabaseST.contains(wayId))
                        way = GetEntity(wayId);
                    else
                        way = null;
                }
                else if(way != null) {
                    int poiId = Integer.parseInt(token);
                    Poi poi = poiDatabase.GetEntity(poiId);
                    if(poi != null){
                        ST<Integer, Poi> wayPois = way.getPois();
                        if(!wayPois.contains(poiId))
                            wayPois.put(poiId, poi);
                    }
                }

                propCount++;
            }

            Update(way);
        }

        br.close();
        fr.close();
    }

    @Override
    public void SaveToBinFile() {

        try {
            FileOutputStream wfile = new FileOutputStream(Const.waysBinOutputFile);
            DataOutputStream wdos = new DataOutputStream(new BufferedOutputStream(wfile));
            FileOutputStream tfile = new FileOutputStream(Const.waysTagsBinOutputFile);
            DataOutputStream tdos = new DataOutputStream(new BufferedOutputStream(tfile));
            FileOutputStream pfile = new FileOutputStream(Const.waysPoisBinOutputFile);
            DataOutputStream pdos = new DataOutputStream(new BufferedOutputStream(pfile));


            for(Integer wayId : wayDatabaseST.keys()){
                Way way = wayDatabaseST.get(wayId);
                wdos.writeUTF(way.toString());
                tdos.writeUTF(way.tagsToString());
                pdos.writeUTF(way.poisToString());
            }

            wdos.flush();
            wdos.close();
            wfile.close();

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
    public void ReadFromBinFile() throws IOException {

        FileInputStream file = new FileInputStream(Const.waysBinInputFile);
        DataInputStream dos = new DataInputStream(new BufferedInputStream(file));

        Way way = new Way();
        int n = dos.readInt();
        for (int i = 0; i < n; i++) {
            way.setId(i);
            way.Name = dos.readUTF();
            way.Start = dos.readInt();
            way.End = dos.readInt();
            wayDatabaseST.put(way.getId(), way);
        }

        Update(way);

        dos.close();
        file.close();

        file = new FileInputStream(Const.waysTagsBinInputFile);
        dos = new DataInputStream(new BufferedInputStream(file));

        way = null;
        n = dos.readInt();
        for (int i = 0; i < n; i++) {
            int wayId = i;
            if (wayDatabaseST.contains(wayId))
                way = GetEntity(wayId);
            else
                way = null;
            if (way != null) {
                int tagId = i;
                Tag tag = tagDatabase.GetEntity(tagId);
                if(tag != null) {
                    ST<Integer, Tag> wayTags = way.getTags();
                    if(!wayTags.contains(tagId))
                        wayTags.put(tagId, tag);
                }
            }
        }
        Update(way);

        dos.close();
        file.close();

        file = new FileInputStream(Const.waysPoisBinInputFile);
        dos = new DataInputStream(new BufferedInputStream(file));

        way = null;
        n = dos.readInt();
        for (int i = 0; i < n; i++) {
            int wayId = i;
            if (wayDatabaseST.contains(wayId))
                way = GetEntity(wayId);
            else
                way = null;
            if (way != null) {
                int poiId = i;
                Poi poi = poiDatabase.GetEntity(poiId);
                if(poi != null) {
                    ST<Integer, Poi> wayPois = way.getPois();
                    if(!wayPois.contains(poiId))
                        wayPois.put(poiId, poi);
                }
            }
        }
        Update(way);

        dos.close();
        file.close();
    }
}


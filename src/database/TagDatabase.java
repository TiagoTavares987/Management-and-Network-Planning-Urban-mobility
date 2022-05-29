package database;

import core.baseEntities.Const;
import core.baseEntities.Entity;
import core.entities.Node;
import core.entities.Poi;
import core.entities.Tag;
import core.interfaces.DatabaseI;
import edu.princeton.cs.algs4.ST;

import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;


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

        ArrayList<Tag> entities = new ArrayList<Tag>();
        for (Integer id : tagDatabaseST)
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

        int id = 1;
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

    public void SaveToFile() throws IOException {

        try {
            FileWriter nfw = new FileWriter(new File(Const.outputPath, Const.tagsFile)); // wrapp do file
            PrintWriter npw = new PrintWriter(nfw);
            FileWriter tfw = new FileWriter(new File(Const.outputPath, Const.extraInfoTagsFile)); // wrapp do file
            PrintWriter tpw = new PrintWriter(tfw);

            for(Integer tagId : tagDatabaseST.keys()){
                Tag tag = tagDatabaseST.get(tagId);
                npw.println(tag.toString());
                tpw.println(tag.extra_info_ToString());
            }

            npw.flush();
            npw.close();
            nfw.close();

            tpw.flush();
            tpw.close();
            tfw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void ReadFromFile() throws IOException {

        FileReader fr = new FileReader(new File(Const.inputPath, Const.tagsFile));
        BufferedReader br = new BufferedReader(fr);

        String line;

        while ((line=br.readLine()) != null) {
            Tag tag = new Tag();

            int propCount = 0;
            StringTokenizer st=new StringTokenizer(line, ",");

            while (st.hasMoreTokens()) {
                String token = st.nextToken();
                if(propCount == 0)
                    tag.setId(Integer.parseInt(token));
                else if(propCount == 1)
                    tag.Description = token;

                propCount++;
            }

            tagDatabaseST.put(tag.getId(), tag);
        }

        br.close();
        fr.close();

        fr = new FileReader(new File(Const.inputPath, Const.extraInfoTagsFile));
        br = new BufferedReader(fr);

        while ((line=br.readLine()) != null) {
            Tag tag = null;

            int propCount = 0;
            StringTokenizer st=new StringTokenizer(line, ",");

            while (st.hasMoreTokens()) {
                String token = st.nextToken();
                if(propCount == 0){
                    int tagId = Integer.parseInt(token);
                    if(tagDatabaseST.contains(tagId))
                        tag = GetEntity(tagId);
                    else
                        tag = null;
                } else if (tag != null) {
                    ArrayList<Entity> infoTags = tag.get_extra_info();
                    if(!infoTags.contains(tag))
                        infoTags.add(tag);

                }

                propCount++;
            }

            Update(tag);
        }

        br.close();
        fr.close();
    }

    @Override
    public void SaveToBinFile() {

        try {
            FileOutputStream tfile = new FileOutputStream(Const.tagsBinOutputFile);
            DataOutputStream tdos = new DataOutputStream(new BufferedOutputStream(tfile));
            FileOutputStream eifile = new FileOutputStream(Const.extraInfoTagsBinOutputFile);
            DataOutputStream eidos = new DataOutputStream(new BufferedOutputStream(eifile));

            for(Integer tagId : tagDatabaseST.keys()){
                Tag tag = tagDatabaseST.get(tagId);
                tdos.writeUTF(tag.toString());
                tdos.writeUTF(tag.extra_info_ToString());
            }

            tdos.flush();
            tdos.close();
            tfile.close();

            eidos.flush();
            eidos.close();
            eifile.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void ReadFromBinFile() throws IOException {

        FileInputStream file = new FileInputStream(Const.tagsBinInputFile);
        DataInputStream dos = new DataInputStream(new BufferedInputStream(file));

        Tag tag = new Tag();
        int n = dos.readInt();
        for (int i = 0; i < n; i++) {
            tag.setId(i);
            tag.Description = dos.readUTF();
            tagDatabaseST.put(tag.getId(), tag);
        }

        Update(tag);

        dos.close();
        file.close();

        file = new FileInputStream(Const.extraInfoTagsBinInputFile);
        dos = new DataInputStream(new BufferedInputStream(file));

        n = dos.readInt();
        for (int i = 0; i < n; i++) {
            int tagId = i;
            if(tagDatabaseST.contains(tagId))
                tag = GetEntity(tagId);
            else if (tag != null) {
                ArrayList<Entity> infoTags = tag.get_extra_info();
                if(!infoTags.contains(tag))
                    infoTags.add(tag);
            }
        }
        Update(tag);

        dos.close();
        file.close();
    }
}

package database;

import core.baseEntities.Const;
import core.entities.Poi;
import core.entities.UserPoi;
import core.interfaces.DatabaseI;
import core.utils.Test;
import edu.princeton.cs.algs4.ST;
import java.io.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class PoiDatabase extends Database implements DatabaseI<Poi> {

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

        ArrayList<Poi> entities = new ArrayList<Poi>();
        for (Integer id : poiDatabaseST)
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

        int id = 1;
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

    public void SaveToFile() throws IOException {

        try {
            FileWriter nfw = new FileWriter(new File(Const.outputPath, Const.poisFile)); // wrapp do file
            PrintWriter npw = new PrintWriter(nfw);

            for(Integer poiId : poiDatabaseST.keys()){
                Poi poi = poiDatabaseST.get(poiId);
                npw.println(poi.toString());
            }

            npw.flush();
            npw.close();
            nfw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void ReadFromFile(String path) throws IOException, ParseException {

        if(Test.isNullOrEmpty(path))
            path = Const.inputPath;

        FileReader fr = new FileReader(new File(path, Const.poisFile));
        BufferedReader br = new BufferedReader(fr);

        String line;

        while ((line = br.readLine()) != null) {
            Poi poi = new Poi();

            int propCount = 0;
            StringTokenizer st = new StringTokenizer(line, ",");

            while (st.hasMoreTokens()) {
                String token = st.nextToken();
                if (propCount == 0)
                    poi.setId(Integer.parseInt(token));
                else if (propCount == 1)
                    poi.Name = token;
                else if (propCount == 2)
                    poi.Description = token;
                else if (propCount == 3)
                    poi.getLocalization().Longitude = Float.parseFloat(token);
                else if (propCount == 4)
                    poi.getLocalization().Latitude = Float.parseFloat(token);

                propCount++;
            }

            poiDatabaseST.put(poi.getId(), poi);
        }

        br.close();
        fr.close();

    }

    @Override
    public void SaveToBinFile() {

        try {
            FileOutputStream file = new FileOutputStream(Const.poisBinOutputFile);
            DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(file));

            for(Integer poiId : poiDatabaseST.keys()){
                Poi poi = poiDatabaseST.get(poiId);
                dos.writeUTF(poi.toString());
            }

            dos.flush();
            dos.close();
            file.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void ReadFromBinFile(String path) throws IOException {

        if(Test.isNullOrEmpty(path))
            path = Const.poisBinInputFile;

        FileInputStream file = new FileInputStream(path);
        DataInputStream dos = new DataInputStream(new BufferedInputStream(file));

        int n = dos.readInt();
        Poi poi = new Poi();
        for (int i = 0; i < n; i++) {
            poi.setId(i);
            poi.Name = dos.readUTF();
            poi.Description = dos.readUTF();
            poi.getLocalization().Longitude = dos.readFloat();
            poi.getLocalization().Latitude = dos.readFloat();
            poiDatabaseST.put(poi.getId(), poi);
        }

        Update(poi);

        dos.close();
        file.close();
    }
}


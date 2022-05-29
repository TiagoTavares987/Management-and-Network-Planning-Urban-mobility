package database;

import core.baseEntities.Const;
import core.entities.Node;
import core.entities.Poi;
import core.entities.User;
import core.entities.UserPoi;
import core.enums.UserType;
import core.interfaces.DatabaseI;
import edu.princeton.cs.algs4.ST;

import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;


public class UserDatabase implements DatabaseI<User> {

    private final ST<Integer, User> userDatabaseST = new ST<>();
    private final PoiDatabase poiDatabase = new PoiDatabase();

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

        ArrayList<User> entities = new ArrayList<User>();
        for (Integer id : userDatabaseST)
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

        int id = 1;
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

        User entity = GetEntity(user_id);
        if (entity == null)
            return false;

        int id = 1;
        ST<Integer, UserPoi> pois = entity.getPois();
        if(pois.size() > 0)
            id += pois.max();

        UserPoi user_poi = new UserPoi();
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

        User entity = GetEntity(user_id);
        if (entity == null)
            return false;

        ST<Integer, UserPoi> pois = entity.getPois();
        if(pois.contains(poi_id)){
            pois.remove(1);
            return Update(entity) != null;
        }

        return false;
    }

    public void SaveToFile() throws IOException {
        try {
            FileWriter nfw = new FileWriter(new File(Const.outputPath, Const.usersFile)); // wrapp do file
            PrintWriter npw = new PrintWriter(nfw);
            FileWriter tfw = new FileWriter(new File(Const.outputPath, Const.usersPoiFile)); // wrapp do file
            PrintWriter tpw = new PrintWriter(tfw);

            for(Integer userId : userDatabaseST.keys()){
                User user = userDatabaseST.get(userId);
                npw.println(user.toString());
                tpw.println(user.poisToString());
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

        FileReader fr = new FileReader(new File(Const.inputPath, Const.usersFile));
        BufferedReader br = new BufferedReader(fr);

        String line;

        while ((line=br.readLine()) != null) {
            User user = new User();

            int propCount = 0;
            StringTokenizer st=new StringTokenizer(line, ",");

            while (st.hasMoreTokens()) {
                String token = st.nextToken();
                if(propCount == 0)
                    user.setId(Integer.parseInt(token));
                else if(propCount == 1)
                    user.UserType = UserType.valueOf(token);
                else if(propCount == 2)
                    user.Username = token;
                else if(propCount == 3)
                    user.Password = token;

                propCount++;
            }

            userDatabaseST.put(user.getId(), user);
        }

        br.close();
        fr.close();

        fr = new FileReader(new File(Const.inputPath, Const.usersPoiFile));
        br = new BufferedReader(fr);

        while ((line=br.readLine()) != null) {
            User user = null;

            int propCount = 0;
            StringTokenizer st=new StringTokenizer(line, ",");

            while (st.hasMoreTokens()) {
                String token = st.nextToken();
                if(propCount == 0){
                    int userId = Integer.parseInt(token);
                    if(userDatabaseST.contains(userId))
                        user = GetEntity(userId);
                    else
                        user = null;
                }
                else if(user != null) {
                    UserPoi userPoi = new UserPoi();
                    userPoi.poi = Integer.parseInt(token);
                    Poi poi = poiDatabase.GetEntity(userPoi.poi);
                    if(poi != null){
                        ST<Integer, UserPoi> userPois = user.getPois();
                        if(!userPois.contains(userPoi.poi))
                            userPois.put(userPoi.poi, userPoi);
                    }
                }

                propCount++;
            }

            Update(user);
        }

        br.close();
        fr.close();
    }

    @Override
    public void SaveToBinFile() {

        try {
            FileOutputStream ufile = new FileOutputStream(Const.usersBinOutputFile);
            DataOutputStream udos = new DataOutputStream(new BufferedOutputStream(ufile));
            FileOutputStream pfile = new FileOutputStream(Const.usersPoiBinOutputFile);
            DataOutputStream pdos = new DataOutputStream(new BufferedOutputStream(pfile));

            for(Integer userId : userDatabaseST.keys()){
                User user = userDatabaseST.get(userId);
                udos.writeUTF(user.toString());
                pdos.writeUTF(user.poisToString());
            }

            udos.flush();
            udos.close();
            ufile.close();

            pdos.flush();
            pdos.close();
            pfile.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void ReadFromBinFile() throws IOException {

        FileInputStream file = new FileInputStream(Const.usersBinInputFile);
        DataInputStream dos = new DataInputStream(new BufferedInputStream(file));

        User user = new User();
        int n = dos.readInt();
        for (int i = 0; i < n; i++) {
            user.setId(i);
            user.UserType = UserType.valueOf(dos.readUTF());
            user.Username = dos.readUTF();
            user.Password = dos.readUTF();
            userDatabaseST.put(user.getId(), user);
        }

        Update(user);

        dos.close();
        file.close();

        file = new FileInputStream(Const.usersPoiBinInputFile);
        dos = new DataInputStream(new BufferedInputStream(file));

        user = null;
        n = dos.readInt();
        for (int i = 0; i < n; i++) {
            int userId = i;
            if (userDatabaseST.contains(userId))
                user = GetEntity(userId);
            else
                user = null;
            if (user != null) {
                UserPoi userPoi = new UserPoi();
                userPoi.poi = i;
                Poi poi = poiDatabase.GetEntity(userPoi.poi);
                if(poi != null) {
                    ST<Integer, UserPoi> userPois = user.getPois();
                    if(!userPois.contains(userPoi.poi))
                        userPois.put(userPoi.poi, userPoi);
                }
            }
        }
        Update(user);

        dos.close();
        file.close();
    }
}
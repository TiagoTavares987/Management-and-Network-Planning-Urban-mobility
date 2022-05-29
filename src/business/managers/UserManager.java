package business.managers;

import core.entities.Localization;
import core.entities.Poi;
import core.entities.User;
import core.enums.UserType;
import core.utils.Test;
import database.UserDatabase;
import edu.princeton.cs.algs4.BST;

import java.io.IOException;
import java.util.ArrayList;


public class UserManager {

    private static int currentUser;
    private static UserDatabase database;
    private static BST<Integer, User> listById;
    private static BST<String, User> listByUsername;

    /**
     * Construtor da class UserManager
     */
    public UserManager() {

        if (database == null) {
            database = new UserDatabase();
            listById = new BST<>();
            listByUsername = new BST<>();

            for (User user : database.GetTable()) {
                listById.put(user.getId(), user);
                listByUsername.put(user.Username, user);
            }
        }
    }

    public static User getCurrentUser() {
        return database.GetEntity(currentUser);
    }

    /**
     * @return lista de users
     */
    public ArrayList<User> GetAll() {
        ArrayList<User> list = new ArrayList<>();

        for(Integer userId : listById.keys()){
            list.add(listById.get(userId));
        }

        return list;
    }

    /**
     * @param id id do user
     * @return user
     */
    public User GetUser(Integer id) {
        return listById.get(id);
    }

    public int newUser(String Name, UserType userType) {

        User user = new User();
        user.Username = Name;
        user.UserType = userType;

        User newUser = SaveUser(user);

        return newUser.getId();
    }

    /**
     * @param user passamos o user que queremos guardar como parametro
     * @return um newuser caso nao exista um utilizador ou null se for um user invalido
     */
    public User SaveUser(User user) {
        if (validateUser(user)) {
            if (user.getId() == 0) {
                User newUser = database.Insert(user);
                if (newUser != null) {
                    listById.put(newUser.getId(), newUser);
                    listByUsername.put(newUser.Username, newUser);
                    return newUser;
                }
            } else {
                if (listById.contains(user.getId())) {
                    User newUser = database.Update(user);
                    if (newUser != null) {
                        String oldUsername = listById.get(user.getId()).Username;
                        listById.put(newUser.getId(), newUser);
                        listByUsername.delete(oldUsername);
                        listByUsername.put(newUser.Username, newUser);
                    }
                }
            }
        }

        return null;
    }

    /**
     * @param id id do user
     * @return false se nao houver o user
     */
    public Boolean DeleteUser(Integer id) {
        if (listById.contains(id) && database.Delete(id)) {
            listByUsername.delete(listById.get(id).Username);
            listById.delete(id);
        }

        return false;
    }

    public void editUser(Integer user_id, String name, String password, UserType userType) throws Exception {

        User user = listById.get(user_id);
        if (user == null)
            throw new Exception("Id do User invalido");

        user = new User();
        user.setId(user_id);
        user.Username = name;
        user.Password = password;
        user.UserType = userType;

        SaveUser(user);
    }

    /**
     * @param username username
     * @param password password
     * @return false se for invalido ou o user que tenha a password igual a que Ã© passada como parametro
     */
    public boolean ValidateLogin(String username, String password) {

        if (Test.isNullOrEmpty(username) || Test.isNullOrEmpty(password))
            return false;

        User user = listByUsername.get(username);
        if (user == null)
            return false;

        if (user.Password.equals(password)) {
            currentUser = user.getId();
            return true;
        }

        return false;
    }

    /**
     * @param user passamos um user
     * @return false se for invalido ou true se for valido
     */
    private boolean validateUser(User user) {

        if (Test.isNullOrEmpty(user.Username) || Test.isNullOrEmpty(user.Password))
            return false;

        return true;
    }

    /**
     *
     * @param user_id Integer recebe id do user
     * @param poi_id Integer recebe o id do poi
     * @return
     */
    public boolean addPoi(Integer user_id, Integer poi_id) {
        return database.addPoi(user_id, poi_id);
    }

    /**
     *
     * @param user_id Integer recebe id do user
     * @param poi_id Integer recebe o id do poi
     * @return
     */
    public boolean deletePoi(Integer user_id, Integer poi_id) {
        return database.deletePoi(user_id, poi_id);
    }

    /**
     *
     * @throws IOException
     */
    public void snapShot() throws IOException {
        database.SaveToFile();
    }
}
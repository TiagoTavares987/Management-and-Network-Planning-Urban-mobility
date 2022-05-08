package business.managers;

import core.entities.Poi;
import core.entities.User;
import core.interfaces.DatabaseI;
import core.utils.Test;
import database.UserDatabase;
import edu.princeton.cs.algs4.BST;

import java.util.ArrayList;


public class UserManager {

    private static DatabaseI<User> database;
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

            for (var user : database.GetTable()) {
                listById.put(user.getId(), user);
                listByUsername.put(user.Username, user);
            }
        }
    }

    public ArrayList<User> GetAll() {
        ArrayList<User> list = new ArrayList<>();

        for(var userId : listById.keys()){
            list.add(listById.get(userId));
        }

        return list;
    }

    /**
     * @param id
     * @return
     */
    public User GetUser(Integer id) {
        return listById.get(id);
    }

    /**
     * @param user
     * @return
     */
    public User SaveUser(User user) {
        if (validateUser(user)) {
            if (user.getId() == 0) {
                var newUser = database.Insert(user);
                if (newUser != null) {
                    listById.put(newUser.getId(), newUser);
                    listByUsername.put(newUser.Username, newUser);
                    return newUser;
                }
            } else {
                if (listById.contains(user.getId())) {
                    var newUser = database.Update(user);
                    if (newUser != null) {
                        var oldUsername = listById.get(user.getId()).Username;
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
     * @param id
     * @return
     */
    public Boolean DeleteUser(Integer id) {
        if (listById.contains(id) && database.Delete(id)) {
            listByUsername.delete(listById.get(id).Username);
            listById.delete(id);
        }

        return false;
    }

    /**
     * @param username
     * @param password
     * @return
     */
    public boolean ValidateLogin(String username, String password) {

        if (Test.isNullOrEmpty(username) || Test.isNullOrEmpty(password))
            return false;

        var user = listByUsername.get(username);
        if (user == null)
            return false;

        return user.Password.equals(password);
    }

    private boolean validateUser(User user) {

        if (Test.isNullOrEmpty(user.Username) || Test.isNullOrEmpty(user.Password))
            return false;

        return true;
    }
}
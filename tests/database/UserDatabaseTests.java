package database;

import core.entities.User;
import core.entities.UserPoi;
import core.enums.UserType;
import edu.princeton.cs.algs4.ST;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class UserDatabaseTests {

    User firstUser;
    UserDatabase userDatabase;

    @BeforeEach
    void setUp() {
        firstUser = new User();
        firstUser.Username = "Maria";
        firstUser.Password = "airaM";
        firstUser.UserType = UserType.Admin;
        userDatabase = new UserDatabase();
        userDatabase.Insert(firstUser);
    }

    @Test
    @DisplayName("Obter um utilizador")
    void GetUserTest() {
        // act
        User user = userDatabase.GetEntity(firstUser.getId());

        // assert
        assertNotNull(user);
        assertEquals(firstUser.getId(), user.getId());
        assertEquals(firstUser.Username, user.Username);
    }

    @Test
    @DisplayName("Obter todos os utilizadores")
    void GetAllUsersTest() {
        // act
       ArrayList<User> users = userDatabase.GetTable();

        // assert
        assertNotNull(users);
        assertEquals(1, users.size());
    }

    @Test
    @DisplayName("Inserir um utilizador")
    void InsertUserTest() {
        // arrange
        User newUser = new User();
        newUser.Username = "Joana";
        newUser.Password = "anaoJ";

        // act
        User insertedUser = userDatabase.Insert(newUser);

        // assert
        assertNotNull(insertedUser);

        User user = userDatabase.GetEntity(insertedUser.getId());
        assertNotNull(user);
        assertEquals(newUser.Username, user.Username);
    }

    @Test
    @DisplayName("Atualizar um utilizador")
    void UpdateUserTest() {
        // arrange
        firstUser.UserType = UserType.Basic;

        // act
        User updatedUser = userDatabase.Update(firstUser);

        // assert
        assertNotNull(updatedUser);
        assertEquals(firstUser.UserType, updatedUser.UserType);

        User user = userDatabase.GetEntity(updatedUser.getId());
        assertNotNull(user);
        assertEquals(firstUser.UserType, user.UserType);
    }

    @Test
    @DisplayName("Apagar um utilizador")
    void DeleteUserTest() {
        // act
        boolean deleted = userDatabase.Delete(firstUser.getId());

        // assert
        assertTrue(deleted);

        User user = userDatabase.GetEntity(firstUser.getId());
        assertNull(user);
    }

    @Test
    @DisplayName("Inserir um poi no utilizador")
    void InsertUserPoi() {
        // arrange
        User user = userDatabase.GetEntity(1);

        // act
        boolean inserted = userDatabase.addPoi(user.getId(), 1);

        // assert
        assertTrue(inserted);

        user = userDatabase.GetEntity(1);
        ST<Integer, UserPoi> pois = user.getPois();
        assertTrue(pois.contains(1));
    }

    @Test
    @DisplayName("Apagar um poi no utilizador")
    void DeleteUserPoi() {
        // arrange
        User user = userDatabase.GetEntity(1);
        userDatabase.addPoi(user.getId(), 1);
        user = userDatabase.GetEntity(1);

        // act
        boolean deleted = userDatabase.deletePoi(user.getId(), 1);

        // assert
        assertTrue(deleted);

        user = userDatabase.GetEntity(1);
        ST<Integer, UserPoi> pois = user.getPois();
        assertFalse(pois.contains(1));
    }
}

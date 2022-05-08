package database;

import core.entities.User;
import core.entities.UserPoi;
import core.enums.UserType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
        var user = userDatabase.GetEntity(firstUser.getId());

        // assert
        assertNotNull(user);
        assertEquals(firstUser.getId(), user.getId());
        assertEquals(firstUser.Username, user.Username);
    }

    @Test
    @DisplayName("Obter todos os utilizadores")
    void GetAllUsersTest() {
        // act
        var users = userDatabase.GetTable();

        // assert
        assertNotNull(users);
        assertEquals(1, users.size());
    }

    @Test
    @DisplayName("Inserir um utilizador")
    void InsertUserTest() {
        // arrange
        var newUser = new User();
        newUser.Username = "Joana";
        newUser.Password = "anaoJ";

        // act
        var insertedUser = userDatabase.Insert(newUser);

        // assert
        assertNotNull(insertedUser);

        var user = userDatabase.GetEntity(insertedUser.getId());
        assertNotNull(user);
        assertEquals(newUser.Username, user.Username);
    }

    @Test
    @DisplayName("Atualizar um utilizador")
    void UpdateUserTest() {
        // arrange
        firstUser.UserType = UserType.Basic;

        // act
        var updatedUser = userDatabase.Update(firstUser);

        // assert
        assertNotNull(updatedUser);
        assertEquals(firstUser.UserType, updatedUser.UserType);

        var user = userDatabase.GetEntity(updatedUser.getId());
        assertNotNull(user);
        assertEquals(firstUser.UserType, user.UserType);
    }

    @Test
    @DisplayName("Apagar um utilizador")
    void DeleteUserTest() {
        // act
        var deleted = userDatabase.Delete(firstUser.getId());

        // assert
        assertTrue(deleted);

        var user = userDatabase.GetEntity(firstUser.getId());
        assertNull(user);
    }

    @Test
    @DisplayName("Inserir um poi no utilizador")
    void InsertUserPoi() {
        // arrange
        var user = userDatabase.GetEntity(1);

        // act
        var inserted = userDatabase.addPoi(user.getId(), 1);

        // assert
        assertTrue(inserted);

        user = userDatabase.GetEntity(1);
        var pois = user.getPois();
        assertTrue(pois.contains(1));
    }

    @Test
    @DisplayName("Apagar um poi no utilizador")
    void DeleteUserPoi() {
        // arrange
        var user = userDatabase.GetEntity(1);
        userDatabase.addPoi(user.getId(), 1);
        user = userDatabase.GetEntity(1);

        // act
        var deleted = userDatabase.deletePoi(user.getId(), 1);

        // assert
        assertTrue(deleted);

        user = userDatabase.GetEntity(1);
        var pois = user.getPois();
        assertFalse(pois.contains(1));
    }
}

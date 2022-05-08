package database;

import core.entities.Poi;
import core.entities.Way;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;

public class WayDatabaseTests {
    Way firstWay;
    WayDatabase wayDatabase;

    @BeforeEach
    void setUp() {
        firstWay = new Way();
        firstWay.Name = "Rua";
        firstWay.End = 4;
        firstWay.Start = 2;
        wayDatabase = new WayDatabase();
        wayDatabase.Insert(firstWay);
    }

    @Test
    @DisplayName("Obter um way")
    void GetWayTest() {
        // act
        var way = wayDatabase.GetEntity(firstWay.getId());

        // assert
        assertNotNull(way);
        assertEquals(firstWay.getId(), way.getId());
        assertEquals(firstWay.Name, way.Name);
        assertEquals(firstWay.End, way.End);
        assertEquals(firstWay.Start, way.Start);
    }

    @Test
    @DisplayName("Obter todos os ways")
    void GetAllWaysTest() {
        // act
        var ways = wayDatabase.GetTable();

        // assert
        assertNotNull(ways);
        assertEquals(1, ways.size());
    }

    @Test
    @DisplayName("Inserir um way")
    void InsertWayTest() {
        // arrange
        var newWay = new Way();
        newWay.Name = "rua alfredo";
        newWay.End = 3;
        newWay.Start = 5;

        // act
        var insertedWay = wayDatabase.Insert(newWay);

        // assert
        assertNotNull(insertedWay);

        var way = wayDatabase.GetEntity(insertedWay.getId());
        assertNotNull(way);

        assertEquals(newWay.Name, way.Name);
        assertEquals(newWay.End, way.End);
        assertEquals(newWay.Start, way.Start);

    }

    @Test
    @DisplayName("Atualizar um way")
    void UpdateWayTest() {
        // arrange
        firstWay.Name = "rua s bento";
        firstWay.Start = 4;
        firstWay.End = 5;

        // act
        var updatedWay = wayDatabase.Update(firstWay);

        // assert
        assertNotNull(updatedWay);
        assertEquals(firstWay.Name, updatedWay.Name);
        assertEquals(firstWay.End, updatedWay.End);
        assertEquals(firstWay.Start, updatedWay.Start);

        var way = wayDatabase.GetEntity(updatedWay.getId());
        assertNotNull(way);
        assertEquals(firstWay.Name, way.Name);
        assertEquals(firstWay.End, way.End);
        assertEquals(firstWay.Start, way.Start);

    }

    @Test
    @DisplayName("Apagar um way")
    void DeleteWayTest() {
        // act
        var deleted = wayDatabase.Delete(firstWay.getId());

        // assert
        assertTrue(deleted);

        var way = wayDatabase.GetEntity(firstWay.getId());
        assertNull(way);
    }
}

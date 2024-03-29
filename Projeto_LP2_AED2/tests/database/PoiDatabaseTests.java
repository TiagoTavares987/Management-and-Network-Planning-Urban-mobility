package database;

import core.entities.Localization;
import core.entities.Poi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PoiDatabaseTests {
    Poi firstPoi;

    Localization firstPoiLocalization;
    PoiDatabase poiDatabase;

    @BeforeEach
    void setUp() {
        firstPoi = new Poi();
        firstPoi.Name = "cafe";
        firstPoi.Description = "rua muito movimentada";
        firstPoiLocalization = firstPoi.getLocalization();
        firstPoiLocalization.Latitude = 1.09234f;
        firstPoiLocalization.Longitude = 2.394823f;
        poiDatabase = new PoiDatabase();
        poiDatabase.Insert(firstPoi);
    }

    @Test
    @DisplayName("Obter um poi")
    void GetPoiTest() {
        // act
        var poi = poiDatabase.GetEntity(firstPoi.getId());

        // assert
        assertNotNull(poi);
        assertEquals(firstPoi.getId(), poi.getId());
        assertEquals(firstPoi.Name, poi.Name);
        assertEquals(firstPoi.Description, poi.Description);

        var poiLocalization = poi.getLocalization();
        assertEquals(firstPoiLocalization.Latitude, poiLocalization.Latitude);
        assertEquals(firstPoiLocalization.Longitude, poiLocalization.Longitude);
    }

    @Test
    @DisplayName("Obter todos os pois")
    void GetAllPoisTest() {
        // act
        var poi = poiDatabase.GetTable();

        // assert
        assertNotNull(poi);
        assertEquals(1, poi.size());
    }

    @Test
    @DisplayName("Inserir um poi")
    void InsertPoiTest() {
        // arrange
        var newPoi = new Poi();
        newPoi.Name = "confeitaria";
        newPoi.Description = "rua pouco movimentada";
        var newPoiLocalization = newPoi.getLocalization();
        newPoiLocalization.Latitude = 1.092433411f;
        newPoiLocalization.Longitude = 2.39482332f;

        // act
        var insertedPoi = poiDatabase.Insert(newPoi);

        // assert
        assertNotNull(insertedPoi);

        var poi = poiDatabase.GetEntity(insertedPoi.getId());
        assertNotNull(poi);
        assertEquals(newPoi.Name, poi.Name);
        assertEquals(newPoi.Description, poi.Description);

        var insertedPoiLocalization = poi.getLocalization();
        assertEquals(newPoiLocalization.Latitude, insertedPoiLocalization.Latitude);
        assertEquals(newPoiLocalization.Longitude, insertedPoiLocalization.Longitude);

    }

    @Test
    @DisplayName("Atualizar um poi")
    void UpdatePoiTest() {
        // arrange
        firstPoi.Name = "aswq";
        firstPoi.Description = "asdasdwe";
        firstPoiLocalization.Latitude = 34.543f;
        firstPoiLocalization.Longitude = 13.834f;

        // act
        var updatedPoi = poiDatabase.Update(firstPoi);

        // assert
        assertNotNull(updatedPoi);
        assertEquals(firstPoi.Name, updatedPoi.Name);
        assertEquals(firstPoi.Description, updatedPoi.Description);

        var updatedPoiLocalization = updatedPoi.getLocalization();
        assertEquals(firstPoiLocalization.Latitude, updatedPoiLocalization.Latitude);
        assertEquals(firstPoiLocalization.Longitude, updatedPoiLocalization.Longitude);

        var poi = poiDatabase.GetEntity(updatedPoi.getId());
        assertNotNull(poi);
        assertEquals(firstPoi.Name, poi.Name);
        assertEquals(firstPoi.Description, poi.Description);

        var poiLocalization = poi.getLocalization();
        assertEquals(firstPoiLocalization.Latitude, poiLocalization.Latitude);
        assertEquals(firstPoiLocalization.Longitude, poiLocalization.Longitude);
    }

    @Test
    @DisplayName("Apagar um poi")
    void DeletePoiTest() {
        // act
        var deleted = poiDatabase.Delete(firstPoi.getId());

        // assert
        assertTrue(deleted);

        var poi = poiDatabase.GetEntity(firstPoi.getId());
        assertNull(poi);
    }
}

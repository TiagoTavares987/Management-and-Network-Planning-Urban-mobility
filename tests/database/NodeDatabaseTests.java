package database;

import core.entities.Localization;
import core.entities.Node;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;

public class NodeDatabaseTests {
    Node firstNode;
    Localization firstNodeLocalization;
    NodeDatabase nodeDatabase;

    @BeforeEach
    void setUp() {
        firstNode = new Node();
        firstNode.Name = "Rwewqr";
        firstNodeLocalization = firstNode.getLocalization();
        firstNodeLocalization.Latitude = 1.02323234f;
        firstNodeLocalization.Longitude = 2.394324f;
        nodeDatabase = new NodeDatabase();
        nodeDatabase.Insert(firstNode);
    }

    @Test
    @DisplayName("Obter um node")
    void GetNodeTest() {
        // act
        Node node = nodeDatabase.GetEntity(firstNode.getId());

        // assert
        assertNotNull(node);
        assertEquals(firstNode.getId(), node.getId());
        assertEquals(firstNode.Name, node.Name);

        Localization nodeLocalization = node.getLocalization();
        assertEquals(firstNodeLocalization.Latitude, nodeLocalization.Latitude);
        assertEquals(firstNodeLocalization.Longitude, nodeLocalization.Longitude);
    }

    @Test
    @DisplayName("Obter todos os nodes")
    void GetAllNodeTest() {
        // act
        ArrayList<Node> node = nodeDatabase.GetTable();

        // assert
        assertNotNull(node);
        assertEquals(1, node.size());
    }

    @Test
    @DisplayName("Inserir um node")
    void InsertNodeTest() {
        // arrange
        Node newNode = new Node();
        newNode.Name = "rwer";
        Localization newNodeLocalization = newNode.getLocalization();
        newNodeLocalization.Latitude = 1.2332423f;
        newNodeLocalization.Longitude = 1.2342222f;

        // act
        Node insertedNode = nodeDatabase.Insert(newNode);

        // assert
        assertNotNull(insertedNode);

        Node node = nodeDatabase.GetEntity(insertedNode.getId());
        assertNotNull(node);
        assertEquals(newNode.Name, node.Name);

        Localization insertedNodeLocalization = node.getLocalization();
        assertEquals(newNodeLocalization.Latitude, insertedNodeLocalization.Latitude);
        assertEquals(newNodeLocalization.Longitude, insertedNodeLocalization.Longitude);
    }

    @Test
    @DisplayName("Atualizar um node")
    void UpdateNodeTest() {
        // arrange
        firstNode.Name = "aaaaa";
        firstNodeLocalization.Latitude = 11.22f;
        firstNodeLocalization.Longitude = 17.21f;

        // act
        Node updatedNode = nodeDatabase.Update(firstNode);

        // assert
        assertNotNull(updatedNode);
        assertEquals(firstNode.Name, updatedNode.Name);

        Localization updatedNodeLocalization = updatedNode.getLocalization();
        assertEquals(firstNodeLocalization.Latitude, updatedNodeLocalization.Latitude);
        assertEquals(firstNodeLocalization.Longitude, updatedNodeLocalization.Longitude);

        Node node = nodeDatabase.GetEntity(updatedNode.getId());
        assertNotNull(node);
        assertEquals(firstNode.Name, node.Name);

        Localization nodeLocalization = node.getLocalization();
        assertEquals(firstNodeLocalization.Latitude, nodeLocalization.Latitude);
        assertEquals(firstNodeLocalization.Longitude, nodeLocalization.Longitude);

    }

    @Test
    @DisplayName("Apagar um node")
    void DeleteNodeTest() {
        // act
        boolean deleted = nodeDatabase.Delete(firstNode.getId());

        // assert
        assertTrue(deleted);

        Node node = nodeDatabase.GetEntity(firstNode.getId());
        assertNull(node);
    }
}

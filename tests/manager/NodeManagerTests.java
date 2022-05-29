package manager;

import business.managers.NodeManager;
import core.entities.Localization;
import core.entities.Node;
import database.NodeDatabase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;

public class NodeManagerTests {
        Node firstNode;
        Localization firstNodeLocalization;
        NodeManager nodeManager;

        @BeforeEach
        void setUp() throws Exception {
            firstNode = new Node();
            firstNode.Name = "Rwewqr";
            firstNodeLocalization = firstNode.getLocalization();
            firstNodeLocalization.Latitude = 1.02323234f;
            firstNodeLocalization.Longitude = 2.394324f;
            //nodeManager = new NodeManager();
            nodeManager.newNode(firstNode.Name, firstNodeLocalization);
        }

        @Test
        @DisplayName("Obter todos os nodes")
        void GetAllNodesTest() {
            // act
            ArrayList<Node> node = nodeManager.GetAll();

            // assert
            assertNotNull(node);
            assertEquals(1, node.size());
        }


        @Test
        @DisplayName("Obter um node")
        void GetNodeTest() {
            // act
            Node node = nodeManager.GetNode(firstNode.getId());

            // assert
            assertNotNull(node);
            assertEquals(firstNode.getId(), node.getId());
            assertEquals(firstNode.Name, node.Name);

            Localization nodeLocalization = node.getLocalization();
            assertEquals(firstNodeLocalization.Latitude, nodeLocalization.Latitude);
            assertEquals(firstNodeLocalization.Longitude, nodeLocalization.Longitude);
        }

        @Test
        @DisplayName("Inserir um node")
        void NewNodeTest() throws Exception {
            // arrange
            Node newNode = new Node();
            newNode.Name = "rwer";
            Localization newNodeLocalization = newNode.getLocalization();
            newNodeLocalization.Latitude = 1.2332423f;
            newNodeLocalization.Longitude = 1.2342222f;

            // act
            Node insertedNode = nodeManager.SaveNode(newNode);

            // assert
            assertNotNull(insertedNode);

            Node node = nodeManager.GetNode(insertedNode.getId());
            assertNotNull(node);
            assertEquals(newNode.Name, node.Name);

            Localization insertedNodeLocalization = node.getLocalization();
            assertEquals(newNodeLocalization.Latitude, insertedNodeLocalization.Latitude);
            assertEquals(newNodeLocalization.Longitude, insertedNodeLocalization.Longitude);
        }

        @Test
        @DisplayName("Atualizar um node")
        void SaveNodeTest() throws Exception {
            // arrange
            firstNode.Name = "aaaaa";
            firstNodeLocalization.Latitude = 11.22f;
            firstNodeLocalization.Longitude = 17.21f;

            // act
            Node updatedNode = nodeManager.SaveNode(firstNode);

            // assert
            assertNotNull(updatedNode);
            assertEquals(firstNode.Name, updatedNode.Name);

            Localization updatedNodeLocalization = updatedNode.getLocalization();
            assertEquals(firstNodeLocalization.Latitude, updatedNodeLocalization.Latitude);
            assertEquals(firstNodeLocalization.Longitude, updatedNodeLocalization.Longitude);

            Node node = nodeManager.GetNode(updatedNode.getId());
            assertNotNull(node);
            assertEquals(firstNode.Name, node.Name);

            Localization nodeLocalization = node.getLocalization();
            assertEquals(firstNodeLocalization.Latitude, nodeLocalization.Latitude);
            assertEquals(firstNodeLocalization.Longitude, nodeLocalization.Longitude);

        }

        @Test
        @DisplayName("Apagar um node")
        void DeleteNodeTest() throws Exception {
            // act
            nodeManager.deleteNode(firstNode.getId(), true);

            // assert
            //assertTrue(deleted);

            Node node = nodeManager.GetNode(firstNode.getId());
            assertNull(node);
        }
}



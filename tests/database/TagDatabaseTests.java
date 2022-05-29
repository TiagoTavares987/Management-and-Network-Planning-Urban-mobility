package database;

import core.baseEntities.Entity;
import core.entities.Node;
import core.entities.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class TagDatabaseTests {

    Tag firstTag;
    Entity first_extra_info;
    TagDatabase tagDatabase;

    @BeforeEach
    void setUp() {
        firstTag = new Tag();
        firstTag.Description = "rua muito movimentada";

        Node node = new Node();
        node.setId(1);
        node.Name = "teste";
        first_extra_info = node;

        ArrayList<Entity> extra_info = firstTag.get_extra_info();
        extra_info.add(first_extra_info);

        tagDatabase = new TagDatabase();
        tagDatabase.Insert(firstTag);
    }

    @Test
    @DisplayName("Obter uma tag")
    void GetTagTest() {
        // act
        Tag tag = tagDatabase.GetEntity(firstTag.getId());

        // assert
        assertNotNull(tag);
        assertEquals(firstTag.getId(), tag.getId());
        assertEquals(firstTag.Description, tag.Description);

        ArrayList<Entity> extraInfoList = tag.get_extra_info();
        assertTrue(extraInfoList.size() > 0);
        assertEquals(first_extra_info.getClass(), extraInfoList.get(0).getClass());
    }

    @Test
    @DisplayName("Obter todas as tags")
    void GetAllTagsTest() {
        // act
        ArrayList<Tag> tag = tagDatabase.GetTable();

        // assert
        assertNotNull(tag);
        assertEquals(1, tag.size());
    }

    @Test
    @DisplayName("Inserir uma tag")
    void InsertTagTest() {
        // arrange
        Tag newTag = new Tag();
        newTag.Description = "rua pouco movimentada";
        //newTag.extra_info = ;

        // act
        Tag insertedTag = tagDatabase.Insert(newTag);

        // assert
        assertNotNull(insertedTag);

        Tag tag = tagDatabase.GetEntity(insertedTag.getId());
        assertNotNull(tag);

        assertEquals(newTag.Description, tag.Description);
        //assertEquals(newTag.extra_info, tag.extra_info);
    }

    @Test
    @DisplayName("Atualizar uma tag")
    void UpdateTagTest() {
        // arrange
        firstTag.Description = "rua com policias";
        //firstTag.extra_info = "";

        // act
        Tag updatedTag = tagDatabase.Update(firstTag);

        // assert
        assertNotNull(updatedTag);
        assertEquals(firstTag.Description, updatedTag.Description);
        //assertEquals(firstTag.extra_info, updatedTag.extra_info);

        Tag tag = tagDatabase.GetEntity(updatedTag.getId());
        assertNotNull(tag);
        assertEquals(firstTag.Description, tag.Description);
        //assertEquals(firstTag.extra_info, tag.extra_info);

    }

    @Test
    @DisplayName("Apagar uma tag")
    void DeleteTagTest() {
        // act
        boolean deleted = tagDatabase.Delete(firstTag.getId());

        // assert
        assertTrue(deleted);

        Tag tag = tagDatabase.GetEntity(firstTag.getId());
        assertNull(tag);
    }
}

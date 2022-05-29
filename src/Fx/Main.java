package Fx;

import business.managers.*;
import java.io.IOException;


public class Main {

    public static NodeManager nodeManager;
    public static PoiManager poiManager;
    public static TagManager tagManager;
    public static UserManager userManager;

    public static UserPoiManager userPoiManager;
    public static WayManager wayManager;

    private static void init(){
        poiManager = new PoiManager(null, null, null);
        tagManager = new TagManager(null, null, null);
        wayManager = new WayManager(tagManager, poiManager);
        nodeManager = new NodeManager(wayManager, tagManager, poiManager);
        poiManager = new PoiManager(nodeManager, wayManager, tagManager);
        tagManager = new TagManager(nodeManager, wayManager, poiManager);
        userManager = new UserManager();
        userPoiManager = new UserPoiManager(userManager, poiManager, wayManager, nodeManager);
    }

    private static void load() throws IOException {
        nodeManager.loadFromFile();
    }

    private static void now() throws IOException {
        nodeManager.snapShot();
        poiManager.snapShot();
        tagManager.snapShot();
        userManager.snapShot();
        wayManager.snapShot();
    }

    public static void main(String[] args) throws Exception {

        init();
        load();

        int tagId = tagManager.newTag("xpto");
        tagManager.deleteTag(tagId, false);

        now();
    }
}

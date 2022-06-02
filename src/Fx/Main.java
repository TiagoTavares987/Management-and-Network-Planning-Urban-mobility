package Fx;

import business.managers.*;
import business.utils.Utils;
import core.baseEntities.Entity;
import core.entities.Localization;
import core.entities.Poi;
import core.entities.User;
import core.entities.Way;
import core.enums.UserType;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


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
        userManager = new UserManager(poiManager);
        userPoiManager = new UserPoiManager(userManager, poiManager, wayManager, nodeManager);
    }

    public static void loadText(String path) throws IOException, ParseException {
        userManager.loadTextFile(path);
        nodeManager.loadTextFile(path);
        poiManager.loadTextFile(path);
        tagManager.loadTextFile(path);
        wayManager.loadTextFile(path);
    }
    public static void loadBin(String path) throws IOException {
        userManager.loadBinFile(path);
        nodeManager.loadBinFile(path);
        poiManager.loadBinFile(path);
        tagManager.loadBinFile(path);
        wayManager.loadBinFile(path);
    }

    public static void now() throws IOException {
        nodeManager.snapShot();
        poiManager.snapShot();
        tagManager.snapShot();
        userManager.snapShot();
        wayManager.snapShot();
    }

    public static void binNow() throws IOException {
        nodeManager.binSnapShot();
        poiManager.binSnapShot();
        tagManager.binSnapShot();
        userManager.binSnapShot();
        wayManager.binSnapShot();
    }

    public static void main(String[] args) throws Exception {

        init();
        loadText("src/data/input");

        // criar user
        userManager.newUser("Diana", Utils.getHash("123456"), UserType.Basic);

        // criar um poi
        //int poiId = poiManager.newPoi("torre de pizza", "é fixe", 41.0434f, 41.34234f);

        // adicionar ao user poi's
        //userManager.addPoi(1, poiId);
        //userManager.addPoi(2, poiId);
        //userManager.addPoi(2, 3);
        //userManager.addPoi(2, 1);

        // adicionar uma tag e poi aos nodes
        //nodeManager.addPoi(3, poiId);
        //nodeManager.addTag(1, 2);

        // apaga o poi
        //nodeManager.deletePoi(3, poiId);

        //wayManager.addPoi(1, poiId);
        //wayManager.addTag(1, 2);
        //wayManager.addTag(1, 3);
        //wayManager.addTag(2, 2);

        //nodeManager.deleteNode(3, true);
        //wayManager.deleteWay(3, true);
        //poiManager.deletePoi(5, true);
        //tagManager.deleteTag(2, true);

        //nodeManager.editNode(1, "cruzamento", nodeManager.GetNode(1).getLocalization());
        //tagManager.addExtraInfo(2, poiManager.GetPoi(poiId));

        //ArrayList<Poi> pois = userPoiManager.poisVisitadosUser(2, new SimpleDateFormat("dd/MM/yyyy").parse("03/05/2021"), new SimpleDateFormat("dd/MM/yyyy").parse("20/06/2022"));
        //ArrayList<User> users = userPoiManager.usersQueVisitraramUmPoi(poiId, new SimpleDateFormat("dd/MM/yyyy").parse("03/05/2021"), new SimpleDateFormat("dd/MM/yyyy").parse("20/06/2022"));
        //ArrayList<Poi> poisNaoVisitados = userPoiManager.poisNaoVisitados(new SimpleDateFormat("dd/MM/yyyy").parse("03/05/2021"), new SimpleDateFormat("dd/MM/yyyy").parse("20/06/2022"));
        //ArrayList<Poi> user = userPoiManager.poisNaoVisitadosUser(2, new SimpleDateFormat("dd/MM/yyyy").parse("03/05/2021"), new SimpleDateFormat("dd/MM/yyyy").parse("20/06/2022"));
        //ArrayList<User> users5 = userPoiManager.top5Users(new SimpleDateFormat("dd/MM/yyyy").parse("03/05/2021"), new SimpleDateFormat("dd/MM/yyyy").parse("20/06/2022"));
        //ArrayList<Poi> pois5 = userPoiManager.top5Pois(new SimpleDateFormat("dd/MM/yyyy").parse("03/05/2021"), new SimpleDateFormat("dd/MM/yyyy").parse("02/06/2022"));
        //ArrayList<Entity> waysNodes = userPoiManager.linhasVerticesArestas(tagManager.GetTag(2).getId());

        now();
        binNow();
    }
}

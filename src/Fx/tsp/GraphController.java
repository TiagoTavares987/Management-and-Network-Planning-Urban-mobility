package Fx.tsp;
import Fx.Main;
import Fx.ProjectApp;
import core.entities.Localization;
import core.entities.Node;
import javafx.scene.Group;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

import java.util.ArrayList;

public class GraphController {
    public Group graphGroup;

    public void updateGraph(){

        double minX = Double.MAX_VALUE;
        double maxX = -Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;
        double maxY = -Double.MAX_VALUE;

        ArrayList<Node> nodes = Main.nodeManager.GetAll();

        for (Node node : nodes){
            Localization point = node.getLocalization();
            if(point.Latitude < minX)
                minX = point.Latitude;
            if(point.Latitude > maxX)
                maxX = point.Latitude;
            if(point.Longitude < minY)
                minY = point.Longitude;
            if(point.Longitude > maxY)
                maxY = point.Longitude;
        }

        // obter tamanho do ecra do grafico
        double width = ProjectApp.main.getWidth() - 200;
        double height = ProjectApp.main.getHeight() - 200;

        // fator de escalonamento da tela para o ecra
        // largura da tela (maxX - minX)
        double factorX = width / (maxX - minX);
        double factorY = height / (maxY - minY);

        // limpa o ecra
        graphGroup.getChildren().clear();

        Localization firstPoint = null;
        Localization lastPoint = null;
        for (Node node : nodes) {
            Localization point = node.getLocalization();

            if(firstPoint == null)
                firstPoint = point;

            Circle c = new Circle(0, 0, 30);
            c.setFill(Color.LIGHTGRAY);

            // cria um retangulo invisivel
            StackPane stack = new StackPane();
            stack.setMinWidth(0);
            stack.setMaxWidth(0);
            stack.setLayoutX(scale(minX, factorX, point.Latitude) + 100); // transforma em x o valor da tela para o valor do ecra
            stack.setLayoutY(scale(minY, factorY, point.Longitude) + 100); // transforma em y o valor da tela para o valor do ecra
            stack.getChildren().addAll(c, new Text(node.getId() + " - " + node.Name)); // coloca dentro do retangulo o circulo e o texto

            graphGroup.getChildren().add(stack); // adicionar o retangulo ao ecra

            if (lastPoint != null) {
                // definimos uma linha entre o ponto atual e o ponto anterior
                Line line = new Line(scale(minX, factorX, point.Latitude) + 100, scale(minY, factorY, point.Longitude) + 130,
                        scale(minX, factorX, lastPoint.Latitude) + 100, scale(minY, factorY, lastPoint.Longitude) + 130);
                line.setStroke(Color.LIGHTGRAY);
                graphGroup.getChildren().add(line); // add a linha ao ecra
            }

            lastPoint = point;
        }

        if (firstPoint != null && lastPoint != null) {
            // definimos uma linha entre o primeiro e o ultimo
            Line line = new Line(scale(minX, factorX, firstPoint.Latitude) + 100, scale(minY, factorY, firstPoint.Longitude) + 130,
                    scale(minX, factorX, lastPoint.Latitude) + 100, scale(minY, factorY, lastPoint.Longitude) + 130);
            line.setStroke(Color.LIGHTGRAY);
            graphGroup.getChildren().add(line);


        }
    }
    private double scale (double shift, double factor, double value){
        return (value - shift) * factor;
    }
}

package org.grawgo.klient;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Translate;

public class CircleButton extends Circle {
    int myX;
    int myY;
    Translate translate;
    public CircleButton(double radius,int myX, int myY){
        super(radius);
        this.myX = myX;
        this.myY = myY;
        translate = new Translate();
        translate.setX(-5);
        translate.setY(-15);
        this.getTransforms().addAll(translate);
        this.setOpacity(0);
        this.setFill(Color.BROWN);
        this.setOnMouseEntered(event -> setOpacity(0.7));
        this.setOnMouseExited(event -> setOpacity(0));
        // to bedzie wysylane jako place x y do serwera:
        this.setOnMouseClicked(mouseEvent -> System.out.println(myX + " , " + myY));

    }
    // nie pytaj czemu to dziala
    public void selfCorrect(){
        translate.setY(0.5);
    }
}

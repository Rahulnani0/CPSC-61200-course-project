package model;

import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

public class Cell extends StackPane {

    int column;
    int row;
    CellType type;
    CellMark mark;
    Label labelF;
    Label labelG;
    Label labelH;

    public Cell(String text, int column, int row, CellType type) {

        this.column = column;
        this.row = row;
        this.type = type;

        updateTypeStyle();

        labelF = new Label();
        labelF.getStyleClass().add("label-f");

        Label labelGoals = new Label(text);
        labelGoals.getStyleClass().add("labelGoals");

        AnchorPane anchorPane = new AnchorPane();

        labelG = new Label("");
        labelG.getStyleClass().add("label-g");
        AnchorPane.setTopAnchor(labelG, 3.);
        AnchorPane.setLeftAnchor(labelG, 3.);

        labelH = new Label("");
        labelH.getStyleClass().add("label-h");
        AnchorPane.setTopAnchor(labelH, 3.);
        AnchorPane.setRightAnchor(labelH, 3.);

        anchorPane.getChildren().addAll(labelG, labelH);

        getChildren().addAll(anchorPane, labelF,labelGoals);
    }

    public void highlight() {
        getStyleClass().add("path");
    }

    public void removeHighlight() {
        getStyleClass().remove("path");
    }

    public void setType(CellType type) {
        this.type = type;
        this.setTextH("");
        this.setTextG("");
        this.setTextF("");
        removeMark();
        removeHighlight();
        updateTypeStyle();
    }

    public void removeMark() {
        this.mark = null;
        updateMarkStyle();
    }

    public void setMark(CellMark mark) {
        this.mark = mark;
        updateMarkStyle();
    }

    public String toString() {
        return this.column + "/" + this.row;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public boolean isTraversable() {
        return type == CellType.TRAVERSABLE;
    }

    public void setTextF(String text) {
        labelF.setText(text);
    }

    public void setTextG(String text) {
        labelG.setText(text);
    }

    public void setTextH(String text) {
        labelH.setText(text);
    }

    private void updateTypeStyle() {

        getStyleClass().remove("traversable");
        getStyleClass().remove("obstacle");

        switch (type) {
            case TRAVERSABLE:
                getStyleClass().add("traversable");
                break;
            case OBSTACLE:
                getStyleClass().add("obstacle");
                break;
        }

    }

    private void updateMarkStyle() {

        getStyleClass().remove("open");
        getStyleClass().remove("closed");
        if (mark == null)
            return;
        switch (mark) {
            case OPEN:
                getStyleClass().add("open");
                break;
            case CLOSED:
                getStyleClass().add("closed");
                break;
        }

    }
}

package model;

public class CellModel<T>  {

    int col;
    int row;
    boolean isTraversable;
    double g;
    double f;
    double h;
    double cost;
    CellModel<T> cameFrom;
    T obj;

    public CellModel<T> getCameFrom() {
        return cameFrom;
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setG(double g) {
        this.g = g;
    }

    public void setF(double f) {
        this.f = f;
    }

    public void setH(double h) {
        this.h = h;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public void setCameFrom(CellModel<T> cameFrom) {
        this.cameFrom = cameFrom;
    }

    public CellModel(int col, int row, boolean isTraversable, T obj) {
        this.col = col;
        this.row = row;
        this.isTraversable = isTraversable;
        this.obj = obj;
    }

    public double getCost() {
        return cost;
    }

    public T getObject() {
        return obj;
    }

    public double getF() {
        return f;
    }

    public double getG() {
        return g;
    }

    public double getH() {
        return h;
    }

}

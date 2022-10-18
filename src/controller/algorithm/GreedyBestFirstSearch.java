package controller.algorithm;

import model.Cell;
import model.CellMark;
import model.Grid;
import javafx.application.Platform;
import javafx.scene.control.Label;
import model.CellModel;
import model.GridModel;
import controller.grid.GridAction;

import java.text.NumberFormat;
import java.util.*;

public class GreedyBestFirstSearch<T extends Cell> {
    List<CellModel> rawPath;
    List<CellModel> unvisitedNodes;
    List<CellModel> visitedNodes;
    Grid mGrid;
    GridModel<T> gridModel = null;
    CellModel<T> startNode = null;
    CellModel<T> endNode = null;
    List<Label> labelList;
    long startTime;
    private boolean running = false, stepViewProperty, showPathProperty;

    public void setup(Grid grid, T start, T goal, List<Label> labelList,boolean allowDiagonals) {
        startTime = System.currentTimeMillis();
        this.mGrid = grid;
        this.labelList = labelList;
        for (Label l : labelList) {
            l.setText("");
            l.getStyleClass().add("label-f");
        }
        marshal(grid, start, goal);
        running = false;
        visitedNodes = new ArrayList<>();
        unvisitedNodes = new ArrayList<>();
        if (allowDiagonals){
            startNode.setH(octileHeuristicCostEstimate(startNode, endNode));
        }else{
            startNode.setH(manhattanHeuristicCostEstimate(startNode, endNode));
        }
        unvisitedNodes.add(startNode);

    }

    private void marshal(Grid grid, T start, T goal) {

        gridModel = new GridModel<>(grid.getColumns(), grid.getRows());
        for (int row = 0; row < grid.getRows(); row++) {
            for (int col = 0; col < grid.getColumns(); col++) {

                T cell = (T) grid.getCell(col, row);

                gridModel.setCell(cell, col, row, cell.isTraversable());
                if (row == start.getRow() && col == start.getColumn()) {
                    startNode = gridModel.getCell(col, row);
                }
                if (row == goal.getRow() && col == goal.getColumn()) {
                    endNode = gridModel.getCell(col, row);
                }

            }
        }
    }


    public void getPath(boolean allowDiagonals, boolean stepViewProperty, boolean showPathProperty) {
        this.stepViewProperty = stepViewProperty;
        this.showPathProperty = showPathProperty;
        if (!unvisitedNodes.isEmpty() && !visitedNodes.contains(endNode)) {
            CellModel currentNode = getLowestCost(unvisitedNodes);
            shiftCellState(currentNode);
            for (CellModel neighbor : gridModel.getNeighbors(currentNode, allowDiagonals)) {
                if (!visitedNodes.contains(neighbor) && neighbor != null) {
                    Double newH=0.0;
                    if (allowDiagonals){
                        newH=octileHeuristicCostEstimate(neighbor, endNode);
                    }else{
                        newH= manhattanHeuristicCostEstimate(neighbor, endNode);
                    }
                    boolean containsNeighbor;
                    if (!(containsNeighbor = unvisitedNodes.contains(neighbor)) || Double.compare(newH, currentNode.getCost()) < 0) {
                        neighbor.setCost(newH);
                        neighbor.setCameFrom(currentNode);
                        if (!containsNeighbor) {
                            unvisitedNodes.add(neighbor);
                        }
                    }
                }
            }
        } else {
            finalizePathFinding();
        }
        Platform.runLater(this::showAnimation);
    }

    private double manhattanHeuristicCostEstimate(CellModel from, CellModel to) {
        int differenceCol = from.getCol() - to.getCol();
        if (differenceCol < 0) {
            differenceCol *= -1;
        }

        int differenceRow = from.getRow() - to.getRow();
        if (differenceRow < 0) {
            differenceRow *= -1;
        }

        return differenceCol + differenceRow;
    }

    private double octileHeuristicCostEstimate(CellModel from, CellModel to) {
        int differenceCol = from.getCol() - to.getCol();
        if (differenceCol < 0) {
            differenceCol *= -1;
        }

        int differenceRow = from.getRow() - to.getRow();
        if (differenceRow < 0) {
            differenceRow *= -1;
        }
        if (differenceCol > differenceRow) {
            return differenceRow * 1.4 + (differenceCol - differenceRow);
        } else {
            return differenceCol * 1.4 + (differenceRow - differenceCol);
        }

    }

    public List<T> unmarshal(Collection<CellModel> path) {

        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(1);

        List<T> list = new ArrayList<>();
        Iterator<CellModel> cellIterator = path.iterator();
        while (cellIterator.hasNext()) {
            CellModel c = cellIterator.next();
            T obj = (T) c.getObject();
            if (stepViewProperty) {
                obj.setTextF(nf.format(c.getCost()));
            } else {
                obj.setTextF("");
            }
            obj.setTextG("");
            obj.setTextH("");
            list.add(obj);
        }
        return list;
    }

    private void finalizePathFinding() {
        running = true;
        Platform.runLater(() -> {
            reconstructPath();
            labelList.get(3).setText("Time Taken: " + getRunningTime());
        });
    }

    private void reconstructPath() {
        CellModel current = endNode;
        if (visitedNodes.contains(current)) {
            rawPath = new ArrayList<>();
            rawPath.add(current);
            while ((current = current.getCameFrom()) != null) {
                rawPath.add(current);
            }
            Collections.reverse(rawPath);
            paintPath((List<Cell>) getRawPath());
        } else {
            new GridAction().notify("END:: SORRY NO PATH FOUND").showError();
        }
    }

    private void paintPath(List<Cell> path) {
        if (showPathProperty) {
            mGrid.removeHighlight();

            if (path != null) {
                labelList.get(2).setText(" :: Path Nodes: " + (rawPath.size()-2)+" :: ");
                new GridAction().notify("END:: PATH FOUND").showInformation();
                Thread paintPathThread = new Thread(() -> {
                    for (Cell cell : path) {
                        try {
                            cell.highlight();
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
                paintPathThread.start();

            } else {
                new GridAction().notify("END:: SORRY NO PATH FOUND").showError();
            }
        }
        labelList.get(3).setText("Time Taken: " + getRunningTime());
    }

    public List<T> getOpenSnapshot() {

        return unmarshal(unvisitedNodes);

    }

    public List<T> getClosedSnapshot() {

        return unmarshal(visitedNodes);

    }

    public void showAnimation() {
        if (!unvisitedNodes.isEmpty()) {
            for (Cell cell : getOpenSnapshot()) {
                cell.setMark(CellMark.OPEN);
            }
            labelList.get(1).setText(" :: Open Nodes: " + unvisitedNodes.size());
        }
        if (!visitedNodes.isEmpty()) {
            for (Cell cell : getClosedSnapshot()) {
                cell.setMark(CellMark.CLOSED);
            }
            labelList.get(0).setText(" :: Closed Nodes: " + visitedNodes.size());
        }
    }

    private String getRunningTime() {
        long currentTime = System.currentTimeMillis();
        long timeDifference=currentTime - startTime;
        if (timeDifference/ 1000>1){
            return timeDifference/ 1000+" s";
        }else{
            return timeDifference+" ms";
        }

    }

    public List<T> getRawPath() {
        return unmarshal(rawPath);
    }

    public boolean isRunning() {
        return running;
    }

    private CellModel getLowestCost(List<CellModel> data) {
        return new SortingAlgorithm().sortCost(data);
    }

    private void shiftCellState(CellModel currentNode) {
        visitedNodes.add(currentNode);
        unvisitedNodes.remove(currentNode);
    }
}

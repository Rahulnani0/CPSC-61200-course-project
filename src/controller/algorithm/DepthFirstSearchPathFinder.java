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

public class DepthFirstSearchPathFinder<T extends Cell> {
    List<CellModel> rawPath;
    Stack<Set> neighborsListStack;
    List<CellModel> visitedNodes;
    Set<CellModel> neighborsNodes;
    Grid mGrid;
    GridModel<T> gridModel = null;
    CellModel<T> startNode = null;
    CellModel<T> endNode = null;
    List<Label> labelList;
    long startTime;
    int nodeIndex = 0, i = 0;
    private boolean running = false, stepViewProperty, showPathProperty, allowDiagonals;

    public void setup(Grid grid, T start, T goal, List<Label> labelList, boolean allowDiagonals) {
        startTime = System.currentTimeMillis();
        this.mGrid = grid;
        this.labelList = labelList;
        this.allowDiagonals = allowDiagonals;
        for (Label l : labelList) {
            l.setText("");
            l.getStyleClass().add("label-f");
        }
        marshal(grid, start, goal);
        running = false;
        visitedNodes = new ArrayList<>();
        neighborsListStack = new Stack<>();
        neighborsNodes = new HashSet<>();
        startNode.setH(nodeIndex);
        visitedNodes.add(startNode);
        for (CellModel neighborONeighbor : gridModel.getNeighbors(startNode, allowDiagonals)) {
            if (!(visitedNodes.contains(neighborONeighbor)) && neighborONeighbor != null) {
                ++nodeIndex;
                neighborONeighbor.setCameFrom(startNode);
                neighborONeighbor.setH(nodeIndex);
                neighborsNodes.add(neighborONeighbor);
            }
        }
        neighborsListStack.push(neighborsNodes);
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

    public void getPath(boolean stepViewProperty, boolean showPathProperty) {

        this.stepViewProperty = stepViewProperty;
        this.showPathProperty = showPathProperty;
        if (!(neighborsListStack.isEmpty()) && !(visitedNodes.contains(endNode))) {
            Set neighborsIteratorSet = neighborsListStack.peek();
            Iterator neighborsIterator = neighborsIteratorSet.iterator();
            if (neighborsIterator.hasNext()) {
                CellModel neighbor = (CellModel) neighborsIterator.next();
                neighborsListStack.peek().remove(neighbor);
                visitedNodes.add(neighbor);
                neighborsNodes = new HashSet<>();
                for (CellModel neighborOfNeighbor : gridModel.getNeighbors(neighbor, allowDiagonals)) {
                    if (!visitedNodes.contains(neighborOfNeighbor) && neighborOfNeighbor != null) {
                        ++nodeIndex;
                        neighborOfNeighbor.setCameFrom(neighbor);
                        neighborOfNeighbor.setH(nodeIndex);
                        neighborsNodes.add(neighborOfNeighbor);
                    }
                }
                neighborsListStack.push(neighborsNodes);
            } else {
                neighborsListStack.pop();
            }
        } else {
            finalizePathFinding();
        }
        Platform.runLater(this::showAnimation);
    }

    public List<T> unmarshal(Collection<CellModel> path) {

        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(1);

        List<T> list = new ArrayList<>();
        for (CellModel c : path) {
            T obj = (T) c.getObject();
            if (stepViewProperty) {
                obj.setTextH(nf.format(c.getH()));
            } else {
                obj.setTextH("");
            }
            obj.setTextG("");
            obj.setTextF("");
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
                labelList.get(2).setText(" :: Path Nodes: " + (rawPath.size() - 1) + " :: ");
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

    public List<T> getClosedSnapshot() {

        return unmarshal(visitedNodes);

    }

    public void showAnimation() {
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
}
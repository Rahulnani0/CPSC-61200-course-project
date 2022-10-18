package controller.grid;

import model.Cell;
import model.CellType;
import model.Grid;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import utils.Settings;

import java.util.List;
import java.util.Random;

public class GridAction {
    private Grid grid;
    private List<Label> labelList;

    public GridAction(Grid grid, List<Label> labelList) {
        this.grid = grid;
        this.labelList = labelList;
    }

    public GridAction() {
    }

    public void simpleVerticalMaze() {
        clearGrid();
        //step 1
        for (int i = 0; i < grid.getRows(); i++) {
            grid.getCell(6, i).setType(CellType.OBSTACLE);
        }
        int nextRow = new Random().nextInt(grid.getRows() - 7);
        grid.getCell(6, nextRow).setType(CellType.TRAVERSABLE);

        for (int i = 7; i < 12; i++) {
            grid.getCell(i, nextRow + 1).setType(CellType.OBSTACLE);
        }
        int openCol = new Random().nextInt((11 - 7)) + 7;
        grid.getCell(openCol, nextRow + 1).setType(CellType.TRAVERSABLE);
        //step 2
        for (int i = 0; i < grid.getRows(); i++) {
            grid.getCell(12, i).setType(CellType.OBSTACLE);
        }
        nextRow = new Random().nextInt((15 - 10)) + 10;
        grid.getCell(12, nextRow).setType(CellType.TRAVERSABLE);

        for (int i = 13; i < 18; i++) {
            grid.getCell(i, nextRow - 1).setType(CellType.OBSTACLE);
        }
        openCol = new Random().nextInt((17 - 13)) + 13;
        grid.getCell(openCol, nextRow - 1).setType(CellType.TRAVERSABLE);
        //step 3
        for (int i = 0; i < grid.getRows(); i++) {
            grid.getCell(18, i).setType(CellType.OBSTACLE);
        }
        nextRow = new Random().nextInt(grid.getRows() - 7);
        grid.getCell(18, nextRow).setType(CellType.TRAVERSABLE);

        for (int i = 19; i < 22; i++) {
            grid.getCell(i, nextRow + 1).setType(CellType.OBSTACLE);
        }
        openCol = new Random().nextInt((21 - 19)) + 19;
        grid.getCell(openCol, nextRow + 1).setType(CellType.TRAVERSABLE);
    }

    public void multipleVerticalBars() {
        clearGrid();
        //column 1
        for (int i = 0; i < grid.getRows(); i++) {
            grid.getCell(2, i).setType(CellType.OBSTACLE);
        }
        grid.getCell(2, new Random().nextInt(grid.getRows())).setType(CellType.TRAVERSABLE);
        //column 2
        for (int i = 0; i < grid.getRows(); i++) {
            grid.getCell(5, i).setType(CellType.OBSTACLE);
        }
        grid.getCell(5, new Random().nextInt(grid.getRows())).setType(CellType.TRAVERSABLE);
        //column 3
        for (int i = 0; i < grid.getRows(); i++) {
            grid.getCell(8, i).setType(CellType.OBSTACLE);
        }
        grid.getCell(8, new Random().nextInt(grid.getRows())).setType(CellType.TRAVERSABLE);
        //column 4
        for (int i = 0; i < grid.getRows(); i++) {
            grid.getCell(11, i).setType(CellType.OBSTACLE);
        }
        grid.getCell(11, new Random().nextInt(grid.getRows())).setType(CellType.TRAVERSABLE);
        //column 5
        for (int i = 0; i < grid.getRows(); i++) {
            grid.getCell(14, i).setType(CellType.OBSTACLE);
        }
        grid.getCell(14, new Random().nextInt(grid.getRows())).setType(CellType.TRAVERSABLE);
        //column 6
        for (int i = 0; i < grid.getRows(); i++) {
            grid.getCell(17, i).setType(CellType.OBSTACLE);
        }
        grid.getCell(17, new Random().nextInt(grid.getRows())).setType(CellType.TRAVERSABLE);
        //column 7
        for (int i = 0; i < grid.getRows(); i++) {
            grid.getCell(20, i).setType(CellType.OBSTACLE);
        }
        grid.getCell(20, new Random().nextInt(grid.getRows())).setType(CellType.TRAVERSABLE);
    }

    public void multipleHorizontalBars() {
        clearGrid();
        //column 1
        for (int i = 0; i < grid.getColumns(); i++) {
            grid.getCell(i, 2).setType(CellType.OBSTACLE);
        }
        grid.getCell(new Random().nextInt(grid.getColumns()), 2).setType(CellType.TRAVERSABLE);
        //column 2
        for (int i = 0; i < grid.getColumns(); i++) {
            grid.getCell(i, 5).setType(CellType.OBSTACLE);
        }
        grid.getCell(new Random().nextInt(grid.getColumns()), 5).setType(CellType.TRAVERSABLE);
        //column 3
        for (int i = 0; i < grid.getColumns(); i++) {
            grid.getCell(i, 8).setType(CellType.OBSTACLE);
        }
        grid.getCell(new Random().nextInt(grid.getColumns()), 8).setType(CellType.TRAVERSABLE);
        //column 4
        for (int i = 0; i < grid.getColumns(); i++) {
            grid.getCell(i, 11).setType(CellType.OBSTACLE);
        }
        grid.getCell(new Random().nextInt(grid.getColumns()), 11).setType(CellType.TRAVERSABLE);
        //column 5
        for (int i = 0; i < grid.getColumns(); i++) {
            grid.getCell(i, 14).setType(CellType.OBSTACLE);
        }
        grid.getCell(new Random().nextInt(grid.getColumns()), 14).setType(CellType.TRAVERSABLE);
    }

    public void singleVerticalBar() {
        clearGrid();
        //column 1
        int col=new Random().nextInt((21-2))+2;
        for (int i = 0; i < grid.getRows(); i++) {
            grid.getCell(col, i).setType(CellType.OBSTACLE);
        }
        grid.getCell(col, new Random().nextInt(grid.getRows())).setType(CellType.TRAVERSABLE);
    }

    public void singleHorizontalBar() {
        clearGrid();
        //column 1
        int row=new Random().nextInt((15-2))+2;
        for (int i = 0; i < grid.getColumns(); i++) {
            grid.getCell(i, row).setType(CellType.OBSTACLE);
        }
        grid.getCell(new Random().nextInt(grid.getColumns()), row).setType(CellType.TRAVERSABLE);
    }

    public void simpleHorizontalMaze() {
        clearGrid();
        //step 1
        for (int i = 0; i < grid.getColumns(); i++) {
            grid.getCell(i, 3).setType(CellType.OBSTACLE);
        }
        int nexColumn = new Random().nextInt(grid.getColumns() - 10);
        grid.getCell(nexColumn, 3).setType(CellType.TRAVERSABLE);

        for (int i = 4; i < 8; i++) {
            grid.getCell(nexColumn + 1, i).setType(CellType.OBSTACLE);
        }
        int openRow = new Random().nextInt((8 - 4)) + 4;
        grid.getCell(nexColumn + 1, openRow).setType(CellType.TRAVERSABLE);
        //step 2
        for (int i = 0; i < grid.getColumns(); i++) {
            grid.getCell(i, 8).setType(CellType.OBSTACLE);
        }
        nexColumn = new Random().nextInt((20 - 15)) + 15;
        grid.getCell(nexColumn, 8).setType(CellType.TRAVERSABLE);

        for (int i = 9; i < 12; i++) {
            grid.getCell(nexColumn - 1, i).setType(CellType.OBSTACLE);
        }
        openRow = new Random().nextInt((12 - 9)) + 9;
        grid.getCell(nexColumn - 1, openRow).setType(CellType.TRAVERSABLE);
        //step 3
        for (int i = 0; i < grid.getColumns(); i++) {
            grid.getCell(i, 12).setType(CellType.OBSTACLE);
        }
        nexColumn = new Random().nextInt(grid.getRows() - 10);
        grid.getCell(nexColumn, 12).setType(CellType.TRAVERSABLE);

        for (int i = 13; i < grid.getRows(); i++) {
            grid.getCell(nexColumn + 1, i).setType(CellType.OBSTACLE);
        }
        openRow = new Random().nextInt((grid.getRows() - 13)) + 13;
        grid.getCell(nexColumn + 1, openRow).setType(CellType.TRAVERSABLE);
    }

    public void simpleStairCaseMaze(){
        clearGrid();
        int smallestClosedRow=0;
        int col=new Random().nextInt(grid.getColumns()-2);
        for (int row = grid.getRows()-1; row >0 ; row--) {
            if (col<22){
                grid.getCell(col,row ).setType(CellType.OBSTACLE);
                grid.getCell(col, row-1).setType(CellType.OBSTACLE);
                ++col;
                smallestClosedRow=row;
            }
        }
        int openRow=new Random().nextInt((grid.getRows())-smallestClosedRow)+smallestClosedRow;
        for (int openCol = 0; openCol <grid.getColumns() ; openCol++) {
            grid.getCell(openCol,openRow ).setType(CellType.TRAVERSABLE);
        }
    }

    public void clearGrid() {

        for (int row = 0; row < Settings.GRID_ROWS; row++) {
            for (int column = 0; column < Settings.GRID_COLUMNS; column++) {
                Cell cell = grid.getCell(column, row);
                cell.setTextF("");
                cell.setTextG("");
                cell.setTextH("");
                cell.removeHighlight();
                cell.removeMark();
                cell.setType(CellType.TRAVERSABLE);
            }
        }
        for (Label l : labelList) {
            l.setText("");
            l.getStyleClass().add("label-f");
        }
    }

    public void resetGrid() {
        for (int row = 0; row < Settings.GRID_ROWS; row++) {
            for (int column = 0; column < Settings.GRID_COLUMNS; column++) {
                Cell cell = grid.getCell(column, row);
                if (cell.isTraversable()) {
                    cell.setTextF("");
                    cell.setTextG("");
                    cell.setTextH("");
                    cell.removeHighlight();
                    cell.removeMark();
                    cell.setType(CellType.TRAVERSABLE);
                }
            }
        }
        for (Label l : labelList) {
            l.setText("");
            l.getStyleClass().add("label-f");
        }
    }

    public Notifications notify(String text) {
        return Notifications.create()
                .title("PATHFINDER VISUALIZER")
                .text(text)
                .position(Pos.TOP_CENTER)
                .hideAfter(Duration.seconds(3));

    }
}

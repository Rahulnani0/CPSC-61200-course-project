package model;


public class GridModel<T> {

    CellModel<T>[][] gridCells;
    int cols;
    int rows;

    public GridModel(int cols, int rows) {
        this.cols = cols;
        this.rows = rows;
        gridCells = new CellModel[rows][cols];
    }

    public void setCell(T cell, int col, int row, boolean path) {
        gridCells[row][col] = new CellModel<T>(col, row, path, cell);
    }

    public CellModel<T> getCell(int col, int row) {
        return gridCells[row][col];
    }

    public CellModel<T>[] getNeighbors(CellModel<T> cell, boolean allowDiagonals) {

        CellModel<T>[] neighbors = new CellModel[allowDiagonals ? 8 : 4];

        int currentColumn = cell.col;
        int currentRow = cell.row;
        int index = 0;

        int neighborColumn;
        int neighborRow;

        // top
        neighborColumn = currentColumn;
        neighborRow = currentRow - 1;

        if (neighborRow >= 0) {
            if (gridCells[neighborRow][neighborColumn].isTraversable) {
                neighbors[index++] = gridCells[neighborRow][neighborColumn];
            }
        }

        // bottom
        neighborColumn = currentColumn;
        neighborRow = currentRow + 1;

        if (neighborRow < rows) {
            if (gridCells[neighborRow][neighborColumn].isTraversable) {
                neighbors[index++] = gridCells[neighborRow][neighborColumn];
            }
        }

        // left
        neighborColumn = currentColumn - 1;
        neighborRow = currentRow;

        if (neighborColumn >= 0) {
            if (gridCells[neighborRow][neighborColumn].isTraversable) {
                neighbors[index++] = gridCells[neighborRow][neighborColumn];
            }
        }

        // right
        neighborColumn = currentColumn + 1;
        neighborRow = currentRow;

        if (neighborColumn < cols) {
            if (gridCells[neighborRow][neighborColumn].isTraversable) {
                neighbors[index++] = gridCells[neighborRow][neighborColumn];
            }
        }

        if (allowDiagonals) {

            // top/left
            neighborColumn = currentColumn - 1;
            neighborRow = currentRow - 1;

            if (neighborRow >= 0 && neighborColumn >= 0) {
                if (gridCells[neighborRow][neighborColumn].isTraversable) {
                    neighbors[index++] = gridCells[neighborRow][neighborColumn];
                }
            }

            // bottom/right
            neighborColumn = currentColumn + 1;
            neighborRow = currentRow + 1;

            if (neighborRow < rows && neighborColumn < cols) {
                if (gridCells[neighborRow][neighborColumn].isTraversable) {
                    neighbors[index++] = gridCells[neighborRow][neighborColumn];
                }
            }

            // top/right
            neighborColumn = currentColumn + 1;
            neighborRow = currentRow - 1;

            if (neighborRow >= 0 && neighborColumn < cols) {
                if (gridCells[neighborRow][neighborColumn].isTraversable) {
                    neighbors[index++] = gridCells[neighborRow][neighborColumn];
                }
            }

            // bottom/left
            neighborColumn = currentColumn - 1;
            neighborRow = currentRow + 1;

            if (neighborRow < rows && neighborColumn >= 0) {
                if (gridCells[neighborRow][neighborColumn].isTraversable) {
                    neighbors[index] = gridCells[neighborRow][neighborColumn];
                }
            }

        }


        return neighbors;
    }

}
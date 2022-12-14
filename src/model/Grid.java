package model;

import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;

public class Grid extends Pane {

	int rows;
	int columns;

	double width;
	double height;

	Cell[][] cells;

	
	public Grid(int columns, int rows, double width, double height) {

		this.columns = columns;
		this.rows = rows;
		this.width = width;
		this.height = height;

		cells = new Cell[rows][columns];
		
	}

	public void add(Cell cell, int column, int row) {

		cells[row][column] = cell;

		double w = width / columns;
		double h = height / rows;
		double x = w * column;
		double y = h * row;

		cell.setLayoutX(x);
		cell.setLayoutY(y);
		cell.setPrefWidth(w);
		cell.setPrefHeight(h);

		getChildren().add(cell);

	}

	public void addOverlay(Cell cell, int column, int row) {

		double w = width / columns;
		double h = height / rows;
		double x = w * column;
		double y = h * row;

		cell.setLayoutX(x);
		cell.setLayoutY(y);
		cell.setPrefWidth(w);
		cell.setPrefHeight(h);

		getChildren().add(cell);

	}

	public void snapToGrid( Cell overlayCell) {
		
		double centerX = overlayCell.getBoundsInParent().getMinX() + overlayCell.getBoundsInParent().getWidth() / 2; 
		double centerY = overlayCell.getBoundsInParent().getMinY() + overlayCell.getBoundsInParent().getHeight() / 2;
		
		Point2D centerPoint = new Point2D( centerX, centerY);
		
		boolean found = false;
		
		for (int row = 0; row < rows; row++) {

			for (int col = 0; col < columns; col++) {
				
				Cell gridCell = cells[row][col];
				
				if( gridCell.getBoundsInParent().contains( centerPoint)) {
					
					overlayCell.setTranslateX(0);
					overlayCell.setTranslateY(0);
					
					overlayCell.setLayoutX( gridCell.getLayoutX());
					overlayCell.setLayoutY( gridCell.getLayoutY());
					
					overlayCell.column = gridCell.column;
					overlayCell.row = gridCell.row;
					
					found = true;
					
				}
				
				if( found) {
					break;
				}
			}
			
			if( found) {
				break;
			}
		}
	}
	
	public Cell getCell(int column, int row) {
		return cells[row][column];
	}

	public void removeHighlight() {
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < columns; col++) {
				cells[row][col].removeHighlight();
			}
		}
	}

	public void resetText() {
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < columns; col++) {
				cells[row][col].setTextF("");
				cells[row][col].setTextG("");
				cells[row][col].setTextH("");
			}
		}
	}

	public void setType( CellType type) {

		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < columns; col++) {
				cells[row][col].setType( type);
			}
		}
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public int getColumns() {
		return columns;
	}

	public void setColumns(int columns) {
		this.columns = columns;
	}

}

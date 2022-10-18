package controller.input;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import model.Cell;
import model.CellType;

public class MousePaintGestures {

	public void makePaintable(Node node) {
		node.setOnMousePressed( onMousePressedEventHandler);
		node.setOnDragDetected( onDragDetectedEventHandler);
		node.setOnMouseDragEntered(onMouseDragEnteredEventHandler);
	}
	
	EventHandler<MouseEvent> onMousePressedEventHandler = this::setType;

	EventHandler<MouseEvent> onDragDetectedEventHandler = event -> {

		Cell cell = (Cell) event.getSource();
		cell.startFullDrag();

	};

	EventHandler<MouseEvent> onMouseDragEnteredEventHandler = this::setType;
	
	private void setType( MouseEvent event) {

		CellType type;
		Cell cell = (Cell) event.getSource();
		if( event.isPrimaryButtonDown()) {
			type = CellType.OBSTACLE;
		} else if( event.isSecondaryButtonDown()) {
			type = CellType.TRAVERSABLE;
		} else {
			return;
		}

		cell.setType(type);
		cell.removeHighlight();

	}

}

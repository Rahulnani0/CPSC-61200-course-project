package controller.input;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import model.Grid;
import model.Cell;
import utils.Utils;

public class MouseDragGestures {

	static class DragContext {
		double x;
		double y;
	}
	
	DragContext dragContext = new DragContext();
	
	Grid grid;
	
	double minX;
	double minY;
	double maxX;
	double maxY;
	
	public MouseDragGestures( Grid grid) {
		this.grid = grid;
	}
	
	public void makeDraggable( Node node) {
		node.addEventFilter(MouseEvent.MOUSE_PRESSED, onMousePressedEventHandler);
		node.addEventFilter(MouseEvent.MOUSE_DRAGGED, onMouseDraggedEventHandler);
		node.addEventFilter(MouseEvent.MOUSE_RELEASED, onMouseReleasedEventHandler);
	}
	
	EventHandler<MouseEvent> onMousePressedEventHandler = new EventHandler<MouseEvent>() {

		@Override
		public void handle(MouseEvent event) {
			
			Cell cell = ((Cell) (event.getSource()));

			dragContext.x = cell.getLayoutX() - event.getSceneX();
			dragContext.y = cell.getLayoutY() - event.getSceneY();
			
			minX = grid.getBoundsInLocal().getMinX();
			minY = grid.getBoundsInLocal().getMinY();
			maxX = grid.getBoundsInLocal().getMaxX() - cell.getBoundsInLocal().getWidth();
			maxY = grid.getBoundsInLocal().getMaxY() - cell.getBoundsInLocal().getHeight();
		}
	};

	EventHandler<MouseEvent> onMouseDraggedEventHandler = new EventHandler<MouseEvent>() {

		@Override
		public void handle(MouseEvent event) {
			
			Cell cell = ((Cell) (event.getSource()));
			
			double x = Utils.clamp( dragContext.x + event.getSceneX(), minX, maxX);
			double y = Utils.clamp( dragContext.y + event.getSceneY(), minY, maxY);

			cell.setLayoutX( x);
			cell.setLayoutY( y);
			
		}
	};
	
	EventHandler<MouseEvent> onMouseReleasedEventHandler = new EventHandler<MouseEvent>() {

		@Override
		public void handle(MouseEvent event) {
			Cell cell = ((Cell) (event.getSource()));
			grid.snapToGrid(cell);
			
		}
	};
	

}
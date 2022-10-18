package view;

import controller.algorithm.*;
import animatefx.animation.BounceIn;
import animatefx.animation.FadeIn;
import animatefx.animation.Shake;
import com.jfoenix.controls.JFXButton;
import model.Cell;
import model.CellType;
import model.Grid;
import controller.grid.GridAction;
import controller.input.MouseDragGestures;
import controller.input.MousePaintGestures;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import utils.MazeSaver;
import utils.Settings;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.interrupted;
import static java.lang.Thread.sleep;


public class Main extends Application implements ActionListener {
    public Main() {
    }

    private Grid grid;
    private boolean allowDiagonalsProperty = true, stepViewProperty = false, showPathProperty = true, aStarAlgorithm,
            dijkstrasAlgorithm, breadthFirstSearchAlgorithm, greedyBestFirstSearchAlgorithm,
            depthFirstSearchPathFinderAlgorithm;
    private final MousePaintGestures mousePaintGestures = new MousePaintGestures();
    private Cell startCell;
    private Cell endCell;
    private double xOffset, yOffset;
    private final Timer timer = new Timer(100, this);


    private AStarPathFinder aStarPathFinder;
    private DijkstrasPathFinder dijkstrasPathFinder;
    private BreadthFirstSearchPathFinder breadthFirstSearchPathFinder;
    private GreedyBestFirstSearch greedyBestFirstSearch;
    private DepthFirstSearchPathFinder depthFirstSearchPathFinder;


    private JFXButton findPathButton, exit;
    private CheckMenuItem aStarCheckMenuItem, dijkstraCheckMenuItem, breadthFirstSearchCheckMenuItem, greedyBestFirstSearchCheckMenuItem,
            depthFirstSearchPathFinderCheckMenuItem, allowDiagonalCheckMenuItem, showPathCheckMenuItem, stepViewCheckMenuItem;
    private MenuItem restGridMenuItem, clearGridMenuItem, saveMazeMenuItem, simpleVerticalMazeMenuItem, multipleVerticalBarsMenuItem,
            multipleHorizontalBarsMenuItem, singleVerticalBarMenuItem, singleHorizontalBarMenuItem, simpleHorizontalMazeMenuItem,
            simpleStaircaseBarMenuItem;
    Menu algorithms, grid_action, attributes, simpleObstacles, savedMaze;
    MenuBar menuBar;
    Slider speedSlider;
    Label status, countOpenSet, countClosedSet, countPathSet, speedLabel, descriptionLabel, lblSplashMessage;
    List<Label> labelList;
    String BASE_DIR = System.getProperty("user.dir").concat("\\_savedMaze\\");


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            String BASE = System.getProperty("user.dir").concat("\\_savedMaze\\");
            File baseFile = new File(BASE);
            if (!baseFile.exists()) {
                baseFile.mkdir();
            }
            Parent root = FXMLLoader.load(getClass().getResource("fxml/splash.fxml"));
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.centerOnScreen();
            primaryStage.initStyle(StageStyle.UNDECORATED);
            primaryStage.setFullScreen(false);
            primaryStage.setMaximized(false);
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void display(Stage primaryStage) {
        try {

            BorderPane root = new BorderPane();
            labelList = new ArrayList<>();
            dijkstrasPathFinder = new DijkstrasPathFinder();
            aStarPathFinder = new AStarPathFinder();
            breadthFirstSearchPathFinder = new BreadthFirstSearchPathFinder();
            greedyBestFirstSearch = new GreedyBestFirstSearch();
            depthFirstSearchPathFinder = new DepthFirstSearchPathFinder();
            StackPane content = new StackPane();
            countClosedSet = new Label();
            countOpenSet = new Label();
            countPathSet = new Label();
            status = new Label();
            speedLabel = new Label();
            descriptionLabel = new Label();

            speedSlider = new Slider();
            speedSlider.setMax(200);
            speedSlider.setMin(0);
            speedSlider.setPrefWidth(200);
            speedSlider.setValue(50);
            speedSlider.setOnMouseDragged(event -> setSpeed());
            speedSlider.setOnMouseClicked(event -> setSpeed());
            speedLabel.setText(" Delay time:  50  ms ");

            labelList.add(countClosedSet);
            labelList.add(countOpenSet);
            labelList.add(countPathSet);
            labelList.add(status);

            root.setCenter(content);

            HBox toolbar = new HBox();
            toolbar.setAlignment(Pos.CENTER);
            toolbar.setPadding(new Insets(10, 10, 10, 10));
            toolbar.setSpacing(4);

            findPathButton = new JFXButton("FIND PATH");
            exit = new JFXButton("EXIT");
            exit.setAlignment(Pos.TOP_RIGHT);


            menuBar = new MenuBar();
            menuBar.setPrefWidth(500);
            algorithms = new Menu("Algorithms");
            grid_action = new Menu("Grid Action");
            attributes = new Menu("Attributes");
            simpleObstacles = new Menu("Simple Obstacles");
            savedMaze = new Menu("Saved Maze");
            populateSavedMaze();


            aStarCheckMenuItem = new CheckMenuItem("A* Algorithm");
            dijkstraCheckMenuItem = new CheckMenuItem("Dijkstra's Algorithm");
            breadthFirstSearchCheckMenuItem = new CheckMenuItem("Breadth First Search Algorithm");
            greedyBestFirstSearchCheckMenuItem = new CheckMenuItem("Greedy Best First Search Algorithm");
            depthFirstSearchPathFinderCheckMenuItem = new CheckMenuItem("Depth First Search Algorithm");

            restGridMenuItem = new MenuItem("Reset Grid");
            clearGridMenuItem = new MenuItem("Clear Grid");
            saveMazeMenuItem = new MenuItem("Save Maze");


            simpleVerticalMazeMenuItem = new MenuItem("Simple Vertical Maze");
            multipleVerticalBarsMenuItem = new MenuItem("Multiple Vertical Bars");
            multipleHorizontalBarsMenuItem = new MenuItem("Multiple Horizontal Bars");
            singleVerticalBarMenuItem = new MenuItem("Single Vertical Bar");
            singleHorizontalBarMenuItem = new MenuItem("Single Horizontal Bar");
            simpleHorizontalMazeMenuItem = new MenuItem("Simple Horizontal Maze");
            simpleStaircaseBarMenuItem = new MenuItem("Simple Staircase Bar");


            allowDiagonalCheckMenuItem = new CheckMenuItem("Allow Diagonals");
            allowDiagonalCheckMenuItem.setSelected(allowDiagonalsProperty);
            stepViewCheckMenuItem = new CheckMenuItem("View Steps");
            stepViewCheckMenuItem.setSelected(stepViewProperty);
            showPathCheckMenuItem = new CheckMenuItem("Show Path");
            showPathCheckMenuItem.setSelected(showPathProperty);

            findPathButton.setOnAction(eventEventHandler);
            aStarCheckMenuItem.setOnAction(eventEventHandler);
            dijkstraCheckMenuItem.setOnAction(eventEventHandler);
            breadthFirstSearchCheckMenuItem.setOnAction(eventEventHandler);
            greedyBestFirstSearchCheckMenuItem.setOnAction(eventEventHandler);
            depthFirstSearchPathFinderCheckMenuItem.setOnAction(eventEventHandler);
            restGridMenuItem.setOnAction(eventEventHandler);
            clearGridMenuItem.setOnAction(eventEventHandler);
            saveMazeMenuItem.setOnAction(eventEventHandler);
            allowDiagonalCheckMenuItem.setOnAction(eventEventHandler);
            stepViewCheckMenuItem.setOnAction(eventEventHandler);
            showPathCheckMenuItem.setOnAction(eventEventHandler);
            simpleVerticalMazeMenuItem.setOnAction(eventEventHandler);
            multipleVerticalBarsMenuItem.setOnAction(eventEventHandler);
            multipleHorizontalBarsMenuItem.setOnAction(eventEventHandler);
            singleVerticalBarMenuItem.setOnAction(eventEventHandler);
            singleHorizontalBarMenuItem.setOnAction(eventEventHandler);
            simpleHorizontalMazeMenuItem.setOnAction(eventEventHandler);
            simpleStaircaseBarMenuItem.setOnAction(eventEventHandler);
            exit.setOnAction(eventEventHandler);

            algorithms.getItems().addAll(aStarCheckMenuItem, dijkstraCheckMenuItem, breadthFirstSearchCheckMenuItem, greedyBestFirstSearchCheckMenuItem, depthFirstSearchPathFinderCheckMenuItem);
            attributes.getItems().addAll(allowDiagonalCheckMenuItem, stepViewCheckMenuItem, showPathCheckMenuItem);
            grid_action.getItems().addAll(restGridMenuItem, clearGridMenuItem, saveMazeMenuItem);
            simpleObstacles.getItems().addAll(simpleVerticalMazeMenuItem, simpleHorizontalMazeMenuItem, multipleVerticalBarsMenuItem, multipleHorizontalBarsMenuItem, singleVerticalBarMenuItem, singleHorizontalBarMenuItem, simpleStaircaseBarMenuItem);

            menuBar.getMenus().addAll(algorithms, simpleObstacles, grid_action, savedMaze, attributes);
            HBox topMenu = new HBox();
            topMenu.getChildren().addAll(menuBar, findPathButton, speedSlider, speedLabel, countClosedSet, countOpenSet, countPathSet, exit);
            VBox menu = new VBox();
            menu.setAlignment(Pos.CENTER);
            menu.getChildren().addAll(topMenu, descriptionLabel);


            toolbar.setAlignment(Pos.CENTER_LEFT);
            toolbar.getChildren().addAll(menu);
            root.setTop(toolbar);

            HBox statusBar = new HBox();

            statusBar.getChildren().add(status);

            Label infoText = new Label("Left mouse button = draw obstacles, right mouse button = remove obstacles. Move start and end cells via dragging.");
            infoText.setAlignment(Pos.CENTER_RIGHT);
            infoText.setMaxWidth(Long.MAX_VALUE);

            lblSplashMessage = new Label();
            infoText.setAlignment(Pos.CENTER);
            Runnable runnable = () -> {
                try {
                    while (!interrupted()) {
                        String[] splashMessage = {"Designed and Developed by Rahul Puchula.", "Algorithms look awesome when visualized", "v2022.1", "Pathfinder Visualizer Software"};
                        int steps = 0;
                        while (steps <= 150) {
                            switch (steps) {
                                case 0:
                                    Platform.runLater(() -> {
                                        new BounceIn(lblSplashMessage).play();
                                        lblSplashMessage.setText(splashMessage[0]);
                                    });
                                    break;
                                case 25:
                                    Platform.runLater(() -> {
                                        new BounceIn(lblSplashMessage).play();
                                        lblSplashMessage.setText(splashMessage[1]);
                                    });
                                    break;
                                case 50:
                                    Platform.runLater(() -> {
                                        new BounceIn(lblSplashMessage).play();
                                        lblSplashMessage.setText(splashMessage[2]);
                                    });
                                    break;
                                case 75:
                                    Platform.runLater(() -> {
                                        new BounceIn(lblSplashMessage).play();
                                        lblSplashMessage.setText(splashMessage[3]);
                                    });
                                    break;

                            }
                            sleep(100);
                            ++steps;
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            };
            new Thread(runnable).start();
            HBox.setHgrow(infoText, Priority.ALWAYS);
            statusBar.getChildren().add(infoText);
            statusBar.getChildren().add(lblSplashMessage);
            root.setBottom(statusBar);

            menuBar.getStyleClass().add("toolbar");
            findPathButton.getStyleClass().add("btnFindPath");
            toolbar.getStyleClass().add("toolbar");
            algorithms.getStyleClass().add("toolbar");
            attributes.getStyleClass().add("toolbar");
            grid_action.getStyleClass().add("toolbar");
            simpleObstacles.getStyleClass().add("toolbar");
            speedLabel.getStyleClass().add("speedLabel");
            descriptionLabel.getStyleClass().add("descriptionLabel");
            infoText.getStyleClass().add("footer-label");
            lblSplashMessage.getStyleClass().add("footer-label");
            status.getStyleClass().add("footer-label");
            statusBar.getStyleClass().add("footer");
            exit.getStyleClass().add("btnExit");


            Scene scene = new Scene(root, Settings.SCENE_WIDTH, Settings.SCENE_HEIGHT);
            scene.getStylesheets().add(getClass().getResource("../css/application.css").toExternalForm());
            primaryStage.getIcons().add(new Image(getClass().getResource("../_gallery/PV.ico").toExternalForm()));
            primaryStage.setScene(scene);
            toolbar.setOnMousePressed(event2 -> {
                xOffset = event2.getSceneX();
                yOffset = event2.getSceneY();
            });
            toolbar.setOnMouseDragged(event1 -> {
                primaryStage.setX(event1.getScreenX() - xOffset);
                primaryStage.setY(event1.getScreenY() - yOffset);
            });
            content.setOpacity(0);
            setUpGrid(content, root);
            new FadeIn(content).setSpeed(1).play();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setUpGrid(StackPane content, BorderPane root) {
        grid = new Grid(Settings.GRID_COLUMNS, Settings.GRID_ROWS, content.getBoundsInParent().getWidth(), content.getBoundsInParent().getHeight());

        for (int row = 0; row < Settings.GRID_ROWS; row++) {
            for (int column = 0; column < Settings.GRID_COLUMNS; column++) {

                String text = "";
                Cell cell = new Cell(text, column, row, CellType.TRAVERSABLE);
                mousePaintGestures.makePaintable(cell);

                grid.add(cell, column, row);

            }
        }

        root.setCenter(grid);

        startCell = new Cell("Start", 0, 0, CellType.TRAVERSABLE);
        endCell = new Cell("End", Settings.GRID_COLUMNS - 1, Settings.GRID_ROWS - 1, CellType.TRAVERSABLE);

        startCell.getStyleClass().add("start");
        endCell.getStyleClass().add("goal");

        startCell.toFront();
        endCell.toFront();

        MouseDragGestures mouseDragGestures = new MouseDragGestures(grid);
        mouseDragGestures.makeDraggable(startCell);
        mouseDragGestures.makeDraggable(endCell);

        grid.addOverlay(startCell, 0, 0);
        grid.addOverlay(endCell, Settings.GRID_COLUMNS - 1, Settings.GRID_ROWS - 1);
    }

    private void populateSavedMaze() {
        File mazeDir = new File(BASE_DIR);
        if (mazeDir.exists()) {
            String[] jsonMazes = mazeDir.list((file1, s) -> new File(file1, s).isFile());
            assert jsonMazes != null;
            if (jsonMazes.length > 0) {
                savedMaze.getItems().remove(0, savedMaze.getItems().size());
                for (String jsonMaze : jsonMazes) {
                    MenuItem savedMazeMenuItem = new MenuItem(stringSplitter(jsonMaze));
                    savedMazeMenuItem.setOnAction(event -> {
                        File mazeFile = new File(System.getProperty("user.dir").concat("\\_savedMaze\\" + jsonMaze));
                        try {
                            new MazeSaver(grid, labelList).displayJsonMaze(mazeFile);
                            new GridAction().notify("Maze Retrieved Successfully").showInformation();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    });
                    savedMaze.getItems().add(savedMazeMenuItem);
                }
            } else {
                MenuItem savedMazeMenuItem = new MenuItem("No Saved Maze");
                savedMazeMenuItem.setDisable(true);
                savedMaze.getItems().add(savedMazeMenuItem);
            }
        } else {
            MenuItem savedMazeMenuItem = new MenuItem("No Saved Maze");
            savedMazeMenuItem.setDisable(true);
            savedMaze.getItems().add(savedMazeMenuItem);
        }
    }

    private String stringSplitter(String s) {
        String[] arr = s.split("\\.");
        return arr[0];
    }

    private void clearGrid() {
        findPathButton.setText("FIND PATH");
        algorithms.setVisible(true);
        savedMaze.setVisible(true);
        allowDiagonalCheckMenuItem.setVisible(true);
        new GridAction(grid, labelList).clearGrid();
    }

    private void resetGrid() {
        findPathButton.setText("FIND PATH");
        algorithms.setVisible(true);
        savedMaze.setVisible(true);
        allowDiagonalCheckMenuItem.setVisible(true);
        new GridAction(grid, labelList).resetGrid();
    }

    private void setSpeed() {
        int delayTime = (int) speedSlider.getValue();
        speedSlider.setValue(delayTime);
        timer.setDelay(delayTime);
        speedLabel.setText(" Delay time: " + delayTime + " ms ");
    }

    EventHandler<javafx.event.ActionEvent> eventEventHandler = event -> {
        if (event.getSource() == findPathButton) {
            new BounceIn(findPathButton).play();
            switch (findPathButton.getText()) {
                case "FIND PATH":
                    if (!timer.isRunning()) {
                        resetGrid();
                        if (aStarAlgorithm) {
                            aStarPathFinder = new AStarPathFinder();
                            findPathButton.setText("PAUSE");
                            aStarPathFinder.setup(grid, startCell, endCell, labelList);
                            algorithms.setVisible(false);
                            savedMaze.setVisible(false);
                            allowDiagonalCheckMenuItem.setVisible(false);
                            setSpeed();
                            timer.start();
                        } else if (dijkstrasAlgorithm) {
                            dijkstrasPathFinder = new DijkstrasPathFinder();
                            findPathButton.setText("PAUSE");
                            dijkstrasPathFinder.setup(grid, startCell, endCell, labelList);
                            algorithms.setVisible(false);
                            savedMaze.setVisible(false);
                            allowDiagonalCheckMenuItem.setVisible(false);
                            setSpeed();
                            timer.start();
                        } else if (breadthFirstSearchAlgorithm) {
                            breadthFirstSearchPathFinder = new BreadthFirstSearchPathFinder();
                            findPathButton.setText("PAUSE");
                            breadthFirstSearchPathFinder.setup(grid, startCell, endCell, labelList);
                            algorithms.setVisible(false);
                            savedMaze.setVisible(false);
                            allowDiagonalCheckMenuItem.setVisible(false);
                            setSpeed();
                            timer.start();
                        } else if (greedyBestFirstSearchAlgorithm) {
                            greedyBestFirstSearch = new GreedyBestFirstSearch();
                            findPathButton.setText("PAUSE");
                            greedyBestFirstSearch.setup(grid, startCell, endCell, labelList, allowDiagonalsProperty);
                            algorithms.setVisible(false);
                            savedMaze.setVisible(false);
                            allowDiagonalCheckMenuItem.setVisible(false);
                            setSpeed();
                            timer.start();
                        } else if (depthFirstSearchPathFinderAlgorithm) {
                            depthFirstSearchPathFinder = new DepthFirstSearchPathFinder();
                            findPathButton.setText("PAUSE");
                            depthFirstSearchPathFinder.setup(grid, startCell, endCell, labelList, allowDiagonalsProperty);
                            algorithms.setVisible(false);
                            savedMaze.setVisible(false);
                            allowDiagonalCheckMenuItem.setVisible(false);
                            setSpeed();
                            timer.start();
                        } else {
                            new GridAction().notify("SORRY::PLEASE SELECT AT LEAST ONE ALGORITHM").showError();
                            new Shake(menuBar).play();

                        }
                    }
                    break;
                case "PAUSE":

                    if (timer.isRunning()) {
                        findPathButton.setText("RESUME");
                        timer.stop();
                    }
                    break;
                case "RESUME":

                    if (!timer.isRunning()) {
                        findPathButton.setText("PAUSE");
                        timer.start();
                    }
                    break;
            }
        } else if (event.getSource() == clearGridMenuItem) {
            if (!timer.isRunning()) {
                clearGrid();
            }
        } else if (event.getSource() == saveMazeMenuItem) {
            if (!timer.isRunning()) {
                try {
                    new MazeSaver(grid, labelList).createJsonMaze(savedMaze);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (event.getSource() == simpleVerticalMazeMenuItem) {
            if (!timer.isRunning()) {
                new GridAction(grid, labelList).simpleVerticalMaze();
            }
        } else if (event.getSource() == multipleVerticalBarsMenuItem) {
            if (!timer.isRunning()) {
                new GridAction(grid, labelList).multipleVerticalBars();
            }
        } else if (event.getSource() == multipleHorizontalBarsMenuItem) {
            if (!timer.isRunning()) {
                new GridAction(grid, labelList).multipleHorizontalBars();
            }
        } else if (event.getSource() == singleVerticalBarMenuItem) {
            if (!timer.isRunning()) {
                new GridAction(grid, labelList).singleVerticalBar();
            }
        } else if (event.getSource() == singleHorizontalBarMenuItem) {
            if (!timer.isRunning()) {
                new GridAction(grid, labelList).singleHorizontalBar();
            }
        } else if (event.getSource() == simpleHorizontalMazeMenuItem) {
            if (!timer.isRunning()) {
                new GridAction(grid, labelList).simpleHorizontalMaze();
            }
        } else if (event.getSource() == simpleStaircaseBarMenuItem) {
            if (!timer.isRunning()) {
                new GridAction(grid, labelList).simpleStairCaseMaze();
            }
        } else if (event.getSource() == restGridMenuItem) {
            if (!timer.isRunning()) {
                resetGrid();
            }
        } else if (event.getSource() == showPathCheckMenuItem) {
            showPathProperty = showPathCheckMenuItem.isSelected();

        } else if (event.getSource() == allowDiagonalCheckMenuItem) {
            allowDiagonalsProperty = allowDiagonalCheckMenuItem.isSelected();

        } else if (event.getSource() == stepViewCheckMenuItem) {
            stepViewProperty = stepViewCheckMenuItem.isSelected();

        } else if (event.getSource() == aStarCheckMenuItem) {
            aStarAlgorithm = aStarCheckMenuItem.isSelected();
            dijkstrasAlgorithm = false;
            dijkstraCheckMenuItem.setSelected(false);
            breadthFirstSearchAlgorithm = false;
            breadthFirstSearchCheckMenuItem.setSelected(false);
            greedyBestFirstSearchAlgorithm = false;
            greedyBestFirstSearchCheckMenuItem.setSelected(false);
            depthFirstSearchPathFinderAlgorithm = false;
            depthFirstSearchPathFinderCheckMenuItem.setSelected(false);
            descriptionLabel.setText("A* Algorithm is a greedy controller.algorithm, it's weighted, guarantees the shortest path,it's informed");
            new BounceIn(descriptionLabel).play();
        } else if (event.getSource() == dijkstraCheckMenuItem) {
            dijkstrasAlgorithm = dijkstraCheckMenuItem.isSelected();
            aStarAlgorithm = false;
            aStarCheckMenuItem.setSelected(false);
            breadthFirstSearchAlgorithm = false;
            breadthFirstSearchCheckMenuItem.setSelected(false);
            greedyBestFirstSearchAlgorithm = false;
            greedyBestFirstSearchCheckMenuItem.setSelected(false);
            depthFirstSearchPathFinderAlgorithm = false;
            depthFirstSearchPathFinderCheckMenuItem.setSelected(false);
            descriptionLabel.setText("Dijkstra's Algorithm is a greedy controller.algorithm, it's weighted, guarantees the shortest path,it's not informed");
            new BounceIn(descriptionLabel).play();
        } else if (event.getSource() == breadthFirstSearchCheckMenuItem) {
            breadthFirstSearchAlgorithm = breadthFirstSearchCheckMenuItem.isSelected();
            aStarAlgorithm = false;
            aStarCheckMenuItem.setSelected(false);
            dijkstrasAlgorithm = false;
            dijkstraCheckMenuItem.setSelected(false);
            greedyBestFirstSearchAlgorithm = false;
            greedyBestFirstSearchCheckMenuItem.setSelected(false);
            depthFirstSearchPathFinderAlgorithm = false;
            depthFirstSearchPathFinderCheckMenuItem.setSelected(false);
            descriptionLabel.setText("Breadth First Search Algorithm is not a greedy controller.algorithm, it's not weighted, guarantees no shortest path,it's not informed");
            new BounceIn(descriptionLabel).play();
        } else if (event.getSource() == greedyBestFirstSearchCheckMenuItem) {
            greedyBestFirstSearchAlgorithm = greedyBestFirstSearchCheckMenuItem.isSelected();
            aStarAlgorithm = false;
            aStarCheckMenuItem.setSelected(false);
            dijkstrasAlgorithm = false;
            dijkstraCheckMenuItem.setSelected(false);
            breadthFirstSearchAlgorithm = false;
            breadthFirstSearchCheckMenuItem.setSelected(false);
            depthFirstSearchPathFinderAlgorithm = false;
            depthFirstSearchPathFinderCheckMenuItem.setSelected(false);
            descriptionLabel.setText("Greedy Best First Search is a greedy controller.algorithm, it's weighted, guarantees the shortest path,it's informed");
            new BounceIn(descriptionLabel).play();
        } else if (event.getSource() == depthFirstSearchPathFinderCheckMenuItem) {
            depthFirstSearchPathFinderAlgorithm = depthFirstSearchPathFinderCheckMenuItem.isSelected();
            aStarAlgorithm = false;
            aStarCheckMenuItem.setSelected(false);
            dijkstrasAlgorithm = false;
            dijkstraCheckMenuItem.setSelected(false);
            breadthFirstSearchAlgorithm = false;
            breadthFirstSearchCheckMenuItem.setSelected(false);
            greedyBestFirstSearchAlgorithm = false;
            greedyBestFirstSearchCheckMenuItem.setSelected(false);
            descriptionLabel.setText("Depth First Search Algorithm is not a greedy controller.algorithm, it's not weighted, guarantees no shortest path,it's not informed");
            new BounceIn(descriptionLabel).play();
        } else if (event.getSource() == exit) {
            new GridAction().notify("CLICK ME TO EXIT")
                    .hideAfter(Duration.seconds(5))
                    .onAction(new EventHandler<javafx.event.ActionEvent>() {
                        @Override
                        public void handle(javafx.event.ActionEvent event) {
                            Stage stage = (Stage) exit.getScene().getWindow();
                            stage.close();
                            System.exit(0);
                        }
                    })
                    .darkStyle()
                    .showConfirm();

        }
    };

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (timer.isRunning()) {
            if (!dijkstrasPathFinder.isRunning() && dijkstrasAlgorithm) {
                dijkstrasPathFinder.getPath(allowDiagonalsProperty, stepViewProperty, showPathProperty);
            } else if (!aStarPathFinder.isRunning() && aStarAlgorithm) {
                aStarPathFinder.getPath(allowDiagonalsProperty, stepViewProperty, showPathProperty);
            } else if (!breadthFirstSearchPathFinder.isRunning() && breadthFirstSearchAlgorithm) {
                breadthFirstSearchPathFinder.getPath(allowDiagonalsProperty, stepViewProperty, showPathProperty);
            } else if (!greedyBestFirstSearch.isRunning() && greedyBestFirstSearchAlgorithm) {
                greedyBestFirstSearch.getPath(allowDiagonalsProperty, stepViewProperty, showPathProperty);
            } else if (!depthFirstSearchPathFinder.isRunning() && depthFirstSearchPathFinderAlgorithm) {
                depthFirstSearchPathFinder.getPath(stepViewProperty, showPathProperty);
            } else {
                Platform.runLater(() -> {
                    findPathButton.setText("FIND PATH");
                    algorithms.setVisible(true);
                    savedMaze.setVisible(true);
                    allowDiagonalCheckMenuItem.setVisible(true);
                });
                timer.stop();
            }
        }


    }

}

package utils;

import model.Cell;
import model.CellType;
import model.Grid;
import controller.grid.GridAction;
import javafx.scene.control.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.util.List;
import java.util.Optional;

public class MazeSaver {
    private Grid grid;
    private Menu savedMaze;
    private List<Label>labelList;
    private JSONArray jsonMaze;
    String BASE_DIR=System.getProperty("user.dir").concat("\\_savedMaze\\");
    public MazeSaver(Grid grid, List<Label>labelList) throws Exception {
        this.labelList=labelList;
        this.grid = grid;
        jsonMaze=new JSONArray();
    }

    public MazeSaver() {
    }

    public void createJsonMaze(Menu savedMaze ) throws Exception {
        this.savedMaze=savedMaze;
        for (int row = 0; row < Settings.GRID_ROWS; row++) {
            for (int column = 0; column < Settings.GRID_COLUMNS; column++) {
                Cell cell = grid.getCell(column, row);
                JSONObject cellObject=new JSONObject();
                if (!(cell.isTraversable())) {
                    cellObject.put("col",String.valueOf(cell.getColumn()));
                    cellObject.put("row",String.valueOf(cell.getRow()));
                    jsonMaze.add(cellObject);
                }
            }
        }
        if (!jsonMaze.isEmpty()){
            String mazeName= create_new_name_of_custom_maze();
            if (mazeName!=null){
                if (!mazeName.contains(" ")){
                    File mazeDir = new File(BASE_DIR);
                    if (mazeDir.exists()){
                        File MazeFile = new File(BASE_DIR+mazeName+".json");
                        write(jsonMaze,MazeFile);
                        new GridAction().notify("Maze Saved Successfully").showInformation();
                    }else{
                        if (mazeDir.mkdir()){
                            File MazeFile = new File(BASE_DIR+mazeName+".json");
                            write(jsonMaze,MazeFile);
                            new GridAction().notify("Maze Saved Successfully").showInformation();
                        }
                    }
                }else {
                    new GridAction().notify("SORRY invalid name should be one word or separated with underscore(_)").showError();
                }
            }else {
                new GridAction().notify("SORRY NO MAZE NAME PROVIDED").showError();
            }
            populateSavedMaze();
        }else{
            new GridAction().notify("SORRY AT LEAST CREATE A MAZE").showError();
        }
    }

    private JSONArray read(File file) throws Exception {
        Reader reader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String jsonLine = "";
        StringBuilder jsonArrayString = new StringBuilder();
        while ((jsonLine = bufferedReader.readLine()) != null) {
            jsonArrayString.append(jsonLine);
        }
        JSONArray jsonArray = (JSONArray) new JSONParser().parse(String.valueOf(jsonArrayString));
        reader.close();
        return jsonArray;
    }

    private void write(JSONArray jsonArray, File file) throws Exception {
        file.setWritable(true);
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(jsonArray.toJSONString());
        fileWriter.flush();
        fileWriter.close();
        file.setReadOnly();
    }

    public void displayJsonMaze(File file) throws Exception {

        new GridAction(grid,labelList).clearGrid();
        jsonMaze=read(file);
        for (Object jsonObject:jsonMaze) {
            JSONObject cellObject= (JSONObject) jsonObject;
            grid.getCell((Integer.parseInt((String)cellObject.get("col"))),(Integer.parseInt((String)cellObject.get("row")))).setType(CellType.OBSTACLE);
        }
    }

    private void populateSavedMaze() {
        File mazeDir = new File(BASE_DIR);
        if (mazeDir.exists()){
            String[] jsonMazes = mazeDir.list((file1, s) -> new File(file1, s).isFile());
            assert jsonMazes != null;
            if (jsonMazes.length > 0) {
                savedMaze.getItems().remove(0,savedMaze.getItems().size());
                for (String jsonMaze : jsonMazes) {
                    MenuItem savedMazeMenuItem=new MenuItem(stringSplitter(jsonMaze));
                    savedMazeMenuItem.setOnAction(event -> {
                        File mazeFile = new File(BASE_DIR+jsonMaze);
                        try {
                            new MazeSaver(grid,labelList).displayJsonMaze(mazeFile);
                            new GridAction().notify("Maze Retrieved Successfully").showInformation();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    });
                    savedMaze.getItems().add(savedMazeMenuItem);
                }
            }else{
                MenuItem savedMazeMenuItem=new MenuItem("No Saved Maze");
                savedMazeMenuItem.setDisable(true);
                savedMaze.getItems().add(savedMazeMenuItem);
            }
        }else{
            MenuItem savedMazeMenuItem=new MenuItem("No Saved Maze");
            savedMazeMenuItem.setDisable(true);
            savedMaze.getItems().add(savedMazeMenuItem);
        }
    }

    private String stringSplitter(String s) {
        String[] arr = s.split("\\.");
        return arr[0];
    }

    private String create_new_name_of_custom_maze() {
        TextInputDialog textInputDialog = new TextInputDialog(null);
        textInputDialog.setTitle("Save Custom Maze");
        textInputDialog.setHeaderText("Type your desired maze name below");
        textInputDialog.setContentText("Maze name should be totally unique!");
        Optional<String> userChoice = textInputDialog.showAndWait();
        return userChoice.orElse(null);
    }

    public boolean getSettings() throws Exception {
        File settingFile = new File(BASE_DIR+"setting.json");
        if (settingFile.exists()){
            JSONArray settingJSONArray=read(settingFile);
            JSONObject settingJSONObject= (JSONObject) settingJSONArray.get(0);
            return (boolean) settingJSONObject.get("fullScreen");
        }else{
            return false;
        }
    }

    public void setSettings(boolean settingBoolean) throws Exception {
        File mazeDir = new File(BASE_DIR);
        if (mazeDir.exists()){
            File settingFile = new File(BASE_DIR+"setting.json");
            JSONObject settingJSONObject=new JSONObject();
            settingJSONObject.put("fullScreen",settingBoolean);
            JSONArray settingJSONArray=new JSONArray();
            settingJSONArray.add(settingJSONObject);
            write(settingJSONArray,settingFile);
        }else{
            if (mazeDir.mkdir()){
                File settingFile = new File(BASE_DIR+"setting.json");
                JSONObject settingJSONObject=new JSONObject();
                settingJSONObject.put("fullScreen",settingBoolean);
                JSONArray settingJSONArray=new JSONArray();
                settingJSONArray.add(settingJSONObject);
                write(settingJSONArray,settingFile);
            }
        }

    }

}

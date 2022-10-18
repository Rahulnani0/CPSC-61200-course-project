package view;

import animatefx.animation.BounceIn;
import animatefx.animation.FadeOut;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

import static java.lang.Thread.sleep;

public class Splash implements Initializable {

    @FXML
    private AnchorPane anchorFirst;
    @FXML
    private Label lblSplashMessage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Runnable runnable = () -> {
            try {
                String[] splashMessage = {"WELCOME TO", "PATHFINDER VISUALIZER", "© ALL RIGHTS RESERVED", "PREPARING WORKSPACE..."};
                int steps = 0;
                while (steps <= 100) {
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
                Platform.runLater(() -> {
                    new FadeOut(anchorFirst).play();
                    Stage stage = (Stage) anchorFirst.getScene().getWindow();
                    new Main().display(stage);
                });

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
        new Thread(runnable).start();
    }
}

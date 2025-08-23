package us.whitedev.gui.clickgui.components.modules;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;
import org.jetbrains.annotations.NotNull;
import us.whitedev.gui.clickgui.utils.StyleUtil;
import us.whitedev.proxy.functions.crashers.CrashHandler;
import us.whitedev.proxy.repository.OptionsRep;
import us.whitedev.proxy.utils.LogType;
import us.whitedev.proxy.utils.ProxyLogger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class BotConfigRenderer {
    private final OptionsRep optionsRep = OptionsRep.getInstance();
    private final HashMap<Button, Boolean> booleanMap = new HashMap<>();
    private Timeline timeline;
    private final CrashHandler crashHandler = CrashHandler.getInstance();
    private StringBuilder builder = new StringBuilder();
    private final String commonButtonStyle = "-fx-background-radius: 15 15 15 15;" +
            "-fx-background-color: rgba(25, 25, 25);" +
            "-fx-font-size: 14; " +
            "-fx-font-weight: bold; " +
            "-fx-text-fill: white;";
    private String commonButtonStyleHover = "-fx-background-radius: 15 15 15 15;" +
            "-fx-background-color: rgba(25, 25, 25);" +
            "-fx-font-size: 14; " +
            "-fx-font-weight: bold; " +
            "-fx-text-fill: rgba(0, 255, 246);";

    public void renderModule(VBox vBox, String name, Object[] settings, String[] types, boolean isViasble) {
        renderTitle(vBox, name);
        renderOptions(vBox, name, settings, types, isViasble);
        renderButtons(vBox, name, settings, types, isViasble);
    }

    private void renderTitle(VBox vBox, String name) {
        Label title = new Label(name + " - Crash Config");
        title.setStyle("-fx-font-size: 27; " +
                "-fx-font-weight: bold; " +
                "-fx-text-fill: rgba(255, 255, 255);" +
                "-fx-padding: 15 0 0 15;");
        vBox.getChildren().add(title);

        Line line = new Line();
        line.setStartX(0.0f);
        line.setEndX(700.0f);
        line.setStrokeWidth(3.0f);
        line.setStroke(LinearGradient.valueOf("from 0% 0% to 100% 0%, white 0%, transparent 100%"));
        vBox.getChildren().add(line);
    }

    private void renderOptions(VBox vBox, String name, Object[] settings, String[] types, boolean isViasble) {
        TextFlow textFlow = new TextFlow();
        Text text1 = new Text("Description: ");
        String description = crashHandler.getCrashClassByName(name).get().getDescription();
        Text text2 = new Text(isViasble ? description : "*".repeat(description.length()));
        text1.setFill(Color.color(0.6, 0.6, 0.6));
        text1.setStyle("-fx-font-size: 16; " +
                "-fx-font-weight: bold;");
        text2.setStyle("-fx-font-size: 16; " +
                "-fx-font-weight: bold;");
        text2.setFill(Color.color(1, 1, 1));
        textFlow.getChildren().addAll(text1, text2);
        textFlow.setStyle("-fx-padding: 15 10 10 15;");
        vBox.getChildren().add(textFlow);
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setHgap(10);
        grid.setVgap(10);
        int index = 0;
        for (Object option : settings) {
            String type = types[index];
            switch (type) {
                case "int", "string" -> {
                    Label label = new Label(isViasble ? option.toString() : "*********");
                    TextField textField = new TextField();
                    textField.setId(String.valueOf(index));
                    textField.setPrefWidth(350);
                    textField.setStyle("-fx-background-radius: 15 15 15 15;" +
                            "-fx-background-color: rgba(25, 25, 25);" +
                            "-fx-font-size: 14; " +
                            "-fx-font-weight: bold; " +
                            "-fx-text-fill: white;");
                    label.setStyle("-fx-font-size: 16; " +
                            "-fx-font-weight: bold;" +
                            "-fx-text-fill: white;");
                    grid.add(label, index % 3, index / 3 * 2);
                    grid.add(textField, index % 3, index / 3 * 2 + 1);
                    ++index;
                }
                case "boolean" -> {
                    Label label = new Label(isViasble ? option.toString() : "*********");
                    Button button = new Button("Disabled ✖");
                    button.setId(String.valueOf(index));
                    booleanMap.put(button, false);
                    button.setPrefWidth(350);
                    button.setStyle("-fx-background-radius: 15 15 15 15;" +
                            "-fx-background-color: rgba(25, 25, 25);" +
                            "-fx-font-size: 14; " +
                            "-fx-font-weight: bold; " +
                            "-fx-text-fill: red;");
                    label.setStyle("-fx-font-size: 16; " +
                            "-fx-font-weight: bold;" +
                            "-fx-text-fill: white;");
                    button.setOnMouseClicked(e -> {
                        booleanMap.replace(button, !booleanMap.get(button));
                        button.setText((booleanMap.get(button) ? "Enabled ✔" : "Disabled ✖"));
                        button.setStyle("-fx-background-radius: 15 15 15 15;" +
                                "-fx-background-color: rgba(25, 25, 25);" +
                                "-fx-font-size: 14; " +
                                "-fx-font-weight: bold; " +
                                (booleanMap.get(button) ? "-fx-text-fill: green;" : "-fx-text-fill: red;"));
                    });
                    grid.add(label, index % 3, index / 3 * 2);
                    grid.add(button, index % 3, index / 3 * 2 + 1);
                    ++index;
                }
                case "list" -> {
                    String[] parts = ((String) option).split("\\[", 2);
                    String mode = parts[0];
                    String[] items = parts[1].substring(0, parts[1].length() - 1).split(", ");
                    ArrayList<String> list = new ArrayList<>(Arrays.asList(items));
                    Label label = new Label(isViasble ? mode : "*********");
                    ChoiceBox<String> choiceBox = new ChoiceBox<>();
                    choiceBox.setId(String.valueOf(index));
                    choiceBox.getItems().addAll(list);
                    choiceBox.setPrefWidth(350);
                    label.setStyle("-fx-font-size: 16; " +
                            "-fx-font-weight: bold;" +
                            "-fx-text-fill: white;");
                    grid.add(label, index % 3, index / 3 * 2);
                    grid.add(choiceBox, index % 3, index / 3 * 2 + 1);
                    ++index;
                }
            }
        }
        vBox.getChildren().add(grid);
    }

    private void renderButtons(VBox vBox, String name, Object[] settings, String[] types, boolean isVisable) {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 30));
        grid.setHgap(10);
        grid.setVgap(10);
        Button saveConfig = getButton(vBox, name);
        Button toggle = new Button("Toggle Visibility");
        toggle.setPrefWidth(200);
        toggle.setStyle(commonButtonStyle);
        toggle.setOnMouseEntered(e -> {
            timeline = new Timeline(new KeyFrame(Duration.millis(10), e2 -> {
                toggle.setStyle(commonButtonStyleHover);
            }));
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();
        });
        toggle.setOnMouseExited(e -> {
            timeline.stop();
            toggle.setStyle(commonButtonStyle);
        });
        toggle.setOnMouseClicked(e -> {
            vBox.getChildren().clear();
            renderModule(vBox, name, settings, types, !isVisable);
        });

        GridPane.setMargin(toggle, new Insets(0, 10, 0, 90));
        GridPane.setMargin(saveConfig, new Insets(0, 110, 0, 10));
        grid.add(toggle, 0, 0);
        grid.add(saveConfig, 1, 0);
        initTimer();
        vBox.getChildren().add(grid);
    }

    private @NotNull Button getButton(VBox vBox, String name) {
        Button saveConfig = new Button("Save Config");
        saveConfig.setPrefWidth(200);
        saveConfig.setStyle(commonButtonStyle);
        saveConfig.setOnMouseEntered(e -> {
            timeline = new Timeline(new KeyFrame(Duration.millis(10), e2 -> {
                saveConfig.setStyle(commonButtonStyleHover);
            }));
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();
        });
        saveConfig.setOnMouseExited(e -> {
            timeline.stop();
            saveConfig.setStyle(commonButtonStyle);
        });
        saveConfig.setOnMouseClicked(e -> {
            for (Node elements : vBox.getChildren()) {
                if (elements instanceof GridPane gridPane) {
                    for (Node gridObj : gridPane.getChildren()) {
                        if (gridObj instanceof TextField textField && gridObj.getId() != null) {
                            if(textField.getText().isEmpty()) break;
                            builder.append(textField.getText()).append(" ");
                        } else if (gridObj instanceof Button button && gridObj.getId() != null) {
                            builder.append(booleanMap.get(button)).append(" ");
                        } else if (gridObj instanceof ChoiceBox choiceBox && gridObj.getId() != null) {
                            builder.append(choiceBox.getSelectionModel().getSelectedItem()).append(" ");
                        }
                    }
                    break;
                }
            }
            optionsRep.addToCrashMap(name, builder.toString().split(" "));
            builder = new StringBuilder();
            vBox.getChildren().clear();
            ProxyLogger.send(String.format("New config for %s was saved!", name), LogType.INFO);
            new BotsRenderer().renderOptions(vBox);
        });
        return saveConfig;
    }

    private void initTimer() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(10), e -> {
            commonButtonStyleHover = "-fx-background-radius: 15 15 15 15;" +
                    "-fx-background-color: rgba(25, 25, 25);" +
                    "-fx-font-size: 14; " +
                    "-fx-font-weight: bold; " +
                    "-fx-text-fill: " + StyleUtil.getFinalColor();
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
}

package us.whitedev.gui.clickgui.components.modules;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;
import org.jetbrains.annotations.NotNull;
import us.whitedev.crashers.CrashManager;
import us.whitedev.crashers.Crasher;
import us.whitedev.exploits.ExploitManager;
import us.whitedev.gui.clickgui.ClickGui;
import us.whitedev.gui.clickgui.utils.StyleUtil;
import us.whitedev.helpers.ConfigHelper;
import us.whitedev.helpers.MessageHelper;
import us.whitedev.utils.OptionType;
import us.whitedev.utils.OptionUtil;

import java.util.HashMap;
import java.util.List;

public class ModuleRenderer {

    private final HashMap<Button, Boolean> booleanMap = new HashMap<>();
    private Timeline timeline;
    private ModuleType moduleType;
    private StringBuilder builder = new StringBuilder();
    private final MessageHelper msgHelper = new MessageHelper();
    private final ModuleConfigRenderer moduleConfigRenderer = new ModuleConfigRenderer();
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

    public void renderModule(VBox vBox, String name, List<OptionUtil> settings, boolean isVisible, ModuleType moduleType) {
        renderTitle(vBox, name, isVisible, moduleType);
        renderOptions(vBox, name, settings, isVisible, moduleType);
        renderButtons(vBox, name, settings, isVisible, moduleType);
    }

    private void renderTitle(VBox vBox, String name, boolean isVisible, ModuleType moduleType) {
        this.moduleType = moduleType;
        Label title = new Label((isVisible ? name : "******") + (moduleType == ModuleType.CRASH ? " - Crash Method" : " - Exploit"));
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

    private void renderOptions(VBox vBox, String name, List<OptionUtil> settings, boolean isVisible, ModuleType moduleType) {
        TextFlow textFlow = new TextFlow();
        Text text1 = new Text("Description: ");
        Text text2;
        if (moduleType == ModuleType.CRASH) {
            String description = CrashManager.getManager().getCrashClassByName(name).get().getDescription();
            text2 = new Text(isVisible ? description : "*".repeat(description.length()));
        } else {
            String description = ExploitManager.getManager().getExploitClassByName(name).get().getDescription();
            text2 = new Text(isVisible ? ExploitManager.getManager().getExploitClassByName(name).get().getDescription() : "*".repeat(description.length()));
        }
        String[] configArgs = ConfigHelper.loadedConfigs.get(name);
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
        for (OptionUtil setting : settings) {
            addSettingToGrid(setting, index, isVisible, configArgs, grid);
            ++index;
        }
        vBox.getChildren().add(grid);
    }

    private void addSettingToGrid(OptionUtil setting, int index, boolean isVisible, String[] configArgs, GridPane grid) {
        Label label = createLabel(isVisible ? setting.name() : "*********");
        OptionType type = setting.type();

        switch (type) {
            case INTEGER, DOUBLE, STRING, MULTIPART -> {
                TextField textField = createTextField(configArgs, index);
                addComponentsToGrid(label, textField, index, grid);
            }
            case BOOLEAN -> {
                Button button = createToggleButton(configArgs, index);
                button.setOnMouseClicked(e -> toggleButtonState(button));
                addComponentsToGrid(label, button, index, grid);
            }
            case LIST -> {
                ChoiceBox<String> choiceBox = createChoiceBox(setting, configArgs, index);
                addComponentsToGrid(label, choiceBox, index, grid);
            }
        }
    }

    private Label createLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-text-fill: white;");
        return label;
    }

    private TextField createTextField(String[] configArgs, int index) {
        TextField textField = new TextField();
        if (configArgs != null) textField.setText(configArgs[index]);
        textField.setId(String.valueOf(index));
        textField.setPrefWidth(350);
        textField.setStyle("-fx-background-radius: 15; -fx-background-color: rgba(25, 25, 25); " +
                "-fx-font-size: 14; -fx-font-weight: bold; -fx-text-fill: white;");
        return textField;
    }

    private Button createToggleButton(String[] configArgs, int index) {
        boolean initialState = configArgs != null && configArgs[index].equals("true");
        Button button = new Button(initialState ? "Enabled ✔" : "Disabled ✖");
        button.setId(String.valueOf(index));
        button.setPrefWidth(350);
        updateButtonStyle(button, initialState);
        booleanMap.put(button, initialState);
        return button;
    }

    private void toggleButtonState(Button button) {
        boolean newState = !booleanMap.get(button);
        booleanMap.replace(button, newState);
        button.setText(newState ? "Enabled ✔" : "Disabled ✖");
        updateButtonStyle(button, newState);
    }

    private void updateButtonStyle(Button button, boolean enabled) {
        button.setStyle("-fx-background-radius: 15; -fx-background-color: rgba(25, 25, 25); " +
                "-fx-font-size: 14; -fx-font-weight: bold; " +
                (enabled ? "-fx-text-fill: green;" : "-fx-text-fill: red;"));
    }

    private ChoiceBox<String> createChoiceBox(OptionUtil setting, String[] configArgs, int index) {
        ChoiceBox<String> choiceBox = new ChoiceBox<>();
        choiceBox.getItems().addAll(setting.args());
        if (configArgs != null) choiceBox.setValue(configArgs[index]);
        choiceBox.setId(String.valueOf(index));
        choiceBox.setPrefWidth(350);
        return choiceBox;
    }

    private void addComponentsToGrid(Label label, Control control, int index, GridPane grid) {
        int column = index % 3;
        int row = (index / 3) * 2;
        grid.add(label, column, row);
        grid.add(control, column, row + 1);
    }

    private void renderButtons(VBox vBox, String name, List<OptionUtil> settings, boolean isVisible, ModuleType moduleType) {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 30));
        grid.setHgap(10);
        grid.setVgap(10);
        Button runCommand = getButton(vBox, name, moduleType);
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
            ClickGui.isVisible = !isVisible;
            renderModule(vBox, name, settings, ClickGui.isVisible, moduleType);
        });
        if (moduleType == ModuleType.CRASH) {
            grid.add(getButton(name), 3, 0);
            grid.add(getConfigButton(vBox, name), 4, 0);
        } else {
            grid.add(getConfigButton(vBox, name), 3, 0);
        }
        grid.add(toggle, 0, 0);
        grid.add(runCommand, 1, 0);
        initTimer();
        vBox.getChildren().add(grid);
    }

    private @NotNull Button getButton(String name) {
        Button stop = new Button("Stop Crashing");
        stop.setPrefWidth(200);
        stop.setStyle(commonButtonStyle);
        stop.setOnMouseEntered(e -> {
            timeline = new Timeline(new KeyFrame(Duration.millis(10), e2 -> {
                stop.setStyle(commonButtonStyleHover);
            }));
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();
        });
        stop.setOnMouseExited(e -> {
            timeline.stop();
            stop.setStyle(commonButtonStyle);
        });
        stop.setOnMouseClicked(e -> {
            if (CrashManager.getManager().getCrashClassByName(name).isPresent()) {
                Crasher crasher = CrashManager.getManager().getCrashClassByName(name).get();
                if (crasher.getEnabled()) {
                    crasher.setEnabled(false);
                    msgHelper.sendMessage("", false);
                    msgHelper.sendMessage("&f" + name + " &7crash has been &cstopped!", true);
                    msgHelper.sendMessage("", false);
                }
            } else throw new IllegalArgumentException();
        });
        return stop;
    }

    private @NotNull Button getConfigButton(VBox vBox, String name) {
        Button stop = new Button("Change Config");
        stop.setPrefWidth(200);
        stop.setStyle(commonButtonStyle);
        stop.setOnMouseEntered(e -> {
            timeline = new Timeline(new KeyFrame(Duration.millis(10), e2 -> {
                stop.setStyle(commonButtonStyleHover);
            }));
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();
        });
        stop.setOnMouseExited(e -> {
            timeline.stop();
            stop.setStyle(commonButtonStyle);
        });
        stop.setOnMouseClicked(e -> {
            moduleConfigRenderer.renderOptions(vBox, name, moduleType);
        });
        return stop;
    }

    private @NotNull Button getButton(VBox vBox, String name, ModuleType moduleType) {
        Button runCommand = new Button(moduleType == ModuleType.CRASH ? "Start Crashing" : "Run Exploit");
        runCommand.setPrefWidth(200);
        runCommand.setStyle(commonButtonStyle);
        runCommand.setOnMouseEntered(e -> {
            timeline = new Timeline(new KeyFrame(Duration.millis(10), e2 -> {
                runCommand.setStyle(commonButtonStyleHover);
            }));
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();
        });
        runCommand.setOnMouseExited(e -> {
            timeline.stop();
            runCommand.setStyle(commonButtonStyle);
        });
        runCommand.setOnMouseClicked(e -> {
            for (Node elements : vBox.getChildren()) {
                if (elements instanceof GridPane gridPane) {
                    for (Node gridObj : gridPane.getChildren()) {
                        if (gridObj instanceof TextField textField && gridObj.getId() != null) {
                            String text;
                            if(textField.getText().isEmpty()) text = "0";
                            else text = textField.getText().replace(" ", "[-space-]");
                            builder.append(text).append(" ");
                        } else if (gridObj instanceof Button button && gridObj.getId() != null) {
                            builder.append(booleanMap.get(button)).append(" ");
                        } else if (gridObj instanceof ChoiceBox choiceBox && gridObj.getId() != null) {
                            builder.append(choiceBox.getSelectionModel().getSelectedItem()).append(" ");
                        }
                    }
                    break;
                }
            }
            if (moduleType == ModuleType.CRASH) {
                CrashManager.getManager().handleMethod(String.format("!crash %s %s", name, builder));
            } else
                ExploitManager.getManager().handleExploit(String.format("!exploit %s %s", name, builder));
            builder = new StringBuilder();
        });
        return runCommand;
    }

    public enum ModuleType {
        EXPLOIT, CRASH
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

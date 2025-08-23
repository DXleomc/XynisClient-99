package us.whitedev.gui.clickgui.components.modules;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.LinearGradient;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import us.whitedev.crashers.CrashManager;
import us.whitedev.exploits.ExploitManager;
import us.whitedev.gui.clickgui.ClickGui;
import us.whitedev.gui.clickgui.utils.StyleUtil;
import us.whitedev.helpers.ConfigHelper;

import java.util.ArrayList;
import java.util.List;

public class ModuleConfigRenderer {
    private Timeline timeline;
    private ModuleRenderer.ModuleType moduleType;
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
    private final ConfigHelper configHelper = new ConfigHelper();

    private void renderTitle(VBox vBox, String name){
        Label title = new Label(name + " - Config");
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

    public void renderOptions(VBox vBox, String name, ModuleRenderer.ModuleType moduleType){
        vBox.getChildren().clear();
        this.moduleType = moduleType;
        renderTitle(vBox, name);
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 0, 0, 20));
        grid.setHgap(10);
        grid.setVgap(10);
        Label welcomeLabel = new Label("Change config for " + name);
        welcomeLabel.setStyle("-fx-font-size: 20; " +
                "-fx-text-fill: rgba(255, 255, 255);");
        grid.add(welcomeLabel, 0, 0);

        List<String> bypassListUtils = getBypassList(name);

        Label configLabel = new Label("Available configs: ");
        configLabel.setStyle("-fx-font-size: 16; " +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: white;");
        grid.add(configLabel, 0, 1);

        ChoiceBox<String> choiceBox = new ChoiceBox<>();
        if(!bypassListUtils.isEmpty()) {
            choiceBox.getItems().addAll(bypassListUtils);
            choiceBox.setPrefWidth(350);
            grid.add(choiceBox, 0, 2);
        }else{
            Label infoLabel = new Label("There are no configs for this module!");
            infoLabel.setStyle("-fx-font-size: 12; " +
                    "-fx-font-weight: bold;" +
                    "-fx-text-fill: white;");
            grid.add(infoLabel, 0, 2);
        }

        Button saveConfigButton = new Button("Save Config");
        saveConfigButton.setPrefWidth(180);
        saveConfigButton.setStyle(commonButtonStyle);
        saveConfigButton.setOnMouseEntered(e -> {
            timeline = new Timeline(new KeyFrame(Duration.millis(10), e2 -> {
                saveConfigButton.setStyle(commonButtonStyleHover);
            }));
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();
        });
        saveConfigButton.setOnMouseExited(e -> {
            timeline.stop();
            saveConfigButton.setStyle(commonButtonStyle);
        });
        initTimer();
        grid.add(saveConfigButton, 0, 3);
        saveConfigButton.setOnMouseClicked(e -> {
            if(choiceBox.getValue() != null && !choiceBox.getValue().isEmpty()) {
                configHelper.loadConfig(choiceBox.getValue().split(" - ")[0]);
                backToMethodScreen(vBox, name);
            }
        });

        Button backButton = new Button("Back");
        backButton.setPrefWidth(180);
        backButton.setStyle(commonButtonStyle);
        backButton.setOnMouseEntered(e -> {
            timeline = new Timeline(new KeyFrame(Duration.millis(10), e2 -> {
                backButton.setStyle(commonButtonStyleHover);
            }));
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();
        });
        backButton.setOnMouseExited(e -> {
            timeline.stop();
            backButton.setStyle(commonButtonStyle);
        });
        initTimer();
        grid.add(backButton, 1, 3);
        backButton.setOnMouseClicked(e -> {
            backToMethodScreen(vBox, name);
        });

        GridPane.setMargin(saveConfigButton, new Insets(0, 10, 0, 90));
        GridPane.setMargin(backButton, new Insets(0, 110, 0, 10));

        vBox.getChildren().add(grid);
    }

    private void backToMethodScreen(VBox vBox, String name){
        vBox.getChildren().clear();
        if(moduleType == ModuleRenderer.ModuleType.CRASH) {
            CrashManager.getManager().getCrashClassByName(name).ifPresent(crasher ->
                    new ModuleRenderer().renderModule(vBox,
                            name, crasher.getOptions(), ClickGui.isVisible,
                            ModuleRenderer.ModuleType.CRASH));
        }else{
            ExploitManager.getManager().getExploitClassByName(name).ifPresent(exploit ->
                    new ModuleRenderer().renderModule(vBox,
                            name, exploit.getOptions(), ClickGui.isVisible,
                            ModuleRenderer.ModuleType.EXPLOIT));
        }
    }

    private List<String> getBypassList(String name){
        List<String> list = new ArrayList<>();
        configHelper.getBypassLists().stream()
                .filter(config -> config.moduleName().split(" ")[1].equalsIgnoreCase(name))
                .forEach(config -> {
                    list.add(config.bypassName() + " - " + config.bypassFor());
                });
        return list;
    }

    private void initTimer(){
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


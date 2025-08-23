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
import us.whitedev.gui.clickgui.ClickGui;
import us.whitedev.gui.clickgui.utils.ColorSlider;
import us.whitedev.gui.clickgui.utils.StyleUtil;
import us.whitedev.gui.hud.HudGui;
import us.whitedev.gui.hud.utils.HudOptions;
import us.whitedev.utils.DiscordRP;

import java.util.List;

public class StyleRenderer {
    private Timeline timeline;
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
    private Label shapeLabel;
    private ColorSlider shapeSlider;

    private void renderTitle(VBox vBox) {
        Label title = new Label("GodIsKingClient - GuiStyle");
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

    public void renderOptions(VBox vBox) {
        renderTitle(vBox);
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 0, 0, 20));
        grid.setHgap(10);
        grid.setVgap(10);
        Label welcomeLabel = new Label("Here you can Change the Design of Gui");
        welcomeLabel.setStyle("-fx-font-size: 20; " +
                "-fx-text-fill: rgba(255, 255, 255);");
        grid.add(welcomeLabel, 0, 0);
        Label colorLabel = new Label("Main Gui Color: ");
        ChoiceBox<ClickGui.GuiStyle> choiceBox = new ChoiceBox<>();
        choiceBox.getItems().addAll(ClickGui.GuiStyle.values());
        choiceBox.setPrefWidth(180);
        choiceBox.setValue(ClickGui.guiStyle);
        colorLabel.setStyle("-fx-font-size: 16; " +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: white;");
        grid.add(colorLabel, 0, 1);
        grid.add(choiceBox, 0, 2);

        shapeLabel = new Label("Gui Radius: " + StyleUtil.getShapeInt());
        shapeLabel.setStyle("-fx-font-size: 16; " +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: white;");
        shapeSlider = new ColorSlider(0, 30, StyleUtil.getShapeInt());
        shapeSlider.setMaxWidth(180);
        grid.add(shapeLabel, 1, 1);
        grid.add(shapeSlider, 1, 2);
        Label hudPlaceLabel = new Label("Hud Place:");
        hudPlaceLabel.setStyle("-fx-font-size: 16; " +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: white;");
        ChoiceBox<ClickGui.HudPlace> choiceBoxHud = new ChoiceBox<>();
        choiceBoxHud.getItems().addAll(ClickGui.HudPlace.values());
        choiceBoxHud.setPrefWidth(180);
        choiceBoxHud.setValue(ClickGui.hudPlace);
        grid.add(hudPlaceLabel, 2, 1);
        grid.add(choiceBoxHud, 2, 2);

        Label clientStyleLabel = new Label("Client Style:");
        clientStyleLabel.setStyle("-fx-font-size: 16; " +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: white;");
        ChoiceBox<ClickGui.ClientStyle> choiceBoxClientStyle = new ChoiceBox<>();
        choiceBoxClientStyle.getItems().addAll(ClickGui.ClientStyle.values());
        choiceBoxClientStyle.setPrefWidth(180);
        choiceBoxClientStyle.setValue(ClickGui.clientStyle);
        grid.add(clientStyleLabel, 0, 3);
        grid.add(choiceBoxClientStyle, 0, 4);

        Label discordLabel = new Label("DiscordRP Visibility:");
        discordLabel.setStyle("-fx-font-size: 16; " +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: white;");
        ChoiceBox<String> choiceBoxDiscord = new ChoiceBox<>();
        choiceBoxDiscord.getItems().addAll(List.of("Enabled", "Disabled"));
        choiceBoxDiscord.setPrefWidth(180);
        choiceBoxDiscord.setValue(DiscordRP.rpSwitch ? "Enabled" : "Disabled");
        grid.add(discordLabel, 1, 3);
        grid.add(choiceBoxDiscord, 1, 4);

        Label hudInfoLabel = new Label("Hud Info:");
        hudInfoLabel.setStyle("-fx-font-size: 16; " +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: white;");
        ChoiceBox<String> hudInfoChoiceBox = new ChoiceBox<>();
        hudInfoChoiceBox.getItems().addAll(HudOptions.getMapKeys());
        hudInfoChoiceBox.setPrefWidth(180);
        grid.add(hudInfoLabel, 2, 3);
        grid.add(hudInfoChoiceBox, 2, 4);

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
        grid.add(saveConfigButton, 0, 6);
        saveConfigButton.setOnMouseClicked(e -> {
            ClickGui.guiStyle = choiceBox.getSelectionModel().getSelectedItem();
            ClickGui.hudPlace = choiceBoxHud.getSelectionModel().getSelectedItem();
            ClickGui.clientStyle = choiceBoxClientStyle.getSelectionModel().getSelectedItem();
            DiscordRP.rpSwitch = choiceBoxDiscord.getSelectionModel().getSelectedItem().equalsIgnoreCase("Enabled");
        });

        Button infoVisButton = new Button("Change Info Visibility");
        infoVisButton.setPrefWidth(180);
        infoVisButton.setStyle(commonButtonStyle);
        infoVisButton.setOnMouseEntered(e -> {
            timeline = new Timeline(new KeyFrame(Duration.millis(10), e2 -> {
                infoVisButton.setStyle(commonButtonStyleHover);
            }));
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();
        });
        infoVisButton.setOnMouseExited(e -> {
            timeline.stop();
            infoVisButton.setStyle(commonButtonStyle);
        });
        initTimer();
        grid.add(infoVisButton, 1, 6);
        infoVisButton.setOnMouseClicked(e -> {
            String hudInfoName = hudInfoChoiceBox.getSelectionModel().getSelectedItem();
            HudOptions.setOptionValue(hudInfoName, !HudOptions.getOption(hudInfoName));
            HudGui.restartGUI();
        });

        GridPane.setMargin(shapeLabel, new Insets(0, 0, 0, -140));
        GridPane.setMargin(shapeSlider, new Insets(0, 0, 0, -140));
        GridPane.setMargin(hudInfoLabel, new Insets(0, 0, 0, 30));
        GridPane.setMargin(hudInfoChoiceBox, new Insets(0, 0, 0, 30));
        GridPane.setMargin(discordLabel, new Insets(0, 0, 0, -140));
        GridPane.setMargin(choiceBoxDiscord, new Insets(0, 0, 0, -140));
        GridPane.setMargin(hudPlaceLabel, new Insets(0, 0, 0, 30));
        GridPane.setMargin(choiceBoxHud, new Insets(0, 0, 0, 30));
        GridPane.setMargin(infoVisButton, new Insets(0, 0, 0, -140));

        vBox.getChildren().add(grid);
    }

    private void initTimer() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(10), e -> {
            commonButtonStyleHover = "-fx-background-radius: 15 15 15 15;" +
                    "-fx-background-color: rgba(25, 25, 25);" +
                    "-fx-font-size: 14; " +
                    "-fx-font-weight: bold; " +
                    "-fx-text-fill: " + StyleUtil.getFinalColor();
            int shapeInt = (int) shapeSlider.getSliderValue();
            shapeLabel.setText("Gui Radius: " + (int) Math.round(shapeInt * 3.33));
            StyleUtil.setShapeInt(shapeInt);
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

}


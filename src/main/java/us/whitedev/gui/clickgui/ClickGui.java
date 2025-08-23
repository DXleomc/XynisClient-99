package us.whitedev.gui.clickgui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import net.minecraft.client.main.Main;
import us.whitedev.gui.clickgui.components.LeftPanelComponent;
import us.whitedev.gui.clickgui.components.modules.WelcomeRenderer;
import us.whitedev.gui.clickgui.utils.StyleUtil;
import us.whitedev.gui.hud.HudGui;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class ClickGui extends Application {

    private static Stage stage;
    private static HBox hBox;
    public static boolean isFocused = false;
    public static boolean isVisible = true;
    public static GuiStyle guiStyle = GuiStyle.RAINBOW;
    public static HudPlace hudPlace = HudPlace.LEFT;
    public static ClientStyle clientStyle = ClientStyle.XYNIS;
    private static final WelcomeRenderer welcomeRenderer = new WelcomeRenderer();
    private static final LeftPanelComponent leftPanelComponent = new LeftPanelComponent();

    private static double xOffset = 0;
    private static double yOffset = 0;

    private static VBox rightVBox;
    private static VBox leftVBox;


    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        stage.initStyle(StageStyle.TRANSPARENT);
        showGUI();
    }

    private static void showGUI() {
        rightVBox = new VBox();
        rightVBox.setStyle("-fx-background-color: rgba(0, 0, 0, 0.8); -fx-background-radius: 0 30 30 0;");
        rightVBox.setPrefSize(700, 550);

        leftVBox = new VBox();
        leftVBox.setStyle("-fx-background-color: black; -fx-background-radius: 30 0 0 30;");
        leftVBox.setAlignment(Pos.TOP_CENTER);
        leftVBox.setPrefSize(200, 550);

        leftPanelComponent.createAllComponents(leftVBox, rightVBox);

        hBox = new HBox(leftVBox, rightVBox);
        hBox.setPrefSize(900, 550);
        hBox.setStyle("-fx-border-color: rgba(40, 255, 246); " +
                "-fx-border-width: 2px;" +
                "-fx-border-radius: 30 30 30 30;");

        StackPane root = new StackPane(hBox);
        root.setStyle("-fx-background-color: rgba(0, 0, 0, 0.0);");

        welcomeRenderer.renderChangelog(rightVBox);

        initTimer();

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);

        root.setOnMousePressed((MouseEvent event) -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        root.setOnMouseDragged((MouseEvent event) -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });

        try {
            InputStream inputStream = Main.class.getResourceAsStream("/client/GuiStyle.css");
            if (inputStream != null) {
                Path tempFile = Files.createTempFile("GuiStyle", ".css");
                Files.copy(inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING);
                scene.getStylesheets().add(tempFile.toUri().toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        HudGui.initGui();

        stage.setScene(scene);
        stage.show();
    }

    private static void initTimer() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(10), e -> {
            hBox.setStyle(String.format("-fx-border-color: %s; -fx-border-width: 2px; -fx-border-radius: %d %d %d %d;",
                    StyleUtil.getFinalColor(),
                    StyleUtil.getShapeInt(), StyleUtil.getShapeInt(), StyleUtil.getShapeInt(), StyleUtil.getShapeInt()));
            rightVBox.setStyle(String.format("-fx-background-color: rgba(0, 0, 0, 0.8); -fx-background-radius: 0 %d %d 0;",
                    StyleUtil.getShapeInt(), StyleUtil.getShapeInt()));
            leftVBox.setStyle(String.format("-fx-background-color: black; -fx-background-radius: %d 0 0 %d;",
                    StyleUtil.getShapeInt(), StyleUtil.getShapeInt()));
            isFocused = stage.isFocused();
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private static void hideGUI() {
        stage.close();
    }

    public static void restartGUI() {
        Platform.runLater(() -> {
            if (stage.isShowing()) {
                stage.toFront();
            } else {
                hideGUI();
                showGUI();
            }
        });
    }

    public static void initGui() {
        if (stage != null) {
            restartGUI();
        } else {
            launch();
        }
    }

    public enum GuiStyle {
        RAINBOW, DEFAULT, RED, ORANGE, YELLOW, GREEN, BLUE, VIOLET
    }

    public enum HudPlace {
        LEFT, RIGHT, HIDE
    }

    public enum ClientStyle {
        MINECRAFT, XYNIS
    }

}

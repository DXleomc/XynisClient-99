package us.whitedev.gui.clickgui.components;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.LinearGradient;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import org.jetbrains.annotations.NotNull;
import us.whitedev.crashers.CrashManager;
import us.whitedev.crashers.Crasher;
import us.whitedev.exploits.Exploit;
import us.whitedev.exploits.ExploitManager;
import us.whitedev.gui.clickgui.ClickGui;
import us.whitedev.gui.clickgui.components.modules.*;
import us.whitedev.gui.clickgui.utils.StyleUtil;

public class RightPanelComponent {

    private Timeline timeline;
    private String mouseEnteredStyle = "-fx-background-radius: 15 15 15 15;" +
            "-fx-background-color: rgba(25, 25, 25);" +
            "-fx-font-size: 18; " +
            "-fx-font-weight: bold; " +
            "-fx-text-fill: rgba(40, 255, 246);";

    private final String mouseExitedStyle = "-fx-background-radius: 15 15 15 15;" +
            "-fx-background-color: rgba(25, 25, 25);" +
            "-fx-font-size: 18; " +
            "-fx-font-weight: bold; " +
            "-fx-text-fill: white;";
    private final CrashManager crashManager = CrashManager.getManager();
    private final ExploitManager exploitManager = ExploitManager.getManager();

    private final StyleRenderer styleRenderer = new StyleRenderer();
    private final AltManagerRenderer altManagerRenderer = new AltManagerRenderer();
    private final BotsRenderer botsRenderer = new BotsRenderer();
    private final VersionRenderer versionRenderer = new VersionRenderer();


    public void createCrashScene(VBox vBox, String[] crashers) {
        Label title = new Label("Crash Methods");
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

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setHgap(10);
        grid.setVgap(10);
        for (int i = 0; i < crashers.length; i++) {
            Button button = getButton1(vBox, crashers[i]);
            grid.add(button, i % 3, i / 3);
        }
        initTimer();
        vBox.getChildren().add(grid);
    }

    private @NotNull Button getButton1(VBox vBox, String crashers) {
        Button button = new Button(crashers);
        button.setStyle(mouseExitedStyle);
        button.setOnMouseEntered(e -> {
            timeline = new Timeline(new KeyFrame(Duration.millis(10), e2 -> {
                button.setStyle(mouseEnteredStyle);
            }));
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();
        });
        button.setOnMouseExited(e -> {
            timeline.stop();
            button.setStyle(mouseExitedStyle);
        });
        button.setOnMouseClicked(e -> {
            if(crashManager.getCrashClassByName(crashers).isPresent()) {
                vBox.getChildren().clear();
                Crasher crasher = crashManager.getCrashClassByName(crashers).get();
                new ModuleRenderer().renderModule(vBox, crashers, crasher.getOptions(), ClickGui.isVisible, ModuleRenderer.ModuleType.CRASH);
            }else throw new IllegalArgumentException();
        });
        button.setPrefWidth(400);
        return button;
    }

    public void createExploitScene(VBox vBox, String[] exploits) {
        Label title = new Label("Exploits");
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
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setHgap(10);
        grid.setVgap(10);
        for (int i = 0; i < exploits.length; i++) {
            Button button = getButton(vBox, exploits[i]);
            grid.add(button, i % 3, i / 3);
        }
        initTimer();
        vBox.getChildren().add(grid);
    }

    private @NotNull Button getButton(VBox vBox, String exploits) {
        Button button = new Button(exploits);
        button.setStyle(mouseExitedStyle);
        button.setOnMouseEntered(e -> {
            timeline = new Timeline(new KeyFrame(Duration.millis(10), e2 -> {
                button.setStyle(mouseEnteredStyle);
            }));
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();
        });
        button.setOnMouseExited(e -> {
            timeline.stop();
            button.setStyle(mouseExitedStyle);
        });
        button.setOnMouseClicked(e -> {
            if(exploitManager.getExploitClassByName(exploits).isPresent()) {
                vBox.getChildren().clear();
                Exploit exploit = exploitManager.getExploitClassByName(exploits).get();
                new ModuleRenderer().renderModule(vBox, exploits, exploit.getOptions(), ClickGui.isVisible, ModuleRenderer.ModuleType.EXPLOIT);
            }else throw new IllegalArgumentException();
        });
        button.setPrefWidth(400);
        return button;
    }

    private void initTimer(){
        timeline = new Timeline(new KeyFrame(Duration.millis(10), e -> mouseEnteredStyle = "-fx-background-radius: 15 15 15 15;" +
                "-fx-background-color: rgba(25, 25, 25);" +
                "-fx-font-size: 18; " +
                "-fx-font-weight: bold; " +
                "-fx-text-fill: " + StyleUtil.getFinalColor()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    public void createAltManagerScene(VBox vBox) {
        altManagerRenderer.renderAltManager(vBox, "Waiting for action...");
    }

    public void createStyleScene(VBox vBox){
        styleRenderer.renderOptions(vBox);
    }

    public void createBotsScene(VBox vBox){
        botsRenderer.renderOptions(vBox);
    }

    public void createVersionScene(VBox vBox){
        versionRenderer.renderOptions(vBox);
    }
}

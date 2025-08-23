package us.whitedev.gui.hud;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import net.minecraft.client.Minecraft;
import us.whitedev.XynisClient;
import us.whitedev.gui.clickgui.ClickGui;
import us.whitedev.gui.clickgui.utils.StyleUtil;
import us.whitedev.gui.hud.utils.HudOptions;
import us.whitedev.gui.hud.utils.ServerInfoUtil;
import us.whitedev.helpers.PacketHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HudGui {

    private static final Minecraft mc = Minecraft.getInstance();
    private static final List<HBox> boxes = new ArrayList<>();
    private static final ServerInfoUtil serverUtil = new ServerInfoUtil();
    private static Stage stage;
    private static Text xynisText;
    private static StackPane pane;

    private static void showGUI() {
        stage.setScene(null);
        stage.setAlwaysOnTop(true);
        setupTextComponents();
        setupScene();

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(HudGui::updateHudData);
            }
        }, 0, 1);

        initTimer();
        stage.show();
    }

    private static void setupTextComponents() {
        xynisText = new Text("GodIsKingClient ");
        xynisText.setFill(Color.color(0.6, 0.6, 0.6));
        xynisText.setStyle("-fx-font-size: 18; -fx-font-weight: bold; -fx-fill: " + StyleUtil.getFinalColor());

        Text versionText = new Text(XynisClient.VERSION);
        versionText.setStyle("-fx-font-size: 18; -fx-font-weight: bold; -fx-fill: white;");

        TextFlow title = new TextFlow(xynisText, versionText);

        Label subtitle = new Label("> Exclusive Minecraft Crash/Exploit Client <");
        subtitle.setStyle("-fx-font-size: 10; -fx-text-fill: rgba(140, 140, 140);");

        HBox titleBox = new HBox(title);
        titleBox.setAlignment(Pos.CENTER);

        HBox subtitleBox = new HBox(subtitle);
        subtitleBox.setAlignment(Pos.CENTER);

        VBox vbox = new VBox(titleBox, subtitleBox);
        initInfoBoxes();
        vbox.getChildren().addAll(boxes);

        pane = new StackPane(vbox);
        pane.setStyle(getPaneStyle());
    }

    private static void initInfoBoxes() {
        boxes.clear();
        createAndAddInfoBox("IP -> ");
        createAndAddInfoBox("Engine -> ");
        createAndAddInfoBox("Last Packet -> ");
        createAndAddInfoBox("XYZ -> ");
        createAndAddInfoBox("FPS -> ");
        createAndAddInfoBox("PPS -> ");
    }

    private static void createAndAddInfoBox(String labelText) {
        if (HudOptions.getOption(labelText.replace(" -> ", ""))) {
            Label label = new Label(labelText);
            int firstGrd = (int) (0.2 * (2 * labelText.length()));
            label.setStyle("-fx-font-size: 14; -fx-font-weight: bold; -fx-text-fill: linear-gradient(to right, rgba(140, 140, 140) " + firstGrd + "%, rgba(70, 70, 70) " + (100 - firstGrd) + "%);");

            Label dataLabel = new Label("");
            dataLabel.setStyle("-fx-font-size: 14; -fx-text-fill: white;");

            HBox box = new HBox(label, dataLabel);
            box.setAlignment(Pos.BASELINE_LEFT);
            boxes.add(box);
        }
    }

    private static String getPaneStyle() {
        return "-fx-background-radius: 30; " +
                "-fx-background-color: rgba(0, 0, 0, 0.7); " +
                "-fx-padding: 20; " +
                "-fx-border-width: 2; " +
                "-fx-border-color: " + StyleUtil.getFinalColor() + " " +
                "-fx-border-radius: 30;";
    }

    private static void setupScene() {
        Scene scene = new Scene(pane, 300, calculateSceneHeight());
        scene.setFill(null);

        stage.setScene(scene);
        stage.setX(mc.getWindow().getX() + 5);
        stage.setY(mc.getWindow().getY());
    }

    private static double calculateSceneHeight() {
        return 40 + (23 * boxes.size());
    }

    private static void updateHudData() {
        if (mc.player != null && mc.player.connection.getConnection().isConnected() && (mc.isWindowActive() || ClickGui.isFocused)) {
            updateLabels();
            adjustHudPosition();
        } else {
            stage.setX(5000);
        }
    }

    private static void updateLabels() {
        boxes.forEach(box -> {
            Label label = (Label) box.getChildren().get(1);
            String text = ((Label) box.getChildren().get(0)).getText().replace(" -> ", "");
            switch (text) {
                case "IP" -> label.setText(serverUtil.getServerIp());
                case "Engine" -> label.setText(serverUtil.getBrand());
                case "Last Packet" -> {
                    label.setText(PacketHelper.lagTimeForSec() + " sec");
                    label.setStyle(getPacketStyle());
                }
                case "XYZ" -> label.setText(serverUtil.getPlayerCoordinates());
                case "FPS" -> label.setText(mc.getFps() + " fps");
                case "PPS" -> label.setText(PacketHelper.packetSent + "s / " + PacketHelper.packetReceived + "r");
            }
        });
    }

    private static void adjustHudPosition() {
        double x = mc.getWindow().getX() * 0.8;

        switch (ClickGui.hudPlace) {
            case LEFT -> stage.setX(x + 5);
            case RIGHT -> stage.setX(calcOffset(mc.getWindow().getScreenWidth(), x));
            case HIDE -> stage.setX(5000);
        }

        stage.setY(mc.getWindow().getY() * 0.8 + 5);
    }

    private static double calcOffset(int windowWidth, double startX) {
        double a = (1222.0 - 368.0) / (1920.0 - 854.0);
        double b = 368.0 - a * 854.0;
        double offset = a * windowWidth + b;
        return Math.round(startX + offset);
    }


    private static void initTimer() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(10), e -> updateStyles()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private static void updateStyles() {
        xynisText.setStyle("-fx-font-size: 18; -fx-font-weight: bold; -fx-fill: " + StyleUtil.getFinalColor() + "-fx-padding: 15 0 0 0;");
        pane.setStyle(getPaneStyle());
    }

    private static String getPacketStyle() {
        long lagTime = PacketHelper.getServerLagTime();
        if (lagTime > 25000) return "-fx-font-size: 14; -fx-text-fill: darkred;";
        if (lagTime > 15000) return "-fx-font-size: 14; -fx-text-fill: red;";
        if (lagTime > 10000) return "-fx-font-size: 14; -fx-text-fill: orange;";
        if (lagTime > 5000) return "-fx-font-size: 14; -fx-text-fill: yellow;";
        if (lagTime > 100) return "-fx-font-size: 14; -fx-text-fill: green;";
        return "-fx-font-size: 14; -fx-text-fill: white;";
    }

    public static void hideGUI() {
        stage.close();
    }

    public static void restartGUI() {
        Platform.runLater(() -> {
            hideGUI();
            showGUI();
        });
    }

    public static void initGui() {
        HudOptions.initMap();
        if (stage != null) {
            restartGUI();
        } else {
            stage = new Stage();
            stage.initStyle(StageStyle.TRANSPARENT);
            showGUI();
        }
    }
              }
          

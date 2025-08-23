package us.whitedev.gui.clickgui.components;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;
import us.whitedev.XynisClient;
import us.whitedev.crashers.CrashManager;
import us.whitedev.exploits.ExploitManager;
import us.whitedev.gui.clickgui.utils.StyleUtil;

public class LeftPanelComponent {

    private Text title;
    private Text playTimeValue;
    private Text nickNameText;
    private Text nickNameValue;
    private int seconds = 0;
    private int minutes = 0;
    private int hours = 0;
    private Timeline timeline;
    private String clickedStyle = "-fx-background-radius: 15 15 15 15;" +
            "-fx-background-color: rgba(25, 25, 25);" +
            "-fx-font-size: 18; " +
            "-fx-font-weight: bold; " +
            "-fx-text-fill: rgba(40, 255, 246);";
    private final String normalButtonStyle = "-fx-background-color: rgba(0, 0, 0, 0);" +
            "-fx-font-size: 18; " +
            "-fx-font-weight: bold; " +
            "-fx-text-fill: rgba(255, 255, 255);";

    public static ClickedButtonType CLICKED_BUTTON = ClickedButtonType.CRASH;
    private final RightPanelComponent rightPanelComponent = new RightPanelComponent();

    private void createCrashComponents(VBox vBoxMain, VBox vBox2){
        Button button = new Button("💣 Crashers              ");
        vBoxMain.getChildren().add(button);
        button.setStyle(normalButtonStyle);
        button.setOnMouseClicked(e -> {
            button.setText("💣 Crashers            | ");
            button.setStyle(clickedStyle);
            CLICKED_BUTTON = ClickedButtonType.CRASH;
            vBox2.getChildren().clear();
            rightPanelComponent.createCrashScene(vBox2, CrashManager.getManager().allMethods().split(", "));
        });
        timeline = new Timeline(new KeyFrame(Duration.millis(10), e -> {
            if(CLICKED_BUTTON != ClickedButtonType.CRASH){
                button.setText("💣 Crashers              ");
                button.setStyle(normalButtonStyle);
            }else{
                button.setStyle(clickedStyle);
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void createExploitComponents(VBox vBoxMain, VBox vBox2){
        Button button = new Button("🔥 Exploits               ");
        vBoxMain.getChildren().add(button);
        button.setStyle(normalButtonStyle);
        button.setOnMouseClicked(e -> {
            button.setText("🔥 Exploits             | ");
            button.setStyle(clickedStyle);
            CLICKED_BUTTON = ClickedButtonType.EXPLOIT;
            vBox2.getChildren().clear();
            rightPanelComponent.createExploitScene(vBox2, ExploitManager.getManager().allExploits().split(", "));
        });
        timeline = new Timeline(new KeyFrame(Duration.millis(10), e -> {
            if(CLICKED_BUTTON != ClickedButtonType.EXPLOIT){
                button.setText("🔥 Exploits               ");
                button.setStyle(normalButtonStyle);
            }else{
                button.setStyle(clickedStyle);
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void createAltLoginComponents(VBox vBoxMain, VBox vBox2){
        Button button = new Button("👤 AltManager         ");
        vBoxMain.getChildren().add(button);
        button.setStyle(normalButtonStyle);
        button.setOnMouseClicked(e -> {
            button.setText("👤 AltManager      | ");
            button.setStyle(clickedStyle);
            CLICKED_BUTTON = ClickedButtonType.ALTMANAGER;
            vBox2.getChildren().clear();
            rightPanelComponent.createAltManagerScene(vBox2);
        });
        timeline = new Timeline(new KeyFrame(Duration.millis(10), e -> {
            if(CLICKED_BUTTON != ClickedButtonType.ALTMANAGER){
                button.setText("👤 AltManager         ");
                button.setStyle(normalButtonStyle);
            }else{
                button.setStyle(clickedStyle);
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void createBotComponents(VBox vBoxMain, VBox vBox2){
        Button button = new Button("👥 Bots                     ");
        vBoxMain.getChildren().add(button);
        button.setStyle(normalButtonStyle);
        button.setOnMouseClicked(e -> {
            button.setText("👥 Bots                   | ");
            button.setStyle(clickedStyle);
            CLICKED_BUTTON = ClickedButtonType.BOTS;
            vBox2.getChildren().clear();
            rightPanelComponent.createBotsScene(vBox2);
        });
        timeline = new Timeline(new KeyFrame(Duration.millis(10), e -> {
            if(CLICKED_BUTTON != ClickedButtonType.BOTS){
                button.setText("👥 Bots                     ");
                button.setStyle(normalButtonStyle);
            }else{
                button.setStyle(clickedStyle);
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void createStyleComponents(VBox vBoxMain, VBox vBox2){
        Button button = new Button("🎨 Style                    ");
        vBoxMain.getChildren().add(button);
        button.setStyle(normalButtonStyle);
        button.setOnMouseClicked(e -> {
            button.setText("🎨 Style                  | ");
            button.setStyle(clickedStyle);
            CLICKED_BUTTON = ClickedButtonType.STYLE;
            vBox2.getChildren().clear();
            rightPanelComponent.createStyleScene(vBox2);
        });
        timeline = new Timeline(new KeyFrame(Duration.millis(10), e -> {
            if(CLICKED_BUTTON != ClickedButtonType.STYLE){
                button.setText("🎨 Style                    ");
                button.setStyle(normalButtonStyle);
            }else{
                button.setStyle(clickedStyle);
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void createVersionComponents(VBox vBoxMain, VBox vBox2){
        Button button = new Button("🔧 Version               ");
        vBoxMain.getChildren().add(button);
        button.setStyle(normalButtonStyle);
        button.setOnMouseClicked(e -> {
            button.setText("🔧 Version             | ");
            button.setStyle(clickedStyle);
            CLICKED_BUTTON = ClickedButtonType.VERSION;
            vBox2.getChildren().clear();
            rightPanelComponent.createVersionScene(vBox2);
        });
        timeline = new Timeline(new KeyFrame(Duration.millis(10), e -> {
            if(CLICKED_BUTTON != ClickedButtonType.VERSION){
                button.setText("🔧 Version               ");
                button.setStyle(normalButtonStyle);
            }else{
                button.setStyle(clickedStyle);
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void createFreeSpace(VBox vBoxMain, VBox vBox2){
        Region topEmptyRegion = new Region();
        VBox.setVgrow(topEmptyRegion, Priority.ALWAYS);
        vBoxMain.getChildren().add(topEmptyRegion);

        createCrashComponents(vBoxMain, vBox2);
        createExploitComponents(vBoxMain, vBox2);
        createAltLoginComponents(vBoxMain, vBox2);
        createVersionComponents(vBoxMain, vBox2);
        createBotComponents(vBoxMain, vBox2);
        createStyleComponents(vBoxMain, vBox2);

        Region bottomEmptyRegion = new Region();
        VBox.setVgrow(bottomEmptyRegion, Priority.ALWAYS);
        vBoxMain.getChildren().add(bottomEmptyRegion);
        bottomEmptyRegion.setPrefSize(200, 60);
    }

    private void createSmallInfo(VBox vBox){
        TextFlow playTime = new TextFlow();
        playTime.setTextAlignment(TextAlignment.CENTER);
        Text playTimeText = new Text("PlayTime: ");
        playTimeValue = new Text("00:00:00");
        playTimeText.setStyle("-fx-font-size: 14; " +
                "-fx-font-weight: bold; " +
                "-fx-fill: white");
        playTimeValue.setStyle("-fx-font-size: 14; " +
                "-fx-fill: " + StyleUtil.getFinalColor());
        playTime.getChildren().addAll(playTimeText, playTimeValue);
        TextFlow nickName = new TextFlow();
        nickName.setTextAlignment(TextAlignment.CENTER);
        nickNameText = new Text("Nickname: ");
        nickNameValue = new Text(Minecraft.getInstance().getUser().getName());
        nickName.getChildren().addAll(nickNameText, nickNameValue);
        VBox.setMargin(nickName, new Insets(0, 0, 10, 0));
        vBox.getChildren().add(playTime);
        vBox.getChildren().add(nickName);
        Timeline privTimeline = getTimeline();
        privTimeline.play();
    }

    private @NotNull Timeline getTimeline() {
        Timeline privTimeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            if(seconds >= 59) {
                if (minutes >= 59) {
                    ++hours;
                    minutes = -1;
                }
                ++minutes;
                seconds = -1;
            }
            ++seconds;
            playTimeValue.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
            nickNameValue.setText(Minecraft.getInstance().getUser().getName());
        }));
        privTimeline.setCycleCount(Timeline.INDEFINITE);
        return privTimeline;
    }

    public void createAllComponents(VBox vBoxMain, VBox vBox2){
        TextFlow title = new TextFlow();
        title.setTextAlignment(TextAlignment.CENTER);
        Text xynisClientText = new Text("GodIsKingClient ");
        Text xynisVersionText = new Text(XynisClient.VERSION);
        xynisClientText.setStyle("-fx-font-size: 22; " +
                "-fx-font-weight: bold; " +
                "-fx-fill: " + StyleUtil.getFinalColor());
        xynisVersionText.setStyle("-fx-font-size: 22; " +
                "-fx-font-weight: bold; " +
                "-fx-fill: white;");
        title.getChildren().addAll(xynisClientText, xynisVersionText);
        title.setStyle("-fx-padding: 0 0 0 0;");
        this.title = xynisClientText;
        Label subtitle = new Label("Exclusive Minecraft Crash/Exploit Client");
        subtitle.setStyle("-fx-font-size: 10; -fx-text-fill: rgba(140, 140, 140);");
        vBoxMain.getChildren().add(title);
        vBoxMain.getChildren().add(subtitle);
        VBox.setMargin(title, new Insets(15, 0, 0, 0));
        createFreeSpace(vBoxMain, vBox2);
        createSmallInfo(vBoxMain);
        initTimer();
    }

    private void initTimer(){
        Timeline privTimeline = new Timeline(new KeyFrame(Duration.millis(10), e -> {
            clickedStyle = "-fx-background-radius: 15 15 15 15;" +
                    "-fx-background-color: rgba(25, 25, 25);" +
                    "-fx-font-size: 18; " +
                    "-fx-font-weight: bold; " +
                    "-fx-text-fill: " + StyleUtil.getFinalColor();
            title.setStyle("-fx-font-size: 22; " +
                    "-fx-font-weight: bold; " +
                    "-fx-fill: " + StyleUtil.getFinalColor());
            playTimeValue.setStyle("-fx-font-size: 14; " +
                    "-fx-fill: " + StyleUtil.getFinalColor());
            if(nickNameValue.getText().length() > 12){
                nickNameText.setStyle("-fx-font-size: 11; " +
                        "-fx-font-weight: bold; " +
                        "-fx-fill: white");
                nickNameValue.setStyle("-fx-font-size: 11; " +
                        "-fx-fill: " + StyleUtil.getFinalColor());
            }else{
                nickNameText.setStyle("-fx-font-size: 14; " +
                        "-fx-font-weight: bold; " +
                        "-fx-fill: white");
                nickNameValue.setStyle("-fx-font-size: 14; " +
                        "-fx-fill: " + StyleUtil.getFinalColor());
            }
        }));
        privTimeline.setCycleCount(Timeline.INDEFINITE);
        privTimeline.play();
    }

    private enum ClickedButtonType{
        CRASH, EXPLOIT, ALTMANAGER, STYLE, BOTS, VERSION
    }

}

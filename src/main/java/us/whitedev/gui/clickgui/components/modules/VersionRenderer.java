package us.whitedev.gui.clickgui.components.modules;

import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import de.florianmichael.vialoadingbase.ViaLoadingBase;
import de.florianmichael.viamcp.protocolinfo.ProtocolInfo;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.LinearGradient;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import us.whitedev.gui.clickgui.ClickGui;
import us.whitedev.gui.clickgui.utils.ColorSlider;
import us.whitedev.gui.clickgui.utils.StyleUtil;

public class VersionRenderer {
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
    private ChoiceBox<String> versionChoiceBox;

    private void renderTitle(VBox vBox){
        Label title = new Label("GodIsKingClient - Version Switcher");
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

    public void renderOptions(VBox vBox){
        ProtocolInfo protocolInfo = ProtocolInfo.fromProtocolVersion(ViaLoadingBase.getInstance().getTargetVersion());
        renderTitle(vBox);
        initTimer();
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 0, 0, 20));
        grid.setHgap(10);
        grid.setVgap(10);
        Label welcomeLabel = new Label("Here you can Switch your client version");
        welcomeLabel.setStyle("-fx-font-size: 20; " +
                "-fx-text-fill: rgba(255, 255, 255);");
        grid.add(welcomeLabel, 0, 0);

        VBox centerBox = new VBox(10);
        centerBox.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("Version Information:");
        titleLabel.setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-text-fill: rgba(255, 255, 255);");

        VBox infoBox= new VBox(0);
        infoBox.setAlignment(Pos.CENTER);

        String protocolName = protocolInfo != null ? protocolInfo.getName() : "unknown";
        String protocolDate = protocolInfo != null ? protocolInfo.getReleaseDate() : "unknown";

        Label infoLabel = new Label("Version: " + ViaLoadingBase.getInstance().getTargetVersion().getName() + " - " + protocolName);
        infoLabel.setStyle("-fx-font-size: 16; -fx-text-fill: rgba(255, 255, 255);");

        String info2String = "Released Data: " + protocolDate;
        Label infoLabel2 = new Label(info2String);
        infoLabel2.setStyle("-fx-font-size: 16; -fx-text-fill: rgba(255, 255, 255);");

        infoBox.getChildren().addAll(infoLabel, infoLabel2);

        Label versionLabel = new Label("Select Version:");
        versionLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-text-fill: white;");

        versionChoiceBox = new ChoiceBox<>();
        ViaLoadingBase.PROTOCOLS.forEach(ver -> versionChoiceBox.getItems().add(ver.getName()));
        versionChoiceBox.setPrefWidth(280);

        Button doneButton = getButton(vBox);

        centerBox.getChildren().addAll(titleLabel, infoBox, versionLabel, versionChoiceBox, doneButton);
        grid.add(centerBox, 0, 1);

        int length = info2String.length();
        int leftMargin;
        if(length > 42) leftMargin = 120;
        else if(length > 25) leftMargin = 180;
        else leftMargin = 200;

        GridPane.setMargin(centerBox, new Insets(45, 0, 0, leftMargin));

        vBox.getChildren().add(grid);
    }

    private @NotNull Button getButton(VBox vBox) {
        Button doneButton = new Button("Switch Version");
        doneButton.setPrefWidth(280);
        doneButton.setStyle(commonButtonStyle);
        doneButton.setOnMouseEntered(e -> {
            timeline = new Timeline(new KeyFrame(Duration.millis(10), e2 -> {
                doneButton.setStyle(commonButtonStyleHover);
            }));
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();
        });
        doneButton.setOnMouseExited(e -> {
            timeline.stop();
            doneButton.setStyle(commonButtonStyle);
        });
        doneButton.setOnMouseClicked(e -> {
            if(Minecraft.getInstance().getConnection() != null && Minecraft.getInstance().getConnection().getConnection().isConnected()) Minecraft.getInstance().getConnection().getConnection().disconnect(Component.literal("Version Changed!"));
            ProtocolVersion version = ViaLoadingBase.PROTOCOLS.get(versionChoiceBox.getSelectionModel().getSelectedIndex());
            ViaLoadingBase.getInstance().reload(version);
            vBox.getChildren().clear();
            renderOptions(vBox);
        });
        return doneButton;
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


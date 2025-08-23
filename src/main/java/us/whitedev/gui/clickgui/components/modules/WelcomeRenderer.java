package us.whitedev.gui.clickgui.components.modules;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.LinearGradient;
import javafx.scene.shape.Line;
import org.jetbrains.annotations.NotNull;
import us.whitedev.XynisClient;
import us.whitedev.proxy.XynisProxy;

public class WelcomeRenderer {
    private void renderTitle(VBox vBox){
        Label title = new Label("GodIsKingClient - Changelog");
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

    public void renderChangelog(VBox vBox){
        renderTitle(vBox);
        Label welcomeLabel = new Label("Welcome on GodIsKingClient!");
        welcomeLabel.setStyle("-fx-font-size: 22; " +
                "-fx-font-weight: bold; " +
                "-fx-text-fill: rgba(255, 255, 255);" +
                "-fx-padding: 5 0 0 15;");
        vBox.getChildren().add(welcomeLabel);
        Label label1 = new Label("Exclusive Crash/Exploit for Minecraft 1.20.1");
        label1.setStyle("-fx-font-size: 18; " +
                "-fx-text-fill: rgba(200, 200, 200);" +
                "-fx-padding: -5 0 0 15;");
        vBox.getChildren().add(label1);
        Label label2 = new Label(String.format("Changelog for GodIsKingClient %s:", XynisClient.VERSION));
        label2.setStyle("-fx-font-size: 22; " +
                "-fx-font-weight: bold; " +
                "-fx-text-fill: rgba(255, 255, 255);" +
                "-fx-padding: 5 0 0 15;");
        vBox.getChildren().add(label2);
        Label label3 = getClientLabel();
        vBox.getChildren().add(label3);
        Label label4 = new Label(String.format("Changelog for GodIsKingProxy %s:", XynisProxy.VERSION));
        vBox.getChildren().add(label4);
        label4.setStyle("-fx-font-size: 22; " +
                "-fx-font-weight: bold; " +
                "-fx-text-fill: rgba(255, 255, 255);" +
                "-fx-padding: 5 0 0 15;");
        Label label5 = getProxyLabel();
        vBox.getChildren().add(label5);
    }

    private static @NotNull Label getClientLabel() {
        Label label3 = new Label("""
                - Added 4 new Crash Methods
                - Added Support for DataComponents 1.20.5 - 1.21.4
                - Added DataComponents Support for NBT Crashers
                - Added/Reworked Packets in XynisProtocol
                - Added new 6 DataComponents
                - Fixed Hud Position
                - Auto Disable Switchable Modules after Disconnect
                - Improved censorship options
                - XynisProxy 2.0 Release
                """);
        label3.setStyle("-fx-font-size: 18; " +
                "-fx-text-fill: rgba(200, 200, 200);" +
                "-fx-padding: 0 0 0 25;");
        return label3;
    }

    private static @NotNull Label getProxyLabel() {
        Label label5 = new Label("""
                - Reworked proxy system
                - Reworked proxy commands
                - Reworked proxy handler
                """);
        label5.setStyle("-fx-font-size: 18; " +
                "-fx-text-fill: rgba(200, 200, 200);" +
                "-fx-padding: 0 0 0 25;");
        return label5;
    }

}


package us.whitedev.gui.clickgui.components.modules;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.LinearGradient;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;
import org.jetbrains.annotations.NotNull;
import us.whitedev.gui.clickgui.utils.StyleUtil;
import us.whitedev.proxy.XynisProxy;
import us.whitedev.proxy.functions.crashers.BotCrasher;
import us.whitedev.proxy.functions.crashers.CrashHandler;
import us.whitedev.proxy.repository.OptionsRep;
import us.whitedev.proxy.utils.ColorUtil;
import us.whitedev.proxy.utils.LogType;
import us.whitedev.proxy.utils.ProxyLogger;

public class BotsRenderer {
    private Timeline timeline;
    private int logsSize = 0;
    private final Minecraft mc = Minecraft.getInstance();
    private final OptionsRep optionsRep = OptionsRep.getInstance();
    private final CrashHandler crashHandler = CrashHandler.getInstance();
    private final BotConfigRenderer botConfigRenderer = new BotConfigRenderer();
    private TextArea textArea;
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

    private void renderTitle(VBox vBox) {
        Label title = new Label("GodIsKingClient - Bots Manager");
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
        Label welcomeLabel = new Label("Here you can manage your proxy");
        welcomeLabel.setStyle("-fx-font-size: 20; -fx-text-fill: rgba(255, 255, 255);");
        grid.add(welcomeLabel, 0, 0);

        String commonTextFieldStyle = "-fx-background-radius: 15 15 15 15;" +
                "-fx-background-color: rgba(25, 25, 25);" +
                "-fx-font-size: 14; " +
                "-fx-font-weight: bold; " +
                "-fx-text-fill: white;";

        String commonLabelStyle = "-fx-font-size: 16; " +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: white;";

        Label serverIp = new Label("Server IP:");
        serverIp.setStyle(commonLabelStyle);
        TextField serverField = new TextField();
        serverField.setMaxWidth(220);
        serverField.setStyle(commonTextFieldStyle);
        grid.add(serverIp, 0, 1);
        grid.add(serverField, 0, 2);

        Button targetServerButton = getButton(serverField, vBox);
        grid.add(targetServerButton, 1, 2);

        Label emailLabel = new Label("NickName/Email:");
        emailLabel.setStyle(commonLabelStyle);
        TextField loginField = new TextField();
        loginField.setMaxWidth(220);
        loginField.setStyle(commonTextFieldStyle);
        grid.add(emailLabel, 0, 3);
        grid.add(loginField, 0, 4);
        Label passwordLabel = new Label("Password:");
        passwordLabel.setStyle(commonLabelStyle);
        PasswordField passwordField = new PasswordField();
        passwordField.setMaxWidth(220);
        passwordField.setStyle(commonTextFieldStyle);
        grid.add(passwordLabel, 1, 3);
        grid.add(passwordField, 1, 4);
        Button saveSessionButton = getButton(loginField, passwordField, vBox);
        initTimer();
        grid.add(saveSessionButton, 2, 4);

        Label crashLabel = new Label("Crash Method:");
        crashLabel.setStyle(commonLabelStyle);
        ChoiceBox<String> crashChoiceBox = new ChoiceBox<>();
        if(crashHandler.getAllMethods().isEmpty()) crashChoiceBox.getItems().add("Proxy not runned!");
        crashHandler.getAllMethods().forEach(s -> crashChoiceBox.getItems().add(s.getName()));
        crashChoiceBox.setPrefWidth(220);

        Button crashButton = getButton(crashChoiceBox);

        Button crashEditButton = getButton(vBox, crashChoiceBox);

        grid.add(crashLabel, 0, 5);
        grid.add(crashChoiceBox, 0, 6);
        grid.add(crashButton, 1, 6);
        grid.add(crashEditButton, 2, 6);

        Label otherLabel = new Label("Other:");
        otherLabel.setStyle(commonLabelStyle);

        Button clearLogsButton = getClearLogsButton();

        grid.add(otherLabel, 0, 7);
        grid.add(clearLogsButton, 1, 8);

        GridPane.setMargin(passwordLabel, new Insets(0, 0, 0, -70));
        GridPane.setMargin(passwordField, new Insets(0, 0, 0, -70));
        GridPane.setMargin(targetServerButton, new Insets(0, 0, 0, -70));
        GridPane.setMargin(crashButton, new Insets(0, 0, 0, -70));
        GridPane.setMargin(clearLogsButton, new Insets(0, 0, 0, -70));

        textArea = new TextArea("Proxy is now disabled! Set target server to run proxy server!");
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.getStyleClass().add("custom-text-area");
        textArea.setMaxWidth(650);
        textArea.setPrefHeight(120);

        vBox.getChildren().add(grid);
        vBox.getChildren().add(textArea);

        VBox.setMargin(textArea, new Insets(15, 0, 0, 25));
    }

    private @NotNull Button getClearLogsButton() {
        Button clearLogsButton = new Button("Clear Logs");
        clearLogsButton.setPrefWidth(220);
        clearLogsButton.setStyle(commonButtonStyle);
        clearLogsButton.setOnMouseEntered(e -> {
            timeline = new Timeline(new KeyFrame(Duration.millis(10), e2 -> {
                clearLogsButton.setStyle(commonButtonStyleHover);
            }));
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();
        });
        clearLogsButton.setOnMouseExited(e -> {
            timeline.stop();
            clearLogsButton.setStyle(commonButtonStyle);
        });
        clearLogsButton.setOnMouseClicked(e -> {
            XynisProxy.LOGS.clear();
            ProxyLogger.send("Logs was cleared!", LogType.INFO);
        });
        return clearLogsButton;
    }

    private @NotNull Button getButton(TextField serverField, VBox vBox) {
        Button targetServerButton = new Button("Set Target Server");
        targetServerButton.setPrefWidth(220);
        targetServerButton.setStyle(commonButtonStyle);
        targetServerButton.setOnMouseEntered(e -> {
            timeline = new Timeline(new KeyFrame(Duration.millis(10), e2 -> {
                targetServerButton.setStyle(commonButtonStyleHover);
            }));
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();
        });
        targetServerButton.setOnMouseExited(e -> {
            timeline.stop();
            targetServerButton.setStyle(commonButtonStyle);
        });
        targetServerButton.setOnMouseClicked(e -> {
            if(!XynisProxy.IS_ENABLED) new Thread(XynisProxy::loadProxy).start();
            String serverIp = serverField.getText();
            if(serverIp != null && !serverIp.isEmpty()) {
                String xynisProxyIp = XynisProxy.HOST + ":" + XynisProxy.PORT;
                ServerList serverlist = new ServerList(mc);
                serverlist.load();
                if (serverlist.get(xynisProxyIp) == null) {
                    serverlist.add(new ServerData(ColorUtil.format("&b&lXynis&f&lProxy"), xynisProxyIp, false), false);
                    serverlist.save();
                }
                if (serverIp.contains(":")) {
                    String[] address = serverIp.split(":");
                    optionsRep.setServerIp(address[0]);
                    optionsRep.setServerPort(Integer.parseInt(address[1]));
                } else {
                    optionsRep.setServerIp(serverIp);
                    optionsRep.setServerPort(25565);
                }
                ProxyLogger.send("Target IP was saved in proxy!", LogType.INFO);
                vBox.getChildren().clear();
                new BotsRenderer().renderOptions(vBox);
            }
        });
        return targetServerButton;
    }

    private @NotNull Button getButton(ChoiceBox<String> crashChoiceBox) {
        Button crashButton = new Button("Send Crash Packet");
        crashButton.setPrefWidth(220);
        crashButton.setStyle(commonButtonStyle);
        crashButton.setOnMouseEntered(e -> {
            timeline = new Timeline(new KeyFrame(Duration.millis(10), e2 -> {
                crashButton.setStyle(commonButtonStyleHover);
            }));
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();
        });
        crashButton.setOnMouseExited(e -> {
            timeline.stop();
            crashButton.setStyle(commonButtonStyle);
        });
        crashButton.setOnMouseClicked(e ->
                crashHandler.getCrashClassByName(crashChoiceBox.getValue())
                        .ifPresent(c ->
                                crashHandler.handleMethod(c.getName())));
        return crashButton;
    }

    private @NotNull Button getButton(TextField loginField, PasswordField passwordField, VBox vBox) {
        Button saveSessionButton = new Button("Save Session");
        saveSessionButton.setPrefWidth(200);
        saveSessionButton.setStyle(commonButtonStyle);
        saveSessionButton.setOnMouseEntered(e -> {
            timeline = new Timeline(new KeyFrame(Duration.millis(10), e2 -> {
                saveSessionButton.setStyle(commonButtonStyleHover);
            }));
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();
        });
        saveSessionButton.setOnMouseExited(e -> {
            timeline.stop();
            saveSessionButton.setStyle(commonButtonStyle);
        });
        saveSessionButton.setOnMouseClicked(e -> {
            if(!XynisProxy.IS_ENABLED) new Thread(XynisProxy::loadProxy).start();
            optionsRep.setUsername(loginField.getText().isEmpty() ? mc.getUser().getName() : loginField.getText());
            optionsRep.setPassword(passwordField.getText());
            ProxyLogger.send("Your session was saved in proxy!", LogType.INFO);
            vBox.getChildren().clear();
            new BotsRenderer().renderOptions(vBox);
        });
        return saveSessionButton;
    }

    private @NotNull Button getButton(VBox vBox, ChoiceBox<String> crashChoiceBox) {
        Button crashEditButton = new Button("Edit Crash Config");
        crashEditButton.setPrefWidth(200);
        crashEditButton.setStyle(commonButtonStyle);
        crashEditButton.setOnMouseEntered(e -> {
            timeline = new Timeline(new KeyFrame(Duration.millis(10), e2 -> {
                crashEditButton.setStyle(commonButtonStyleHover);
            }));
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();
        });
        crashEditButton.setOnMouseExited(e -> {
            timeline.stop();
            crashEditButton.setStyle(commonButtonStyle);
        });
        crashEditButton.setOnMouseClicked(e -> {
            crashHandler.getCrashClassByName(crashChoiceBox.getValue()).ifPresent(c -> {
                vBox.getChildren().clear();
                botConfigRenderer.renderModule(vBox, c.getName(), c.getArgsName(), c.getArgsType(), true);

            });
        });
        return crashEditButton;
    }

    private void initTimer() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(10), e -> {
            commonButtonStyleHover = "-fx-background-radius: 15 15 15 15;" +
                    "-fx-background-color: rgba(25, 25, 25);" +
                    "-fx-font-size: 14; " +
                    "-fx-font-weight: bold; " +
                    "-fx-text-fill: " + StyleUtil.getFinalColor();
            if(logsSize !=  XynisProxy.LOGS.size()) {
                StringBuilder builder = new StringBuilder();
                for (int i = XynisProxy.LOGS.size() - 1; i > 0; i--) builder.append(XynisProxy.LOGS.get(i));
                textArea.setText(builder.toString());
                logsSize =  XynisProxy.LOGS.size();
            }
        }
        ));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

}

package us.whitedev.gui.clickgui.components.modules;

import com.mojang.authlib.GameProfile;
import fr.litarvan.openauth.microsoft.MicrosoftAuthResult;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticationException;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticator;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.LinearGradient;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.User;
import org.jetbrains.annotations.NotNull;
import us.whitedev.gui.clickgui.utils.StyleUtil;
import us.whitedev.helpers.AccountHelper;

import java.io.File;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import java.util.List;

public class AltManagerRenderer {

    private Timeline timeline;
    private final Minecraft mc = Minecraft.getInstance();
    private static String lastRefreshToken;
    private final AccountHelper accountHelper = new AccountHelper();
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
        Label title = new Label("AltManager");
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

    public void renderAltManager(VBox vBox, String titleString) {
        renderTitle(vBox);
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 130, 10));
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);

        Label title = new Label(titleString);
        if (titleString.length() < 30)
            title.setPadding(new Insets(0, 0, 0, 120));
        else
            title.setPadding(new Insets(0, 0, 0, 80));
        String commonTextStyle = "-fx-font-size: 16; " +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: white;";
        title.setStyle(commonTextStyle);

        Label nickname = new Label("NickName:");
        nickname.setStyle(commonTextStyle);
        TextField nicknameField = new TextField();
        nicknameField.setPrefWidth(350);
        nicknameField.setStyle("-fx-background-radius: 15 15 15 15;" +
                "-fx-background-color: rgba(25, 25, 25);" +
                "-fx-font-size: 14; " +
                "-fx-font-weight: bold; " +
                "-fx-text-fill: white;");

        grid.add(title, 0, 0);
        grid.add(nickname, 0, 1);
        grid.add(nicknameField, 0, 2);

        Label accountsLabel = new Label("Accounts:");
        accountsLabel.setStyle(commonTextStyle);
        ChoiceBox<String> choiceBox = new ChoiceBox<>();
        choiceBox.getItems().addAll(accountHelper.getAccountsName());
        choiceBox.setPrefWidth(270);

        HBox choiceBoxContainer = getAccChoiceBox(choiceBox, vBox);

        grid.add(accountsLabel, 0, 3);
        grid.add(choiceBoxContainer, 0, 4);

        GridPane grid2 = new GridPane();
        grid2.setPadding(new Insets(10, 10, 10, 10));
        grid2.setHgap(10);
        grid2.setVgap(10);
        Button microsoftButton = getMicrosoftButton(vBox);
        Button offlineButton = getOfflineButton(vBox, nicknameField);
        Button saveButton = getSaveButton(vBox);
        Button cookieButton = getCookieButton(vBox);

        grid2.add(microsoftButton, 0, 0);
        grid2.add(offlineButton, 1, 0);
        grid2.add(saveButton, 2, 0);
        grid2.add(cookieButton, 0, 1);
        grid.add(grid2, 0, 6);

        GridPane mainGrid = new GridPane();
        mainGrid.setPadding(new Insets(10, 10, 10, 10));
        mainGrid.setHgap(10);
        mainGrid.setVgap(10);

        Image image = new Image("https://mc-heads.net/body/" + Minecraft.getInstance().getUser().getName());
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(250);
        imageView.setFitWidth(100);

        Region topEmptyRegion = new Region();
        topEmptyRegion.setPrefSize(1, 90);

        mainGrid.add(topEmptyRegion, 0, 0);
        GridPane.setMargin(imageView, new Insets(0, 50, 100, 50));
        mainGrid.add(imageView, 0, 1);
        mainGrid.add(grid, 1, 1);

        initTimer();
        vBox.getChildren().addAll(mainGrid);

    }

    private @NotNull HBox getAccChoiceBox(ChoiceBox<String> choiceBox, VBox vBox) {
        Button loginButton = new Button("Login");
        loginButton.setPrefWidth(80);
        loginButton.setStyle(commonButtonStyle);
        loginButton.setOnMouseEntered(e -> {
            timeline = new Timeline(new KeyFrame(Duration.millis(10), e2 -> {
                loginButton.setStyle(commonButtonStyleHover);
            }));
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();
        });
        loginButton.setOnMouseExited(e -> {
            timeline.stop();
            loginButton.setStyle(commonButtonStyle);
        });
        loginButton.setOnMouseClicked(e -> {
            String selectedAcc = choiceBox.getSelectionModel().getSelectedItem();
            String token = accountHelper.getToken(selectedAcc);
            if (token != null && !token.isEmpty()) {
                new Thread(() -> {
                    try {
                        CookieManager cookieManager = (CookieManager) CookieHandler.getDefault();
                        if (cookieManager != null) cookieManager.getCookieStore().removeAll();
                        MicrosoftAuthResult result;
                        MicrosoftAuthenticator authenticator = new MicrosoftAuthenticator();
                        result = authenticator.loginWithRefreshToken(token);
                        loginByMicrosoft(result, vBox);
                    } catch (MicrosoftAuthenticationException ex) {
                        Platform.runLater(() -> {
                            vBox.getChildren().clear();
                            renderAltManager(vBox, "Cannot login to this account!");
                        });
                        ex.printStackTrace();
                    }
                }).start();
            } else {
                vBox.getChildren().clear();
                renderAltManager(vBox, "There is no selected acc to remove!");
            }
        });
        return getRemoveAccButton(choiceBox, loginButton, vBox);
    }

    private @NotNull HBox getRemoveAccButton(ChoiceBox<String> choiceBox, Button loginButton, VBox vBox) {
        Button removeButton = new Button("Remove");
        removeButton.setPrefWidth(80);
        removeButton.setStyle(commonButtonStyle);
        removeButton.setOnMouseEntered(e -> {
            timeline = new Timeline(new KeyFrame(Duration.millis(10), e2 -> {
                removeButton.setStyle(commonButtonStyleHover);
            }));
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();
        });
        removeButton.setOnMouseExited(e -> {
            timeline.stop();
            removeButton.setStyle(commonButtonStyle);
        });
        removeButton.setOnMouseClicked(e -> {
            String selectedAcc = choiceBox.getSelectionModel().getSelectedItem();
            vBox.getChildren().clear();
            if (selectedAcc != null && !selectedAcc.isEmpty()) {
                accountHelper.removeAccount(selectedAcc);
                renderAltManager(vBox, "Account " + selectedAcc + " was removed correctly!");
            } else {
                renderAltManager(vBox, "There is no selected acc to remove!");
            }
        });
        HBox choiceBoxContainer = new HBox(10, choiceBox, loginButton, removeButton);
        choiceBoxContainer.setAlignment(Pos.CENTER);
        return choiceBoxContainer;
    }

    private @NotNull Button getSaveButton(VBox vBox) {
        Button saveButton = new Button("Save Account");
        saveButton.setPrefWidth(200);
        saveButton.setStyle(commonButtonStyle);
        saveButton.setOnMouseEntered(e -> {
            timeline = new Timeline(new KeyFrame(Duration.millis(10), e2 -> {
                saveButton.setStyle(commonButtonStyleHover);
            }));
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();
        });
        saveButton.setOnMouseExited(e -> {
            timeline.stop();
            saveButton.setStyle(commonButtonStyle);
        });
        saveButton.setOnMouseClicked(e -> {
            vBox.getChildren().clear();
            if (lastRefreshToken != null) {
                String userName = mc.getUser().getName();
                accountHelper.addNewAccount(userName, lastRefreshToken);
                renderAltManager(vBox, "Account " + userName + " was saved correctly!");
            } else {
                renderAltManager(vBox, "There is no microsoft token to save!");
            }
        });
        return saveButton;
    }

    private @NotNull Button getOfflineButton(VBox vBox, TextField nameTextField) {
        Button offlineButton = new Button("Offline Login");
        offlineButton.setPrefWidth(200);
        offlineButton.setStyle(commonButtonStyle);
        offlineButton.setOnMouseEntered(e -> {
            timeline = new Timeline(new KeyFrame(Duration.millis(10), e2 -> {
                offlineButton.setStyle(commonButtonStyleHover);
            }));
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();
        });
        offlineButton.setOnMouseExited(e -> {
            timeline.stop();
            offlineButton.setStyle(commonButtonStyle);
        });
        offlineButton.setOnMouseClicked(e -> {
            offlineLogin(vBox, nameTextField.getText());
        });
        return offlineButton;
    }

    // Thanks to pukas06 that he change the microsoft login system to login by a site

    private @NotNull Button getMicrosoftButton(VBox vBox) {
        Button microsoftButton = new Button("Microsoft Login");
        microsoftButton.setPrefWidth(200);
        microsoftButton.setStyle(commonButtonStyle);
        microsoftButton.setOnMouseEntered(e -> {
            timeline = new Timeline(new KeyFrame(Duration.millis(10), e2 -> {
                microsoftButton.setStyle(commonButtonStyleHover);
            }));
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();
        });
        microsoftButton.setOnMouseExited(e -> {
            timeline.stop();
            microsoftButton.setStyle(commonButtonStyle);
        });

        microsoftButton.setOnMouseClicked(e -> {
            new Thread(() -> {
                try {
                    CookieManager cookieManager = (CookieManager) CookieHandler.getDefault();
                    if (cookieManager != null) cookieManager.getCookieStore().removeAll();
                    MicrosoftAuthResult result;
                    MicrosoftAuthenticator authenticator = new MicrosoftAuthenticator();
                    result = authenticator.loginWithAsyncWebview().get();
                    loginByMicrosoft(result, vBox);
                } catch (InterruptedException | ExecutionException ex) {
                    Platform.runLater(() -> {
                        vBox.getChildren().clear();
                        renderAltManager(vBox, "Cannot login to this account (Enabled double-authentication)!");
                    });
                    ex.printStackTrace();
                }
            }).start();
        });
        return microsoftButton;
    }

    private @NotNull Button getCookieButton(VBox vBox) {
        Button cookieLogin = new Button("Cookie Login");
        cookieLogin.setPrefWidth(200);
        cookieLogin.setStyle(commonButtonStyle);
        cookieLogin.setOnMouseEntered(e -> {
            timeline = new Timeline(new KeyFrame(Duration.millis(10), e2 -> {
                cookieLogin.setStyle(commonButtonStyleHover);
            }));
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();
        });
        cookieLogin.setOnMouseExited(e -> {
            timeline.stop();
            cookieLogin.setStyle(commonButtonStyle);
        });

        cookieLogin.setOnMouseClicked(e -> {
            try {
                File file = accountHelper.initFileSelector();
                List<String> cookieList = accountHelper.getCookiesList(file);
                List<String> accountData = accountHelper.fetchUserDetails(cookieList);
                if (accountData != null && !accountData.isEmpty()) {
                    User user = new User(accountData.get(0), accountData.get(1), accountData.get(2), Optional.of(""), Optional.of(""), User.Type.LEGACY);
                    Minecraft.getInstance().setUser(user);
                    GameProfile gameprofile = Minecraft.getInstance().getMinecraftSessionService().fillProfileProperties(user.getGameProfile(), false);
                    Minecraft.getInstance().profileProperties.clear();
                    Minecraft.getInstance().profileProperties.putAll(gameprofile.getProperties());
                    vBox.getChildren().clear();
                    renderAltManager(vBox, "Logged in (" + accountData.get(0) + ")");
                } else {
                    vBox.getChildren().clear();
                    renderAltManager(vBox, "Failed to login.");
                }
            } catch (Exception ex) {
                vBox.getChildren().clear();
                renderAltManager(vBox, "Failed to update user profile.");
                ex.printStackTrace();
            }
        });
        return cookieLogin;
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

    public void loginByMicrosoft(MicrosoftAuthResult result, VBox vBox) {
        if (result.getAccessToken() != null) {
            Platform.runLater(() -> {
                try {
                    User user = new User(result.getProfile().getName(), result.getProfile().getId(), result.getAccessToken(), Optional.of(""), Optional.of(""), User.Type.LEGACY);
                    Minecraft.getInstance().setUser(user);
                    GameProfile gameprofile = Minecraft.getInstance().getMinecraftSessionService().fillProfileProperties(user.getGameProfile(), false);
                    Minecraft.getInstance().profileProperties.clear();
                    Minecraft.getInstance().profileProperties.putAll(gameprofile.getProperties());
                    lastRefreshToken = result.getRefreshToken();
                    vBox.getChildren().clear();
                    renderAltManager(vBox, "Logged in (" + result.getProfile().getName() + ")");
                } catch (Exception ex) {
                    vBox.getChildren().clear();
                    renderAltManager(vBox, "Failed to update user profile.");
                    ex.printStackTrace();
                }
            });
        } else {
            Platform.runLater(() -> {
                vBox.getChildren().clear();
                renderAltManager(vBox, "Disconnect from the server and then use this option");
            });
        }
    }

    public void offlineLogin(VBox vBox, String name) {
        try {
            User user = new User(name, "", "", Optional.of(""), Optional.of(""), User.Type.MSA);
            Minecraft.getInstance().setUser(user);
            GameProfile gameprofile = Minecraft.getInstance().getMinecraftSessionService().fillProfileProperties(user.getGameProfile(), false);
            Minecraft.getInstance().profileProperties.clear();
            Minecraft.getInstance().profileProperties.putAll(gameprofile.getProperties());
            vBox.getChildren().clear();
            renderAltManager(vBox, "Logged in (" + name + ")");
        } catch (Exception ex) {
            vBox.getChildren().clear();
            renderAltManager(vBox, "Cant login to this account!");
            ex.printStackTrace();
        }
    }

          }

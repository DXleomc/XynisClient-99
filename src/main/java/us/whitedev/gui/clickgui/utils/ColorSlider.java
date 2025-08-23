package us.whitedev.gui.clickgui.utils;

import javafx.scene.control.Slider;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ColorSlider extends StackPane {

    private final Slider slider;

    public ColorSlider(int min, int max, int value) {
        slider = new Slider(min, max, value);
        slider.setId("color-slider");

        Rectangle progressRec = new Rectangle();
        progressRec.heightProperty().bind(slider.heightProperty().subtract(7));
        progressRec.widthProperty().bind(slider.widthProperty());

        progressRec.setFill(Color.color(0.24, 0.24, 0.24));

        progressRec.setArcHeight(15);
        progressRec.setArcWidth(15);

        slider.valueProperty().addListener((ov, old_val, new_val) -> {
            String style = String.format("-fx-fill: linear-gradient(to right, rgba(75, 75, 75) %d%%, rgba(25, 25, 25) %d%%);",
                    new_val.intValue() * 3, new_val.intValue() * 3);
            progressRec.setStyle(style);
        });

        getChildren().addAll(progressRec, slider);
    }

    public double getSliderValue(){
        return slider.getValue();
    }
}

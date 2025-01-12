package domein;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import java.awt.image.BufferedImage;

public class ImageConverter {


    public static Image convertToFxImage(java.awt.Image awtImage) {
        // Convert java.awt.Image to BufferedImage if needed
        BufferedImage bufferedImage;
        if (awtImage instanceof BufferedImage) {
            bufferedImage = (BufferedImage) awtImage;
        } else {
            // Create a BufferedImage with the same dimensions and draw the awtImage into it
            bufferedImage = new BufferedImage(
                    awtImage.getWidth(null),
                    awtImage.getHeight(null),
                    BufferedImage.TYPE_INT_ARGB
            );
            bufferedImage.getGraphics().drawImage(awtImage, 0, 0, null);
        }

        // Convert BufferedImage to JavaFX Image
        return SwingFXUtils.toFXImage(bufferedImage, null);
    }
}

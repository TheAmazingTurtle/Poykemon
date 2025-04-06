import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class ImageSplitter {
    public static void main(String[] args) {
        try {

            char[] directions = {'U', 'D', 'L', 'R'};

            for (int i = 0; i < 4; i++){
                for (int j = 0; j < 4; j++){
                    File inputFile = new File(directions[i] + "-" + j + ".png"); // Replace with your image path
                    BufferedImage original = ImageIO.read(inputFile);

                    // Get dimensions
                    int width = original.getWidth();

                    // Create top and bottom images
                    BufferedImage img1 = original.getSubimage(0, 0, width, 34);

                    // Save the split images (optional)
                    ImageIO.write(img1, "png", new File(directions[i] + "-" + j + "-H" + ".png"));

                    System.out.println("Image split successfully!");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
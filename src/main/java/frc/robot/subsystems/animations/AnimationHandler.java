package frc.robot.subsystems.animations;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import edu.wpi.first.wpilibj.Filesystem;
public class AnimationHandler {
    public static int animationFrames;
    
    public static Integer[][][][] getAnimation(String pathname){
        BufferedImage[] bufferedImages;
        Integer[][][][] twoDarrayList = new Integer[0][0][0][0];
        File input = new File(Filesystem.getDeployDirectory()+ "/" + pathname);
        try {
            bufferedImages = getFramesFromGif(input);
        } catch (Exception e) {
            bufferedImages = new BufferedImage[0];
        }
        
        //twoDarrayList[frame][row][col][r, g, b index (0, 1, 2)]
        twoDarrayList = convertTo2DUsingGetRGB(bufferedImages);
        return twoDarrayList;
    }

    
    private static BufferedImage[] getFramesFromGif(File gifFile) throws IOException {
        List<BufferedImage> frames = new ArrayList<>();
        ImageInputStream imageStream = ImageIO.createImageInputStream(gifFile);
        Iterator<ImageReader> readers = ImageIO.getImageReaders(imageStream);

        if (readers.hasNext()) {
            ImageReader reader = readers.next();
            reader.setInput(imageStream);
            int numFrames = reader.getNumImages(true);

            for (int i = 0; i < numFrames; i++) {
                BufferedImage frame = reader.read(i);
                frames.add(frame);
            }
        }
        return frames.toArray(new BufferedImage[0]);
    }


    private static Integer[][][][] convertTo2DUsingGetRGB(BufferedImage[] image) {
        List<Integer[][][]> pixelsPerFrame = new ArrayList<>();
        int width = image[0].getWidth();
        int height = image[0].getHeight();
        for(int frames = 0; frames < image.length; frames++){
            Integer[][][] result = new Integer[height][width][];
            for (int row = 0; row < height; row++) {
                for (int col = 0; col < width; col++) {
                    Integer[] pixel = intToRGB(image[frames].getRGB(col, row));
                    result[row][col] = pixel;
                }
            }
            pixelsPerFrame.add(result);
        }

        return pixelsPerFrame.toArray(new Integer[0][0][0][0]) ;
    }


    private static Integer[] intToRGB(int hex){
        int red = (hex & 0x00ff0000) >> 16;
        int green = (hex & 0x0000ff00) >> 8;
        int blue = hex & 0x000000ff;
        Integer[] RGB = {red, green, blue};
        return RGB;
    }
}

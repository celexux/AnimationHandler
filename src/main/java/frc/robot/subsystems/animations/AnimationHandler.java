package frc.robot.subsystems.animations;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.ToDoubleBiFunction;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import edu.wpi.first.wpilibj.Filesystem;
public class AnimationHandler {
    public static int animationFrames;
    
    public static Integer[][][][] getAnimation(String pathname){
        String path = pathname;
        ArrayList<BufferedImage> bufferedImages;
        if(pathname == null) path = "coinanimation.gif";
        
        File input = new File(Filesystem.getDeployDirectory()+ "/" + path);
        try {
            bufferedImages = convertGifToBufferedImages(input);
        } catch (Exception e) {
            bufferedImages = new ArrayList<BufferedImage>();
        }
        
        animationFrames = bufferedImages.size();
        //twoDarrayList[frame][row][col][r, g, b index (0, 1, 2)]
        Integer[][][][] twoDarrayList = convertTo2DUsingGetRGB(bufferedImages).toArray(new Integer[animationFrames][16][16][3]);
        return twoDarrayList;
            
    }

    

    private static ArrayList<BufferedImage> convertGifToBufferedImages(File gifFile) throws IOException{
        ArrayList<BufferedImage> frames = new ArrayList<>();
        try (ImageInputStream input = ImageIO.createImageInputStream(gifFile)) {
            Iterator<ImageReader> readers = ImageIO.getImageReaders(input);
            if (!readers.hasNext()) {
                throw new IllegalArgumentException("No reader found for given format");
            }

            ImageReader reader = readers.next();
            reader.setInput(input);

            int numImages = reader.getNumImages(true);
            for (int i = 0; i < numImages; i++) {
                BufferedImage frame = reader.read(i);
                frames.add(frame);
            }
        }
        return frames;
    }


    private static ArrayList<Integer[][][]> convertTo2DUsingGetRGB(ArrayList<BufferedImage> image) {
        ArrayList<Integer[][][]> pixelsPerFrame = new ArrayList<Integer[][][]>();
        /*int width = image.get(0).getWidth();
        int height = image.get(0).getHeight();*/
        int width = 16;
        int height = 16;
        Integer[][][] result = new Integer[height][width][];
        for(int frames = 0; frames < image.size(); frames++){
            for (int row = 0; row < height; row++) {
                for (int col = 0; col < width; col++) {
                    Integer[] pixel = intToRGB(image.get(frames).getRGB(col, row));
                    result[row][col] = pixel;
                }
            }
            pixelsPerFrame.add(result);
        }
        return pixelsPerFrame;
    }


    private static Integer[] intToRGB(int hex){
        int red = (hex & 0x00ff0000) >> 16;
        int green = (hex & 0x0000ff00) >> 8;
        int blue = hex & 0x000000ff;
        Integer[] RGB = {red, green, blue};
        return RGB;
    }
}

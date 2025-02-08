package frc.robot.subsystems.animations;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.ToDoubleBiFunction;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import edu.wpi.first.wpilibj.Filesystem;
public class AnimationHandler {
    public static int animationFrames;
    
    public static Integer[][][][] getAnimation(String pathname){
        String path = pathname;
        if(pathname == null) path = "coinanimation.gif";
        
        File input = new File(Filesystem.getDeployDirectory()+ "/" + path);
        ArrayList<BufferedImage> bufferedImages = convertGIFtoBufferedImages(input);
        animationFrames = bufferedImages.size();
        Integer[][][][] twoDarrayList = convertTo2DUsingGetRGB(bufferedImages).toArray(new Integer[animationFrames][15][15][]);
        return twoDarrayList;
            
    }

    //twoDarrayList[frame in gif][x][y][r, g, b index (0, 1, 2)]


    private static ArrayList<BufferedImage> convertGIFtoBufferedImages(File input){
        ArrayList<BufferedImage> imageList = new ArrayList<BufferedImage>();

        try {
            ImageReader reader = ImageIO.getImageReadersByFormatName("gif").next();
            ImageInputStream stream = ImageIO.createImageInputStream(new FileInputStream(input));
            reader.setInput(stream);
            
            int count = reader.getNumImages(true);
            for (int index = 0; index < count; index++) {
                BufferedImage frame = reader.read(index);
                imageList.add(frame);
            }
            
        } catch (IOException ex) {
            // An I/O problem has occurred
            System.out.println("An exception has occured:");
            System.err.println(ex);
        }
        return imageList;
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
                    Integer[] tmp = intToRGB(image.get(frames).getRGB(col, row));

                    /*if(tmp.length() == 5){
                        System.out.println(tmp);
                        tmp+="0";

                    }*/
                    result[row][col] = tmp;
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

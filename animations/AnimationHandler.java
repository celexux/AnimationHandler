//package frc.robot.subsystems.animations;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
public class AnimationHandler {
    public static void main(String[] args){
        File input = new File("coinanimation.gif");
        ArrayList<BufferedImage> bufferedImages = convertGIFtoBufferedImages(input);
        /*ArrayList<Integer[][]> twoDArrayList = convertTo2DWithoutUsingGetRGB(bufferedImages);
        */
        //BufferedImage[][][] bufferedImages = convertGIFtoBufferedImages(input).toArray();

        //twoDarrayList [frame#] [x] [y]
        String[][][] twoDarrayList = convertTo2DUsingGetRGB(bufferedImages).toArray(new String[bufferedImages.size()][15][15]);
        System.out.println("First image: " + bufferedImages.get(0));
        System.out.println(Arrays.toString(twoDarrayList[4][5]));
        /*for(int i = 0; i < twoDarrayList[0].length; i++){
            System.out.println(i + ": " + twoDarrayList[8][0][i]);
        }
        //System.out.println("Second arraylist: " + twoDArrayList.get(1));*/
        
    }


    private static ArrayList<BufferedImage> convertGIFtoBufferedImages(File input){
        ArrayList<BufferedImage> imageList = new ArrayList<BufferedImage>();

        try {
            ImageReader reader = ImageIO.getImageReadersByFormatName("gif").next();
            ImageInputStream stream = ImageIO.createImageInputStream(input);
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

    private static ArrayList<String[][]> convertTo2DUsingGetRGB(ArrayList<BufferedImage> image) {
        ArrayList<String[][]> pixelsPerFrame = new ArrayList<String[][]>();
        int width = image.get(0).getWidth();
        int height = image.get(0).getHeight();
        String[][] result = new String[height][width];
        for(int frames = 0; frames < image.size(); frames++){
            for (int row = 0; row < height; row++) {
                for (int col = 0; col < width; col++) {
                    String tmp = intToInvertedHex(image.get(frames).getRGB(col, row));
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

    /*
    private static ArrayList<String[][]> convertTo2DWithoutUsingGetRGB(ArrayList<BufferedImage> image) {
        ArrayList<Integer[][]> pixelsPerFrame = new ArrayList<Integer[][]>();
        final Integer width = image.get(0).getWidth();
        final Integer height = image.get(0).getHeight();
        final boolean hasAlphaChannel = image.get(0).getAlphaRaster() != null;
        for(int frames = 0; frames < image.size(); frames++){
            final byte[] pixels = ((DataBufferByte) image.get(frames).getRaster().getDataBuffer()).getData();
            
            Integer[][] result = new Integer[height][width];
            if (hasAlphaChannel) {
            final Integer pixelLength = 4;
            for (Integer pixel = 0, row = 0, col = 0; pixel + 3 < pixels.length; pixel += pixelLength) {
                Integer argb = 0;
                argb += Integer.valueOf(((int) pixels[pixel] & 0xff) << 24); // alpha
                argb += Integer.valueOf((int) pixels[pixel + 1] & 0xff); // blue
                argb += Integer.valueOf(((int) pixels[pixel + 2] & 0xff) << 8); // green
                argb += Integer.valueOf(((int) pixels[pixel + 3] & 0xff) << 16); // red
                result[row][col] = argb;
                if(result[row][col] == null){
                    System.out.println("ERRORR EROEOR ERJ EROEOREOR");
                }
                col++;
                if (col == width) {
                    col = 0;
                    row++;
                }
            }
            } else {
            final Integer pixelLength = 3;
            for (Integer pixel = 0, row = 0, col = 0; pixel + 2 < pixels.length; pixel += pixelLength) {
                Integer argb = 0;
                argb += -16777216; // 255 alpha
                argb += Integer.valueOf((int) pixels[pixel] & 0xff); // blue
                argb += Integer.valueOf(((int) pixels[pixel + 1] & 0xff) << 8); // green
                argb += Integer.valueOf(((int) pixels[pixel + 2] & 0xff) << 16); // red
                result[row][col] = argb;
                if(result[row][col] == null){
                    System.out.println("ERRORR EROEOR ERJ EROEOREOR");
                }
                col++;
                if (col == width) {
                    col = 0;
                    row++;
                }
            }
            }
            pixelsPerFrame.add(result);
        }
        return pixelsPerFrame;
     }
    */
    private static String intToInvertedHex(int RGB) {
        String hex = Integer.toString(RGB);
        
        //hex = hex.substring(hex.length()-6);

        long decimal = Long.parseLong(hex, 16);
        long invertedDecimal = ~decimal;
        return Long.toHexString(invertedDecimal).toUpperCase();
    }

}

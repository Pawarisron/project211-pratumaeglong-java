package ku.cs.services;

import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class ImageFileDataSource {

    private BufferedImage imageFound;

    public int width;
    public int height;

    private boolean isReadDone;
    private final String directory;

    public ImageFileDataSource(String directoryName) {
        width = 1000;
        height = 1000;
        this.directory =  "data" + File.separator + "image"  + File.separator + directoryName;
        checkFileIsExisted();
        isReadDone = false;
    }
    public ImageFileDataSource(int width, int height , String  directoryName){
        this.width = width;
        this.height = height;
        this.directory =  "data" + File.separator + "image"  + File.separator + directoryName;
        checkFileIsExisted();
        isReadDone = false;
    }

    public void readData(File imageFile) {
        //ส่วนของการ อ่านไฟล์รูป
        //เอาไฟล์ path จาก FileChooser
        assert imageFile != null : "imageFile == null";

        //สร้างและกําหนด format
        imageFound = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        try {
            //อ่านไฟล์
            imageFound = ImageIO.read(imageFile);
            if(imageFound.getWidth() > width || imageFound.getHeight() > height){
                System.out.println("width > " +width );
                System.out.println("height > "+height);
                imageFound = null;
                isReadDone = false;
                return;
            }
            System.out.println("Reading complete. : " + imageFile.getPath());
            isReadDone = true;

        } catch (IOException e) {
            System.out.println("อ่านไฟล์ไม่ได้");
            System.out.println("ลองเช็คว่า Path ถูกต้องมั้ย");
            System.out.println(e);
            isReadDone = false;
        }
    }

    public void writeData(String fileName) {
        //ส่วนของการ เขียนไฟล์รูป
        String outputPath = directory + File.separator + fileName + ".jpg";

        File outputImageFile = new File(outputPath);
        try {
            //เขียนไฟล์
            if( imageFound != null && !Objects.equals(fileName, "")) {
                ImageIO.write(imageFound, "jpg", outputImageFile);
                System.out.println("Writing complete. : " + outputPath);
            } else {
                System.out.println("imageFound == null");
            }


        } catch (IOException e) {
            System.out.println("เขียนไฟล์ไม่ได้");
            System.out.println("ลองเช็คว่า Path ถูกต้องมั้ย");
            System.out.println(e);
        }
    }
    private void checkFileIsExisted() {
        File file = new File(directory);
        if (!file.exists()) {
            System.out.println("create directory : "+ directory);
            file.mkdirs();
        }
    }

    public BufferedImage getImageFound() {

        return imageFound;
    }

    public boolean isReadDone() {
        return isReadDone;
    }
}



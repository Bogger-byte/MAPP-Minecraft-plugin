package me.bogger.mapp.renderer;

import me.bogger.mapp.objects.RegionFile;
import me.bogger.mapp.objects.RegionImage;
import org.apache.commons.io.FilenameUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Renderer_1_16_5 extends Renderer {
    @Override
    public RegionImage render(RegionFile regionFile) throws IOException {
        BufferedImage bufferedImage = new BufferedImage(512, 512, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.setColor(Color.ORANGE);
        g2d.fillRect(0, 0, 512, 512);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", baos);
        byte[] data = baos.toByteArray();

        String name = FilenameUtils.removeExtension(regionFile.getName()) + ".png";
        return new RegionImage(data, name);
    }

    @Override
    public RegionImage[] renderQueue(RegionFile[] regionFiles) {
        List<RegionImage> imageList = new ArrayList<>();
        for (RegionFile regionFile : regionFiles) {
            try {
                RegionImage image = render(regionFile);
                imageList.add(image);
            } catch (IOException ignored) {}
        }
        RegionImage[] images = new RegionImage[imageList.size()];
        imageList.toArray(images);
        return images;
    }
}

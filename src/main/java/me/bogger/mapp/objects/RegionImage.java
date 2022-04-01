package me.bogger.mapp.objects;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class RegionImage {

    private final BufferedImage bufferedImage;
    private final String format;

    public RegionImage(BufferedImage bufferedImage, String format) {
        this.bufferedImage = bufferedImage;
        this.format = format;
    }

    public byte[] getBytes() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, format, baos);
        return baos.toByteArray();
    }
}

package ru.netology.graphics.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.net.URL;

public class Converter implements TextGraphicsConverter {
    private double maxRatio;
    private int height;
    private int width;
    private TextColorSchema schema;

    @Override
    public String convert(String url) throws IOException, BadImageSizeException {
        BufferedImage img = ImageIO.read(new URL(url));
        double ratio = (double) img.getHeight() / img.getWidth();

        if (ratio > maxRatio) {
            throw new BadImageSizeException(ratio, maxRatio);
        }

        double koeff;
        if ((img.getHeight() > img.getWidth() && img.getHeight() > height) || (img.getHeight() == img.getWidth() && img.getHeight() > height)) {
            koeff = (double) img.getHeight() / height;
        } else if (img.getWidth() > img.getHeight() && img.getWidth() > width) {
            koeff = (double) img.getWidth() / width;
        } else {
            koeff = 1;
        }

        int newWidth = (int) (img.getWidth() / koeff);
        int newHeight = (int) (img.getHeight() / koeff);

        Image scaledImage = img.getScaledInstance(newWidth, newHeight, BufferedImage.SCALE_SMOOTH);

        BufferedImage bwImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D graphics = bwImg.createGraphics();
        graphics.drawImage(scaledImage, 0, 0, null);
        WritableRaster bwRaster = bwImg.getRaster();

        String[][] colors = new String[bwImg.getWidth()][bwImg.getHeight()];

        for (int w = 0; w < bwImg.getWidth(); w++) {
            for (int h = 0; h < bwImg.getHeight(); h++) {
                int color = bwRaster.getPixel(w, h, new int[3])[0];
                char c;
                if (schema != null) {
                    c = schema.convert(color);
                } else {
                    c = new ColorSchema('#', '$', '@', '%', '*', '+', '-', '·').convert(color);
                }
                colors[w][h] = String.valueOf(c);
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int h = 0; h < bwImg.getHeight(); h++) {
            for (int w = 0; w < bwImg.getWidth(); w++) {
                String str = colors[w][h] + colors[w][h];
                stringBuilder.append(str);
            }
            stringBuilder.append("\n");
        }
        return String.valueOf(stringBuilder);
    }

    @Override
    public void setMaxWidth(int width) {
        this.width = width;
    }

    @Override
    public void setMaxHeight(int height) {
        this.height = height;
    }

    @Override
    public void setMaxRatio(double maxRatio) {
        this.maxRatio = maxRatio;
    }

    @Override
    public void setTextColorSchema(TextColorSchema schema) {
        this.schema = schema;
    }
}
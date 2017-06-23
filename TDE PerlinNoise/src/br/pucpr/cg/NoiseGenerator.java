package br.pucpr.cg;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

/**
 * Created by Lucas on 16/06/2017.
 */

public class NoiseGenerator {

    static float noise[][];


    public static double smoothNoise(double x, double y, int width, int height) {

        double fractX = x - (int) x;
        double fractY = y - (int) y;

        int x1 = (int) x + width % width;
        int y1 = (int) y + height % height;

        int x2 = (x1 + width - 1) % width;
        int y2 = (y1 + height - 1) % height;

        double value = 0.0;

        value += fractX * fractY * noise[y1][x1];
        value += (1 - fractX) * fractY * noise[y1][x2];
        value += fractX * (1 - fractY) * noise[y2][x1];
        value += (1 - fractX) * (1 - fractY) * noise[y2][x2];

        return value;
    }

    public static double turbulence(double x, double y, double size, int width, int height) {
        double value = 0.0, initialSize = size;

        while (size >= 1) {
            value += smoothNoise(x / size, y / size, width, height) * size;
            size /= 2.0;
        }
        return (128.0 * value / initialSize);
    }

    static void GeneratePerlin(int width, int height) {

        Random generator = new Random();
        noise = new float[width][height];
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < result.getHeight() - 1; y++) {
            for (int x = 0; x < result.getWidth() - 1; x++) {
                noise[x][y] = generator.nextFloat();
            }
        }
        for (int y = 0; y < result.getHeight() - 1; y++) {
            for (int x = 0; x < result.getWidth() - 1; x++) {
                int cor = (int) turbulence(x, y, 32, width, height);
                result.setRGB(x, y, new Color(cor, cor, cor).getRGB());
            }
        }
        try {
            ImageIO.write(result, "png", new File("perlin1.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run () {
		Scanner reader = new Scanner(System.in);
		System.out.println("Digite a largura desejada: ");
		int width = reader.nextInt();
		System.out.println("Digite a altura desejada: ");
		int height = reader.nextInt();

        GeneratePerlin(width, height);
    }
}

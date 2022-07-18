package ru.netology.graphics.image;

public class ColorSchema implements TextColorSchema {
    public ColorSchema(char c1, char c2, char c3, char c4, char c5, char c6, char c7, char c8) {
    }

    @Override
    public char convert(int color) {
        char[] simbols = {'#', '$', '@', '%', '*', '+', '-', '·'};
        int index = color * simbols.length / 256;
        char range = simbols[index];
        return range;
    }
}

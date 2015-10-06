package net.exkazuu.mimicdance;

public class Lessons {
    private static String[] coccoCodes = { // hard
        "左腕を上げる右腕を上げる\n左腕を下げる\n右腕を下げる",
        "左腕を上げる\n左腕を下げる\n右腕を上げる\n右腕を下げる\n左腕を上げる\n左腕を下げる\n右腕を上げる\n右腕を下げる\n左腕を上げる\n左腕を下げる\n右腕を上げる\n右腕を下げる",
        "くりかえし3\n左腕を上げる\n左腕を下げる\n右腕を上げる\n右腕を下げる\nここまで",
        "くりかえし3\n左腕を上げる\n左腕を下げる\nここまで\nくりかえし3\n右腕を上げる\n右腕を下げる\nここまで",
        "もしもしろ\n左腕を上げる\n左腕を下げる\nもしくは\n右腕を上げる\n右腕を下げる\nもしおわり",
        "右腕を上げる\nもしもしろ\n右腕を下げる\nもしくは\n左腕を上げる\nもしおわり",
        "くりかえし3\nもしもしろ\n右腕を上げる\n右腕を下げる\nもしくは\n左腕を上げる\n左腕を下げる\nもしおわり\nここまで",
        //"くりかえし2\nもしもしろ\n左腕を上げる\n左腕を下げる\nもしくは\n右腕を上げる\n右腕を下げる\nもしおわり\nここまで",
    };


    private static int[] maxSteps = {10, 12, 10, 8, 7, 10, 10};

    public static String getCoccoCode(int lessonNumber) {
        return coccoCodes[lessonNumber - 1];
    }

    public static int getMaxStep(int lessonNumber) {
        return maxSteps[lessonNumber - 1];
    }

    public static int getLessonCount() {
        return coccoCodes.length;
    }

    public static boolean hasLoop(int lessonNumber) {
        return coccoCodes[lessonNumber - 1].contains("くりかえし");
    }

    public static boolean hasIf(int lessonNumber) {
        return coccoCodes[lessonNumber - 1].contains("もしも");
    }
}

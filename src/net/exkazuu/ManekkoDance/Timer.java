package net.exkazuu.ManekkoDance;

public class Timer {
	private static long start;
	private static long end;

	public static void startTimer() {
		start = System.currentTimeMillis();
	}

	public static void stopTimer() {
		end = System.currentTimeMillis();
	}

	public static String getResultTime() {
		long t = (end - start) / 1000;
		long min = t / 60;
		long sec = t % 60;

		String result = "ƒ^ƒCƒ€‚Í" + min + "•ª" + sec + "•b‚Å‚·B";
		return result;
	}
}

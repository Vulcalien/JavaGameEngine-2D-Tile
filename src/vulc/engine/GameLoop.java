/*
 * This code is licensed under MIT License by Vulcalien (see LICENSE)
 */
package vulc.engine;

public class GameLoop {

	private static Thread thread;
	private static boolean running = false;

	public static void start() {
		if(running) return;
		running = true;
		thread = new Thread(GameLoop::run);
		thread.start();
	}

	public static void stop() {
		if(!running) return;
		running = false;
		try {
			thread.join();
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void run() {
		int ticksPerSecond = 60;
		int ticks = 0, fps = 0;

		long nanosPerTick = 1_000_000_000 / ticksPerSecond;
		long unprocessedNanos = 0;
		long lastTime = System.nanoTime();

		while(running) {
			long now = System.nanoTime();
			long passedTime = now - lastTime;
			lastTime = now;

			if(passedTime < 0) passedTime = 0;
			if(passedTime > 1_000_000_000) passedTime = 1_000_000_000;

			unprocessedNanos += passedTime;

			boolean ticked = false;
			while(unprocessedNanos >= nanosPerTick) {
				unprocessedNanos -= nanosPerTick;

				Game.tick();
				ticked = true;
				ticks++;

				if(ticks % ticksPerSecond == 0) {
					System.out.println(fps + " fps");
					fps = 0;
				}
			}

			if(ticked) {
				Game.frame.render();
				fps++;
			}

			try {
				Thread.sleep(4);
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}

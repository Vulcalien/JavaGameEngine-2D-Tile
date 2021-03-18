/*******************************************************************************
 * Copyright 2019-2021 Vulcalien
 * This code or part of it is licensed under MIT License by Vulcalien
 ******************************************************************************/
package vulc.engine;

import vulc.engine.gfx.GameFrame;
import vulc.engine.gfx.Screen;
import vulc.engine.input.InputHandler;
import vulc.engine.level.Level;

public abstract class Game {

	private Game() {
	}

	// the size of the game screen (not the Frame)
	public static final int WIDTH = 320, HEIGHT = 320;

	// the number of Frame's pixels that correspond to 1 pixel of the game screen
	public static final int SCALE = 1;

	public static final InputHandler INPUT = new InputHandler();

	protected static GameFrame frame;

	public static Level level;

	private static void init() {
		INPUT.init(frame.canvas);
	}

	public static void tick() {
		INPUT.tick();

		if(level != null) level.tick();
	}

	public static void render(Screen screen) {
		screen.clear(0x000000);

		if(level != null) {
			level.render(screen, 10, 10);
		}
	}

	public static void main(String[] args) {
		frame = new GameFrame("game name", WIDTH, HEIGHT, SCALE);
		frame.setVisible(true);

		init();
		GameLoop.start();
	}

}

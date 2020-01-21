/*******************************************************************************
 * This code or part of it is licensed under MIT License by Vulcalien
 ******************************************************************************/
package vulc.engine.gfx;

import vulc.bitmap.Bitmap;
import vulc.bitmap.IntBitmap;
import vulc.bitmap.font.Font;
import vulc.engine.Game;
import vulc.engine.level.Level;

public class Screen extends IntBitmap {

	public static final Font FONT = new Font(Screen.class.getResourceAsStream("/fonts/linkwriter-monospaced.lwfont"));

	private static final int BACKGROUND_COLOR = 0x000000;

	private final Game game;

	public int xOffset = 0, yOffset = 0;

	public Screen(Game game) {
		super(Game.WIDTH, Game.HEIGHT);
		this.game = game;
	}

	public void render() {
		clear(BACKGROUND_COLOR);

		Level level = game.level;
		if(level != null) {
			level.render(this, 10, 10);
		}
	}

	public void setOffset(int x, int y) {
		this.xOffset = x;
		this.yOffset = y;
	}

	public void renderSprite(Bitmap<Integer> sprite, int x, int y) {
		draw(sprite, x - xOffset, y - yOffset);
	}

}
/*******************************************************************************
 * This code or part of it is licensed under MIT License by Vulcalien
 ******************************************************************************/
package vulc.engine.gfx;

import vulc.bitmap.Bitmap;
import vulc.bitmap.IntBitmap;
import vulc.bitmap.font.Font;

public class Screen extends IntBitmap {

	public static final Font FONT = new Font(Screen.class.getResourceAsStream("/fonts/tinyfont.fv4"));

	public int xOffset = 0, yOffset = 0;

	public Screen(int width, int height, int[] pixels) {
		super(width, height, pixels);

		setFont(FONT);
	}

	public void setOffset(int x, int y) {
		this.xOffset = x;
		this.yOffset = y;
	}

	public void renderSprite(Bitmap<Integer> sprite, int x, int y) {
		draw(sprite, x - xOffset, y - yOffset);
	}

}

/*
 * This code is licensed under MIT License by Vulcalien (see LICENSE)
 */
package vulc.engine.gfx;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import vulc.engine.Game;

public class GameFrame extends Frame {

	private static final long serialVersionUID = 1L;

	public final Canvas canvas = new Canvas();

	private final Screen screen;

	private final BufferedImage img;
	protected final int[] pixels;

	public final int width;
	public final int height;
	public final int scale;

	public GameFrame(String title, int width, int height, int scale) {
		super(title);

		this.width = width;
		this.height = height;
		this.scale = scale;

		img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();

		screen = new Screen(width, height, pixels);

		// initialize GUI
		setResizable(false);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			};
		});

		canvas.setPreferredSize(new Dimension(width * scale, height * scale));
		canvas.setMaximumSize(new Dimension(width * scale, height * scale));
		canvas.setMinimumSize(new Dimension(width * scale, height * scale));

		add(canvas);
		pack();
		setLocationRelativeTo(null);
	}

	public void render() {
		BufferStrategy bs = canvas.getBufferStrategy();
		if(bs == null) {
			canvas.createBufferStrategy(3);
			return;
		}

		Game.render(screen);

		Graphics g = bs.getDrawGraphics();
		g.drawImage(img, 0, 0, width * scale, height * scale, null);
		g.dispose();
		bs.show();
	}

}

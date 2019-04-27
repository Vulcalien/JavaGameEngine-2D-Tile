package author.gamename.level.tile;

import author.gamename.Game;
import author.gamename.gfx.Screen;
import author.gamename.level.Level;
import author.gamename.level.entity.Entity;

public class Tile {

	public static final int T_SIZE = Game.T_SIZE;

	public static final Tile[] TILES = new Tile[128];

	public final byte id;

	public Tile(int id) {
		this.id = (byte) id;
		if(TILES[id] != null) {
			throw new RuntimeException("Duplicate tile ids");
		}
		TILES[id] = this;
	}

	public void render(Screen screen, Level level, int xt, int yt) {
	}

	public boolean mayPass(Entity e, int xm, int ym, Level level, int xt, int yt) {
		return true;
	}

}

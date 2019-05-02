/*******************************************************************************
 * Copyright (C) 2019 Vulcalien
 * This code or part of it is licensed under MIT License by Vulcalien
 ******************************************************************************/
package author.gamename.level;

import java.util.ArrayList;
import java.util.List;

import author.gamename.Game;
import author.gamename.gfx.Screen;
import author.gamename.level.entity.Entity;
import author.gamename.level.tile.Tile;

public class Level {

	public static final int T_SIZE = Game.T_SIZE;

	public final int width, height;
	public final byte[] tiles;
	public final List<Entity> entities = new ArrayList<Entity>();
	public final List<Entity>[] entitiesInTile;

	@SuppressWarnings("unchecked")
	public Level(int width, int height) {
		this.width = width;
		this.height = height;
		this.tiles = new byte[width * height];
		this.entitiesInTile = new ArrayList[width * height];
		for(int i = 0; i < entitiesInTile.length; i++) {
			entitiesInTile[i] = new ArrayList<Entity>();
		}
	}

	public void tick() {
		for(int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);

			int xt0 = e.x >> T_SIZE;
			int yt0 = e.y >> T_SIZE;

			e.tick();

			if(e.removed) {
				removeEntityFromTile(e, xt0, yt0);
				removeEntity(e);
				i--;
			} else {
				int xt1 = e.x >> T_SIZE;
				int yt1 = e.y >> T_SIZE;

				if(xt1 != xt0 || yt1 != yt0) {
					removeEntityFromTile(e, xt0, yt0);
					insertEntityInTile(e, xt1, yt1);
				}
			}
		}
	}

	public void render(Screen screen, int xTiles, int yTiles) {
		//set screen's offset

		int xt0 = (screen.xOffset) >> T_SIZE;
		int yt0 = (screen.yOffset) >> T_SIZE;
		int xt1 = xt0 + xTiles;
		int yt1 = yt0 + yTiles;

		for(int yt = yt0; yt <= yt1; yt++) {
			if(yt < 0 || yt >= height) continue;

			for(int xt = xt0; xt <= xt1; xt++) {
				if(xt < 0 || xt >= width) continue;

				getTile(xt, yt).render(screen, this, xt, yt);
			}
		}

		List<Entity> entities = getEntitiesInTile(xt0 - 1, yt0 - 1, xt1 + 1, yt1 + 1);
		for(int i = 0; i < entities.size(); i++) {
			entities.get(i).render(screen);
		}
	}

	public void setTile(Tile tile, int xt, int yt) {
		if(xt < 0 || xt >= width || yt < 0 || yt >= height) return;
		tiles[xt + yt * width] = tile.id;
	}

	public Tile getTile(int xt, int yt) {
		if(xt < 0 || xt >= width || yt < 0 || yt >= height) return null;
		return Tile.TILES[tiles[xt + yt * width]];
	}

	public void addEntity(Entity e) {
		entities.add(e);
		insertEntityInTile(e, e.x >> T_SIZE, e.y >> T_SIZE);
		e.removed = false;
		e.level = this;
	}

	public void removeEntity(Entity e) {
		entities.remove(e);
		removeEntityFromTile(e, e.y >> T_SIZE, e.y >> T_SIZE);
		e.removed = true;
	}

	public void insertEntityInTile(Entity e, int xt, int yt) {
		if(xt < 0 || xt >= width || yt < 0 || yt >= height) return;
		entitiesInTile[xt + yt * width].add(e);
	}

	public void removeEntityFromTile(Entity e, int xt, int yt) {
		if(xt < 0 || xt >= width || yt < 0 || yt >= height) return;
		entitiesInTile[xt + yt * width].remove(e);
	}

	public List<Entity> getEntities(int x0, int y0, int x1, int y1) {
		List<Entity> result = new ArrayList<Entity>();

		int xt0 = (x0 >> T_SIZE) - 1;
		int yt0 = (y0 >> T_SIZE) - 1;
		int xt1 = (x1 >> T_SIZE) + 1;
		int yt1 = (y1 >> T_SIZE) + 1;

		for(int yt = yt0; yt <= yt1; yt++) {
			if(yt < 0 || yt >= height) continue;

			for(int xt = xt0; xt <= xt1; xt++) {
				if(xt < 0 || xt >= width) continue;

				List<Entity> inTile = entitiesInTile[xt + yt * width];
				for(int i = 0; i < inTile.size(); i++) {
					Entity e = inTile.get(i);

					if(e.intersects(x0, y0, x1, y1)) result.add(e);
				}
			}
		}
		return result;
	}

	public List<Entity> getEntitiesInTile(int xt0, int yt0, int xt1, int yt1) {
		List<Entity> result = new ArrayList<Entity>();
		for(int yt = yt0; yt <= yt1; yt++) {
			if(yt < 0 || yt >= height) continue;

			for(int xt = xt0; xt <= xt1; xt++) {
				if(xt < 0 || xt >= width) continue;

				result.addAll(entitiesInTile[xt + yt * width]);
			}
		}
		return result;
	}

}

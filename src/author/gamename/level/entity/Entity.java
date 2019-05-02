/*******************************************************************************
 * Copyright (C) 2019 Vulcalien
 * This code or part of it is licensed under MIT License by Vulcalien
 ******************************************************************************/
package author.gamename.level.entity;

import java.util.ArrayList;
import java.util.List;

import author.gamename.Game;
import author.gamename.gfx.Screen;
import author.gamename.level.Level;
import author.gamename.level.tile.Tile;

public abstract class Entity {

	public static final int T_SIZE = Game.T_SIZE;

	public Level level;
	public boolean removed = false;

	public int x, y;
	//xr is half of the width, yr = half of the height
	public int xr, yr;

	public void tick() {
	}

	public void render(Screen screen) {
	}

	public boolean[] move(int xm, int ym) {
		List<Entity> touchedEntities = new ArrayList<Entity>();

		boolean xMoved = move2(xm, 0, touchedEntities);
		boolean yMoved = move2(0, ym, touchedEntities);

		for(int i = 0; i < touchedEntities.size(); i++) {
			Entity e = touchedEntities.get(i);
			e.touchedBy(this);
		}

		return new boolean[] {xMoved, yMoved};
	}

	//returns false if totally blocked by something and no movement is made
	private boolean move2(int xm, int ym, List<Entity> touchedEntities) {
		if(xm == 0 && ym == 0) return true;
		if(xm != 0 && ym != 0) throw new RuntimeException("Error: move2 moves only in one axis");

		boolean blocked = false;

		//will be set just if a tile blocks the movement
		int xBlockTile = -1, yBlockTile = -1;

		//will be set just if an entity blocks the movement
		Entity blockEntity = null;

		int x0 = x - xr;
		int y0 = y - yr;
		int x1 = x + xr - 1;
		int y1 = y + yr - 1;

		int xto0 = (x0 + xm) >> T_SIZE;
		int yto0 = (y0 + ym) >> T_SIZE;
		int xto1 = (x1 + xm) >> T_SIZE;
		int yto1 = (y1 + ym) >> T_SIZE;

		main_loop:
		for(int yt = yto0; yt <= yto1; yt++) {
			for(int xt = xto0; xt <= xto1; xt++) {
				Tile tile = null;
				boolean isOutOfLevel = false;

				if(xt < 0 || xt >= level.width || yt < 0 || yt >= level.height) {
					isOutOfLevel = true;
				} else {
					tile = level.getTile(xt, yt);
				}

				if(isOutOfLevel || !tile.mayPass(this, xm, ym, level, xt, yt)) {
					blocked = true;
					xBlockTile = xt;
					yBlockTile = yt;
					break main_loop;
				}
			}
		}

		if(blocked) {
			if(xm != 0) {
				if(xm < 0) xm = ((xBlockTile + 1) << T_SIZE) - x0;
				else xm = (xBlockTile << T_SIZE) - x1 - 1;
			} else {
				if(ym < 0) ym = ((yBlockTile + 1) << T_SIZE) - y0;
				else ym = (yBlockTile << T_SIZE) - y1 - 1;
			}

			if(xm == 0 && ym == 0) return false;
			blocked = false;
		}

		//FIX it is possible that entity doesn't touch all the entities because one of them blocks it
		//maybe it could be fixed moving this code in Entity.move()
		//and add to touchedEntities the one who blocks it
		List<Entity> entities = level.getEntities(x0 + xm, y0 + ym, x1 + xm, y1 + ym);
		for(int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			if(!touchedEntities.contains(e)) touchedEntities.add(e);
		}

		List<Entity> wasInside = level.getEntities(x0, y0, x1, y1);
		entities.removeAll(wasInside);

		for(int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			if(e == this) continue;

			if(this.isBlockedBy(e) && e.blocks(this)) {
				blocked = true;
				blockEntity = e;
			}
		}

		if(blocked) {
			if(xm != 0) {
				if(xm < 0) xm = (blockEntity.x + blockEntity.xr) - x0;
				else xm = (blockEntity.x - blockEntity.xr) - x1 - 1;
			} else {
				if(ym < 0) ym = (blockEntity.y + blockEntity.yr) - y0;
				else ym = (blockEntity.y - blockEntity.yr) - y1 - 1;
			}

			if(xm == 0 && ym == 0) return false;
			blocked = false;
		}

		x += xm;
		y += ym;
		return true;
	}

	public void touchedBy(Entity e) {
	}

	public void remove() {
		removed = true;
	}

	public boolean isBlockedBy(Entity e) {
		return false;
	}

	public boolean blocks(Entity e) {
		return false;
	}

	public boolean touches(Entity e) {
		return intersects(e.x - e.xr, e.y - e.yr, e.x + e.xr, e.y + e.yr);
	}

	public boolean intersects(int x0, int y0, int x1, int y1) {
		return !(x - xr > x1 || x + xr - 1 < x0 || y - yr > y1 || y + yr - 1 < y0);
	}

}

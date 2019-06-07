/*******************************************************************************
 * Copyright (C) 2019 Vulcalien
 * This code or part of it is licensed under MIT License by Vulcalien
 ******************************************************************************/
package author.gamename.level.entity;

import java.util.ArrayList;
import java.util.List;

import author.gamename.gfx.Screen;
import author.gamename.level.Level;
import author.gamename.level.tile.Tile;

public abstract class Entity {

	public Level level;
	public boolean removed = false;

	public int x, y;
	//xr is half of the width, yr is half of the height
	public int xr, yr;

	public void tick() {
	}

	public void render(Screen screen) {
	}

	public boolean[] move(int xm, int ym) {
		List<Entity> touchedEntities = new ArrayList<Entity>();

		boolean xMoved = move2(xm, 0, touchedEntities);
		boolean yMoved = move2(0, ym, touchedEntities);

		List<Entity> isInside = level.getEntities(x - xr, y - yr, x + xr - 1, y + yr - 1);
		isInside.remove(this);
		touchedEntities.addAll(isInside);

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

		int x0 = x - xr;
		int y0 = y - yr;
		int x1 = x + xr - 1;
		int y1 = y + yr - 1;

		int xto0 = Level.posToTile(x0 + xm);
		int yto0 = Level.posToTile(y0 + ym);
		int xto1 = Level.posToTile(x1 + xm);
		int yto1 = Level.posToTile(y1 + ym);

		boolean blocked = false;
		int xBlockTile = -1, yBlockTile = -1;

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
				if(xm < 0) xm = Level.tileToPos(xBlockTile + 1) - x0;
				else xm = Level.tileToPos(xBlockTile) - x1 - 1;
			} else {
				if(ym < 0) ym = Level.tileToPos(yBlockTile + 1) - y0;
				else ym = Level.tileToPos(yBlockTile) - y1 - 1;
			}

			if(xm == 0 && ym == 0) return false;
			blocked = false;
		}

		List<Entity> isInside = level.getEntities(x0 + xm, y0 + ym, x1 + xm, y1 + ym);
		List<Entity> wasInside = level.getEntities(x0, y0, x1, y1);
		isInside.removeAll(wasInside);

		List<Entity> blockingEntities = new ArrayList<Entity>();

		for(int i = 0; i < isInside.size(); i++) {
			Entity e = isInside.get(i);
			if(e == this) continue;

			if(this.isBlockedBy(e) && e.blocks(this)) {
				blocked = true;
				blockingEntities.add(e);
			}
		}

		if(blocked) {
			Entity blockEntity = null;
			List<Entity> touchedBlockingEntities = new ArrayList<Entity>();
			for(int i = 0; i < blockingEntities.size(); i++) {
				Entity e = blockingEntities.get(i);

				if(blockEntity == null) {
					blockEntity = e;
					touchedBlockingEntities.add(e);
				} else {
					if(xm != 0) {
						if(xm < 0) {
							int xb0 = e.x + e.xr;
							int xb1 = blockEntity.x + blockEntity.xr;
							if(xb0 > xb1) {
								blockEntity = e;
								touchedBlockingEntities.clear();
								touchedBlockingEntities.add(e);
							} else if(xb0 == xb1) {
								touchedBlockingEntities.add(e);
							}
						} else {
							int xb0 = e.x - e.xr;
							int xb1 = blockEntity.x - blockEntity.xr;
							if(xb0 < xb1) {
								blockEntity = e;
								touchedBlockingEntities.clear();
								touchedBlockingEntities.add(e);
							} else if(xb0 == xb1) {
								touchedBlockingEntities.add(e);
							}
						}
					} else {
						if(ym < 0) {
							int yb0 = e.y + e.yr;
							int yb1 = blockEntity.y + blockEntity.yr;
							if(yb0 > yb1) {
								blockEntity = e;
								touchedBlockingEntities.clear();
								touchedBlockingEntities.add(e);
							} else if(yb0 == yb1) {
								touchedBlockingEntities.add(e);
							}
						} else {
							int yb0 = e.y - e.yr;
							int yb1 = blockEntity.y - blockEntity.yr;
							if(yb0 < yb1) {
								blockEntity = e;
								touchedBlockingEntities.clear();
								touchedBlockingEntities.add(e);
							} else if(yb0 == yb1) {
								touchedBlockingEntities.add(e);
							}
						}
					}
				}
			}
			touchedEntities.addAll(touchedBlockingEntities);

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

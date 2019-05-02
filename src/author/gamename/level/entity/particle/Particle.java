/*******************************************************************************
 * Copyright (C) 2019 Vulcalien
 * This code or part of it is licensed under MIT License by Vulcalien
 ******************************************************************************/
package author.gamename.level.entity.particle;

import author.gamename.level.entity.Entity;

public abstract class Particle extends Entity {

	public final int lifeTime;
	public int remainingTime;

	public Particle(int lifeTime, int x, int y) {
		this.lifeTime = lifeTime;
		remainingTime = lifeTime;
		this.x = x;
		this.y = y;
	}

	public void tick() {
		remainingTime--;
		if(remainingTime == 0) remove();
	}

}

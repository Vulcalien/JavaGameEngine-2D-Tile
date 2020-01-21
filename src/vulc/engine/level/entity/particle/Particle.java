/*******************************************************************************
 * This code or part of it is licensed under MIT License by Vulcalien
 ******************************************************************************/
package vulc.engine.level.entity.particle;

import vulc.engine.level.entity.Entity;

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

/*******************************************************************************
 * This code or part of it is licensed under MIT License by Vulcalien
 ******************************************************************************/
package vulc.engine.sfx;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Sound {

	private Clip clip;

	public Sound(String file) {
		try {
			AudioInputStream ais = AudioSystem.getAudioInputStream(Sound.class.getResource(file));
			Clip clip = AudioSystem.getClip();
			clip.open(ais);
			this.clip = clip;
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void play() {
		if(clip == null) return;
		if(clip.isRunning()) clip.stop();
		clip.setFramePosition(0);
		clip.start();
	}

	public void loop() {
		if(clip == null) return;
		if(clip.isRunning()) clip.stop();
		clip.setFramePosition(0);
		clip.loop(Clip.LOOP_CONTINUOUSLY);
	}

	public void stop() {
		if(clip == null) return;
		clip.stop();
	}

}

package author.gamename.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import author.gamename.Game;

public class InputHandler implements KeyListener, MouseListener {

	public static enum Type {KEYBOARD, MOUSE}
	public static enum Action {PRESS, RELEASE}

	public static final List<Key> KEYBOARD_KEYS = new ArrayList<Key>();
	public static final List<Key> MOUSE_KEYS = new ArrayList<Key>();

	public static void init(Game game) {
		InputHandler instance = new InputHandler();
		game.addKeyListener(instance);
		game.addMouseListener(instance);

		game.requestFocus();
	}

	public static void tick() {
		for(int i = 0; i < KEYBOARD_KEYS.size(); i++) {
			KEYBOARD_KEYS.get(i).tick();
		}
		for(int i = 0; i < MOUSE_KEYS.size(); i++) {
			MOUSE_KEYS.get(i).tick();
		}
	}

	private static void receiveInput(Action action, Type type, int code) {
		List<Key> keys = getList(type);
		for(int i = 0; i < keys.size(); i++) {
			Key key = keys.get(i);
			if(key.code == code) {

				if(action == Action.PRESS) {
					key.isKeyDown = true;
				} else if(action == Action.RELEASE) {
					key.isReleased = true;
				}

				return;
			}
		}
	}

	private static List<Key> getList(Type type) {
		switch(type) {
			case KEYBOARD:
				return KEYBOARD_KEYS;

			case MOUSE:
				return MOUSE_KEYS;

			default:
				return null;
		}
	}

	public void keyTyped(KeyEvent e) {
	}

	public void keyPressed(KeyEvent e) {
		receiveInput(Action.PRESS, Type.KEYBOARD, e.getKeyCode());
	}

	public void keyReleased(KeyEvent e) {
		receiveInput(Action.RELEASE, Type.KEYBOARD, e.getKeyCode());
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
		receiveInput(Action.PRESS, Type.MOUSE, e.getButton());
	}

	public void mouseReleased(MouseEvent e) {
		receiveInput(Action.RELEASE, Type.MOUSE, e.getButton());
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public static class Key {

		private Type type;
		private int code;

		private boolean isKeyDown = false;
		private boolean wasKeyDown = false;
		private boolean isReleased = false;

		public Key(Type type, int code) {
			init(type, code);
		}

		private void init(Type type, int code) {
			this.type = type;
			this.code = code;
			getList(type).add(this);
		}

		private void tick() {
			wasKeyDown = isKeyDown;
			if(isReleased) {
				isKeyDown = false;
				isReleased = false;
			}
		}

		public void setKeyBinding(Type newType, int newCode) {
			getList(this.type).remove(this);
			init(newType, newCode);
		}

		public boolean isKeyDown() {
			return isKeyDown;
		}

		public boolean isPressed() {
			return !wasKeyDown && isKeyDown;
		}

		public boolean isReleased() {
			return wasKeyDown && !isKeyDown;
		}

	}

}

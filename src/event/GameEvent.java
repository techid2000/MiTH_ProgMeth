package event;

import java.util.HashMap;
import java.util.Map;

import constants.SystemCache;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.util.Duration;
import scene.GameScene;

public class GameEvent {

	private Scene scene;

	private Map<KeyCode, SimpleDoubleProperty> keyFraction;
	private Map<KeyCode, Boolean> keyHolding;
	private Map<KeyCode, Boolean> bufferedSingleKeyDown, bufferedSingleKeyUp;
	private Map<MouseButton, Boolean> mouseHolding;
	private Map<MouseButton, Boolean> bufferedSingleMouseDown, bufferedSingleMouseUp;
	private boolean scrollChanged;

	private Point2D mousePosition;

	public GameEvent(GameScene scene) {
		SystemCache.getInstance().gameEvent = this;

		this.scene = scene;
		assign();

		keyFraction = new HashMap<KeyCode, SimpleDoubleProperty>();
		keyHolding = new HashMap<KeyCode, Boolean>();
		bufferedSingleKeyDown = new HashMap<KeyCode, Boolean>();
		bufferedSingleKeyUp = new HashMap<KeyCode, Boolean>();

		mouseHolding = new HashMap<MouseButton, Boolean>();
		bufferedSingleMouseDown = new HashMap<MouseButton, Boolean>();
		bufferedSingleMouseUp = new HashMap<MouseButton, Boolean>();

		mousePosition = new Point2D(0, 0);

		for (KeyCode key : KeyCode.values()) {
			keyFraction.put(key, new SimpleDoubleProperty(0.0));
			keyHolding.put(key, Boolean.FALSE);
			bufferedSingleKeyDown.put(key, Boolean.FALSE);
			bufferedSingleKeyUp.put(key, Boolean.FALSE);
		}
		for (MouseButton btn : MouseButton.values()) {
			mouseHolding.put(btn, Boolean.FALSE);
			bufferedSingleMouseDown.put(btn, Boolean.FALSE);
			bufferedSingleMouseUp.put(btn, Boolean.FALSE);
		}
	}

	public void assign() {
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (GameEvent.this.keyHolding.get(event.getCode()) == false)
					GameEvent.this.bufferedSingleKeyDown.put(event.getCode(), true);
				GameEvent.this.keyHolding.put(event.getCode(), true);
				lerpValue(GameEvent.this.keyFraction.get(event.getCode()), 1.0);
			}
		});
		scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				GameEvent.this.keyHolding.put(event.getCode(), false);
				lerpValue(GameEvent.this.keyFraction.get(event.getCode()), 0.0);
				GameEvent.this.bufferedSingleKeyUp.put(event.getCode(), true);

				if (event.getCode() == KeyCode.TAB) {
					SystemCache.getInstance().shopUI.toggle();
				}
				if (event.getCode() == KeyCode.ESCAPE) {
					SystemCache.getInstance().shopUI.hide();
				}
			}
		});
		scene.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (GameEvent.this.mouseHolding.get(event.getButton()) == false)
					GameEvent.this.bufferedSingleMouseDown.put(event.getButton(), true);
				GameEvent.this.mouseHolding.put(event.getButton(), true);
			}
		});
		scene.setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				GameEvent.this.mouseHolding.put(event.getButton(), false);
				GameEvent.this.bufferedSingleMouseUp.put(event.getButton(), true);
			}
		});
		scene.setOnMouseMoved(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				GameEvent.this.mousePosition = new Point2D(event.getX(), event.getY());
			}
		});
		scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				GameEvent.this.mousePosition = new Point2D(event.getX(), event.getY());
			}
		});
		scene.setOnScroll(new EventHandler<ScrollEvent>() {
			@Override
			public void handle(ScrollEvent arg0) {
				GameEvent.this.scrollChanged = true;
			}
		});
	}

	private void lerpValue(SimpleDoubleProperty value, double to) {
		double from = value.get();
		new Transition() {
			{
				setCycleDuration(new Duration(200));
				setInterpolator(Interpolator.LINEAR);
			}

			protected void interpolate(double frac) {
				// TODO Auto-generated method stub
				value.set((to - from) * frac + from);
			}
		}.play();
	}

	public double getHorizontalFraction() {
		return this.keyFraction.get(KeyCode.D).get() - this.keyFraction.get(KeyCode.A).get();
	}

	public double getVerticalFraction() {
		return this.keyFraction.get(KeyCode.S).get() - this.keyFraction.get(KeyCode.W).get();
	}

	public boolean getKeyHolding(KeyCode key) {
		return this.keyHolding.get(key);
	}

	public boolean getSingleKeyDown(KeyCode key) {
		return this.bufferedSingleKeyDown.get(key);
	}

	public boolean getSingleKeyUp(KeyCode key) {
		return this.bufferedSingleKeyUp.get(key);
	}

	public boolean getMouseHolding(MouseButton btn) {
		return this.mouseHolding.get(btn);
	}

	public boolean getSingleMouseDown(MouseButton btn) {
		return this.bufferedSingleMouseDown.get(btn);
	}

	public boolean getSingleMouseUp(MouseButton btn) {
		return this.bufferedSingleMouseUp.get(btn);
	}

	public boolean getScrollChanged() {
		return this.scrollChanged;
	}

	public Point2D getMousePosition() {
		return this.mousePosition;
	}

	public void clearSingleKeyBuffer() {
		for (KeyCode key : KeyCode.values()) {
			this.bufferedSingleKeyDown.put(key, false);
			this.bufferedSingleKeyUp.put(key, false);
		}
		for (MouseButton btn : MouseButton.values()) {
			this.bufferedSingleMouseDown.put(btn, false);
			this.bufferedSingleMouseUp.put(btn, false);
		}
		this.scrollChanged = false;
	}
}

package com.diamondhunter.hub;

import java.awt.Color;
import java.awt.Graphics2D;

import com.diamondhunter.entity.base.EntityManager;
import com.diamondhunter.graphics.font.ImageFont;
import com.diamondhunter.graphics.window.Window;
import com.diamondhunter.start.Start;
import com.diamondhunter.state.base.StateManager;
import com.diamondhunter.state.game.MenuState;
import com.diamondhunter.state.game.PlayState;
import com.diamondhunter.util.Version;
import com.diamondhunter.util.input.KeyHandler;
import com.diamondhunter.util.input.MouseHandler;
import com.diamondhunter.util.logging.Logger;
import com.diamondhunter.util.logging.LoggerOptions;

public final class DiamondHunter implements Runnable {

	// Game information
	private static final String GAME_TITLE = "Diamond Hunter";
	private static final Version version = new Version("1.0-BETA");

	private static final String DEBUG_MODE_ARGUMENT = "-debug";
	private static final Color DEBUG_MODE_COLOR = Color.GREEN;
	private static ImageFont DEBUG_MODE_FONT;

	// Manager name
	public static final String MANAGER_NAME = GAME_TITLE + "-" + version.toString();

	// Game refresh rate
	private static final double TARGET_HERTZ = 60.0f;
	private static final double TARGET_FPS = 60.0f;

	// Logging
	private static final String DEFAULT_LOG_FORMAT = "[" + LoggerOptions.Format.HOUR12 + ":"
			+ LoggerOptions.Format.MINUTE + ":" + LoggerOptions.Format.SECOND + " " + LoggerOptions.Format.HOUR_AM_PM
			+ " - " + LoggerOptions.Format.LEVEL + "] " + LoggerOptions.Format.PROMPT;
	private static boolean DEBUG_MODE = false;

	// Implementation for other use
	private static DiamondHunter implementation = new DiamondHunter();

	// Display
	private static int DISPLAY_WIDTH = 1280;
	private static int DISPLAY_HEIGHT = DISPLAY_WIDTH / 16 * 9;

	// Logging
	private final Logger logger;

	// Input
	private KeyHandler keyHandler;
	private MouseHandler mouseHandler;

	// Thread information
	private boolean running = false;
	private Thread mainThread;
	private Window window;

	// Game info
	private boolean startedRendering = false;

	// Frames and updates
	private double currentFps;
	private double currentUps;

	// Game state manager
	private StateManager stateManager;

	// Game states
	private MenuState menuState;
	private PlayState playState;

	// Entity manager
	private EntityManager entityManager;

	private DiamondHunter() {
		logger = Logger.createLogger(MANAGER_NAME);
		logger.getOptions().setLogFormat(DEFAULT_LOG_FORMAT);
	}

	public static DiamondHunter getImplementation() {
		return implementation;
	}

	public static Version getVersion() {
		return version;
	}

	public static String getTitle() {
		return GAME_TITLE;
	}

	public static double getTargetHertz() {
		return TARGET_HERTZ;
	}

	public static double getTargetFPS() {
		return TARGET_FPS;
	}

	public static boolean isDebugMode() {
		return DEBUG_MODE;
	}

	public static void setDebugMode(boolean mode) {
		if (!Thread.currentThread().getStackTrace()[2].getClassName().equals(Start.class.getName()))
			return;
		DEBUG_MODE = mode;
	}

	public static String getDebugModeArgument() {
		return DEBUG_MODE_ARGUMENT;
	}

	public static Color getDebugModeColor() {
		return DEBUG_MODE_COLOR;
	}

	public static ImageFont getDebugModeFont() {
		return DEBUG_MODE_FONT;
	}

	public synchronized void start() {
		if (running)
			return;

		running = true;
		mainThread = new Thread(this, GAME_TITLE);
		mainThread.start();
	}

	public synchronized void stop() {
		if (!running)
			return;
		running = false;

		try {
			mainThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void initialize() {
		// Log the current status
		logger.logLine(Logger.Level.INFO, "Building...");

		// Display
		window = new Window(GAME_TITLE, DISPLAY_WIDTH, DISPLAY_HEIGHT, false);

		// Input
		keyHandler = new KeyHandler(this);
		mouseHandler = new MouseHandler(this);

		// Log the current status
		logger.logLine(Logger.Level.INFO, "Loading assets...");

		// State Manager
		stateManager = new StateManager();

		// States
		menuState = new MenuState(stateManager);
		playState = new PlayState(stateManager);
		
		stateManager.setFallbackState(menuState);

		// Entity manager
		entityManager = EntityManager.createManager(MANAGER_NAME);

		DEBUG_MODE_FONT = new ImageFont("font/diamond-font.png", 7, 12);
		DEBUG_MODE_FONT.setSize(2);
		DEBUG_MODE_FONT.setLetterSpacing((int) (DEBUG_MODE_FONT.getLetterSpacing() * 2));
	}

	private void update() {
		// Make sure the game has loaded and started rendering before we update && make
		// sure window is displaying
		if (!startedRendering)
			return;

		// Display the current state(s)
		stateManager.update();
	}

	private void input() {
		// Make sure the game has loaded and started rendering before we take input
		if (!startedRendering)
			return;

		// Take input for states
		stateManager.input(keyHandler, mouseHandler);
		entityManager.input(keyHandler, mouseHandler);

		// Update input
		keyHandler.update();
		mouseHandler.update();
	}

	private void render() {

		// Make sure the game has loaded and started rendering before we render entities
		if (startedRendering) {
			// Render state
			stateManager.render(window.getDisplay().getGraphics());

			// Render entities
			entityManager.render(window.getDisplay().getGraphics());

			if (isDebugMode()) {
				// Render debug options
				renderDebug(window.getDisplay().getGraphics());
			}
		}

		// Update window render
		window.getDisplay().update();

		// Make sure we show the window once the game has started rendering for 1 frame.
		// We need this so we do not get a white screen caused by the loading of assets
		// before our game.
		if (!window.isVisible()) {
			// Log the current status
			logger.logLine(Logger.Level.INFO, "Launching...");

			// Now show the windows and notify we can start
			window.setVisible(true);
			startedRendering = true;
		}
	}

	private void renderDebug(Graphics2D g) {
		// Color oldColor = g.getColor(); -- Only needed when using text font

		// g.setColor(getDebugModeColor()); -- Only needed when using text font
		ImageFont font = getDebugModeFont();

		// Render FPS

		font.renderText(g, (int) this.currentFps + " FPS", 10, 10);
		font.renderText(g, (int) this.currentUps + " UPS", 10, 40);
	}

	public void closeProgram() {
		// TODO Add save game code
		System.exit(0);
	}

	@Override
	public void run() {

		/*
		 * Note that this game loop may not always be optimized for your pc. If your pc
		 * gets random lag spikes or if your game is constantly freezing, this game loop
		 * will update the updates that were lost from that lag. By doing so, your game
		 * will be faster than usual during that time.
		 *
		 * If this game loop does not work for you, please try to use the game loops
		 * below.
		 */

		// The amount of time we should wait for before doing an update.
		// Basically how long we should wait before one game hertz.
		final double TIME_BETWEEN_UPDATES = 1000000000 / TARGET_HERTZ;

		// The maximum amount of times we should update before rendering.
		// Set to '1' if you are worried about hitches and flickering.
		final int MAX_UPDATES_BEFORE_RENDER = 5;

		// The last time, in nanoseconds, that we updated the game.
		// This gets changed later.
		double lastUpdateTime = System.nanoTime();

		// The last time, in nanoseconds, that we render the game.
		// This gets changed later.
		double lastRenderTime;

		// The amount of time we should wait for before rendering.
		// Basically how long we should wait before one game frame.
		final double TIME_BETWEEN_RENDERS = 1000000000 / TARGET_FPS;

		// The current time in seconds (from nanoseconds)
		int lastSecondTime = (int) (lastUpdateTime / 1000000000);

		// The FPS of the last updates
		int oldFrameCount = 0;

		// The current fps (this is later stored in a field)
		int frameCount = 0;

		// The UPS(updates per second or ticks) of the last updates
		int oldUpdateCount = 0;

		// The current ups(updates per second) (this is later stored in a field)
		int updateCount = 0;

		// Initialize the game
		initialize();

		while (running) {

			// Get the current time in nanoseconds
			double now = System.nanoTime();

			// The amount of updates we do
			int updateAmount = 0;

			// If it's been long enough for us to be able to update (specified by
			// TIME_BETWEEN_UPDATES)
			// && we haven't updated the right amount of times (specified by
			// MAX_UPDATES_BEFORE_RENDER)
			// (which basically means: if we haven't reached the desired amount of updates).
			while (now - lastUpdateTime > TIME_BETWEEN_UPDATES && updateAmount < MAX_UPDATES_BEFORE_RENDER) {
				update();

				// Only take input if we can see the screen
				if (!window.isMinimized() && window.hasFocus())
					input();

				lastUpdateTime += TIME_BETWEEN_UPDATES;
				updateAmount++;

				// Save the updates (a.k.a UPS or ticks) so we can use it later. The only use
				// for this
				// variable is to be used to count UPS (or ticks).
				updateCount++;
			}

			// If one update takes too long, don't try to forgive it, just continue
			if (now - lastUpdateTime > TIME_BETWEEN_UPDATES) {
				lastUpdateTime = now - TIME_BETWEEN_UPDATES;
			}

			// Render and take input if we need to
			if (!window.isMinimized()) {
				render();

				if (window.getDisplay().hasFocus())
					input();
			}

			// Now is the last time we rendered
			lastRenderTime = now;

			// Update frame count
			frameCount++;

			// The current time in seconds (from nanoseconds)
			int thisSecond = (int) (lastUpdateTime / 1000000000);

			// If it has been 1 second
			if (thisSecond > lastSecondTime) {
				// If the current frame count is different than the previous one
				if (oldFrameCount != frameCount) {
					oldFrameCount = frameCount;
				}

				// If the current update count is different than the previous one
				if (oldUpdateCount != updateCount) {
					oldUpdateCount = updateCount;
				}

				// Set the current frames and updates
				currentFps = frameCount;
				currentUps = updateCount;

				updateCount = 0;
				frameCount = 0;
				lastSecondTime = thisSecond;
			}

			// Sleep if we should not update or render yet. This is determined by the
			// TIME_BETWEEN_RENDERS
			// and TIME_BETWEEN_UPDATES. We run this loop while waiting for the elapsed time
			// to reach the
			// desired time (which are specified by those two variables). So basically, we
			// wait until it has
			// been one frame.
			//
			// This is crucial for the game to not hog the CPU.
			while (now - lastRenderTime < TIME_BETWEEN_RENDERS && now - lastUpdateTime < TIME_BETWEEN_UPDATES) {

				// Helps clear CPU usage.
				Thread.yield();

				// Sleeps the thread (basically do nothing for the given amount of time (which
				// in this case,
				// if 1 millisecond)).
				try {
					Thread.sleep(1);
				} catch (Exception e) {
					if (isDebugMode())
						logger.logLine(Logger.Level.ERROR, "Could not sleep thread, CPU usage increased...");
				}

				Thread.yield();

				try {
					Thread.sleep(0);
				} catch (InterruptedException e) {
					if (isDebugMode())
						logger.logLine(Logger.Level.ERROR, "Could not sleep thread, CPU usage increased...");
				}

				// Make sure we're updating the current time so we can use it to check if we
				// should update or render.
				now = System.nanoTime();
			}

			Thread.yield();

			try {
				Thread.sleep(0);
			} catch (InterruptedException e) {
				if (isDebugMode())
					logger.logLine(Logger.Level.ERROR, "Could not sleep thread, CPU usage increased...");
			}
		}

		/*
		 * Another functional game loop. If CPU Usage is getting too high, try using
		 * this game loop, it might help keep the CPU usage down.
		 */

//        long start;
//        long elapsed;
//        long wait;
//        long TARGET_TIME = (long) (1000 / TARGET_FPS);
//
//        initialize();
//
//        // game loop
//        while (running) {
//
//            start = System.nanoTime();
//
//            update();
//            input();
//            render();
//
//            elapsed = System.nanoTime() - start;
//
//            wait = TARGET_TIME - elapsed / 1000000;
//            if (wait < 0) wait = TARGET_TIME;
//
//            try {
//                Thread.yield();
//                Thread.sleep(wait);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }

//        /**
//         * Custom made game loop; fps is also not traceable. This is just a decent/bad game loop
//         */
//        initialize();
//
//        while (running) {
//            update();
//            input();
//            render();
//
//            try {
//                Thread.yield();
//                Thread.sleep((long) (1000 / TARGET_HERTZ));
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }

		/*
		 * A final alternative game-loop. Use if game loop above does not help as a last
		 * attempt. !WARNING! This game loop my help the CPU usage decrease although
		 * there is no way of keeping track of FPS and/or UPS.
		 */

//        initialize();
//
//        while (running) {
//            long startTime = System.currentTimeMillis();
//
//            update();
//            input();
//            render();
//
//            long remaining = (System.currentTimeMillis() - startTime);
//
//            try {
//                Thread.sleep(remaining - 2); // adds up to 18 ms since start if repaint took 10ms
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//            remaining = (System.currentTimeMillis() - startTime);
//
//            try {
//                Thread.yield();
//                Thread.sleep(remaining); // should be 1-2ms
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }

		// Properly stop the thread if it wasn't already stopped by the user
		stop();
	}

	public MenuState getMenuState() {
		return menuState;
	}
	
	public PlayState getPlayState() {
		return playState;
	}

	public Window getWindow() {
		return window;
	}

	public Thread getThread() {
		return mainThread;
	}

	public boolean isRunning() {
		return running;
	}

	@Deprecated
	public void setRunning(boolean running) {
		this.running = running;
	}

	public Logger getLogger() {
		return logger;
	}

	public double getCurrentUPS() {
		return currentUps;
	}

	public double getCurrentFPS() {
		return currentFps;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public StateManager getStateManager() {
		return stateManager;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;

		if (obj instanceof Thread) {
			return mainThread.equals(obj);
		} else if (obj instanceof DiamondHunter) {
			return version.equals(((DiamondHunter) obj).version);
		}

		return super.equals(obj);
	}
}

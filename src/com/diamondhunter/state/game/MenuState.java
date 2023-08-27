package com.diamondhunter.state.game;

import com.diamondhunter.graphics.font.ImageFont;
import com.diamondhunter.graphics.gui.base.GUIButton;
import com.diamondhunter.graphics.gui.menubutton.PlayButton;
import com.diamondhunter.graphics.gui.menubutton.QuitButton;
import com.diamondhunter.graphics.gui.menubutton.StatsButton;
import com.diamondhunter.graphics.sprite.Sprite;
import com.diamondhunter.graphics.sprite.SpriteSheet;
import com.diamondhunter.hub.DiamondHunter;
import com.diamondhunter.state.base.State;
import com.diamondhunter.state.base.StateManager;
import com.diamondhunter.util.graphics.Animation;
import com.diamondhunter.util.input.KeyHandler;
import com.diamondhunter.util.input.MouseHandler;
import com.diamondhunter.util.loading.ImageLoader;
import com.diamondhunter.util.loading.ResourceLoader;
import com.diamondhunter.util.logic.AABB;
import com.diamondhunter.util.logic.Vector2f;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * The {@code MenuState} for this game. Like most likely all the other states,
 * there will only be one instance of this {@code MenuState} that will be
 * created. This allows for consistent use of the class and is more
 * efficient than if we were to create multiple instances.
 * <p>
 * This {@code MenuState} also introduces the use of a
 * {@link com.diamondhunter.graphics.gui.base.GUIButton} which are used to display
 * the buttons on the title screen.
 * <p>
 * The use of this state might be vital for other existing states. This menu state is
 * the default fallback state used for this game. This fallback state can be used when
 * there are errors in an active state. The user can be sent to this fallback state.
 * <p>
 * To learn more about how states are used, visit {@link StateManager}.
 *
 * @see com.diamondhunter.graphics.gui.base.GUIButton
 * @see StateManager
 */
public class MenuState extends State {

    /**
     * The background used for the menu state. This may be implemented into it's own
     * class in later versions of the game for a parallax background effect.
     * But for now, we just save the sprite of the background.
     */
    private Sprite background;

    /**
     * The position of the background on the screen. Even though this will most likely
     * be at the point (0,0), we still need this because the {@link Sprite} class needs
     * a {@link Vector2f} position to be drawn at.
     */
    private Vector2f backgroundPosition;

    /**
     * The spritesheet that is used for the title of the game.
     */
    private SpriteSheet titleSpriteSheet;

    /**
     * In order to be able to use the {@link #titleSpriteSheet}, we must use an animation
     * to display each frame of the title animation from the spritesheet.
     */
    private Animation titleAnimation;

    /**
     * The position of the title on the screen.
     */
    private Vector2f titlePosition;

    /**
     * The scale at which the title will be draw at.
     */
    private int titleScale = 6;

    // < ------- START OF BUTTON CREATION ------- > \\

    private float buttonSpacing = 10;

    // PLAY BUTTON

    /**
     * The play button. This changes the game state to the Play State.
     */
    private GUIButton playButton;

    /**
     * The play button spritesheet as a sprite.
     */
    private Sprite playButtonSprite;

    /**
     * The play button position on the screen.
     */
    private Vector2f playButtonPosition;

    // STATS BUTTON

    /**
     * The stats button. This changes the game state to the Stats State.
     */
    private GUIButton statsButton;

    /**
     * The stats button spritesheet as a sprite.
     */
    private Sprite statsButtonSprite;

    /**
     * The stats button position on the screen.
     */
    private Vector2f statsButtonPosition;

    // QUIT BUTTON

    /**
     * The quit button. This exits the game.
     */
    private GUIButton quitButton;

    /**
     * The quit button spritesheet as a sprite.
     */
    private Sprite quitButtonSprite;

    /**
     * The quit button position on the screen.
     */
    private Vector2f quitButtonPosition;

    // < ------- END OF BUTTON CREATION ------- > \\

    /**
     * Constructs a {@code MenuState} with the given arguments.
     *
     * @param manager The state manager used for this menu.
     */
    public MenuState(StateManager manager) {
        super(manager, Type.MENU);

        // Assign the background to a sprite
        background = new Sprite("menu/backgrounds/menu-background.png");

        // Save the background position
        backgroundPosition = new Vector2f();

        // Assign the title to a sprite sheet
        titleSpriteSheet = new SpriteSheet("menu/title/menu-title-spritesheet.png", 69, 37);

        // Save the title position
        titlePosition = new Vector2f((background.getWidth() / 2) - (titleSpriteSheet.getSpriteWidth() * titleScale / 2)
                , (titleSpriteSheet.getSpriteHeight()));

        // Create the animation
        titleAnimation = new Animation(titleSpriteSheet.getSpritesRow(0), 0.05f);

        // Create the buttons
        // The play button
        playButtonSprite = new Sprite("menu/buttons/play/menu-play-button-spritesheet-resized.png");
        playButtonPosition = new Vector2f(titlePosition.getPosition().x + (titleSpriteSheet.getSpriteWidth() * titleScale) / 2f - (256 / 2f),
                titlePosition.y + (titleSpriteSheet.getSpriteHeight() * titleScale) + buttonSpacing);
        playButton = new PlayButton(playButtonPosition, playButtonSprite, 256, 68);

        // The stats button
        statsButtonSprite = new Sprite("menu/buttons/stats/menu-stats-button-spritesheet-resized.png");
        statsButtonPosition = new Vector2f(playButtonPosition.x, playButtonPosition.y + 68 + buttonSpacing);
        statsButton = new StatsButton(statsButtonPosition, statsButtonSprite, 256, 68);

        // The quit button
        quitButtonSprite = new Sprite("menu/buttons/quit/menu-quit-button-spritesheet-resized.png");
        quitButtonPosition = new Vector2f(statsButtonPosition.x, statsButtonPosition.y + 68 + buttonSpacing);
        quitButton = new QuitButton(quitButtonPosition, quitButtonSprite, 256, 68);

        // A menu state instance will likely only be created once during
        // the whole game so it would be sensible for it to be activated
        // during creation. You may remove this if multiple instances of
        // this state are created.
        //
        // This state can later be accessed by the DiamondHunter class.
        setActivated(true);
    }

    @Override
    public void update() {
        // Update the animation
        titleAnimation.update();

        // < ------- START OF BUTTON UPDATING ------- >
        // Update play button
        playButton.update();

        //Update stats button
        statsButton.update();

        // Update quit button
        quitButton.update();
        // < ------- END OF BUTTON UPDATING ------- >
    }

    @Override
    public void input(KeyHandler keyHandler, MouseHandler mouseHandler) {
        // Input play button
        playButton.input(keyHandler, mouseHandler);

        // Input stats button
        statsButton.input(keyHandler, mouseHandler);

        // Input quit button
        quitButton.input(keyHandler, mouseHandler);
    }

    @Override
    public void render(Graphics2D g) {
        // Draw background first so it stays below everything else
        background.render(g, backgroundPosition);

        // Draw the game title on top of the background
        titleAnimation.getCurrentSprite().render(g, titlePosition, titleSpriteSheet.getSpriteWidth() * titleScale, titleSpriteSheet.getSpriteHeight() * titleScale);

        // < ------- START OF BUTTON RENDERING ------- >
        // Draw the play button
        playButton.render(g);

        // Draw the stats button
        statsButton.render(g);

        // Render quit button
        quitButton.render(g);
        // < ------- END OF BUTTON RENDERING ------- >
    }
}

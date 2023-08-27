package com.diamondhunter.entity.base;

import com.diamondhunter.graphics.sprite.Sprite;
import com.diamondhunter.hub.DiamondHunter;
import com.diamondhunter.util.input.KeyHandler;
import com.diamondhunter.util.input.MouseHandler;
import com.diamondhunter.util.logic.AABB;
import com.diamondhunter.util.logic.Vector2f;

import java.awt.*;

/**
 * An {@code Entity}, in this game, is basically any object that has defining
 * properties of which need changed or manipulated in any way. These entities
 * can be forms of others. For example, a {@link HumanEntity} is a form of
 * {@code Entity}. That type of {@code Entity} is used for usually living
 * beings (as suggested by the name) or moving objects.
 *
 * Although, entities do not have to be living. For example, you could have a
 * tree in the game and it would not be living although, the tree cannot be
 * simply described as a block or tile in the game because it has more properties
 * than a normal block or tile. This is when you would use this {@code Entity} class;
 * to represent an abstract object with properties.
 */
public abstract class Entity {

    /**
     * The default entity manager used for this {@code Entity}.
     *
     * @see #manager
     */
    public static final EntityManager DEFAULT_ENTITY_MANAGER = DiamondHunter.getImplementation().getEntityManager();

    /**
     * The default depth of each entity when it is initially created.
     *
     * @see #depth
     */
    public static final float DEFAULT_DEPTH = 0.0f;

    /**
     * The sprite that will be assigned to this {@code Entity} when rendered on
     * the screen.
     */
    protected Sprite sprite;

    /**
     * The position of this {@code Entity} on the world or on the screen.
     */
    protected Vector2f pos;

    /**
     * The collision bounds of this {@code Entity}. This can later be used to check
     * collision between two entities; which is used in the {@link #collides(Entity)}
     * method.
     *
     * @see #collides(Entity)
     */
    protected AABB.Rectangle collision;

    /**
     * The manager used to manage entities. This can be from drawing the entities,
     * to sorting them according to their depth, to removing all entities from
     * a certain class.
     *
     * @see EntityManager
     */
    protected EntityManager manager;

    /**
     * The depth at which this {@code Entity} will be drawn at. The way depth works with the
     * entity manager (and also in most games) it that an entity with a lower depth
     * will be drawn on top of an entity with a higher depth.
     * <p>
     * So for example, if an entity had a depth of -1000 and another entity had a
     * depth of -999, the entity with the depth of -1000 will be drawn over the entity
     * with a depth of -999.
     * <p>
     * A typical usage of this depth variable, is to assign it to a value relative to
     * your y position. It would look something like this:
     * <blockquote><pre>
     *     depth = -y;
     * </pre></blockquote>
     * or
     * <blockquote><pre>
     *     depth = -pos.y;
     * </pre></blockquote>
     * <p>
     * This would "correctly" draw the entities with a proper depth value. So an entity
     * is in front of another, it will be on top, and if it is behind, it will be on the
     * bottom.
     */
    protected float depth = DEFAULT_DEPTH;

    /**
     * Constructs an {@code Entity} with the given arguments.
     *
     * @param sprite The sprite of this {@code Entity}
     * @param pos    The position of this {@code Entity}
     */
    public Entity(Sprite sprite, Vector2f pos) {
        this(sprite, pos, new AABB.Rectangle(pos, sprite.getWidth(), sprite.getHeight()));
    }

    /**
     * Constructs an {@code Entity} with the given arguments. Also, because there is no
     * given manager, this entities then uses the {@link #DEFAULT_ENTITY_MANAGER} as
     * it's entity manager. If you wish the specify the manager, visit
     * {@link #Entity(EntityManager, Sprite, Vector2f)}
     *
     * @param sprite    The sprite of this {@code Entity}
     * @param pos       The position of this {@code Entity}
     * @param collision The collision bounds of this {@code Entity}
     */
    public Entity(Sprite sprite, Vector2f pos, AABB.Rectangle collision) {
        this(DEFAULT_ENTITY_MANAGER, sprite, pos, collision);
    }

    /**
     * Constructs an {@code Entity} with the given arguments.
     *
     * @param manager The entity manager used for this {@code Entity}
     * @param sprite  The sprite of this {@code Entity}
     * @param pos     The position of this {@code Entity}
     */
    public Entity(EntityManager manager, Sprite sprite, Vector2f pos) {
        this(manager, sprite, pos, new AABB.Rectangle(pos, sprite.getWidth(), sprite.getHeight()));
    }

    /**
     * Constructs an {@code Entity} with the given arguments.
     *
     * @param manager   The entity manager used for this {@code Entity}
     * @param sprite    The sprite of this {@code Entity}
     * @param pos       The position of this {@code Entity}
     * @param collision The collision bounds of this {@code Entity}
     */
    public Entity(EntityManager manager, Sprite sprite, Vector2f pos, AABB.Rectangle collision) {
        this.manager = manager;
        this.sprite = sprite;
        this.pos = pos;
        this.collision = collision;

        manager.addEntity(this);
    }

    public void render(Graphics2D g) {
        sprite.render(g, pos.getWorldVector());
    }

    public void update() {
        updateCollision();
    }

    /**
     * A method used to take user input.
     *
     * @param keyHandler   The key-input handler
     * @param mouseHandler The mouse-input handler
     */
    public abstract void input(KeyHandler keyHandler, MouseHandler mouseHandler);

    /**
     * This method runs whenever this {@code Entity} collides with another {@code Entity}.
     *
     * @param other The {@code Entity} that this one is colliding with.
     */
    public void collides(Entity other) {
    }

    /**
     * This method updates the collision bounds of this {@code Entity} by checking if
     * this entity is colliding with another {@code Entity}. If such is {@code true},
     * then this method will call the {@link #collides(Entity)} method
     */
    private void updateCollision() {
        if (manager == null) return;

        for (int i = 0; i < manager.getEntities().size(); i++) {
            Entity e = manager.getEntities().get(i);

            if (e != null && e != this) {
                if (e.collision.collides(collision))
                    collides(manager.getEntities().get(i));
            }
        }
    }

    /**
     * The sprite of this {@code Entity}.
     *
     * @return Returns the sprite of this {@code Entity}.
     * @see #sprite
     */
    public Sprite getSprite() {
        return sprite;
    }

    /**
     * Assigns the sprite of this {@code Entity} to the given value.
     *
     * @param sprite The sprite to be assigned to.
     * @see #sprite
     */
    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    /**
     * The position at which this {@code Entity} will be drawn at.
     *
     * @return Returns the position of this {@code Entity}.
     * @see #pos
     */
    public Vector2f getPosition() {
        return pos;
    }

    /**
     * Assigns the position of this {@code Entity} to the given value.
     *
     * @param pos The position to be assigned to.
     * @see #pos
     */
    public void setPosition(Vector2f pos) {
        this.pos = pos;
    }

    /**
     * The depth at which this {@code Entity} will be drawn at.
     *
     * @return Returns the depth of this {@code Entity}.
     * @see #depth
     */
    public float getDepth() {
        return depth;
    }

    /**
     * Assigns the depth of this {@code Entity} to the given value.
     *
     * @param depth The depth to be assigned to.
     * @see #depth
     */
    public void setDepth(float depth) {
        this.depth = depth;
    }

    /**
     * The collision bounds of this {@code Entity}.
     *
     * @return Returns the collision bounds of this {@code Entity}.
     * @see #collision
     */
    public AABB.Rectangle getCollisionBounds() {
        return collision;
    }

    /**
     * Assigns the collision bounds of this {@code Entity} to the given value.
     *
     * @param bounds The collision bounds to be assigned to.
     * @see #collision
     */
    public void setCollisionBounds(AABB.Rectangle bounds) {
        this.collision = bounds;
    }

    /**
     * The entity manager of this {@code Entity}.
     *
     * @return Returns the entity manager for this {@code Entity}.
     */
    public EntityManager getManager() {
        return manager;
    }
}

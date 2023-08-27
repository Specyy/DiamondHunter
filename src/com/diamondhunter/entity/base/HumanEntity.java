package com.diamondhunter.entity.base;

import com.diamondhunter.graphics.sprite.Sprite;
import com.diamondhunter.util.general.Utils;
import com.diamondhunter.util.input.KeyHandler;
import com.diamondhunter.util.input.MouseHandler;
import com.diamondhunter.util.input.MovementDirection;
import com.diamondhunter.util.logic.AABB;
import com.diamondhunter.util.logic.Vector2f;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * A {@code HumanEntity} is an entity that is able to move in at least 4 different directions. Those
 * directions are {@code up}, {@code down}, {@code left} and {@code right}. The current directions t
 * hat this entity is moving in is stored in the {@link #directions} set.
 * <p>
 * Like the name suggests, this class is usually used for entities that are humans. Although,
 * because an animation is not required, you may use this class for any type of entity
 * that can move or that is living.
 * <p>
 * {@code HumanEntity}s usually have an {@link com.diamondhunter.util.graphics.Animation}
 * that plays when the entity is moving to indicate that the entity is doing something.
 * This animation is usually a walking animation. It is recommended to create a 4-directional
 * sprite animations to fit the for directions in the {@link MovementDirection} enumeration
 * although you could also create 8-directional sprite animations and apply the 8 directions
 * with some simple code using the {@link #input(KeyHandler, MouseHandler)} method from
 * the {@link Entity} class.
 */
public abstract class HumanEntity extends Entity {
    /**
     * The speed at which this entity is currently moving at in the x-axis. We use this
     * to change the x position in the {@link #move()} method.
     */
    protected float dx;
    /**
     * The speed at which this entity is currently moving at in the y-axis. We use this
     * to change the y position in the {@link #move()} method.
     */
    protected float dy;
    /**
     * The acceleration at which we should go at to achieve our {@link #maxSpeed}. This
     * can be changed to your liking.
     */
    protected float acc = 0.2f;
    /**
     * The deceleration at which we should go at to slow down from our {@link #maxSpeed}.
     * This can also be changed to your liking.
     */
    protected float deacc = acc;
    /**
     * The maximum speed at which this entity can move at.
     */
    protected float maxSpeed = 1.5f;
    /**
     * The current directions that this entity is moving in. If this entity is not moving,
     * this set will have one value that is equal to {@link MovementDirection#NO_DIRECTION}.
     */
    private Set<MovementDirection> directions = new LinkedHashSet<>();
    /**
     * Whether the player is moving up or not.
     */
    private boolean up;

    /**
     * Whether the player is moving down or not.
     */
    private boolean down;

    /**
     * Whether the player is moving left or not.
     */
    private boolean left;

    /**
     * Whether the player is moving right or not.
     */
    private boolean right;

    /**
     * Construct a {@code HumanEntity} with the given arguments.
     *
     * @param sprite The sprite of this entity
     * @param pos    The position of this entity
     */
    public HumanEntity(Sprite sprite, Vector2f pos) {
        super(sprite, pos);
    }

    /**
     * Construct a {@code HumanEntity} with the given arguments.
     *
     * @param sprite    The sprite of this entity
     * @param pos       The position of this entity
     * @param collision The collision bounds of this entity
     */
    public HumanEntity(Sprite sprite, Vector2f pos, AABB.Rectangle collision) {
        super(sprite, pos, collision);
    }

    /**
     * Construct a {@code HumanEntity} with the given arguments.
     *
     * @param manager The entity manager used for this entity
     * @param sprite  The sprite of this entity
     * @param pos     The position of this entity
     */
    public HumanEntity(EntityManager manager, Sprite sprite, Vector2f pos) {
        super(manager, sprite, pos);
    }

    /**
     * Construct a {@code HumanEntity} with the given arguments.
     *
     * @param manager   The entity manager used for this entity
     * @param sprite    The sprite of this entity
     * @param pos       The position of this entity
     * @param collision The collision bounds of this entity
     */
    public HumanEntity(EntityManager manager, Sprite sprite, Vector2f pos, AABB.Rectangle collision) {
        super(manager, sprite, pos, collision);
    }

    /**
     * Update the collision from the super-class ({@link Entity}) and move this entity
     * if it is needed.
     *
     * @see #move()
     */
    @Override
    public void update() {
        super.update();
        move();
    }

    /**
     * Moves this given entity to a certain position according to their direction.
     * The speed at which this entity moves at can be customized by changing the
     * {@link #acc}, {@link #deacc} and the {@link #maxSpeed} variables.
     * <p>
     * This method will also return the value of how much this entity moved relative
     * to the world. However, the value returned will be the distance squared of
     * how much this entity actually moved. If you wish to find the distance, use
     * may use the {@link Math#sqrt(double)} function to find the square root of that
     * value, which is equal to the distance.
     * <p>
     * The reason why this method returns the distance squared and not just the distance,
     * is because of cpu-usage reasons and we do not want to use the {@link Math#sqrt(double)}
     * function if it is not needed because it is a math-heavy method.
     *
     * @return Returns the distance squared that was moved by this entity.
     * @see Math#sqrt(double)
     */
    private float move() {

        // Move up if up == true
        if (up) {

            dy += (MovementDirection.UP.getDirection() * acc);

            if (dy < (MovementDirection.UP.getDirection() * maxSpeed)) {
                dy = (MovementDirection.UP.getDirection() * maxSpeed);
            }
        } else {

            // Decelerate if up == false
            if (dy < 0) {
                MovementDirection opposite = MovementDirection.UP.getOpposite();

                if (opposite == null)
                    dy += deacc;
                else
                    dy += (opposite.getDirection() * deacc);

                if (dy > 0)
                    dy = 0;
            }
        }

        // Move down if down == true
        if (down) {

            dy += (MovementDirection.DOWN.getDirection() * acc);

            if (dy > (MovementDirection.DOWN.getDirection() * maxSpeed)) {
                dy = (MovementDirection.DOWN.getDirection() * maxSpeed);
            }
        } else {

            // Decelerate if down == false
            if (dy > 0) {
                MovementDirection opposite = MovementDirection.DOWN.getOpposite();

                if (opposite == null)
                    dy -= deacc;
                else
                    dy += (opposite.getDirection() * deacc);

                if (dy < 0)
                    dy = 0;
            }
        }

        // Move left if left == true
        if (left) {

            dx += (MovementDirection.LEFT.getDirection() * acc);

            if (dx < (MovementDirection.LEFT.getDirection() * maxSpeed)) {
                dx = (MovementDirection.LEFT.getDirection() * maxSpeed);
            }
        } else {

            // Decelerate if left == false
            if (dx < 0) {
                MovementDirection opposite = MovementDirection.LEFT.getOpposite();

                if (opposite == null)
                    dx += deacc;
                else
                    dx += (opposite.getDirection() * deacc);

                if (dx > 0)
                    dx = 0;
            }
        }

        // Move right if right == true
        if (right) {

            dx += (MovementDirection.RIGHT.getDirection() * acc);

            if (dx > (MovementDirection.RIGHT.getDirection() * maxSpeed)) {
                dx = (MovementDirection.RIGHT.getDirection() * maxSpeed);
            }
        } else {

            // Decelerate if right == false
            if (dx > 0) {
                MovementDirection opposite = MovementDirection.RIGHT.getOpposite();

                if (opposite == null)
                    dx -= deacc;
                else
                    dx += (opposite.getDirection() * deacc);

                if (dx < 0)
                    dx = 0;
            }
        }

        // Cancel out movement so we don't get any weird acceleration and deceleration effects
        if (up && down)
            dy = 0;
        if (left && right)
            dx = 0;

        // Created for efficiency
        Vector2f worldVector = pos.getWorldVector();

        // The distance moved squared
        float returnValue = worldVector.distanceSq(worldVector.x + dx, worldVector.y + dy);

        // Add movement amount to position
        pos.x += dx;
        pos.y += dy;

        return returnValue;
    }

    /**
     * Moves this entity in a certain direction. This method if called whenever the
     * {@link #setDirection(MovementDirection, boolean)}. It will set the {@link #up},
     * {@link #down}, {@link #left}, and {@link #right} variables to their corresponding
     * value if the given direction corresponds to their name.
     *
     * @param direction The direction to move towards.
     * @param value     Whether to toggle it on or off.
     * @see #setDirection(MovementDirection, boolean)
     */
    private void setMovementDirection(MovementDirection direction, boolean value) {
        if ((direction == null || direction == MovementDirection.NO_DIRECTION) && value) {
            up = false;
            down = false;
            left = false;
            right = false;
            return;
        }

        if (direction == MovementDirection.UP) {
            up = value;
        }

        if (direction == MovementDirection.DOWN) {
            down = value;
        }

        if (direction == MovementDirection.LEFT) {
            left = value;
        }

        if (direction == MovementDirection.RIGHT) {
            right = value;
        }

        if (value)
            directions.add(direction);
        else directions.remove(direction);
    }

    /**
     * Moves this entity in a certain direction. It will set the {@link #up},
     * {@link #down}, {@link #left}, and {@link #right} variables to their corresponding
     * value if the given direction corresponds to their name. It will also set the current
     * direction equal to the corresponding direction.
     *
     * @param direction The direction to move towards.
     * @param value     Whether to toggle it on or off.
     * @see #setMovementDirection(MovementDirection, boolean)
     */
    public void setDirection(MovementDirection direction, boolean value) {
        setMovementDirection(direction, value);
    }

    /**
     * If this entity is moving in multiple directions at once, this method will return
     * all the directions that this entity is moving in. If this entity is not moving,
     * this array will have one value that is equal to {@link MovementDirection#NO_DIRECTION}.
     *
     * @return Returns the directions in which this entity is moving in.
     * @see #directions
     */
    public MovementDirection[] getDirections() {
        return Utils.getArrayUtils().stripEmpty(directions.toArray(new MovementDirection[0])).length == 0 ?
                new MovementDirection[]{MovementDirection.NO_DIRECTION} : Utils.getArrayUtils().stripEmpty(directions.toArray(new MovementDirection[0]));
    }

    /**
     * The acceleration at which we should go at to achieve our {@link #maxSpeed}. This
     * can be changed to your liking.
     *
     * @return Returns the acceleration at which we should go at to achieve our {@link #maxSpeed}.
     */
    public float getAcceleration() {
        return acc;
    }

    /**
     * The deceleration at which we should go at to slow down from our {@link #maxSpeed}.
     * This can also be changed to your liking.
     *
     * @return Returns the deceleration at which we should go at to slow down from our {@link #maxSpeed}.
     */
    public float getDeceleration() {
        return deacc;
    }

    /**
     * The speed at which this entity is currently moving at in the x-axis. We use this
     * to change the x position in the {@link #move()} method.
     *
     * @return Returns the speed at which this entity is currently moving at in the x-axis.
     */
    public float getDirectionX() {
        return dx;
    }

    /**
     * The speed at which this entity is currently moving at in the y-axis. We use this
     * to change the y position in the {@link #move()} method.
     *
     * @return Returns the speed at which this entity is currently moving at in the y-axis.
     */
    public float getDirectionY() {
        return dy;
    }
}

package com.diamondhunter.util.logic;

import java.awt.geom.Point2D;
import java.util.regex.Pattern;

/**
 * A {@code Vector2f} class represents a 2D point, stored as a float. This class
 * saves the X and Y position of a point so that it can be used for further interpretation.
 * This {@code Vector2f} has many interpretation possibilities which can be used to great use in this
 * game. A couple of those possibilities are:
 * <blockquote>
 * <ul>
 * <li> Adjusting the X and Y position with offsets </li>
 * <li>Converting X and Y float positions into integer positions</li>
 * <li>Converting X and Y positions into other {@code Vector2f} relative positions</li>
 * <li>Finding the distance between two X and Y points</li>
 * </ul>
 * </blockquote>
 * <p>
 * {@code Vector2f}s are sortable and comparable. This can be used to sort collections of {@code Vector2f}
 * from smallest position to largest position.
 */
public class Vector2f implements Comparable<Vector2f> {

    /**
     * The position of the world. This variable will determine how
     * much a tile or an entity should be moved to get the correct position on
     * the world or map.
     * <p>
     * Let's say that the map/world moves like how it is illustrated below:
     * <blockquote><pre>
     * |----------------------------------------|                |----------------------------------------|
     * |  |------------------|                  |                |---------------|                        |
     * |  |                  |                  |                |               |                        |
     * |  |------------------|                  |   ------->     |---------------|                        |
     * |                                        |                |                                        |
     * |                                        |                |                                        |
     * |----------------------------------------|                |----------------------------------------|
     * </pre></blockquote>
     * <p>
     * Then this {@code world.x} would equal to, let's say 5 (assuming the map moved five pixels
     * to the left), and every tile would have to shift to the opposite direction of the map.
     * So if {@code world.x} == 5, then every tile's X position will change to their current
     * position -5 on the x-axis which will then shift the map towards the left.
     * <p>
     * Now let's say that the map/world moves like how it is illustrated below:
     *
     * <blockquote><pre>
     * |----------------------------------------|                |----------------------------------------|
     * |  |------------------|                  |                |                                        |
     * |  |                  |                  |                |                                        |
     * |  |------------------|                  |   ------->     |  |------------------|                  |
     * |                                        |                |  |                  |                  |
     * |                                        |                |  |------------------|                  |
     * |----------------------------------------|                |----------------------------------------|
     * </pre></blockquote>
     * <p>
     * Then this {@code world.y} would equal to, let's say 5 (assuming the map moved five pixels
     * to the down), and every tile would have to shift to the opposite direction of the map.
     * So if {@code world.y} == 5, then every tile's Y position will change to their current
     * position -5 on the y-axis which will then shift the map downwards.
     * <p>
     * So this world variable is basically a camera for the entities and tiles.
     */
    private static final Vector2f world = new Vector2f();

    /**
     * The X position of this {@code Vector2f}. This should not be used directly to achieve the position
     * of this {@code Vector2f} because it does not take into account the {@link #xOffset xOffset} to your position.
     * Instead, you should use {@link #getPosition()} to get the current position of this
     * {@code Vector2f} on the screen.
     * <p>
     * Think of this as the raw X position.
     *
     * @see #getPosition()
     */
    public float x;

    /**
     * The Y position of this {@code Vector2f}. This should not be used directly to achieve the position
     * of this {@code Vector2f} because it does not take into account the {@link #xOffset yOffset} to your position.
     * Instead, you should use {@link #getPosition()} to get the current position of this
     * {@code Vector2f} on the screen.
     * <p>
     * Think of this as the raw Y position.
     *
     * @see #getPosition()
     */
    public float y;

    /**
     * The amount that the {@link #x} position should be offset by. This can be a positive or a negative
     * value.
     */
    public float xOffset;

    /**
     * The amount that the {@link #y} position should be offset by. This can be a positive or a negative
     * value.
     */
    public float yOffset;

    /**
     * Creates a {@code Vector2f} with an {@code} x and {@code y} position of {@code 0,0}
     */
    public Vector2f() {
        this(0f, 0f);
    }

    /**
     * Creates a {@code Vector2f} with the position from the denoted {@code Vector2f}. Basically,
     * it copies the values of that {@code Vector2f}.
     *
     * @param vec The {@code Vector2f} to copy from.
     */
    public Vector2f(Vector2f vec) {
        this(vec.x, vec.y);

        this.xOffset = vec.xOffset;
        this.yOffset = vec.yOffset;
    }

    /**
     * Creates a {@code Vector2f} from the given point.
     *
     * @param point The point to copy the coordinates from
     */
    public Vector2f(Point2D point) {
        this((float) point.getX(), (float) point.getY());
    }

    /**
     * Creates a {@code Vector2f} with the {@code x} and {@code y} position that is denoted.
     *
     * @param x The x position.
     * @param y The y position.
     */
    public Vector2f(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Assigns the X and Y positions for the world to be the same and the specified
     * X and Y positions.
     *
     * @param x The X position.
     * @param y The Y position.
     */
    public static void setWorldVar(float x, float y) {
        world.x = x;
        world.y = y;
    }

    /**
     * Sets the X and Y positions for the world to be the same and the specified
     * {@code Vector2f}'s X and Y positions.
     *
     * @param vec The {@code Vector2f} to copy positions from.
     */
    public static void setWorldVar(Vector2f vec) {
        setWorldVar(vec.getPosition().x, vec.getPosition().y);
    }

    /**
     * Translates the world position by the factor of the given position.
     *
     * @param translate How much we should translate
     */
    public static void setWorldVar(float translate) {
        setWorldVar(getWorldVar().x + translate, getWorldVar().y + translate);
    }

    /**
     * Returns the world {@code Vector2f} which can be used for changing the world's position, or
     * change the x and y offsets.
     *
     * @return Returns the world's {@code Vector2f}.
     * @see #world
     */
    public static Vector2f getWorldVar() {
        return world;
    }

    /**
     * Unlike the variables {@link #x x} and {@link #y y}, this method returns the
     * position of this {@code Vector2f} with the offsets accounted for. In this case, using
     * the {@code x} and {@code y} variables from the returned {@code Vector2f} would be
     * preferable because the {@code xOffset} and {@code yOffset} will both be {@code 0}.
     * <p>
     * If wish to get the position of the {@code Vector2f} on-screen, use this method.
     *
     * @return Returns the on-screen position of this {@code Vector2f}.
     */
    public Vector2f getPosition() {
        return new Vector2f(x + xOffset, y + yOffset);
    }

    /**
     * The position of this {@code Vector2f} relative to the world. With this method, the point
     * {@code 0,0} will be equal to {@code world.x == 0} && {@code world.y == 0}. The
     * returned {@code Vector2f} will treat that point as {@code 0,0} and not the screen's {@code 0,0}
     * point (which is the top-left of the screen). This can later be used to achieve the
     * position of this {@code Vector2f} relative to the world/map.
     * <p>
     * In this case, using the {@code x} and {@code y} variables from the returned {@code Vector2f}
     * would be preferable because the {@code xOffset} and {@code yOffset} will both be
     * {@code 0}.
     *
     * @return Returns the {@code Vector2f} position relative to the world/map.
     * @see #world
     */
    public Vector2f getWorldVector() {
        return getRelation(getWorldVar());
    }

    public Vector2f getRelation(float x, float y) {
        return new Vector2f(getPosition().x - x, getPosition().y - y);
    }

    public Vector2f getRelation(Vector2f vec) {
        return getRelation(vec.getPosition().x, vec.getPosition().y);
    }

    /**
     * Finds the distance squared between two {@code Vector2f} points.
     *
     * @param vec The vector to measure to.
     * @return Returns the distance squared between the two vectors.
     */
    public float distanceSq(Vector2f vec) {
        float distX = (getPosition().x - vec.getPosition().x);
        float distY = (getPosition().y - vec.getPosition().y);

        return (distX * distX) + (distY * distY);
    }

    /**
     * Finds the distance squared between this {@code Vector2f},
     * and an x and y point.
     *
     * @param x The x point to measure to.
     * @param y The y point to measure to.
     * @return Returns the distance squared between this {@code Vector2f},
     * and an x and y point.
     * @see #distance(float, float)
     */
    public float distanceSq(float x, float y) {
        float distX = (getPosition().x - x);
        float distY = (getPosition().y - y);

        return (distX * distX) + (distY * distY);
    }

    /**
     * Finds the distance between this {@code Vector2f} and the given x and y
     * point. Unlike {@link #distanceSq(float, float)}, this method will find
     * the exact distance between points.
     * <p>
     * So for a triangle with legs a = x2-x1 and b = y2-y1, this method will return
     * the hypotenuse, c. An illustration of this triangle is shown below to help
     * better visualize the situation:
     * <blockquote><pre>
     *                                      |----|
     *                           / | <----- | y2 |
     *                         /   |        |----|
     *                       /     |        |    |
     *                     /       |        |    |
     *                   /         |        |    |
     *  Hypotenuse C   /           |        |    |  Leg B
     *   (distance)  /             |        |    |
     *             /               |        |    |
     *           /                 |        |    |
     *         /                   |        |    |
     *       /                     |        |----|
     * ->  /-----------------------| <------| y1 |
     * |                                |   |----|
     * |   |--|------------------|--|   |
     * |-- |x1|                  |x2| - |
     *     |--|------------------|--|
     *               Leg A
     * </pre></blockquote>
     *
     * @param x The x point to measure
     * @param y The y point to measure
     * @return Returns the distance squared between this {@code Vector2f},
     * and an x and y point.
     * @see #distanceSq(float, float)
     */
    public float distance(float x, float y) {
        return (float) Math.sqrt(distanceSq(x, y));
    }

    /**
     * Finds the distance between this {@code Vector2f} and the given x and y
     * point. Unlike {@link #distanceSq(float, float)}, this method will find
     * the exact distance between points.
     * <p>
     * So for a triangle with legs a = x2-x1 and b = y2-y1, this method will return
     * the hypotenuse, c. An illustration of this triangle is shown below to help
     * better visualize the situation:
     * <blockquote><pre>
     *                                      |----|
     *                           / | <----- | y2 |
     *                         /   |        |----|
     *                       /     |        |    |
     *                     /       |        |    |
     *                   /         |        |    |
     *  Hypotenuse C   /           |        |    |  Leg B
     *   (distance)  /             |        |    |
     *             /               |        |    |
     *           /                 |        |    |
     *         /                   |        |    |
     *       /                     |        |----|
     * ->  /-----------------------| <------| y1 |
     * |                                |   |----|
     * |   |--|------------------|--|   |
     * |-- |x1|                  |x2| - |
     *     |--|------------------|--|
     *               Leg A
     * </pre></blockquote>
     *
     * @param vec The vector to measure to.
     * @return Returns the distance squared between the two vectors.
     */
    public float distance(Vector2f vec) {
        return (float) Math.sqrt(distanceSq(vec));
    }

    /**
     * Returns the X position of this {@code Vector2f} as an integer with the offset
     * accounted for.
     *
     * @return Returns the X position of this {@code Vector2f} as an integer.
     */
    public int getX() {
        return (int) getPosition().x;
    }

    /**
     * Returns the Y position of this {@code Vector2f} as an integer with the offset
     * accounted for.
     *
     * @return Returns the Y position of this {@code Vector2f} as an integer.
     */
    public int getY() {
        return (int) getPosition().y;
    }

    /**
     * Compares whether the given object has the same values as this {@code Vector2f}.
     *
     * @param vec The {@code Vector2f} to compare
     * @return Returns {@code 0} if this object is equal to the given object,
     * {@code -1} if the given object is greater and {@code 1} if this is
     * greater.
     */
    @Override
    public int compareTo(Vector2f vec) {

        if (getPosition().x > vec.getPosition().x && getPosition().y > vec.getPosition().y) {
            return 1;
        } else if (getPosition().x < vec.getPosition().x && getPosition().y < vec.getPosition().y) {
            return -1;
        } else if (getPosition().x > vec.getPosition().x) {
            return 1;
        } else if (getPosition().y > vec.getPosition().y) {
            return 1;
        } else if (getPosition().x < vec.getPosition().x) {
            return -1;
        } else if (getPosition().y < vec.getPosition().y) {
            return -1;
        }

        return 0;
    }

    /**
     * Compares whether the given object has the same values as this {@code Vector2f}.
     *
     * @param obj The object to compare
     * @return Returns {@code true} if this object is equal to the given object,
     * and {@code false} otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;

        if (obj instanceof Vector2f) {
            boolean result = getPosition().x == ((Vector2f) obj).getPosition().x && getPosition().y == ((Vector2f) obj).getPosition().y;

            if (!result)
                return getWorldVector().x == ((Vector2f) obj).getWorldVector().x && getWorldVector().y == ((Vector2f) obj).getWorldVector().y;
        } else if (obj instanceof String) {
            String[] split = null;

            if (!((String) obj).contains(",")) {
                if (!((String) obj).contains(":")) {
                    if (!((String) obj).contains(";")) {
                        if (((String) obj).matches(".*\\s+.*")) {
                            split = ((String) obj).split("\\s+");
                        }
                    } else split = ((String) obj).split(Pattern.quote(";"));
                } else split = ((String) obj).split(Pattern.quote(":"));
            } else split = ((String) obj).split(Pattern.quote(","));

            if (split == null || split.length == 0 || split.length > 2) return false;

            float parsedX;
            float parsedY;

            try {
                parsedX = Float.parseFloat(split[0]);
                parsedY = Float.parseFloat(split[1]);
            } catch (Exception e) {
                return false;
            }

            boolean result = getPosition().x == parsedX && getPosition().y == parsedY;

            if (!result)
                return getWorldVector().x == Float.parseFloat(split[0]) && getWorldVector().y == Float.parseFloat(split[1]);
            else return true;
        }

        return super.equals(obj);
    }

    @Override
    public String toString() {
        return "[x=" + x + ";y=" + y + ";xOffset=" + xOffset + ";yOffset=" + yOffset + ";position.x=" + getPosition().x +
                ";position.y=" + getPosition().y + ";worldX=" + getWorldVector().x + ";worldY=" + getWorldVector().y + "]";
    }
}

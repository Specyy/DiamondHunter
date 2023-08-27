package com.diamondhunter.util.logic;

import com.diamondhunter.util.general.Utils;

import java.awt.*;

public abstract class AABB {

    /**
     * The x-offset of which calculations and rendering will be offset
     * by.
     */
    public float xOffset;

    /**
     * The y-offset of which calculations and rendering will be offset
     * by.
     */
    public float yOffset;


    /**
     * The position of this {@code AABB} stored as a {@link Vector2f}.
     *
     * @see Vector2f
     */
    protected Vector2f pos;

    private AABB(Vector2f pos) {
        this.pos = pos;
    }

    public abstract boolean collides(AABB aabb);

    public abstract boolean collides(Vector2f point);

    public abstract void render(Graphics2D g);

    public abstract float getArea();

    /**
     * The x-offset of the position of this AABB.
     *
     * @return Returns the x-offset of this AABB
     */
    public int getXOffset() {
        return (int) xOffset;
    }

    /**
     * The y-offset of the position of this AABB.
     *
     * @return Returns the y-offset of this AABB
     */
    public int getYOffset() {
        return (int) yOffset;
    }

    public void setVector(Vector2f pos) {
        this.pos = pos;
    }

    /**
     * A {@link Vector2f} representation of the position of this rectangle.
     *
     * @return Returns the position of this rectangle as a {@link Vector2f}.
     * @see Vector2f
     */
    public Vector2f getVector() {
        return pos;
    }

    /**
     * AABB Rectangle
     */
    public static class Rectangle extends AABB {
        // Attributes
        private float width;
        private float height;

        public Rectangle() {
            this(1f);
        }

        public Rectangle(float size) {
            this(new Vector2f(), size);
        }

        public Rectangle(Vector2f pos) {
            this(pos, 1f);
        }

        public Rectangle(Vector2f pos, float size) {
            this(pos, size, size);
        }

        public Rectangle(float width, float height) {
            this(new Vector2f(), width, height);
        }

        public Rectangle(Vector2f pos, float width, float height) {
            super(pos);
            this.pos = pos;
            this.width = width;
            this.height = height;
        }

        @Override
        public boolean collides(AABB aabb) {
            if (aabb instanceof Circle)
                return collidesCircle((Circle) aabb);
            else if (aabb instanceof Rectangle) {
                return collides((Rectangle) aabb);
            }

            return false;
        }

        @Override
        public boolean collides(Vector2f point) {
            float ax = (pos.getPosition().x + xOffset + width / 2);
            float ay = (pos.getPosition().y + yOffset + height / 2);

            // Colliding on X axis
            if (Math.abs(ax - point.getPosition().x) <= width / 2) {
                // Colliding on Y axis
                return Math.abs(ay - point.getPosition().y) <= height / 2;
            }

            return false;
        }

        @Override
        public void render(Graphics2D g) {
            g.drawRect(pos.getX(), pos.getY(), (int) width, (int) height);
        }

        /**
         * Checks whether the given rectangle's bounds are within this rectangle's bounds.
         * This {@link #xOffset} and {@link #yOffset} will be accounted in the calculation.
         * This method however, does not check whether the two rectangles collide on the world
         * but rather if they collide on the screen. For such usage, input the world vector for
         * the position of this rectangle and for the rectangle to check.
         *
         * @param rec The rectangle to be checked for collision
         * @return Returns {@code true} if this rectangle's bounds lies withing the bounds of
         * the denoted rectangle on the screen.
         */
        public boolean collides(AABB.Rectangle rec) {
            float ax = (pos.getPosition().x + xOffset + width / 2);
            float ay = (pos.getPosition().y + yOffset + height / 2);
            float bx = (rec.pos.getPosition().x + rec.xOffset + rec.width / 2);
            float by = (rec.pos.getPosition().y + rec.yOffset + rec.height / 2);

            // Colliding on X axis
            if (Math.abs(ax - bx) <= width / 2 + rec.width / 2) {
                // Colliding on Y axis
                return Math.abs(ay - by) <= height / 2 + rec.height / 2;
            }

            return false;
        }

        /**
         * Like the {@link #collides(Rectangle)} method, this method checks whether two rectangles
         * intersect on the screen rather than the world. However, this method is lighter than that
         * method because we use less math-heavy functions. It would be preferable if you use this
         * method if you are low on memory or if you worry about CPU usage.
         *
         * @param rec The rectangle to check intersection from.
         * @return Returns {@code true} if this rectangle's bounds lies withing the bounds of
         * the denoted rectangle on the screen.
         */
        public boolean collidesTraditional(AABB.Rectangle rec) {
            return this.pos.getPosition().x + xOffset < rec.pos.getPosition().x + rec.xOffset + rec.width &&
                    this.pos.getPosition().x + xOffset + width > rec.pos.getPosition().x + rec.xOffset &&
                    this.pos.getPosition().y + yOffset < rec.pos.getPosition().y + rec.yOffset + rec.height &&
                    this.pos.getPosition().y + yOffset + height > rec.pos.getPosition().y + rec.yOffset;
        }

        /**
         * Checks whether this rectangle intersects a {@link AABB.Circle Circle}. However,
         * this checks the collision bounds on the screen and not on the world. If you
         * wish for such usage, input the world vector for the position of this rectangle.
         *
         * @param circle The circle to be checked
         * @return Returns {@code true} if this rectangle's bounds lies withing the bounds of
         * the denoted circle on the screen.
         */
        public boolean collidesCircle(AABB.Circle circle) {
            float closestX = circle.pos.getPosition().x + circle.xOffset + circle.radius;
            float closestY = circle.pos.getPosition().y + circle.yOffset + circle.radius;

            closestX = Utils.getNumberUtils().clamp(closestX, pos.getPosition().x + xOffset, pos.getPosition().x + xOffset + width);
            closestY = Utils.getNumberUtils().clamp(closestY, pos.getPosition().y + yOffset, pos.getPosition().y + yOffset + height);

            float distanceX = (circle.pos.getPosition().x + circle.xOffset + circle.radius) - (closestX);
            float distanceY = (circle.pos.getPosition().y + circle.yOffset + circle.radius) - (closestY);

            return (distanceX * distanceX) + (distanceY * distanceY) <= (circle.radius * circle.radius);
        }

        public boolean inside(AABB.Rectangle rec) {
            return this.pos.getPosition().x + xOffset > rec.pos.getPosition().x + rec.xOffset
                    && this.pos.getPosition().x + xOffset + width < rec.pos.getPosition().x + rec.xOffset + rec.width
                    && this.pos.getPosition().y + yOffset > rec.pos.getPosition().y + rec.yOffset
                    && this.pos.getPosition().y + yOffset + height < rec.pos.getPosition().y + rec.yOffset + rec.height;
        }

        /**
         * The area that this {@code Rectangle} occupies.
         *
         * @return Returns the area of this {@code Rectangle}.
         */
        public float getArea() {
            return width * height;
        }

        /**
         * The width of this rectangle.
         *
         * @return Returns the width of this rectangle.
         */
        public float getWidth() {
            return width;
        }

        /**
         * Sets the width of this rectangle to the denoted width if it is not equal.
         *
         * @param width The width to set it to.
         */
        public void setWidth(float width) {
            if (this.width != width)
                this.width = width;
        }

        /**
         * The height of this rectangle.
         *
         * @return Returns the height of this rectangle.
         */
        public float getHeight() {
            return height;
        }

        /**
         * Sets the height of this rectangle to the denoted width if it is not equal.
         *
         * @param height The height to set it to.
         */
        public void setHeight(float height) {
            if (this.height != height)
                this.height = height;
        }

        @Override
        public String toString() {
            return pos.toString() + "[width=" + width + ";height=" + height + "]";
        }
    }

    /**
     * AABB Circle
     */
    public static class Circle extends AABB {
        // Attributes
        private float radius;

        public Circle(float radius) {
            this(new Vector2f(), radius);
        }

        public Circle(Vector2f pos) {
            this(pos, 1f);
        }

        public Circle(Vector2f pos, float radius) {
            super(pos);
            this.pos = pos;
            this.radius = radius;
        }

        @Override
        public boolean collides(AABB aabb) {
            if (aabb instanceof Circle)
                return collides((Circle) aabb);
            else if (aabb instanceof Rectangle) {
                return collidesRectangle((Rectangle) aabb);
            }

            return false;
        }

        @Override
        public boolean collides(Vector2f point) {
            float distanceX = (pos.getPosition().x + xOffset + radius) - (point.getPosition().x);
            float distanceY = (pos.getPosition().y + yOffset + radius) - (point.getPosition().y);

            return (distanceX * distanceX) + (distanceY * distanceY) <= (radius * radius);
        }

        @Override
        public void render(Graphics2D g) {
            g.drawOval(pos.getX(), pos.getY(), (int) (radius * 2.0f), (int) (radius * 2.0f));
        }

        public boolean collides(AABB.Circle circle) {
            float ax = (circle.pos.getPosition().x + circle.xOffset + circle.radius) - (pos.getPosition().x + xOffset + radius);
            float ay = (circle.pos.getPosition().y + circle.yOffset + circle.radius) - (pos.getPosition().y + yOffset + radius);

            return (ax * ax) + (ay * ay) <= (circle.radius + radius) * (circle.radius + radius);
        }

        public boolean collidesRectangle(AABB.Rectangle rec) {
            return rec.collidesCircle(this);
        }

        public float getArea() {
            return (float) (Math.PI * radius * radius);
        }

        public Vector2f getVector() {
            return pos;
        }

        public float getRadius() {
            return radius;
        }

        public void setRadius(float radius) {
            this.radius = radius;
        }

        @Override
        public String toString() {
            return pos.toString() + "[radius=" + radius + "]";
        }
    }
}

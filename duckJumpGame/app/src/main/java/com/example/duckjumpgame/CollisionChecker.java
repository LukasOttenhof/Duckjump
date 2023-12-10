package com.example.duckjumpgame;

/**
 * CollisionChecker.java is designed to check the collision of the duck object through a set of
 * parameters. These parameters are the x and y values that outline the duck, and the x and y
 * values that outline another Imageview, and are therefore the collision boxes of the duck and another
 * ImageView. This functions by checking if the collision box of the duck overlaps with the
 * collision box of the other image who's collision box is being compared to the duck.
 */

public class CollisionChecker {
    private int duckTopY;
    private int duckBottomY;
    private int objectTopY;
    private int objectBottomY;
    private int duckLeft;
    private int duckRight;
    private int duckHalf;
    private int objectLeft;
    private int objectRight;

    /**
     * In the constructor initialize all private variables which are coordinates, to the corresponding
     * parameter.
     *
     * @param duckTopY
     * @param duckBottomY
     * @param objectTopY
     * @param objectBottomY
     * @param duckLeft
     * @param duckRight
     * @param duckHalf
     * @param objectLeft
     * @param objectRight
     */
    public CollisionChecker(int duckTopY, int duckBottomY, int objectTopY, int objectBottomY,
                            int duckLeft, int duckRight, int duckHalf, int objectLeft, int objectRight) {
        this.duckTopY = duckTopY;
        this.duckBottomY = duckBottomY;
        this.objectTopY = objectTopY;
        this.objectBottomY = objectBottomY;
        this.duckLeft = duckLeft;
        this.duckRight = duckRight;
        this.duckHalf = duckHalf;
        this.objectLeft = objectLeft;
        this.objectRight = objectRight;
    }

    /**
     * This method compares the coordinates of the duck and the ImageView that it is being
     * compared to. If there is overlap between them the method will return ture, otherwise the
     * method will return false.
     *
     * @return True if there is collision, false if there is not
     */
    public boolean checkCollision() {
        // First create booleans that evaluate to true if there is vertical overlap
        boolean topCollision = duckTopY <= objectBottomY && duckTopY >= objectTopY;
        boolean bottomCollision = duckBottomY >= objectTopY && duckBottomY <= objectBottomY;
        boolean middleCollision = (duckBottomY + duckHalf >= objectTopY && (duckBottomY +
                (duckHalf) <= objectBottomY));

        // Next create booleans that evaluate to true if there is horizontal overlap
        boolean leftCollision = duckLeft >= objectLeft && duckLeft <= objectRight;
        boolean rightCollision = duckRight >= objectLeft && duckRight <= objectRight;

        // Return true if there is vertical overlap and horizontal overlap to indicate collision
        return (topCollision || bottomCollision || middleCollision) && (leftCollision || rightCollision);
    }
}

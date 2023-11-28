package com.example.duckjumpgame;

public class CollisionChecker {
    private int duckTopY;
    private int duckBottomY;
    private int platformTopY;
    private int platformBottomY;
    private int duckLeft;
    private int duckRight;
    private int duckHalf;
    private int platformLeft;
    private int platformRight;

    public CollisionChecker(int duckTopY, int duckBottomY, int platformTopY, int platformBottomY,
                            int duckLeft, int duckRight, int duckHalf, int platformLeft, int platformRight) {
        this.duckTopY = duckTopY;
        this.duckBottomY = duckBottomY;
        this.platformTopY = platformTopY;
        this.platformBottomY = platformBottomY;
        this.duckLeft = duckLeft;
        this.duckRight = duckRight;
        this.duckHalf = duckHalf;
        this.platformLeft = platformLeft;
        this.platformRight = platformRight;
    }

    public boolean checkCollision() {
        // First create booleans that evaluate to true if there is vertical overlap
        boolean topCollision = duckTopY <= platformBottomY && duckTopY >= platformTopY;
        boolean bottomCollision = duckBottomY >= platformTopY && duckBottomY <= platformBottomY;
        boolean middleCollision = (duckBottomY + duckHalf >= platformTopY && (duckBottomY +
                (duckHalf) <= platformBottomY));

        // Next create booleans that evaluate to true if there is horizontal overlap
        boolean leftCollision = duckLeft >= platformLeft && duckLeft <= platformRight;
        boolean rightCollision = duckRight >= platformLeft && duckRight <= platformRight;

        // Return true if there is vertical overlap and horizontal overlap to indicate collision
        return (topCollision || bottomCollision || middleCollision) && (leftCollision || rightCollision);
    }
}

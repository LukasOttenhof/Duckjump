package com.example.duckjumpgame;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TestAnimateAndDetectCollision {
    @Test
    public void testBottomCollision() {
        assertEquals(checkCollision(50,100, 90, 110,
                100, 120, 80, 100), true);
    }
    public boolean checkCollision(int duckTopY, int duckBottomY, int platformTopY,
                                  int platformBottomY,  int duckLeft,int duckRight,
                                  int platformLeft, int platformRight){


        // First create booleans that eveluate to true if there is vertical overlap
        boolean topCollision = duckTopY <= platformBottomY && duckTopY >= platformTopY;
        boolean bottomCollision = duckBottomY >= platformTopY && duckBottomY <= platformBottomY;
        boolean middleCollision = (duckBottomY - duckTopY /2) >= platformTopY && (duckBottomY +
                (duckBottomY - duckTopY /2) <= platformBottomY);

        // Next create booleans that evaluate to true if there is horizontal overlap
        boolean leftCollision = duckLeft >= platformLeft && duckLeft <= platformRight;
        boolean rightCollision = duckRight >= platformLeft && duckRight <= platformRight;

        // Return true if there is vertical overlap and horizontal overlap to indicate collision
        return (topCollision || bottomCollision || middleCollision) && (leftCollision || rightCollision);

    }
}


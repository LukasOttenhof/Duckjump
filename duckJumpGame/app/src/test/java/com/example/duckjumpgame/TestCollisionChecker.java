package com.example.duckjumpgame;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestCollisionChecker {

    @Test
    public void testCollision() {
        // Set up test data
        int duckTopY = 50;
        int duckBottomY = 100;
        int platformTopY = 80;
        int platformBottomY = 120;
        int duckLeft = 30;
        int duckRight = 70;
        int duckHalf = 25;
        int platformLeft = 20;
        int platformRight = 90;

        // Create an instance of CollisionChecker with the test data
        CollisionChecker collisionChecker = new CollisionChecker(
                duckTopY, duckBottomY, platformTopY, platformBottomY,
                duckLeft, duckRight, duckHalf, platformLeft, platformRight
        );

        // Perform the test
        boolean result = collisionChecker.checkCollision();

        // Assert the result
        assertTrue(result);
    }

    @Test
    public void testNoCollision() {
        // Set up test data for a case where there is no collision
        int duckTopY = 50;
        int duckBottomY = 100;
        int platformTopY = 150;
        int platformBottomY = 200;
        int duckLeft = 30;
        int duckRight = 70;
        int duckHalf = 25;
        int platformLeft = 20;
        int platformRight = 90;

        // Create an instance of CollisionChecker with the test data
        CollisionChecker collisionChecker = new CollisionChecker(
                duckTopY, duckBottomY, platformTopY, platformBottomY,
                duckLeft, duckRight, duckHalf, platformLeft, platformRight
        );

        // Perform the test
        boolean result = collisionChecker.checkCollision();

        // Assert the result
        assertFalse(result);
    }
}

package com.example.duckjumpgame;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestCollisionChecker {

    @Test
    public void testCollisionFromTop() {
        // Set up test data for collision from the top
        int duckTopY = 90;
        int duckBottomY = 140;
        int platformTopY = 80;
        int platformBottomY = 120;
        int duckLeft = 100;
        int duckRight = 120;
        int duckHalf = 25;
        int platformLeft = 80;
        int platformRight = 100;

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
    public void testCollisionFromBottom() {
        // Set up test data for collision from the bottom
        int duckTopY = 50;
        int duckBottomY = 100;
        int platformTopY = 90;
        int platformBottomY = 110;
        int duckLeft = 100;
        int duckRight = 120;
        int duckHalf = 25;
        int platformLeft = 80;
        int platformRight = 100;

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
    public void testCollisionFromRight() {
        // Set up test data for collision from the right
        int duckTopY = 50;
        int duckBottomY = 100;
        int platformTopY = 80;
        int platformBottomY = 120;
        int duckLeft = 65;
        int duckRight = 110;
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
    public void testCollisionFromLeft() {
        // Set up test data for collision from the left
        int duckTopY = 50;
        int duckBottomY = 100;
        int platformTopY = 80;
        int platformBottomY = 120;
        int duckLeft = 5;
        int duckRight = 35;
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


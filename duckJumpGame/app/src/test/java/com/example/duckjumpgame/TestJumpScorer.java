package com.example.duckjumpgame;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestJumpScorer {

    private JumpScorer jumpScorer;

    @Before
    public void setUp() {
        jumpScorer = new JumpScorer();
    }

    @Test
    public void testUpdateScore() {
        int expectedScore = 10; // Assuming the scoring logic increments by 1 for each of the 10 iterations
        jumpScorer.updateScore();
        assertEquals(expectedScore, jumpScorer.getScoreDistance());
    }

    @Test
    public void testResetScore() {
        int initialScore = 5;
        jumpScorer.updateScore(); // Increment the score to a non-zero value
        jumpScorer.resetScore();
        assertEquals(0, jumpScorer.getScoreDistance());
    }

    @Test
    public void testMultipleUpdateScore() {
        int expectedScore = 20; // Assuming the scoring logic increments by 1 for each of the 10 iterations, and we call it twice
        jumpScorer.updateScore();
        jumpScorer.updateScore();
        assertEquals(expectedScore, jumpScorer.getScoreDistance());
    }
}

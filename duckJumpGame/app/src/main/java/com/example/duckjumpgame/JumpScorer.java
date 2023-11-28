package com.example.duckjumpgame;

public class JumpScorer {
    private int scoreDistance;

    public JumpScorer() {
        // Initialize any necessary variables or settings for scoring
    }

    /**
     * Update the score distance based on a jump.
     */
    public void updateScore() {
        // Modify the scoring logic if needed
        for (int scoreCount = 10; scoreCount > 0; scoreCount -= 1) {
            scoreDistance += 1;
        }
    }

    /**
     * Get the current score distance.
     *
     * @return The current score distance.
     */
    public int getScoreDistance() {
        return scoreDistance;
    }

    /**
     * Reset the score distance to zero.
     */
    public void resetScore() {
        scoreDistance = 0;
    }
}
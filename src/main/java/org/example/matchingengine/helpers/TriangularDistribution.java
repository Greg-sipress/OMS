package org.example.matchingengine.helpers;

import java.util.Random;

public class TriangularDistribution {
    private static final Random random = new Random();

    /**
     * Generate a random number following a triangular distribution.
     *
     * @param min   - Minimum value
     * @param max   - Maximum value
     * @param mode  - Most likely value (middle value where the distribution peaks)
     * @return Random number that follows the triangular distribution
     */
    public static double triangular(double min, double max, double mode) {
        double u = random.nextDouble(); // Generate a random number between 0 and 1
        double c = (mode - min) / (max - min); // Calculate the mode's position relative to min and max

        // Determine if the number falls to the left or right of the mode
        if (u <= c) {
            return min + Math.sqrt(u * (max - min) * (mode - min)); // Left of the mode
        } else {
            return max - Math.sqrt((1 - u) * (max - min) * (max - mode)); // Right of the mode
        }
    }

}
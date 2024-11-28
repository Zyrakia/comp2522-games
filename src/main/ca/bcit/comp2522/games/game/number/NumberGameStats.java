package ca.bcit.comp2522.games.game.number;

import ca.bcit.comp2522.games.util.Strings;

import java.text.DecimalFormat;

/**
 * Stat tracker for the number game.
 *
 * @author Ole Lammer
 * @version 1.0
 */
public final class NumberGameStats {

    private int wins;
    private int losses;
    private int placements;

    /**
     * Increments the wins counter by one.
     */
    public void recordWin() {
        this.wins++;
    }

    /**
     * Increments the loss counter by one.
     */
    public void recordLoss() {
        this.losses++;
    }

    /**
     * Increments the placements counter by one.
     */
    public void recordPlacement() {
        this.placements++;
    }

    /**
     * Resets all tracked stats.
     */
    public void reset() {
        this.wins = 0;
        this.losses = 0;
        this.placements = 0;
    }

    /**
     * Returns the total amount of games played.
     *
     * @return the total games
     */
    private int getTotalGames() {
        return this.wins + this.losses;
    }

    /**
     * Returns the average amount of placements per game.
     *
     * @return the average successful placements per game
     */
    private double getAveragePlacements() {
        final int totalGames;
        totalGames = this.getTotalGames();

        if (totalGames == 0) {
            return 0;
        }

        return ((double) this.placements) / totalGames;
    }

    @Override
    public String toString() {
        final DecimalFormat avgFmt;
        final StringBuilder sb;
        final int totalGames;
        final double averagePlacements;

        avgFmt = new DecimalFormat("#.##");
        sb = new StringBuilder();
        totalGames = this.getTotalGames();
        averagePlacements = this.getAveragePlacements();

        if (this.wins != 0) {
            sb.append("You won ")
                    .append(this.wins)
                    .append(" out of ")
                    .append(totalGames)
                    .append(Strings.pluralize(totalGames, " game"))
                    .append(" and you ");
        } else {
            sb.append("You ");
        }

        sb.append("lost ")
                .append(this.losses)
                .append(" out of ")
                .append(totalGames)
                .append(Strings.pluralize(totalGames, " game"));

        sb.append(", with ")
                .append(this.placements)
                .append(" successful ")
                .append(Strings.pluralize(this.placements, "placement"))
                .append(", an average of ")
                .append(avgFmt.format(averagePlacements))
                .append(" per game!");

        return sb.toString();
    }

}

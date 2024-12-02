package ca.bcit.comp2522.games.game.crafter;

import ca.bcit.comp2522.games.game.crafter.item.ItemStack;
import ca.bcit.comp2522.games.util.Observable;

import java.util.Map;
import java.util.Random;

/**
 * Represents a harvester which can be harvested multiple times to "break it" and then receive a reward.
 *
 * @author Ole Lammers
 * @version 1.0
 */
public final class Harvester extends Observable<Integer> {

    private static final double MIN_CHANCE = 0.0;
    private static final double MAX_CHANCE = 1.0;
    private static final int MIN_STAGES = 1;

    private final Random rand;
    private final Map<ItemStack, Double> drops;
    private final int stageCount;

    private int currentStage;

    /**
     * Creates a new harvester.
     *
     * @param drops      the drops, each mapped to their drop chance
     * @param stageCount the amount of stages that must be harvested to receive a reward
     */
    public Harvester(final Map<ItemStack, Double> drops, final int stageCount) {
        Harvester.validateDrops(drops);
        Harvester.validateStageCount(stageCount);

        this.rand = new Random();
        this.drops = drops;
        this.stageCount = stageCount;

        this.resetCurrentStage();
    }

    /**
     * Validates the given drops to ensure that the drop chances add up properly, and that there is at least one drop.
     *
     * @param drops the drops to validate
     */
    private static void validateDrops(final Map<ItemStack, Double> drops) {
        if (drops.isEmpty()) {
            throw new IllegalArgumentException("A harvester must have at least one drop.");
        }

        final double sum;
        sum = drops.values().stream().reduce(Harvester.MIN_CHANCE, Double::sum);

        if (sum != Harvester.MAX_CHANCE) {
            throw new IllegalArgumentException(
                    "The drop chances of a harvest block must add to " + Harvester.MAX_CHANCE + ".");
        }
    }

    /**
     * Validates the given stage count to ensure it is within limits.
     *
     * @param stageCount the stage count
     */
    private static void validateStageCount(final int stageCount) {
        if (stageCount < Harvester.MIN_STAGES) {
            throw new IllegalArgumentException("A harvester must have at least " + Harvester.MIN_STAGES + " stages.");
        }
    }

    /**
     * Harvests one stage of this harvester.
     * <p>
     * If a reward is issued, the current stage is set to {@value Harvester#MIN_STAGES} - 1 again.
     *
     * @return the reward, can be null if the harvester has not been fully harvested.
     */
    public ItemStack harvest() {
        final int newStage;
        newStage = this.currentStage + 1;

        if (newStage >= this.stageCount) {
            this.resetCurrentStage();
            return this.pickReward();
        }

        this.currentStage = newStage;
        this.announceUpdate(this.currentStage);

        return null;
    }

    /**
     * Resets the current stage to the base stage.
     */
    private void resetCurrentStage() {
        this.currentStage = Harvester.MIN_STAGES - 1;
        this.announceUpdate(this.currentStage);
    }

    /**
     * Returns the amount of stages remaining before a reward is issued.
     * <p>
     * If the result is 1, the next harvest will result in a reward.
     *
     * @return the remaining stages
     */
    public int getRemainingStages() {
        return this.stageCount - this.currentStage;
    }

    /**
     * Returns the current stage this harvester is at.
     *
     * @return the current stage
     */
    public int getCurrentStage() {
        return this.currentStage;
    }

    /**
     * Returns the amount of stages in this harvester.
     *
     * @return the total stage count
     */
    public int getStageCount() {
        return this.stageCount;
    }

    /**
     * Picks a random reward based on the drops of this harvester.
     *
     * @return the drop
     */
    private ItemStack pickReward() {
        double randomValue;
        randomValue = this.rand.nextDouble();

        for (final Map.Entry<ItemStack, Double> drop : this.drops.entrySet()) {
            randomValue -= drop.getValue();
            if (randomValue <= 0) {
                return drop.getKey();
            }
        }

        throw new IllegalStateException("No valid drop found, which should not be possible.");
    }

}

package theater;

/**
 * Abstract calculator for performance-related calculations.
 */
public abstract class AbstractPerformanceCalculator {
    private final Performance performance;
    private final Play play;

    public AbstractPerformanceCalculator(Performance performance, Play play) {
        this.performance = performance;
        this.play = play;
    }

    public Performance getPerformance() {
        return performance;
    }

    public Play getPlay() {
        return play;
    }

    public static AbstractPerformanceCalculator createPerformanceCalculator(Performance performance, Play play) {
        return new AbstractPerformanceCalculator(performance, play) {
            @Override
            public int amountFor() {
                return 0;
            }
        };
    }

    public abstract int amountFor();
}

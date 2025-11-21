package theater;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class holds data for generating statements.
 */
public class StatementData {
    private final Invoice invoice;
    private final Map<String, Play> plays;
    private final List<PerformanceData> performances;

    public StatementData(Invoice invoice, Map<String, Play> plays) {
        this.invoice = invoice;
        this.plays = plays;
        this.performances = new ArrayList<>();
        for (Performance performance : invoice.getPerformances()) {
            performances.add(createPerformanceData(performance));
        }
    }

    public String getCustomer() {
        return invoice.getCustomer();
    }

    public List<PerformanceData> getPerformances() {
        return performances;
    }

    private PerformanceData createPerformanceData(Performance performance) {
        Play play = plays.get(performance.getPlayID());
        int amount = calculateAmount(performance, play);
        int volumeCredits = calculateVolumeCredits(performance, play);
        return new PerformanceData(play.getName(), performance.getAudience(),
                play.getType(), amount, volumeCredits);
    }

    private int calculateAmount(Performance performance, Play play) {
        int result;
        switch (play.getType()) {
            case "tragedy":
                result = Constants.TRAGEDY_BASE_AMOUNT;
                if (performance.getAudience() > Constants.TRAGEDY_AUDIENCE_THRESHOLD) {
                    result += Constants.TRAGEDY_OVER_BASE_CAPACITY_PER_PERSON
                            * (performance.getAudience() - Constants.TRAGEDY_AUDIENCE_THRESHOLD);
                }
                break;
            case "comedy":
                result = Constants.COMEDY_BASE_AMOUNT;
                if (performance.getAudience() > Constants.COMEDY_AUDIENCE_THRESHOLD) {
                    result += Constants.COMEDY_OVER_BASE_CAPACITY_AMOUNT
                            + (Constants.COMEDY_OVER_BASE_CAPACITY_PER_PERSON
                                    * (performance.getAudience() - Constants.COMEDY_AUDIENCE_THRESHOLD));
                }
                result += Constants.COMEDY_AMOUNT_PER_AUDIENCE * performance.getAudience();
                break;
            default:
                throw new RuntimeException(String.format("unknown type: %s", play.getType()));
        }
        return result;
    }

    private int calculateVolumeCredits(Performance performance, Play play) {
        int result = 0;
        result += Math.max(performance.getAudience() - Constants.BASE_VOLUME_CREDIT_THRESHOLD, 0);
        // add extra credit for every five comedy attendees
        if ("comedy".equals(play.getType())) {
            result += performance.getAudience() / Constants.COMEDY_EXTRA_VOLUME_FACTOR;
        }
        return result;
    }

    public int totalAmount() {
        int result = 0;
        for (PerformanceData perfData : performances) {
            result += perfData.getAmount();
        }
        return result;
    }

    public int volumeCredits() {
        int result = 0;
        for (PerformanceData perfData : performances) {
            result += perfData.getVolumeCredits();
        }
        return result;
    }
}

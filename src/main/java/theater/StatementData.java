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
        AbstractPerformanceCalculator calculator = AbstractPerformanceCalculator
                .createPerformanceCalculator(performance, play);
        int amount = calculator.amountFor();
        int volumeCredits = calculator.volumeCredits();
        return new PerformanceData(play.getName(), performance.getAudience(),
                play.getType(), amount, volumeCredits);
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

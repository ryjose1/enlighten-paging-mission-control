import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        // Get the input telemetry
        List<Telemetry> telemetryData = Parser.readTelemetryFile(args[0]);

        // Group the telemetry by time intervals
        Map<Long, Interval> intervals = new HashMap<>();
        for (Telemetry t : telemetryData) {
            long key = Interval.getIntervalStart(t.getTimestamp());
            if (intervals.containsKey(key)) {
                intervals.get(key).addTelemetry(t);
            } else {
                Interval interval = new Interval();
                interval.addTelemetry(t);
                intervals.put(key, interval);
            }
        }

        // Gather the alerts from each interval
        List<Alert> alerts = new ArrayList<>();
        for (Interval interval : intervals.values()) {
            alerts.addAll(interval.generateAlerts());
        }

        // Output the results
        System.out.println(Alert.toJson(alerts));
    }
}

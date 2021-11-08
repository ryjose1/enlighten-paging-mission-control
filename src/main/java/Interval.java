import java.util.*;

public class Interval {
    // Interval is five-min buckets
    private static final long INTERVAL_DURATION = 300000;

    // Key: Satellite ID, Val: all collected telemetry for that satellite
    private final Map<String, List<Telemetry>> data;

    public static long getIntervalStart(long epochTime) {
        return epochTime / INTERVAL_DURATION * INTERVAL_DURATION;
    }

    public Interval() {
        this.data = new HashMap<>();
    }

    public void addTelemetry(Telemetry t) {
        String key = t.getSatelliteId();
        if (data.containsKey(key)) {
            data.get(key).add(t);
        } else {
            List<Telemetry> val = new ArrayList<>();
            val.add(t);
            data.put(key, val);
        }
    }

    public List<Alert> generateAlerts() {
        List<Alert> alerts = new ArrayList<>();
        for (String key : data.keySet()) {
            alerts.addAll(Alert.generateAlerts(getTelemetry(key), key));
        }
        Collections.sort(alerts);
        return alerts;
    }

    private List<Telemetry> getTelemetry(String key) {
        return data.getOrDefault(key, new ArrayList<>());
    }
}

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Alert implements Comparable<Alert> {
    private final int satelliteId;
    private final String severity;
    private final String component;
    private final long timestamp;

    public Alert(int satelliteId, String severity, String component, long timestamp) {
        this.satelliteId = satelliteId;
        this.severity = severity;
        this.component = component;
        this.timestamp = timestamp;
    }

    public static String toJson(List<Alert> alerts) {
        return "[\n" +
                alerts.stream().map(Alert::toString).collect(Collectors.joining(",\n")) +
                "\n]";
    }

    public static List<Alert> generateAlerts(List<Telemetry> data, String satelliteId) {
        List<Alert> alerts = new ArrayList<>();
        int id = Integer.parseInt(satelliteId);
        alerts.addAll(AlertRules.batteryIsRedLow(data, id));
        alerts.addAll(AlertRules.thermostatIsRedHigh(data, id));
        return alerts;
    }

    private static class AlertRules {
        public static List<Alert> batteryIsRedLow(List<Telemetry> data, int satelliteId) {
            List<Alert> alerts = new ArrayList<>();
            List<Telemetry> violations = data.stream()
                    .filter(t -> t.getComponent().equals(Telemetry.Component.BATTERY.getType()))
                    .filter(Telemetry::isUnderRedLowLimit)
                    .collect(Collectors.toList());

            if (violations.size() >= 3) {
                long timestamp = Telemetry.getFirstTimestamp(violations);
                alerts.add(new Alert(satelliteId, "RED LOW", Telemetry.Component.BATTERY.getType(), timestamp));
            }
            return alerts;
        }

        public static List<Alert> thermostatIsRedHigh(List<Telemetry> data, int satelliteId) {
            List<Alert> alerts = new ArrayList<>();
            List<Telemetry> violations = data.stream()
                    .filter(t -> t.getComponent().equals(Telemetry.Component.THERMOMETER.getType()))
                    .filter(Telemetry::isAboveRedHighLimit)
                    .collect(Collectors.toList());

            if (violations.size() >= 3) {
                long timestamp = Telemetry.getFirstTimestamp(violations);
                alerts.add(new Alert(satelliteId, "RED HIGH", Telemetry.Component.THERMOMETER.getType(), timestamp));
            }
            return alerts;
        }
    }

    private static String toOutputDate(long epochTimestamp) {
        LocalDateTime date = LocalDateTime.ofInstant(Instant.ofEpochMilli(epochTimestamp), ZoneId.of("UTC"));
        DateTimeFormatter formatter = DateTimeFormatter.ISO_INSTANT;
        return date.atOffset(ZoneOffset.UTC).format(formatter);
    }

    public int compareTo(Alert that) {
        return (int) (that.timestamp - this.timestamp);
    }

    @Override
    public String toString() {
        String[] pairs = {
                String.format("\"%s\": %d", "satelliteId", satelliteId),
                String.format("\"%s\": \"%s\"", "severity", severity),
                String.format("\"%s\": \"%s\"", "component", component),
                String.format("\"%s\": \"%s\"", "timestamp", toOutputDate(timestamp))
        };
        return "{\n" +
                String.join(",\n", pairs) +
                "\n}";
    }
}


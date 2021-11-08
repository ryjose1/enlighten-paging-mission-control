import java.util.Collections;
import java.util.List;

public class Telemetry implements Comparable<Telemetry> {
    public enum Component {
        BATTERY("BATT"),
        THERMOMETER("TSTAT");

        private final String type;

        Component(String type) {
            this.type = type;
        }

        public String getType() {
            return this.type;
        }
    }

    private long timestamp;
    private int redHighLimit;
    private int yellowHighLimit;
    private int yellowLowLimit;
    private int redLowLimit;
    private String satelliteId;
    private double rawValue;
    private String component;

    public static long getFirstTimestamp(List<Telemetry> data) {
        Collections.sort(data);
        return data.get(0).timestamp;
    }

    public boolean isUnderRedLowLimit() {
        return rawValue < redLowLimit;
    }

    public boolean isAboveRedHighLimit() {
        return rawValue > redHighLimit;
    }

    public int compareTo(Telemetry that) {
        return (int) (this.timestamp - that.timestamp);
    }

    @Override
    public String toString() {
        String format = "Telemetry{" +
                "timestamp='%d', satelliteId='%s', redHighLimit='%d', yellowHighLimit='%d'" +
                ", yellowLowLimit='%d', redLowLimit='%d', rawValue='%.2f', component='%s'" +
                "}";
        return String.format(format, timestamp, satelliteId, redHighLimit, yellowHighLimit,
                yellowLowLimit, redLowLimit, rawValue, component);
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setSatelliteId(String satelliteId) {
        this.satelliteId = satelliteId;
    }

    public String getSatelliteId() {
        return satelliteId;
    }

    public void setRedHighLimit(int redHighLimit) {
        this.redHighLimit = redHighLimit;
    }

    public void setYellowHighLimit(int yellowHighLimit) {
        this.yellowHighLimit = yellowHighLimit;
    }

    public void setYellowLowLimit(int yellowLowLimit) {
        this.yellowLowLimit = yellowLowLimit;
    }

    public void setRedLowLimit(int redLowLimit) {
        this.redLowLimit = redLowLimit;
    }

    public void setRawValue(double rawValue) {
        this.rawValue = rawValue;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getComponent() {
        return component;
    }
}

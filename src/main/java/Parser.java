import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Parser {
    private enum TelemetryFields {
        TIMESTAMP,
        SATELLITE_ID,
        RED_HIGH_LIMIT,
        YELLOW_HIGH_LIMIT,
        YELLOW_LOW_LIMIT,
        RED_LOW_LIMIT,
        RAW_VALUE,
        COMPONENT
    }

    public static List<Telemetry> readTelemetryFile(String filename) {
        List<Telemetry> telemetryData = new ArrayList<>();
        try {
            Stream<String> lines = Files.lines(Paths.get(filename));
            telemetryData = lines.map(Parser::parseTelemetry).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return telemetryData;
    }

    public static long parseTimestamp(String timestamp) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss.SSS");
        return LocalDateTime.parse(timestamp, formatter).atZone(ZoneId.of("UTC")).toInstant().toEpochMilli();
    }

    public static Telemetry parseTelemetry(String data) {
        String[] fields = data.split("\\|");

        Telemetry t = new Telemetry();
        for (int i = 0; i < fields.length; i++) {
            String fieldVal = fields[i];
            TelemetryFields field = TelemetryFields.values()[i];
            switch (field) {
                case TIMESTAMP:
                    t.setTimestamp(parseTimestamp(fieldVal));
                    break;
                case SATELLITE_ID:
                    t.setSatelliteId(fieldVal);
                    break;
                case RED_HIGH_LIMIT:
                    t.setRedHighLimit(Integer.parseInt(fieldVal));
                    break;
                case YELLOW_HIGH_LIMIT:
                    t.setYellowHighLimit(Integer.parseInt(fieldVal));
                    break;
                case YELLOW_LOW_LIMIT:
                    t.setYellowLowLimit(Integer.parseInt(fieldVal));
                    break;
                case RED_LOW_LIMIT:
                    t.setRedLowLimit(Integer.parseInt(fieldVal));
                    break;
                case RAW_VALUE:
                    t.setRawValue(Double.parseDouble(fieldVal));
                    break;
                case COMPONENT:
                    t.setComponent(fieldVal);
                    break;
            }
        }
        return t;
    }
}

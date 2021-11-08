import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class ParserTest {

    private static final Path RESOURCES_PATH = Paths.get("src", "test", "resources");
    @Test
    public void correctParsesSingleTelemetry() {

    }

    @Test
    public void correctParsesSampleTest() {
        String test = RESOURCES_PATH.resolve("SampleInput.txt").toFile().getAbsolutePath();
        Parser.readTelemetryFile(test);
        List<Telemetry> readings = Parser.readTelemetryFile(test);
        for (Telemetry t : readings) {
            System.out.println(t.toString());
        }
    }
}

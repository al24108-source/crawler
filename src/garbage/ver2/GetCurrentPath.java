package ver2;

import java.nio.file.Path;
import java.nio.file.Paths;

public class GetCurrentPath {
    public Path get() {
        return Paths.get("").toAbsolutePath();
    }
}

package ver3.finder;

import java.nio.file.Path;
import java.util.HashMap;

import org.jsoup.nodes.Document;

public interface ResourceFinder {
    void find(Document doc, HashMap<String, Path> Map);
}

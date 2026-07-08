package ver2.finder;

import java.nio.file.Path;
import java.util.HashMap;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class JsFinder implements ResourceFinder {
    
    @Override
    public void find(Document doc, HashMap<String, Path> resourceMap) {

        Elements links = doc.select("script[src]");
        for(Element element : links){
            resourceMap.put(element.attr("abs:src"), null);
        }
    }
    
}

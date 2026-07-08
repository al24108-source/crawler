package ver2.finder;

import java.nio.file.Path;
import java.util.HashMap;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class LinkFinder {

    public void find(Document doc, HashMap<String, Path> linkMap) {
        
        Elements links = doc.select("a[href]");
        for(Element element : links){
            linkMap.put(element.attr("abs:href"), null);
        }
    }
}
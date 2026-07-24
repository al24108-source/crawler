package ver3.finder;

import java.nio.file.Path;
import java.util.HashMap;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CssFinder implements ResourceFinder{

    @Override
    public void find(Document doc, HashMap<String, Path> resourceMap) {

        Elements links = doc.select("link[href~=.css]");
        for(Element element : links){
            resourceMap.put(element.attr("abs:href"), null);
        }
    }
    
}

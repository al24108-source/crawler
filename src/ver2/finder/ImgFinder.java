package ver2.finder;

import java.nio.file.Path;
import java.util.HashMap;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ImgFinder implements ResourceFinder{
    
    @Override
    public void find(Document doc, HashMap<String, Path> resourceMap) {

        Elements links = doc.select("img[src]");
        for(Element element : links){
            resourceMap.put(element.attr("abs:src"), null);
        }
    }

}
package ver2;

import java.nio.file.Path;
import java.util.HashMap;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class DocReplacer {
    public void replaceResource(Document doc, HashMap<String, Path> resourceMap){

        Elements links = doc.select("img[src]");
        for(Element element : links){
            String url = element.attr("abs:src");
            if(resourceMap.containsKey(url)){
                element.attr("src", resourceMap.get(url).toString());
            }
        }

        links = doc.select("link[href~=.css]");
        for(Element element : links){
            String url = element.attr("abs:href");
            if(resourceMap.containsKey(url)){
                element.attr("href", resourceMap.get(url).toString());
            }
        }

        links = doc.select("script[src]");
        for(Element element : links){
            String url = element.attr("abs:src");
            if(resourceMap.containsKey(url)){
                element.attr("src", resourceMap.get(url).toString());
            }
        }
    }

    public void replaceLink(Document doc, HashMap<String, Path> linkMap){
        Elements links = doc.select("a[href]");
        for(Element element : links){
            String url = element.attr("abs:href");
            if(linkMap.containsKey(url) && linkMap.get(url) != null){
                element.attr("href", linkMap.get(url).toString());
            }
        }
    }
}

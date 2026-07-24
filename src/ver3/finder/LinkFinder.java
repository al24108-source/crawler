package ver3.finder;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class LinkFinder {

    public List<String> find(Document doc) {
        List<String> urls = new ArrayList<>();
        Elements links = doc.select("a[href]");
        for (Element element : links) {
            String absUrl = element.attr("abs:href");
            if (!absUrl.isEmpty()) {
                urls.add(absUrl);
                // System.out.println(absUrl);
            }
        }
        return urls;
    }
}
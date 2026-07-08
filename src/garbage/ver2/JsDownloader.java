package ver2;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class JsDownloader {
    
    Document doc;
    String folderpath;

    // コンストラクタ
    JsDownloader(Document doc, String folderpath){
        this.doc = doc;
        this.folderpath = folderpath;
    }

    // jsのダウンロード
    public void download(){
        Elements links = doc.select("script[src]");
        for(Element element : links){
            String url = element.attr("abs:src");
            
            // ダウンロードし、属性値の変更
            UrlDownloader downloadFromUrl = new UrlDownloader(url, folderpath);
            element.attr("src", downloadFromUrl.download());
        }
    }
    
}

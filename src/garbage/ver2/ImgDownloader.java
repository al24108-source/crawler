package ver2;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ImgDownloader {
    
    Document doc;
    String folderpath;

    // コンストラクタ
    ImgDownloader(Document doc, String folderpath){
        this.doc = doc;
        this.folderpath = folderpath;
    }

    // imgのダウンロード
    public void download(){
        Elements links = doc.select("img[src]");
        for(Element element : links){
            String url = element.attr("abs:src");
            
            // ダウンロードし、属性値の変更
            UrlDownloader downloadFromUrl = new UrlDownloader(url, folderpath);
            element.attr("src", downloadFromUrl.download());
        }
    }
}

package ver2;

import java.nio.file.Path;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Downloader {
    Document doc;
    static Path srcpath;

    // コンストラクタ
    Downloader(Document doc, Path srcpath){
        this.doc = doc;
        Downloader.srcpath = srcpath;
    }

    // imgのダウンロード
    public void imgdownload(){
        Elements links = doc.select("img[src]");
        for(Element element : links){
            String url = element.attr("abs:src");
            
            // ダウンロードし、属性値の変更
            DownloadFromUrl downloadFromUrl = new DownloadFromUrl(url, srcpath);
            element.attr("src", downloadFromUrl.download());

            sleep(100);
        }
    }

    // cssのダウンロード
    public void cssdownload(){
        Elements links = doc.select("link[href~=.css]");
        for(Element element : links){
            String url = element.attr("abs:href");
            
            // ダウンロードし、属性値の変更
            DownloadFromUrl downloadFromUrl = new DownloadFromUrl(url, srcpath);
            element.attr("href", downloadFromUrl.download());

            sleep(100);
        }
    }

    // jsのダウンロード
    public void jsdownload(){
        Elements links = doc.select("script[src]");
        for(Element element : links){
            String url = element.attr("abs:src");
            
            // ダウンロードし、属性値の変更
            DownloadFromUrl downloadFromUrl = new DownloadFromUrl(url, srcpath);
            element.attr("src", downloadFromUrl.download());

            sleep(100);
        }
    }

    private void sleep(int i){
        // サーバーに負荷をかけないよう間隔を空ける
        try {
            Thread.sleep(i);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}

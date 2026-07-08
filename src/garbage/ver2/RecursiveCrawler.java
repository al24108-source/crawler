package ver2;

import java.io.BufferedWriter;
import java.io.FileWriter;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class RecursiveCrawler {
    
    Document doc;
    int depth;

    // コンストラクタ
    RecursiveCrawler(Document doc, int depth){
        this.doc = doc;
        this.depth = depth;
    }

    // htmlのダウンロード
    public void recursiveCrawl(){

        Elements links = doc.select("a[href]");
        for(Element element : links){
            String url = element.attr("abs:href");

            try{
                
                if(url.startsWith("javascript:")){
                    return;
                }

                // 次のクロール
                Crawler nextCrawler = new Crawler(url, depth - 1);
                if(nextCrawler.crawl()){
                    element.attr("href", nextCrawler.docSavepath);
                }

            }catch(Exception e){
                System.out.println(e);
                try{
                    BufferedWriter bw = new BufferedWriter(new FileWriter("C:\\Users\\kawa1\\sotsuken1\\crawler\\data\\https___www.rakuten.co.jp_\\src\\Exception.txt", true));
                    bw.write(e + "\n");
                    bw.close();
                }catch(Exception e2){
                    System.out.println(e2);
                }
            }

            // サーバーに負荷をかけないよう間隔を空ける
            try {
                Thread.sleep(100);
            } catch (Exception e) {
                System.out.println(e);
            }

        }

    }
    
}

package ver2;

import java.io.BufferedWriter;
import java.io.FileWriter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.nio.file.Path;

public class Crawler {
    
    // 変数
    int depth;
    Document doc;

    // static変数
    static Path UrlNameFolder;

    // URLを保存する静的なハッシュマップ

    // 保存するhtmlのパス
    Path docSavePath;

    NextLinkMap nextLinkMap = new NextLinkMap();

    // CrawlerMainからのみのコンストラクタ
    Crawler(String url, int depth, Path UrlNameFolder){
        try{
            this.doc = Jsoup.connect(url).get();
        }catch(Exception e){
            System.out.println(e);
        }
        this.depth = depth;
        this.UrlNameFolder = UrlNameFolder;
        this.docSavePath = UrlNameFolder.resolve(url);
    }
    
    // クロールメソッド
    public void crawl() {
        
        // mapに保存されている場合は戻る
        if(nextLinkMap.containsKey(doc.location())){
            return;
        }
        
        // depthが残っていないならクロールせずに戻る
        if(depth <= 0){
            return;
        }

        System.out.println(doc.title());
        
        // mapに追加　<url, そのurlから保存するhtmlのパス>
        nextLinkMap.put(doc.location(), docSavePath);

        // クロール開始
        try{
            Downloader downloader = new Downloader(doc, srcpath);
            
            // html内のimgをダウンロード
            downloader.imgdownload();
            
            // html内のcssをダウンロード
            downloader.cssdownload();
            
            // html内のjsをダウンロード
            downloader.jsdownload();
            
            // 再帰的にクロール
            RecursiveCrawler recursiveCrawler = new RecursiveCrawler(doc, depth);
            recursiveCrawler.recursiveCrawl();
            
            // htmlを保存
            SaveDoc();
            
        }catch(Exception e){
            System.out.println(e);
            try{
                BufferedWriter bw = new BufferedWriter(new FileWriter(srcpath + "Exception.txt", true));
                bw.write(e + "\n");
                bw.close();
            }catch(Exception e2){
                System.out.println(e2);
            }
        }

        return true;
    }
    


    // HTML書き出し関数
    private void SaveDoc(){
        
        try{
            // htmlをStringに
            String htmlString = doc.html();
            
            // 書き出し
            BufferedWriter bw = new BufferedWriter(new FileWriter(docSavepath));
            bw.write(htmlString);
            bw.close();

        }catch(Exception e){
            System.out.println(e);
            try{
                BufferedWriter bw = new BufferedWriter(new FileWriter(srcpath + "Exception.txt", true));
                bw.write(e + "\n");
                bw.close();
            }catch(Exception e2){
                System.out.println(e2);
            }
        }
    }

}

package ver2;

import java.nio.file.Path;
import java.nio.file.Paths;

public class CrawlerMain {

    static String downloadUrl = "https://www.rakuten.co.jp/";
    // static String downloadUrl = "https://books.toscrape.com/";

    static Path currentPath = Paths.get("").toAbsolutePath();

    static int depth = 1;
    
    public static void main(String[] args){
        
        MakeFolder makeFolder = new MakeFolder();
        
        ReplaceCannotUseWord replaceCannotUseWord = new ReplaceCannotUseWord();
        String replacedDownloadUrl = replaceCannotUseWord.replace(downloadUrl);
        Path urlNameFolder = downloadFolder.resolve(replacedDownloadUrl);
        makeFolder.make(urlNameFolder);
        
        Crawler crawler = new Crawler(downloadUrl, depth, urlNameFolder);
        try{
            crawler.crawl();
        }catch(Exception e){
            System.out.println(e);
        }
    }

}

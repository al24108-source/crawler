package ver2;

import java.nio.file.Path;
import java.nio.file.Paths;

public class CrawlerMain {

    static String targetUrl = "https://www.rakuten.co.jp/";
    // static String downloadUrl = "https://books.toscrape.com/";

    static int maxDepth = 1;
    
    public static void main(String[] args){
        
        // クロール用の保存フォルダを指すパスの作成
        Path folderPath = makeCrawlFolderPath();
        
        int currentDepth = 1;

        // クロール
        Crawler crawler = new Crawler(folderPath);
        crawler.crawl(targetUrl, currentDepth, maxDepth);
    }
    

    // ダウンロードするフォルダの作成
    private static Path makeCrawlFolderPath(){
        
        // カレントリのパス
        Path currentPath = getCurrentPath();

        // targetUrlをフォルダ名に使用する
        ReplaceCannotUseWord replaceCannotUseWord = new ReplaceCannotUseWord();
        String replacedTargetUrl = replaceCannotUseWord.replace(targetUrl);
        
        // 保存するフォルダのパスを作成
        return currentPath.resolve("download").resolve(replacedTargetUrl);
    }


    private static Path getCurrentPath(){
        // カレントリのパスを取得
        return Paths.get("").toAbsolutePath();
    }
    
}

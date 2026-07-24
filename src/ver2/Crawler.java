package ver2;

import org.jsoup.nodes.Document;

import ver2.finder.*;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Crawler {

    // resourceを保存するパス
    static Path resourceFolderPath;

    // linkを保存するパス
    static Path linkFolderPath;

    // 最大深度
    static int maxDepth;

    // docを保存する配列
    static ArrayList<Document> docList = new ArrayList<Document>();

    // docを書き換える際に使うマップ
    // link用 <url, そのurlから取得するファイルのパス>
    static HashMap<String, Path> linkMap = new HashMap<String, Path>();
    // resource用 <url, そのurlから取得するファイルのパス>
    static HashMap<String, Path> resourceMap = new HashMap<String, Path>();
    

    public Crawler(Path folderPath, int maxDepth) {
        FolderMaker folderMaker = new FolderMaker();

        // resourceフォルダの制作
        Crawler.resourceFolderPath = folderPath.resolve("resource");
        folderMaker.make(resourceFolderPath);

        // htmlフォルダの制作
        Crawler.linkFolderPath = folderPath.resolve("html");
        folderMaker.make(linkFolderPath);

        // 最大深度の設定
        Crawler.maxDepth = maxDepth;
    }


    // クロールメソッド
    public void crawl(String url, int currentDepth) {
        
        // depthが残っていないならクロールせずに戻る
        if(currentDepth > maxDepth){
            return;
        }

        // urlがmapに保存されている場合は戻る
        if(linkMap.containsKey(url)){
            return;
        }

        // urlをlinkMapに追加　<url, そのurlから取得するファイルのパス>
        putLinkMap(url, linkFolderPath, linkMap);
        
        // docを作る
        Document doc = docMake(url);
        
        // docListに追加
        docList.add(doc);

        // img, css, js を探す
        findResources(doc, resourceMap);

        // link を探す
        findLinks(doc, linkMap);

        // link をクロール
        int nextDepth = currentDepth + 1;
        List<String> links = new ArrayList<>(linkMap.keySet());
        for(String link : links){
            crawl(link, nextDepth);
        }

        // 全部終わってから実行
        if(currentDepth <= 1){
            // resourceをダウンロード
            downloadResources(resourceMap, resourceFolderPath);

            // linkを保存
            saveDocuments(resourceMap, linkMap, linkFolderPath);
        }

    }

    public void putLinkMap(String url, Path linkFolderPath, HashMap<String, Path> linkMap){
        ReplaceCannotUseWord replaceCannotUseWord = new ReplaceCannotUseWord();
        String fileName = replaceCannotUseWord.replace(url)+".html";
        Path filePath = linkFolderPath.resolve(fileName);
        linkMap.put(url, filePath);
    }

    public Document docMake(String url){
        DocMaker docMaker = new DocMaker();
        Document doc = docMaker.make(url);
        return doc;
    }

    private void findResources(Document doc, HashMap<String, Path> resourceMap){
        // img, css, js のURLを探す
        ResourceFinder imgFinder = new ImgFinder();
        ResourceFinder cssFinder = new CssFinder();
        ResourceFinder jsFinder = new JsFinder();

        ResourceFinder[] resourceFinders = {imgFinder, cssFinder, jsFinder};
        for(ResourceFinder resourceFinder : resourceFinders){
            resourceFinder.find(doc, resourceMap);
        }
    }

    private void findLinks(Document doc, HashMap<String, Path> linkMap){
        LinkFinder linkFinder = new LinkFinder();
        linkFinder.find(doc, linkMap);
    }

    private void downloadResources(HashMap<String, Path> resourceMap, Path resourceFolderPath){
        ResourceDownloader resourceDownloader = new ResourceDownloader();
        resourceDownloader.download(resourceMap, resourceFolderPath);
    }

    private void saveDocuments(HashMap<String, Path> resourceMap, HashMap<String, Path> linkMap, Path linkFolderPath){
        DocSaver docSaver = new DocSaver();
        docSaver.save(docList, resourceMap, linkMap, linkFolderPath);
    }

}

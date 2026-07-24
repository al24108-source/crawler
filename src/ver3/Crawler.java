package ver3;

import org.jsoup.nodes.Document;

import ver2.finder.*;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Crawler {

    // resourceを保存するパス
    Path resourceFolderPath;

    // linkを保存するパス
    Path linkFolderPath;

    // 最大深度
    int maxDepth;

    // docを保存する配列
    ArrayList<Document> docList = new ArrayList<Document>();

    // docを書き換える際に使うマップ
    // <url, そのurlから取得するファイルのパス>
    HashMap<String, Path> linkMap = new HashMap<String, Path>();
    

    public Crawler(Path folderPath, int maxDepth) {
        FolderMaker folderMaker = new FolderMaker();

        // resourceフォルダの制作
        this.resourceFolderPath = folderPath.resolve("resource");
        folderMaker.make(resourceFolderPath);

        // htmlフォルダの制作
        this.linkFolderPath = folderPath.resolve("html");
        folderMaker.make(linkFolderPath);

        // 最大深度の設定
        this.maxDepth = maxDepth;
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
        if (doc == null) {
            return;
        }
        
        // docListに追加
        docList.add(doc);

        // img, css, js を探す
        findResources(doc, linkMap);

        // linkを探してくる
        List<String> nextLinks = findLinks(doc);

        // 見つけたlinkをクロール
        int nextDepth = currentDepth + 1;
        for(String link : nextLinks){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            crawl(link, nextDepth);
        }

        // 全部終わってから実行
        if(currentDepth <= 1){
            // resourceをダウンロード
            downloadResources(linkMap, resourceFolderPath);

            // linkを保存
            saveDocuments(linkMap, linkFolderPath);
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

    private void findResources(Document doc, HashMap<String, Path> linkMap){
        // img, css, js のURLを探す
        ResourceFinder imgFinder = new ImgFinder();
        ResourceFinder cssFinder = new CssFinder();
        ResourceFinder jsFinder = new JsFinder();

        ResourceFinder[] resourceFinders = {imgFinder, cssFinder, jsFinder};
        for(ResourceFinder resourceFinder : resourceFinders){
            resourceFinder.find(doc, linkMap);
        }
    }

    private List<String> findLinks(Document doc){
        LinkFinder linkFinder = new LinkFinder();
        return linkFinder.find(doc);
    }

    private void downloadResources(HashMap<String, Path> linkMap, Path resourceFolderPath){
        ResourceDownloader resourceDownloader = new ResourceDownloader();
        resourceDownloader.download(linkMap, resourceFolderPath);
    }

    private void saveDocuments(HashMap<String, Path> linkMap, Path linkFolderPath){
        DocSaver docSaver = new DocSaver();
        docSaver.save(docList, linkMap, linkFolderPath);
    }

}

package ver2;

import org.jsoup.nodes.Document;

import ver2.finder.*;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class Crawler {

    // resourceを保存するパス
    static Path resourceFolderPath;

    // linkを保存するパス
    static Path linkFolderPath;

    // docを保存する配列
    static ArrayList<Document> docList = new ArrayList<Document>();

    // docを書き換える際に使うマップ
    // link用 <url, そのurlから取得するファイルのパス>
    static HashMap<String, Path> linkMap = new HashMap<String, Path>();
    // resource用 <url, そのurlから取得するファイルのパス>
    static HashMap<String, Path> resourceMap = new HashMap<String, Path>();
    

    public Crawler(Path folderPath) {
        Crawler.resourceFolderPath = folderPath.resolve("resource");
        Crawler.linkFolderPath = folderPath.resolve("html");
        FolderMaker folderMaker = new FolderMaker();
        folderMaker.make(resourceFolderPath);
        folderMaker.make(linkFolderPath);
    }

    // クロールメソッド
    public void crawl(String url, int currentDepth, int maxDepth) {
        
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

        // urlからdocを作る
        DocMaker docMaker = new DocMaker();
        Document doc = docMaker.make(url);

        // docListに追加
        docList.add(doc);

        // img, css, js を探す
        findResources(doc, resourceMap);

        // link を探す
        findLinks(doc, linkMap);

        // link をクロール
        int nextDepth = currentDepth + 1;
        Set<String> links = linkMap.keySet();
        for(String link : links){
            crawl(link, nextDepth, maxDepth);
        }

        // 全部終わってから実行
        if(currentDepth <= 1){
            // resourceをダウンロード
            downloadResources(resourceMap, resourceFolderPath);

            // linkを保存
            saveDoc(resourceMap, linkMap, linkFolderPath);
        }

    }

    public void putLinkMap(String url, Path linkFolderPath, HashMap<String, Path> linkMap){
        ReplaceCannotUseWord replaceCannotUseWord = new ReplaceCannotUseWord();
        String fileName = replaceCannotUseWord.replace(url)+".html";
        Path filePath = linkFolderPath.resolve(fileName);
        linkMap.put(url, filePath);
    }
    

    private void findResources(Document doc, HashMap<String, Path> resourceMap){
        // img, css, js のURLを探す
        ResourceFinder imgManager = new ImgFinder();
        ResourceFinder cssManager = new CssFinder();
        ResourceFinder jsManager = new JsFinder();

        ResourceFinder[] resourceManagers = {imgManager, cssManager, jsManager};
        for(ResourceFinder resourceManager : resourceManagers){
            resourceManager.find(doc, resourceMap);
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

    private void saveDoc(HashMap<String, Path> resourceMap, HashMap<String, Path> linkMap, Path linkFolderPath){
        DocSaver docSaver = new DocSaver();
        docSaver.save(docList, resourceMap, linkMap, linkFolderPath);
    }
    
}

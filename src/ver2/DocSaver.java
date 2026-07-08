package ver2;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;

import org.jsoup.nodes.Document;

public class DocSaver {
    public void save(ArrayList<Document> docList, HashMap<String, Path> resourceMap, HashMap<String, Path> linkMap, Path folderPath) {
        for(Document doc : docList){
            DocManager docManager = new DocManager();
            docManager.manageResource(doc, resourceMap);
            docManager.manageLink(doc, linkMap);
            saveDoc(doc, linkMap.get(doc.location()));
        }
    }

    private void saveDoc(Document doc, Path filePath){
        // htmlをStringに
        String htmlString = doc.html();
        
        // 書き出し
        BufferedWriter bw;
        try {
            bw = new BufferedWriter(new FileWriter(filePath.toString()));
            bw.write(htmlString);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(filePath);
    }
}

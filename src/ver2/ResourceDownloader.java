package ver2;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Set;

public class ResourceDownloader {
    public void download(HashMap<String, Path> resourceMap, Path path) {
        Set<String> resourceUrls = resourceMap.keySet();
        for(String resourceUrl : resourceUrls){
            UrlDownloader downloadFromUrl = new UrlDownloader(resourceUrl, path);
            resourceMap.put(resourceUrl, downloadFromUrl.download());
        }
    }
}
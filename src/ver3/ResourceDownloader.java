package ver3;

import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ResourceDownloader {
    public void download(Map<String, Path> resourceMap, Path path) {
        ExecutorService executor = Executors.newFixedThreadPool(5);

        for (Map.Entry<String, Path> entry : resourceMap.entrySet()) {
            String resourceUrl = entry.getKey();
            executor.submit(() -> {
                UrlDownloader downloadFromUrl = new UrlDownloader(resourceUrl, path);
                Path downloadedPath = downloadFromUrl.download();
                if (downloadedPath != null) {
                    resourceMap.put(resourceUrl, downloadedPath);
                }
            });
        }

        // 全スレッドの終了を待機
        executor.shutdown();
        try {
            executor.awaitTermination(1, TimeUnit.HOURS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
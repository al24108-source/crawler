package ver2;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import java.nio.file.Path;

public class UrlDownloader {

    String url;
    Path path;
    String removedUrl;

    String fileName;
    Path downloadPath;
    
    int index;

    // コンストラクタ
    UrlDownloader(String url, Path path){
        this.url = url;
        this.path = path;
    }

    // URLからダウンロードする
    public Path download() {
        
        // http:// を除去
        if(url.startsWith("https://")){
            removedUrl = url.replace("https://", "");
        }

        // http:// を除去
        if(url.startsWith("http://")){
            removedUrl = url.replace("http://", "");
        }
        
        index = removedUrl.lastIndexOf("/");
        if(index >= 0){
            fileName = removedUrl.substring(index + 1);
            downloadPath = path.resolve(removedUrl.substring(0, index));
        }

        // URLの"/"ごとにフォルダを制作
        FolderMaker makeFolder = new FolderMaker();
        makeFolder.make(downloadPath);

        // ファイル名から"?"以降を除去
        index = fileName.indexOf("?");
        if(index >= 0){
            fileName = fileName.substring(0, index);
        }

        // ファイル名から拡張子を除去
        index = fileName.lastIndexOf(".");
        if(index >= 0){
            fileName = fileName.substring(0, index);
        }

        // コネクションを開く
        URLConnection con;
        try {
            con = new URL(url).openConnection();
            con.setConnectTimeout(60000);
            con.setReadTimeout(60000);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        
        // 拡張子を取得
        String fileExtension = getFileExtension(con.getContentType());
        
        // ファイル名を作成
        Path filepath =  downloadPath.resolve(fileName + fileExtension);
        
        // ダウンロード
        InputStream is;
        try {
            is = con.getInputStream();
            OutputStream os = new FileOutputStream(filepath.toString());
            int line;
            while((line = is.read()) != -1){
                os.write(line);
            }
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        
        // ダウンロードしたURLを表示 確認用
        System.out.println(url);
        
        return filepath;

    }

    private String getFileExtension(String contentsType){
        
        String fileExtension = "";

        if(contentsType.contains("image/png")){
            fileExtension = ".png";
        }else if(contentsType.contains("image/jpeg")){
            fileExtension = ".jpg";
        }else if(contentsType.contains("image/gif")){
            fileExtension = ".gif";
        }else if(contentsType.contains("image/svg+xml")){
            fileExtension = ".svg";
        }else if(contentsType.contains("text/html")){
            fileExtension = ".html";
        }else if(contentsType.contains("text/css")){
            fileExtension = ".css";
        }else if(contentsType.contains("application/javascript")){
            fileExtension = ".js";
        }else if(contentsType.contains("application/x-javascript")){
            fileExtension = ".js";
        }else if(contentsType.contains("text/javascript")){
            fileExtension = ".js";
        }else if(contentsType.contains("application/ecmascript")){
            fileExtension = ".js";
        }else{
            try{
                BufferedWriter bw = new BufferedWriter(new FileWriter(path + "ErrorFileExtension.txt", true));
                bw.write("不明な拡張子：" + contentsType + "\n");
                bw.close();

                System.out.println("不明な拡張子：" + contentsType);

            }catch(Exception e){
                System.out.println(e);
            }
        }

        return fileExtension;
    }
}

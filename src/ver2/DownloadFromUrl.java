package ver2;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;

import java.net.URL;
import java.net.URLConnection;

import java.nio.file.Path;

public class DownloadFromUrl {

    String url;
    Path path;
    String removedUrl;

    String fileName;
    Path downloadPath;
    
    int index;

    // コンストラクタ
    DownloadFromUrl(String url, Path path){
        this.url = url;
        this.path = path;
    }

    // URLからダウンロードする
    public String download() {
        
        // http:// を除去
        if(url.startsWith("https://")){
            removedUrl = url.replace("https://", "");
        }
        
        index = removedUrl.lastIndexOf("/");
        if(index >= 0){
            fileName = removedUrl.substring(index + 1);
            downloadPath = path.resolve(removedUrl.substring(0, index));
        }

        // URLの"/"ごとにフォルダを制作
        MakeFolder makeFolder = new MakeFolder();
        makeFolder.make(downloadPath);

        // URLから"?"を除去
        index = fileName.indexOf("?");
        if(index >= 0){
            fileName = fileName.substring(0, index);
        }

        // URLから拡張子を除去
        index = fileName.lastIndexOf(".");
        if(index >= 0){
            fileName = fileName.substring(0, index);
        }

        try{
            // コネクションを開く
            URLConnection con = new URL(url).openConnection();
            con.setConnectTimeout(60000);
            con.setReadTimeout(60000);
            
            // 拡張子を取得
            String fileExtension = getFileExtension(con.getContentType());

            // ファイル名を作成
            String filepath =  downloadPath.toString() + fileName + fileExtension;
            
            // ダウンロード
            InputStream is = con.getInputStream();
            OutputStream os = new FileOutputStream(filepath);
            int line;
            while((line = is.read()) != -1){
                os.write(line);
            }
            os.close();
            
            // ダウンロードしたURLを表示
            System.out.println(url);
            
            return filepath;

        }catch(Exception e){
            System.out.println(e);
            try{
                BufferedWriter bw = new BufferedWriter(new FileWriter(path + "Exception.txt", true));
                bw.write(e + "\n");
                bw.close();
            }catch(Exception e2){
                System.out.println(e2);
            }

            return url;
        }

    }



    // URLからフォルダ名を取得し，そのフォルダ名を返す
    private String makeFolderName(String str){
       String folderName = str;
       int index = str.indexOf("/");
       if(index >= 0){
           folderName = str.substring(0, index);
           if(folderName.length() == 0){
               str = str.substring(1);
               return makeFolderName(str);
           }
       }
       folderName = folderName.replaceAll("[\\\\/:*?\"<>|]", "_");
       return folderName;
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

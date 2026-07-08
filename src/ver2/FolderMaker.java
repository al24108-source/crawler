package ver2;

import java.io.IOException;

import java.nio.file.Path;
import java.nio.file.Files;

public class FolderMaker {

    // ダウンロードしたファイルを保存するためのフォルダを作り，そのフォルダのパスを返す
    public void make(Path path) {
        // フォルダ作成
        try{
            Files.createDirectories(path);
            // System.out.println("フォルダを作成しました");
        }catch(IOException e){
            // System.out.println("フォルダを作成できませんでした");
            e.printStackTrace();
        }
    }
}

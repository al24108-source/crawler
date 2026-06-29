package ver2;

import java.util.HashMap;
import java.nio.file.Path;

public class NextLinkMap extends HashMap<String, Path>{
    
    HashMap<String, Path> links = new HashMap<String, Path>();

    public Path put(String key, Path value){
        return links.put(key, value);
    }

    public Path get(String key){
        return links.get(key);
    }

    public boolean containsKey(String key){
        return links.containsKey(key);
    }
}

package ver3;

public class ReplaceCannotUseWord{
    public String replace(String string) {
        return string.replaceAll("[\\\\/:*?\"<>|]", "_");
    }
}
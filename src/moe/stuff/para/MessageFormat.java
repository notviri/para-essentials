package moe.stuff.para;

public class MessageFormat {
    public static String parseFormat(String s) {
        return s.replaceAll("%", "%%").replaceAll("NAME", "%s").replaceAll("MSG", "%s");
    }
}

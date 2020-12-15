import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Blob {
    private String name;
    private String path;
    public static String addPath;
    /*读取文件路径，生成对应的文件（文件名为key，文件内容是value）*/
    public static void Blob(String path) throws IOException {
        Hash.KVstore(path);
    }
    /*按照key去寻找value*/
    public static String SearchValue(String name) throws IOException{
        return Hash.readObjectFromFile(name);
    }
    /*新增对应文件，生成对应的KV存储文件*/
    public static void add(String addPath) throws IOException{
        String path = "E:\\MyGit\\1111\\11\\game";
        File dir = new File(path + "\\" + addPath);
        String key = "";
        Hash.Filehash(dir);
        Hash.KVstore(Hash.objectpath);
    }
}

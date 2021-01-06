import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Tree {
    private String path;
    /*直接按照路径构造tree*/
    public static void Tree(String path) throws IOException{
        Hash.Show_KVstore(path);
    }

}

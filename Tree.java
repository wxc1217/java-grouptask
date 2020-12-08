import java.io.File;
import java.io.FileOutputStream;

public class Tree extends key_value_save {
    private String key;
    private String content = ""; 

    public Tree(File file) throws Exception {
        for(File f:file.listFiles()){
            if(f.isFile()){   
                content = content + "\n" + "100644 blob " + new Blob(f).getKey() + " " + f.getName();
            }                 //create mode 100644
            if(f.isDirectory()){
                content = content + "\n" + "100644 tree " + new Tree(f).getKey() + " " + f.getName();
            }

        }
        this.key=treehash(content) ;
    }

    public void writeTree() throws Exception{
        String out = this.key;
        FileOutputStream ops = new FileOutputStream(out);
        ops.write(content.getBytes());
    }

    public String getTreeKey() {
        return this.key;
    }

    public String getTreeContent(){
        return this.content;
    }

    public String toStringOfTree() {
        return "100644 tree " + this.key;
    }
}

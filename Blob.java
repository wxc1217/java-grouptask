import java.io.*;

public class Blob extends key_value_save {
    private String key; 
    private String type;
    FileInputStream input;
    
    public Blob(File file) throws Exception {
        filename=file.getName();
        this.key=Filehash(file);
        this.type = "blob";   // the only type "blob"
        this.input = new FileInputStream(filename)
    } 

    public void writeBlob() throws IOException {
        String out = this.key;
        FileOutputStream ops = new FileOutputStream(out);
        ops.write(content.getBytes());
    }

    public String getBlobKey(){
        return this.key;
    }

    public String getBlobInput(){
        return this.Input;
    }

    public String toBlobString() {
        return "100644 blob " + this.key;
    }
}

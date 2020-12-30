import java.io.*;
import java.util.*;

public class Commit {
    private String key; 
    private String value;
    private String presentcommit;
    
    public Commit(String rootDirPath) throws Exception {
        String rootDirKey=Hash.prKVstore(rootDirPath)  //根据所给定工作区目录，对根文件夹进行遍历并返回对应key值；
        File HEAD=new File(Hash.objectpath + File.separator + "HEAD");

        if(HEAD.exists()) {
            if(getPresentcommit()==rootDirKey){
                System.out.println("文件未发生变动，无需commit。" );
            }
            else{
                createCommit(rootDirKey);
                updateHEAD(rootDirKey);
            }    
        }
        else{
            createCommit(rootDirKey);
            createHEAD(rootDirKey);
        }  
    } 

    public void createCommit(String rootDirKey){
        this.value ="";
        this.value += "tree " + rootDirKey + "\n";
        this.value += "parent " + getPresentcommit()+ "\n";
        this.value += "author " + getAuthor() + "\n";
        this.value += "committer " + getCommitter() + "\n";
        this.value += getUserInput()+"\n";
        this.key = Hash.showhash(value);
        Hash.mystorage(String this.key, String this.value);
    }

    public String getPresentcommit() throws IOException{
        this.presentcommit=Hash.readObjectFromFile(File HEAD);
        return presentcommit;
    }

    public void createHEAD(String rootDirKey){
        try {
            file.createNewFile();
        } catch (IOException e) {
            System.out.println("HEAD文件创建失败。");
            e.printStackTrace();
        }
        updateHEAD(rootDirKey)；
    }

    public void updateHEAD(String rootDirKey){
        try {
            FileWriter writer;
            writer =new FileWriter(Hash.objectpath + File.separator + "HEAD");
            writer.write(rootDirKey);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            System.out.println("HEAD文件更新失败。");
            e.printStackTrace();
        }
    }

    public String getAuthor(){
        System.out.println("请输入作者："); 
        Scanner input = new Scanner(System.in);
        String author = input.nextLine();
        return author；
    }

    public String getCommitter(){
        System.out.println("请输入该次commit的提交者："); 
        Scanner input = new Scanner(System.in);
        String committer = input.nextLine();
        return committer；
    }

    public String getUserInput(){
        System.out.println("请输入该次commit的备注："); 
        Scanner input = new Scanner(System.in);
        String UserInput = input.nextLine();
        return UserInput；
    }

    public String returnKey(){
        return this.key;
    }

    public String returnValue(){
        return this.value;
    }

    public String returnPresentcommit(){
        return this.presentcommit;
    }
}

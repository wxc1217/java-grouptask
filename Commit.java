import java.io.*;
import java.util.*;

public class Commit extends Hash{
    private String key; 
    private String value;
    protected String previousCommit; 
    protected String rootTreeKey;       
    protected String author;
    protected String committer; 
    protected String message;
    
    //无参构造方法
    public Commit(){}

    //仅给定Commit的Key的构造方法
    public Commit(String givenCommitKey){
        this.key=givenCommitKey;
    }

    //给定根目录路径、作者信息、提交者信息、备注信息4个字符串参数的构造方法
    public Commit(String rootDirPath,String author,String committer,String message) throws Exception {
        //根据所给定工作区目录，对根文件夹进行遍历并返回对应key值
        String rootDirKey=Hash.Show_KVstore(rootDirPath);
        //创建HEAD File对象
        File HEAD=new File(Hash.objectpath + File.separator + "HEAD");

        //判定工作目录下是否已有HEAD文件，分情况处理
        if(HEAD.exists()) {
            Commit lastCommit=new Commit(readHEAD());
            String lastRootDirKey=lastCommit.getRootTreeKey();

            if(lastRootDirKey==rootDirKey){ //若有HEAD文件且根目录Key与上次Commit中储存的根目录Key相同，则为重复提交，提示用户即可；
                System.out.println("文件未发生变动，无需commit。" );
            }
            else{//若有HEAD文件且根目录Key不同，读入previousCommit，创建Commit，更新HEAD指针文件；
                this.previousCommit=lastCommit.getKey();
                createCommit(rootDirKey,author,committer,message);
            }    
        }
        else{ //若无HEAD文件，创建并设置previousCommit为空，创建Commit；
            file.createNewFile();
            this.previousCommit="";
            createCommit(rootDirKey,author,committer,message);  
        }
    } 

    //创建Commit
    public void createCommit(String rootDirKey,String author,String committer,String message){
        this.author=author;
        this.committer=committer;
        this.message=message;
        this.rootTreeKey=rootDirKey;

        this.value ="";
        this.value += "tree " + this.rootTreeKey + "\n";
        this.value += "parent " + this.previousCommit+ "\n";
        this.value += "author " + this.author + "\n";
        this.value += "committer " + getCommitter() + "\n";
        this.value += this.message+"\n";
        
        //根据value计算key
        StringBuilder key = new StringBuilder();
        try {
            ByteArrayInputStream is = new ByteArrayInputStream(this.value.getBytes());
            byte[] sha1 = Hash.SHA1Checksum(is);
            for (int i=0;i<sha1.length;i++) {
                key.append(Integer.toString((sha1[i]>>4)&0x0F, 16));
                key.append(Integer.toString(sha1[i]&0x0F, 16));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.key = key;

        //根据Key和Value创建Commit文件写入磁盘
        Hash.mystorage(this.key,this.value);

        //更新HEAD指针
        updateHEAD(this.key);
    }

    //从HEAD指针文件中读取上一次Commit的内容，也即上一次的根目录Key；
    public String readHEAD() throws IOException{
        File head=new File(Hash.objectpath + File.separator + "HEAD");
        this.previousCommit=Hash.readFromTextFile(head);
        return previousCommit;
    }

    //更新HEAD，覆盖写入本次的根目录Key；
    public void updateHEAD(String key){
        try {
            FileWriter writer;
            writer =new FileWriter(Hash.objectpath + File.separator + "HEAD");
            writer.write(key);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            System.out.println("HEAD文件更新失败。");
            e.printStackTrace();
        }
    }   

    //访问器：get方法

    public String getRootTreeKey(){
        return this.rootTreeKey;
    }

    public String getAuthor(){
        return this.author;
    }

    public String getCommitter(){
        return committer;
    }

    public String getMessage(){
        return message;
    }

    public String getKey(){
        return key;
    }

    public String getValue(){
        return value;
    }

    public String getPreviousCommit(){
        return this.previousCommit;
    }
}

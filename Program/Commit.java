import java.io.*;
import java.util.*;

public class Commit extends Hash implements Serializable{
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
    public Commit(String rootDirKey,String author,String committer,String message,String previousCommit) throws Exception{
        this.previousCommit = previousCommit;
        this.author=author;
        this.committer=committer;
        this.message=message;
        this.rootTreeKey= rootDirKey;
        this.value ="";
        this.value += "tree " + this.rootTreeKey + "\n";
        this.value += "parent " + this.previousCommit+ "\n";
        this.value += "author " + this.author + "\n";
        this.value += "committer " + getCommitter() + "\n";
        this.value += this.message+"\n";
        this.key = Treehash(value);

    }

    //创建Commit
    public static Commit createCommit(String rootDirPath,String author,String committer,String message) throws Exception {
        Commit nCommit = new Commit();
        //根据所给定工作区目录，对根文件夹进行遍历并返回对应key值
        String rootDirKey = Hash.Show_KVstore(rootDirPath);
        // 查找当前分支
        String branchname = Branch.branchcheck();
        // 若分支存在，创建commit并提交新内容
        if(branchname != "") {
            String lastCommitid = Branch.branchcommit();
            if(lastCommitid != ""){
                Commit lastCommit = (Commit) readCommitFromFile(lastCommitid, Hash.objectpath);
                String lastRootDirKey = lastCommit.getRootTreeKey();
                //若与上次Commit中储存的根目录Key相同，则为重复提交，提示用户即可；
                if(lastRootDirKey.equals(rootDirKey)){
                    System.out.println("文件未发生变动，无需commit。" );
                }else{//若根目录Key不同，读入previousCommit，创建Commit，更新分支信息；
                    String previousCommit = lastCommit.getKey();
                    //创建Commit对象写入文件
                    nCommit = new Commit(rootDirKey,author,committer,message,previousCommit);
                    Object o = (Object) nCommit;
                    writeCommitToFile(o, nCommit.key, Hash.objectpath);
                    //更新分支信息
                    Branch.commitupdate(nCommit.key);
                }
            }else{
                //创建Commit对象写入文件
                nCommit = new Commit(rootDirKey,author,committer,message,null);
                Object o = (Object) nCommit;
                writeCommitToFile(o, nCommit.key, Hash.objectpath);
                //更新分支信息
                Branch.commitupdate(nCommit.key);
            }
        }
        return nCommit;
    }

    /* object对象保存方法 */
    public static void writeCommitToFile(Object obj, String name,  String objpath)
    {
        File file =new File(objpath + "\\" + name);
        FileOutputStream out;
        try {
            out = new FileOutputStream(file);
            ObjectOutputStream objOut=new ObjectOutputStream(out);
            objOut.writeObject(obj);
            objOut.flush();
            objOut.close();
        } catch (IOException e) {
            System.out.println("存储失败！");
            e.printStackTrace();
        }
    }

    /* object对象读取方法 */
    public static Object readCommitFromFile(String objectname , String path)
    {
        Object temp=null;
        File file =new File(path + "\\" + objectname);
        FileInputStream in;
        try {
            in = new FileInputStream(file);
            ObjectInputStream objIn=new ObjectInputStream(in);
            temp=objIn.readObject();
            objIn.close();
        } catch (IOException e) {
            System.out.println("读取失败！");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return temp;
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

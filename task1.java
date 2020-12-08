import java.io.*;
import java.security.MessageDigest;
import java.util.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;


public class task1{
    public static void main(String[] args) {
        try {
            //提示用户输入文件夹路径名
            System.out.println("请输入文件夹路径：" );
            Scanner input = new Scanner(System.in);
            String path = input.next();
            //对文件夹进行深度优先遍历
            Map<String,String> mmap1 = new HashMap<>();
            mmap1 = Kvalue(path);
            printKvalue(path);
            find(path,"c08a80ccba4888f34c1f16bdebec4de1ce23f0");
            addfile(path,"C:\\Users\\Lenovo\\Desktop\\1111\\addfile.txt");
            FileOutputStream outStream = new FileOutputStream("E:/map.txt");//将生成的键值对集合存储在E:/map.txt文件中
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outStream);
            objectOutputStream.writeObject(mmap1);
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //构造键值对集合
    public static Map<String,String> Kvalue(String path) throws Exception{
        Map<String,String> mmap = new HashMap<>();
        mmap = hash1(path);
        return mmap;
    };

    //根据key值进行value的查找
    public static void find(String path,String name) throws Exception{
        Map<String,String> mmap = new HashMap<>();
        mmap = Kvalue(path);//先构造键值对集合
        System.out.println("According to the hashcode "+name+" ,the value of the file is: "+mmap.get(name));//之后根据特定键值查找value
    }

    //git的实现原理：先有文件的拖拽，才有commit和push的提交，就是在目标区域，新增文件已经存在
    public static  Map<String,String> addfile(String path,String filepath) throws Exception{
        Map<String,String> mmap = new HashMap<>();
        mmap = Kvalue(path);//先构造键值对集合
        File file = new File(filepath);
        mmap.put(showhash(file),readFromTextFile(file));
        return mmap;
    };

    public static byte[] SHA1Checksum(InputStream is) throws Exception {
        // 用于计算hash值的文件缓冲区
        byte[] buffer = new byte[1024];
        // 使用SHA1哈希/摘要算法
        MessageDigest complete = MessageDigest.getInstance("SHA-1");
        int numRead = 0;
        do {
            // 读出numRead字节到buffer中
            numRead = is.read(buffer);
            if (numRead > 0) {
                // 根据buffer[0:numRead]的内容更新hash值
                complete.update(buffer, 0, numRead);
            }
            // 文件已读完，退出循环
        } while (numRead != -1);
        // 关闭输入流
        is.close();
        // 返回SHA1哈希值
        return complete.digest();
    }

    public static String showhash(File file) {
        //显示特定文件Hash值
        StringBuilder result = new StringBuilder();
        try {
            FileInputStream is = new FileInputStream(file);
            byte[] sha1 = SHA1Checksum(is);
            for (byte i : sha1) {
                result.append(Integer.toString(i&0XFF, 16));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    //将文件内的内容读出并转化为字符串
    public static String readFromTextFile(File filename) throws IOException{
        ArrayList<String> strArray = new ArrayList<String>();

        InputStreamReader reader = new InputStreamReader(new FileInputStream(filename));
        BufferedReader br = new BufferedReader(reader);
        String line = "";
        line = br.readLine();
        while(line != null) {
            strArray.add(line);
            line = br.readLine();//先读出为字符数组
        }
        String listString = String.join(" ",strArray);//再将字符数组串联得到字符串
        return listString;
    }

    //求指定文件夹内各文件的哈希值序列
    public static Map<String,String> hash1(String path) throws IOException {
        //对文件夹进行深度优先遍历
        File dir = new File(path);
        //返回该文件夹中文件的抽象路径名数组
        File[] fs = dir.listFiles();
        Map<String,String> mymap = new HashMap<>();

        if(fs != null) {
            //采用ArrayList对listFiles()的结果进行排序
            ArrayList<File> paths = new ArrayList<>();
            Collections.addAll(paths, fs);
            paths.sort(Comparator.naturalOrder());
            for(File f : paths) {
                if (f.isFile()) {
                    mymap.put(showhash(f),readFromTextFile(f));
                }
            }
        }
        return mymap;
    }
    //对获得的文件夹内的哈希值序列进行输出
    public static void printKvalue(String path) throws IOException {
        //对文件夹进行深度优先遍历
        File dir = new File(path);
        //返回该文件夹中文件的抽象路径名数组
        File[] fs = dir.listFiles();
        Map<String,String> mymap = new HashMap<>();

        if(fs != null) {
            //采用ArrayList对listFiles()的结果进行排序
            ArrayList<File> paths = new ArrayList<>();
            Collections.addAll(paths, fs);
            paths.sort(Comparator.naturalOrder());
            for (File f : paths) {
                if (f.isFile()) {
                    mymap.put(showhash(f), readFromTextFile(f));
                    System.out.println("file " + f.getName() + " Hash值 " + showhash(f));
                }
            }
        }
    }


}

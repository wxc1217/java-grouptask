import java.io.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.*;

public class key_value_save {
    public static void main(String[] args) {
        Map<String,String> map = new HashMap<>();
        //读取当前key-value数据值
        System.out.println("请输入key与value存储文件的路径：" );
        Scanner input = new Scanner(System.in);
        String kvpath = input.next();
        File kvmap = new File(kvpath);
        try {
            Object obj;
            // 如果不存在key-value数据，新建map并储存
            if(!kvmap.exists()){
                obj = (Object) map;
                writeObjectToFile(obj,kvpath);
            }else{
                obj = readObjectFromFile(kvpath);
                if(obj instanceof HashMap){
                    map =(HashMap)obj;
                }else{
                    System.out.println("读取错误！" );
                }
            }
            showmap(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int end = 0;
        while(end == 0){
            System.out.println("请输入当前操作（1.存储文件夹及其中文件与子文件夹的key-value；2.给出key寻找value；3.给出value新增key-value）：" );
            int i = input.nextInt();
            if(i == 1){
                savekeyvalue(map);
            }else if(i == 2){
                getvalue(map);
            }else if(i == 3){
                addvalue(map);
            }else{
                System.out.println("操作选择错误" );
            }
            System.out.println("请选择是否继续（0.继续；1.停止）：" );
            int j = input.nextInt();
            if(j != 0){
                end = 1;
            }
            Object obj2 = (Object) map;
            writeObjectToFile(obj2,kvpath);
        }
        input.close();
    }

    /* 存储文件夹及其中文件与子文件夹的key-value */
    public static void savekeyvalue(Map map){
        try {
            // 提示用户输入文件夹路径名
            System.out.println("请输入文件夹路径：" );
            Scanner input3 = new Scanner(System.in);
            String path = input3.next();
            // 遍历文件夹，存储key-value
            dfs(path, map);
            showmap(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* 给出key寻找value */
    public static void getvalue(Map map){
        try {
            // 提示用户输入寻找的key值
            System.out.println("请输入文件key值：" );
            Scanner input4 = new Scanner(System.in);
            String key= input4.next();
            // 直接采用get函数得到value
            System.out.println(map.get(key));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* 给出value新增key-value */
    public static void addvalue(Map map){
        try {
            // 提示用户输入主文件夹路径
            System.out.println("请输入主文件夹路径：" );
            Scanner input3 = new Scanner(System.in);
            String path = input3.next();
            // 新建插入文件
            File p = add();
            dfs(path, map);
            showmap(map); // 重新显示map，检查插入是否成功
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* 文件保存 */
    public static void writeObjectToFile(Object obj, String path)
    {
        File file =new File(path);
        FileOutputStream out;
        try {
            out = new FileOutputStream(file);
            ObjectOutputStream objOut=new ObjectOutputStream(out);
            objOut.writeObject(obj);
            objOut.flush();
            objOut.close();
            System.out.println("write object success!");
        } catch (IOException e) {
            System.out.println("write object failed");
            e.printStackTrace();
        }
    }

    /* 文件读取 */
    public static Object readObjectFromFile(String path)
    {
        Object temp=null;
        File file =new File(path);
        FileInputStream in;
        try {
            in = new FileInputStream(file);
            ObjectInputStream objIn=new ObjectInputStream(in);
            temp=objIn.readObject();
            objIn.close();
            System.out.println("read object success!");
        } catch (IOException e) {
            System.out.println("read object failed");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return temp;
    }

    /* 输出Map中所有键值对 */
    public static void showmap(Map mmap){
        Iterator iter = mmap.entrySet().iterator();
        System.out.println("当前map为：" );
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Object key = entry.getKey();
            Object val = entry.getValue();
            System.out.println(key+"    "+val);
        }
    }

    /* 给定value，添加对应的key-value */
    public static  File add(){
        System.out.println("请输入想要插入的文件路径、文件名称、文件内容：" );
        Scanner input2 = new Scanner(System.in);
        String path1 = input2.next();
        String name = input2.next();
        String content = input2.next();
        File p = new File(path1);
        p = writeTXT(path1,name,content);
        return p;
    };

    /* 依照输入流计算Hash值 */
    public static byte[] SHA1Checksum(InputStream is) throws Exception {
        // 用于计算hash值的文件缓冲区
        byte[] buffer = new byte[1024];
        // 使用SHA1哈希/摘要算法
        MessageDigest complete = MessageDigest.getInstance("SHA-1");
        int numRead;
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

    /* 计算文件的Hash值 */
    public static String Filehash(File file) {
        // 显示特定文件Hash值
        StringBuilder result = new StringBuilder();
        try {
            FileInputStream is = new FileInputStream(file);
            byte[] sha1 = SHA1Checksum(is);
            for (byte i : sha1) {
                result.append(Integer.toString(i&0xFF, 16));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    /* 计算文件夹的Hash值 */
    public static String treehash(String value) {
        // 显示特定文件Hash值
        StringBuilder result = new StringBuilder();
        try {
            ByteArrayInputStream is = new ByteArrayInputStream(value.getBytes());
            byte[] sha1 = SHA1Checksum(is);
            for (byte i : sha1) {
                result.append(Integer.toString(i&0xFF, 16));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    /* 读取文件内容 */
    public static String readFromTextFile(File filename) throws IOException{
        // 按行读取文件中的内容
        ArrayList<String> strArray = new ArrayList<String>();
        InputStreamReader reader = new InputStreamReader(new FileInputStream(filename));
        BufferedReader br = new BufferedReader(reader);
        String line = "";
        line = br.readLine();
        while(line != null) {
            strArray.add(line);
            line = br.readLine();
        }
        // 将ArrayList转化为String类型返回
        String listString = String.join(" ",strArray);
        return listString;
    }

    /* 对当前文件夹进行遍历，储存文件及文件夹的key-value值 */
    public static String dfs(String path, Map mymap) throws IOException {
        // 对文件夹进行深度优先遍历
        File dir = new File(path);
        System.out.println("Tree " + dir.getName() + " :");
        // 采用ArrayList记录该文件下文件与文件夹的名称与Hash值
        ArrayList<String> files = new ArrayList<>();
        // 返回该文件夹中文件的抽象路径名数组
        File[] fs = dir.listFiles();
        String key = "";
        if(fs != null) {
            // 采用ArrayList对listFiles()的结果进行排序
            ArrayList<File> paths = new ArrayList<>();
            Collections.addAll(paths, fs);
            paths.sort(Comparator.naturalOrder());
            for(File f : paths) {
                // 若为文件
                if (f.isFile()) {
                    // 计算Hash值并储存
                    key = Filehash(f);
                    mymap.put(key, readFromTextFile(f));
                    System.out.println("Blob " + f.getName() + " Hash值 " + key);
                }
                // 若为文件夹
                if (f.isDirectory()) {
                    key = dfs(f.toString(), mymap);
                }
                // 将文件与文件夹的名称、Hash值记录在当前文件夹的ArrayList中
                files.add(f.getName());
                files.add(key);
            }
        }
        // 将ArrayList转化为String类型返回
        String value = String.join(" ", files);
        key = treehash(value);
        mymap.put(key, value);
        System.out.println("Tree " + dir.getName() + " Hash值 " + key);
        return key;
    }

    /* 写入Txt文件 */
    public static File writeTXT(String path,String title,String content){
        File writename = new File(path);
        // 防止文件建立或读取失败，用catch捕捉错误并打印，也可以throw
        try {
            // 如果该文件不存在，则建立一个新的txt文件
            if(!writename.exists()){
                writename.mkdirs();
            }
            writename = new File(path+"\\"+title+".txt"); // 相对路径
            writename.createNewFile(); // 创建新文件
            BufferedWriter out = new BufferedWriter(new FileWriter(writename));
            out.write(content); // \r\n即为换行
            out.flush(); // 把缓存区内容压入文件
            out.close(); // 关闭文件
        } catch (Exception e) {
            e.printStackTrace();
        }
        return writename;
    }

}

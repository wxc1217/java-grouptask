import java.awt.desktop.SystemSleepEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

public class testforblob {
    public static void main(String args[])throws IOException {
        Scanner input = new Scanner(System.in);
        System.out.println("请输入新增文件名称：");
        String name = input.next();
        Blob.add(name);
    }
}

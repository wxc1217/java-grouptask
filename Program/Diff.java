import java.io.*;
import java.lang.*;
import java.util.*;

public class Diff {
    public static String[] cat(String filename) throws Exception {
        String[] list = null;
        String branchname = Branch.branchcheck();
        if(branchname != "") {
            String lastCommitid = Branch.branchcommit();
            if(lastCommitid != ""){
                list = catfile(filename, lastCommitid);
            }
        }
        return list;
    }

    public static String[] cat(String filename, String commitid) throws Exception {
        return catfile(filename, commitid);
    }

    public static String[] catfile(String filename, String commitid) throws Exception {
        String[] list = null;
        Commit pCommit = (Commit) Commit.readCommitFromFile(commitid, Hash.objectpath);
        String roottreehash = pCommit.getRootTreeKey();
        String filehash = getfilehash(filename, roottreehash);
        if(filehash != null){
            String value = Hash.readObjectFromFile(filehash, Hash.objectpath);
            if(value != null){
                list = value.split ( "\r\n" );
                for (int i = 0; i < list.length; i++){
                    System.out.println(list[i]);
                }
            }
        }else {
            System.out.println("文件不存在！");
        }
        return list;
    }

    public static String getfilehash(String filename, String roottreehash) throws Exception {
        String filehash = null;
        String rootTreeKeyPath = Hash.objectpath + "\\" + roottreehash;
        File treeNode = new File ( rootTreeKeyPath );
        InputStreamReader isr = new InputStreamReader ( new FileInputStream ( treeNode ) );
        BufferedReader br = new BufferedReader ( isr );
        String line = br.readLine ( );
        while ( line != null ) {
            String[] list = line.split ( " " );
            if ( list[ 1 ].equals ( "Blob" ) ) {
                if ( list[ 3 ].equals ( filename ) ) {
                    filehash = list[2];
                }
                if(filehash != null){
                    break;
                }
            }else if ( list[ 1 ].equals ( "Tree" ) ) {
                filehash = getfilehash(filename, list[2]);
            }
            line = br.readLine ( );
        }
        br.close ( );
        return filehash;
    }

}

import java.io.*;
import java.lang.*;
import java.util.Scanner;

    public class RollBack {
        private String rootTreeKey;
        private final String rollBackPath;

        //无参构造方法,无commit key，给定回滚路径常量默认为Hash类提供的默认工作路径；
        public RollBack ( ) {
            rollBackPath = Hash.objectpath;
        }

        //以给定commit key为参数的构造方法，
        public RollBack ( String givenCommitKey ) throws Exception {

            //检查默认工作路径下是否已有回滚仓库，有则删除；
            File[] folder = new File ( Hash.objectpath ).listFiles ( );
            if(folder!=null){
                for ( File f : folder ) {
                    if (!f.getName().equals(".RollBackRepository")) {
                        deleteFolder(f);
                    }
                }
            }

            //在Hash类提供的默认工作路径下创建回滚仓库储存路径；
            rollBackPath = Hash.objectpath + "\\.RollBackRepository";

            //根据给定commit key构造Commit对象，使用访问器得到根目录tree的key；
            rootTreeKey = readRootTreeKey ( givenCommitKey );

            //解析根目录Tree的Key，依据该tree对象所代表的文件夹内的子文件与子文件夹名称以及对应的blob/tree key进行恢复;
            recoverRollBack ( rootTreeKey , rollBackPath );

            //更新分支信息（更新指针）
            Branch.commitupdate(givenCommitKey);
        }

        //以给定commit key givenCommitKey、给定储存路径givenPath为参数的构造方法
        public RollBack ( String givenCommitKey , String givenPath ) throws Exception {

            //检查给定文件夹路径下是否已有，有则删除；
            File[] folder = new File ( givenPath ).listFiles ( );
            if(folder!=null){
                for ( File f : folder ) {
                    if (!f.getName().equals(".RollBackRepository")) {
                        deleteFolder(f);
                    }
                }
            }

            //根据给定路径，构成回滚仓库储存路径；
            rollBackPath = givenPath + "\\.RollBackRepository";

            //根据给定commit key构造Commit对象，使用访问器得到根目录tree的key；
            rootTreeKey = readRootTreeKey ( givenCommitKey );

            //解析根目录Tree的Key，依据该tree对象所代表的文件夹内的子文件与子文件夹名称以及对应的blob/tree key进行恢复;
            recoverRollBack ( rootTreeKey , rollBackPath );

            //更新分支信息（更新指针）
            Branch.commitupdate(givenCommitKey);
        }

        //根据所给commit key读取对应commit文件内容，获得第一行第二个标记符读取的根目录Tree的key
        private String readRootTreeKey ( String givenCommitKey ) throws IOException {
            File commitFile = new File ( Hash.objectpath + "\\" + givenCommitKey );
            Scanner input_read = new Scanner ( commitFile );
            String rTsign = input_read.next ( );
            String rTKey = input_read.next ( );
            return rTKey;
        }

        ///对tree对象所代表的文件夹内的子文件与子文件夹名称以及对应的blob/tree key进行恢复;
        private void recoverRollBack ( String rootTreeKey , String rollBackPath ) throws Exception {
            String rootTreeKeyPath = rollBackPath + "\\" + rootTreeKey;
            File treeNode = new File ( rootTreeKeyPath );
            InputStreamReader isr = new InputStreamReader ( new FileInputStream ( treeNode ) );
            BufferedReader br = new BufferedReader ( isr );
            String line = br.readLine ( );
            while ( line != null ) {
                String[] list = line.split ( " " );
                if ( list[ 0 ].equals ( "Blob" ) ) {
                    FileInputStream is = new FileInputStream ( rollBackPath + "\\" + list[ 1 ] );
                    FileOutputStream os = new FileOutputStream ( rollBackPath + "\\" + list[ 2 ] );
                    byte[] buffer = new byte[ 1024 ];
                    int numRead = 0;
                    while ( numRead != - 1 ) {
                        numRead = is.read ( buffer );
                        if ( numRead > 0 ) {
                            os.write(buffer, 0, numRead);
                        }
                        is.close ( );
                        os.close ( );
                    }
                } else if ( list[ 0 ].equals ( "Tree" ) ) {
                    File f = new File ( rollBackPath + "\\" + list[ 2 ] );
                    if ( ! f.exists ( ) ) {
                        f.mkdir();
                    }
                    // �ݹ�dfs
                    recoverRollBack ( list[ 1 ] , rollBackPath + "\\" + list[ 2 ] );
                }
                line = br.readLine ( );
                br.close ( );
            }
        }

        //清除已有的文件夹
        private void deleteFolder ( File f ) throws Exception {
            if ( ! f.exists ( ) ) {
                throw new Exception ( "No folder!" );
            }
            File[] files = f.listFiles ( );
            if ( files != null ) {
                for ( File file : files ) {
                    if ( file.isDirectory ( ) ) {
                        deleteFolder ( file );
                    } else {
                        file.delete ( );
                    }
                }
            }
            f.delete ( );
        }

        //展示当前历史Commit的日志；用于回滚前查询
        public static void printCommitLog ( ) throws IOException {

            //从Branch文件(指针）中读取当前分支最新的Commit key
            String branchName = Branch.branchcheck();
            String presentCommit = "";
            if(branchName != "") {
                presentCommit = Hash.readObjectFromFile ( branchName, "E:\\MyGit\\branches" );
            }
            String pCommit = presentCommit;

            //递归遍历并显示
            while ( pCommit != null ) {
                File pCommitFile = new File ( Hash.objectpath + "\\" + pCommit );
                Scanner input_log = new Scanner ( pCommitFile );
                String tsign =input_log.next( );
                String treeKey = input_log.next( );
                String psign = input_log.next ( );
                String previousCommit = input_log.next ( );
                System.out.println ( pCommit );
                System.out.println ( Hash.readFromTextFile ( pCommitFile ) );
                pCommit = previousCommit;
                input_log.close ( );
            }
        }

        //访问器方法
        public String getRootTreeKey ( ) {
            return this.rootTreeKey;
        }

    }



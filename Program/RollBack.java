import java.io.*;
import java.lang.*;
import java.util.Scanner;

    public class RollBack {
        private String rootTreeKey;
        private final String rollBackPath;

        //�޲ι��췽��,��commit key�������ع�·������Ĭ��ΪHash���ṩ��Ĭ�Ϲ���·����
        public RollBack ( ) {
            rollBackPath = Hash.objectpath;
        }

        //�Ը���commit keyΪ�����Ĺ��췽����
        public RollBack ( String givenCommitKey ) throws Exception {

            //���Ĭ�Ϲ���·�����Ƿ����лع��ֿ⣬����ɾ����
            File[] folder = new File ( Hash.objectpath ).listFiles ( );
            if(folder!=null){
                for ( File f : folder ) {
                    if (!f.getName().equals(".RollBackRepository")) {
                        deleteFolder(f);
                    }
                }
            }

            //��Hash���ṩ��Ĭ�Ϲ���·���´����ع��ֿⴢ��·����
            rollBackPath = Hash.objectpath + "\\.RollBackRepository";

            //���ݸ���commit key����Commit����ʹ�÷������õ���Ŀ¼tree��key��
            rootTreeKey = readRootTreeKey ( givenCommitKey );

            //������Ŀ¼Tree��Key�����ݸ�tree������������ļ����ڵ����ļ������ļ��������Լ���Ӧ��blob/tree key���лָ�;
            recoverRollBack ( rootTreeKey , rollBackPath );

            //���·�֧��Ϣ������ָ�룩
            Branch.commitupdate(givenCommitKey);
        }

        //�Ը���commit key givenCommitKey����������·��givenPathΪ�����Ĺ��췽��
        public RollBack ( String givenCommitKey , String givenPath ) throws Exception {

            //�������ļ���·�����Ƿ����У�����ɾ����
            File[] folder = new File ( givenPath ).listFiles ( );
            if(folder!=null){
                for ( File f : folder ) {
                    deleteFolder(f);
                }
            }

            //���ݸ���·�������ɻع��ֿⴢ��·����
            rollBackPath = givenPath;

            //���ݸ���commit key����Commit����ʹ�÷������õ���Ŀ¼tree��key��
            rootTreeKey = readRootTreeKey ( givenCommitKey );
            //������Ŀ¼Tree��Key�����ݸ�tree������������ļ����ڵ����ļ������ļ��������Լ���Ӧ��blob/tree key���лָ�;
            recoverRollBack ( rootTreeKey , rollBackPath );

            //���·�֧��Ϣ������ָ�룩
            Branch.commitupdate(givenCommitKey);
        }

        //��������commit key��ȡ��Ӧcommit�ļ����ݣ���õ�һ�еڶ�����Ƿ���ȡ�ĸ�Ŀ¼Tree��key
        private String readRootTreeKey ( String givenCommitKey ) throws IOException {
            Commit pCommit = (Commit) Commit.readCommitFromFile(givenCommitKey, Hash.objectpath);
            return pCommit.getRootTreeKey();
        }

        ///��tree������������ļ����ڵ����ļ������ļ��������Լ���Ӧ��blob/tree key���лָ�;
        private void recoverRollBack ( String rootTreeKey , String rollBackPath ) throws Exception {
            String rootTreeKeyPath = Hash.objectpath + "\\" + rootTreeKey;
            File treeNode = new File ( rootTreeKeyPath );
            InputStreamReader isr = new InputStreamReader ( new FileInputStream ( treeNode ) );
            BufferedReader br = new BufferedReader ( isr );
            String line = br.readLine ( );
            while ( line != null ) {
                String[] list = line.split ( " " );
                if ( list[ 1 ].equals ( "Blob" ) ) {
                    String value = Hash.readObjectFromFile(list[2], Hash.objectpath);
                    Hash.writetofile(value,list[3],rollBackPath);
                } else if ( list[ 1 ].equals ( "Tree" ) ) {
                    File f = new File ( rollBackPath + "\\" + list[ 3 ] );
                    if ( ! f.exists ( ) ) {
                        f.mkdir();
                    }
                    // ???dfs
                    recoverRollBack ( list[ 2 ] , rollBackPath + "\\" + list[ 3 ] );
                }
                line = br.readLine ( );
            }
            br.close ( );
        }

        //������е��ļ���
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

        //չʾ��ǰ��ʷCommit����־�����ڻع�ǰ��ѯ
        public static void printCommitLog ( ) throws IOException {

            //��Branch�ļ�(ָ�룩�ж�ȡ��ǰ��֧���µ�Commit key
            String branchName = Branch.branchcheck();
            String presentCommit = "";
            if(branchName != "") {
                presentCommit = Hash.readObjectFromFile ( branchName, "E:\\MyGit\\branches" );
            }
            String pCommit = presentCommit;

            //�ݹ��������ʾ
            while ( pCommit != null ) {
                Commit tempCommit = (Commit) Commit.readCommitFromFile(pCommit, Hash.objectpath);
                System.out.println ( pCommit );
                System.out.println ( tempCommit.getValue() );
                pCommit = tempCommit.getPreviousCommit();
            }
        }

        //����������
        public String getRootTreeKey ( ) {
            return this.rootTreeKey;
        }

    }



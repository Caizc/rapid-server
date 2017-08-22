import java.util.*;
import java.io.*;
import java.net.*;

public class ServerAgentThread extends Thread {
    static int count = 0;
    int RedOrBlue = 0;
    static float rd;
    static float rs;
    static float bd;
    static float bs;
    static float rx;
    static float ry;
    static float rz;
    static float rRotx;
    static float rRoty;
    static float rRotz;
    static float bx;
    static float by;
    static float bz;
    static float bRotx;
    static float bRoty;
    static float bRotz;
    static int rjump;
    static int bjump;

    static List<ServerAgentThread> ulist = new ArrayList<ServerAgentThread>();
    static Queue<Action> aq = new LinkedList<Action>();
    static Object lock = new Object();
    Socket sc;
    DataInputStream din;
    DataOutputStream dout;
    boolean flag = true;

    //static int iii=0;
    public ServerAgentThread(Socket sc) {
        this.sc = sc;
        try {
            din = new DataInputStream(sc.getInputStream());
            dout = new DataOutputStream(sc.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void broadcastState() {
        for (ServerAgentThread sa : ulist) {
            try {
//				System.out.println(rd);
//				System.out.println(bd);
                byte[] brd = ByteUtil.float2ByteArray(rd);
                byte[] brs = ByteUtil.float2ByteArray(rs);
                byte[] bbd = ByteUtil.float2ByteArray(bd);
                byte[] bbs = ByteUtil.float2ByteArray(bs);
                byte[] brx = ByteUtil.float2ByteArray(rx);
                byte[] bry = ByteUtil.float2ByteArray(ry);
                byte[] brz = ByteUtil.float2ByteArray(rz);
                byte[] brrx = ByteUtil.float2ByteArray(rRotx);
                byte[] brry = ByteUtil.float2ByteArray(rRoty);
                byte[] brrz = ByteUtil.float2ByteArray(rRotz);
                byte[] bbx = ByteUtil.float2ByteArray(bx);
                byte[] bby = ByteUtil.float2ByteArray(by);
                byte[] bbz = ByteUtil.float2ByteArray(bz);
                byte[] bbrx = ByteUtil.float2ByteArray(bRotx);
                byte[] bbry = ByteUtil.float2ByteArray(bRoty);
                byte[] bbrz = ByteUtil.float2ByteArray(bRotz);
                byte[] brjump = ByteUtil.int2ByteArray(rjump);
                byte[] bbjump = ByteUtil.int2ByteArray(bjump);

                byte[] bt = {
                        brd[0], brd[1], brd[2], brd[3],
                        brs[0], brs[1], brs[2], brs[3],
                        bbd[0], bbd[1], bbd[2], bbd[3],
                        bbs[0], bbs[1], bbs[2], bbs[3],
                        brx[0], brx[1], brx[2], brx[3],
                        bry[0], bry[1], bry[2], bry[3],
                        brz[0], brz[1], brz[2], brz[3],
                        brrx[0], brrx[1], brrx[2], brrx[3],
                        brry[0], brry[1], brry[2], brry[3],
                        brrz[0], brrz[1], brrz[2], brrz[3],
                        bbx[0], bbx[1], bbx[2], bbx[3],
                        bby[0], bby[1], bby[2], bby[3],
                        bbz[0], bbz[1], bbz[2], bbz[3],
                        bbrx[0], bbrx[1], bbrx[2], bbrx[3],
                        bbry[0], bbry[1], bbry[2], bbry[3],
                        bbrz[0], bbrz[1], bbrz[2], bbrz[3],
                        brjump[0], brjump[1], brjump[2], brjump[3],
                        bbjump[0], bbjump[1], bbjump[2], bbjump[3]
                };
                synchronized (lock) {
                    byte[] blen = ByteUtil.int2ByteArray(bt.length);//�ȷ�����
                    sa.dout.write(blen);
                    //System.out.println(bt.length);
                    sa.dout.write(bt, 0, bt.length);//�ٷ�����
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void run() {
        while (flag) {
            try {
                if (din.available() == 0) {
                    continue;
                }
                byte[] byteslen = new byte[4];
                din.read(byteslen, 0, byteslen.length);
                int length = ByteUtil.byteArray2Int(byteslen);

                byte[] bytes = new byte[length];
                int count = 0;
                while (count < length) {
                    int templen = din.read(bytes);
                    count += templen;
                }

                splitPackage(bytes);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            din.close();
            dout.close();
            sc.close();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public void splitPackage(byte[] bytes) {
        int length = bytes.length;

        if (length == 0) {
            return;
        }
        if (length == 11) {
            if (count == 0) {
                RedOrBlue = 0;
                ulist.add(this);
                byte[] rob = ByteUtil.int2ByteArray(0);
                byte[] roblen = ByteUtil.int2ByteArray(4);
                byte[] sendrob = {roblen[0], roblen[1], roblen[2], roblen[3],
                        rob[0], rob[1], rob[2], rob[3]};
                try {
                    this.dout.write(sendrob, 0, sendrob.length);
                    count++;
                    System.out.println("one player online");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (count == 1) {
                RedOrBlue = 1;
                ulist.add(this);
                byte[] rob = ByteUtil.int2ByteArray(1);
                byte[] roblen = ByteUtil.int2ByteArray(4);
                byte[] sendrob = {roblen[0], roblen[1], roblen[2], roblen[3],
                        rob[0], rob[1], rob[2], rob[3]};
                try {
                    this.dout.write(sendrob, 0, sendrob.length);
                    count++;
                    System.out.println("two player online");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("full");
                return;
            }
        } else if (length == 72 && count >= 2) {
            byte[] brx = {bytes[0], bytes[1], bytes[2], bytes[3]};
            byte[] bry = {bytes[4], bytes[5], bytes[6], bytes[7]};
            byte[] bbx = {bytes[8], bytes[9], bytes[10], bytes[11]};
            byte[] bby = {bytes[12], bytes[13], bytes[14], bytes[15]};
            byte[] rx = {bytes[16], bytes[17], bytes[18], bytes[19]};
            byte[] ry = {bytes[20], bytes[21], bytes[22], bytes[23]};
            byte[] rz = {bytes[24], bytes[25], bytes[26], bytes[27]};
            byte[] rRotx = {bytes[28], bytes[29], bytes[30], bytes[31]};
            byte[] rRoty = {bytes[32], bytes[33], bytes[34], bytes[35]};
            byte[] rRotz = {bytes[36], bytes[37], bytes[38], bytes[39]};
            byte[] bx = {bytes[40], bytes[41], bytes[42], bytes[43]};
            byte[] by = {bytes[44], bytes[45], bytes[46], bytes[47]};
            byte[] bz = {bytes[48], bytes[49], bytes[50], bytes[51]};
            byte[] bRotx = {bytes[52], bytes[53], bytes[54], bytes[55]};
            byte[] bRoty = {bytes[56], bytes[57], bytes[58], bytes[59]};
            byte[] bRotz = {bytes[60], bytes[61], bytes[62], bytes[63]};
            byte[] brjump = {bytes[64], bytes[65], bytes[66], bytes[67]};
            byte[] bbjump = {bytes[68], bytes[69], bytes[70], bytes[71]};
            Action a = new Action(
                    this.RedOrBlue,
                    ByteUtil.byteArray2Float(brx),
                    ByteUtil.byteArray2Float(bry),
                    ByteUtil.byteArray2Float(bbx),
                    ByteUtil.byteArray2Float(bby),
                    ByteUtil.byteArray2Float(rx),
                    ByteUtil.byteArray2Float(ry),
                    ByteUtil.byteArray2Float(rz),
                    ByteUtil.byteArray2Float(rRotx),
                    ByteUtil.byteArray2Float(rRoty),
                    ByteUtil.byteArray2Float(rRotz),
                    ByteUtil.byteArray2Float(bx),
                    ByteUtil.byteArray2Float(by),
                    ByteUtil.byteArray2Float(bz),
                    ByteUtil.byteArray2Float(bRotx),
                    ByteUtil.byteArray2Float(bRoty),
                    ByteUtil.byteArray2Float(bRotz),
                    ByteUtil.byteArray2Int(brjump),
                    ByteUtil.byteArray2Int(bbjump)
            );
            //System.out.println(ByteUtil.byteArray2Float(rx)+" "+ByteUtil.byteArray2Float(ry)+" "+ByteUtil.byteArray2Float(rz));
            synchronized (lock) {
                aq.offer(a);
            }
        }
    }
}
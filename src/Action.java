/**
 * 动作指令对象
 */
public class Action {

    // 红色还是蓝色熊
    int redOrBlue;

    float rd;
    float rs;
    float bd;
    float bs;
    float rx;
    float ry;
    float rz;
    float rRotx;
    float rRoty;
    float rRotz;
    float bx;
    float by;
    float bz;
    float bRotx;
    float bRoty;
    float bRotz;
    int rjump;
    int bjump;

    /**
     * 构造方法
     */
    public Action(int redOrBlue, float rd, float rs, float bd, float bs,
                  float rx, float ry, float rz, float rRotx, float rRoty, float rRotz,
                  float bx, float by, float bz, float bRotx, float bRoty, float bRotz,
                  int rjump, int bjump
    ) {
        this.redOrBlue = redOrBlue;

        this.rd = rd;
        this.rs = rs;
        this.bd = bd;
        this.bs = bs;

        this.rx = rx;
        this.ry = ry;
        this.rz = rz;
        this.rRotx = rRotx;
        this.rRoty = rRoty;
        this.rRotz = rRotz;

        this.bx = bx;
        this.by = by;
        this.bz = bz;
        this.bRotx = bRotx;
        this.bRoty = bRoty;
        this.bRotz = bRotz;

        this.rjump = rjump;
        this.bjump = bjump;
    }

    /**
     * 执行动作指令
     */
    public void doAction() {

        if (redOrBlue == 1) {

            AgentThread.bd = bd;
            AgentThread.bs = bs;
            AgentThread.bx = bx;
            AgentThread.by = by;
            AgentThread.bz = bz;
            AgentThread.bRotx = bRotx;
            AgentThread.bRoty = bRoty;
            AgentThread.bRotz = bRotz;
            AgentThread.bjump = bjump;

        } else if (redOrBlue == 0) {

            AgentThread.rd = rd;
            AgentThread.rs = rs;
            AgentThread.rx = rx;
            AgentThread.ry = ry;
            AgentThread.rz = rz;
            AgentThread.rRotx = rRotx;
            AgentThread.rRoty = rRoty;
            AgentThread.rRotz = rRotz;
            AgentThread.rjump = rjump;

        }

    }
}

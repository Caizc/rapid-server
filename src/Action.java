public class Action {
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
    int RedOrBlue;

    public Action(int RedOrBlue, float rd, float rs, float bd, float bs,
                  float rx, float ry, float rz, float rRotx, float rRoty, float rRotz,
                  float bx, float by, float bz, float bRotx, float bRoty, float bRotz,
                  int rjump, int bjump
    ) {
        this.RedOrBlue = RedOrBlue;
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

    public void doAction() {
        if (RedOrBlue == 1)
        {
            ServerAgentThread.bd = bd;
            ServerAgentThread.bs = bs;
            ServerAgentThread.bx = bx;
            ServerAgentThread.by = by;
            ServerAgentThread.bz = bz;
            ServerAgentThread.bRotx = bRotx;
            ServerAgentThread.bRoty = bRoty;
            ServerAgentThread.bRotz = bRotz;
            ServerAgentThread.bjump = bjump;

        } else if (RedOrBlue == 0)
        {
            ServerAgentThread.rd = rd;
            ServerAgentThread.rs = rs;
            ServerAgentThread.rx = rx;
            ServerAgentThread.ry = ry;
            ServerAgentThread.rz = rz;
            ServerAgentThread.rRotx = rRotx;
            ServerAgentThread.rRoty = rRoty;
            ServerAgentThread.rRotz = rRotz;
            ServerAgentThread.rjump = rjump;
        }
    }
}

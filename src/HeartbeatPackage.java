/**
 * 心跳包
 */
public class HeartbeatPackage {

    int RedOrBlue;

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

    /**
     * 构造方法
     */
    public HeartbeatPackage(int RedOrBlue, float rx, float ry, float rz, float rRotx, float rRoty, float rRotz,
                            float bx, float by, float bz, float bRotx, float bRoty, float bRotz) {
        this.RedOrBlue = RedOrBlue;
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
    }

    public void Heartbeatdata() {

        if (RedOrBlue == 0) {

            AgentThread.rx = rx;
            AgentThread.ry = ry;
            AgentThread.rz = rz;
            AgentThread.rRotx = rRotx;
            AgentThread.rRoty = rRoty;
            AgentThread.rRotz = rRotz;

        } else if (RedOrBlue == 1) {

            AgentThread.bx = bx;
            AgentThread.by = by;
            AgentThread.bz = bz;
            AgentThread.bRotx = bRotx;
            AgentThread.bRoty = bRoty;
            AgentThread.bRotz = bRotz;

        }

    }
}

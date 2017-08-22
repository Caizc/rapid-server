public class HeartbeatPackage {
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
    int RedOrBlue;

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
            ServerAgentThread.rx = rx;
            ServerAgentThread.ry = ry;
            ServerAgentThread.rz = rz;
            ServerAgentThread.rRotx = rRotx;
            ServerAgentThread.rRoty = rRoty;
            ServerAgentThread.rRotz = rRotz;

        } else if (RedOrBlue == 1) {
            ServerAgentThread.bx = bx;
            ServerAgentThread.by = by;
            ServerAgentThread.bz = bz;
            ServerAgentThread.bRotx = bRotx;
            ServerAgentThread.bRoty = bRoty;
            ServerAgentThread.bRotz = bRotz;

        }

    }
}

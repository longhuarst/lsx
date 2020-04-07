package com.lsx.service.device.netty;

public class NettyStatistic {


    //每秒有效报文条目
    private int pps;
    private int ppsCount;



    //每秒连接数
    private int cps;
    private int cpsCount;

    //每秒断连
    private int dps;
    private int dpsCount;







    public void addCps(){
        cpsCount++;
    }

    public void commitCps(){
        cps = cpsCount;
        cpsCount = 0;
    }









    public void addPps(){
        ppsCount++;
    }

    public void commitPps(){
        pps = ppsCount;
        ppsCount = 0;
    }



    public void addDps(){
        dpsCount++;
    }

    public void commitDps(){
        dps = dpsCount;
        dpsCount = 0;
    }












    public int getPps() {
        return pps;
    }

    public void setPps(int pps) {
        this.pps = pps;
    }

    public int getPpsCount() {
        return ppsCount;
    }

    public void setPpsCount(int ppsCount) {
        this.ppsCount = ppsCount;
    }


    public int getCps() {
        return cps;
    }

    public void setCps(int cps) {
        this.cps = cps;
    }

    public int getCpsCount() {
        return cpsCount;
    }

    public void setCpsCount(int cpsCount) {
        this.cpsCount = cpsCount;
    }


    public int getDps() {
        return dps;
    }

    public void setDps(int dps) {
        this.dps = dps;
    }

    public int getDpsCount() {
        return dpsCount;
    }

    public void setDpsCount(int dpsCount) {
        this.dpsCount = dpsCount;
    }
}

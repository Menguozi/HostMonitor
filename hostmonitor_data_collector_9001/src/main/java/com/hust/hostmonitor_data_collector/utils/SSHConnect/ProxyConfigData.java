package com.hust.hostmonitor_data_collector.utils.SSHConnect;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

public class ProxyConfigData {
    public int proxyId;
    public String proxyType;
    public String proxyIp;
    public int proxyPort;

    public ProxyConfigData(){
        proxyId = -1;
        proxyType = "";
        proxyIp = "";
        proxyPort = 0;
    }

    public ProxyConfigData(int proxyId, String proxyType, String proxyIp, int proxyPort) {
        this.proxyId = proxyId;
        this.proxyType = proxyType;
        this.proxyIp = proxyIp;
        this.proxyPort = proxyPort;
    }

    @Override
    public String toString(){
        return "["+ proxyId + "," + proxyType + "," + proxyIp + "," + proxyPort + "]";
    }
}

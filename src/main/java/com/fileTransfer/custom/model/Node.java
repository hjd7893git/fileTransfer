package com.fileTransfer.custom.model;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by nano on 18-4-22.
 */
@XmlRootElement
public class Node implements Serializable {
    private String ip;
    private int port;
    private String path;
    private String pk;

    public Node() {
        super();
    }

    public Node(String ip, int port, String path, String pk) {
        super();
        this.ip = ip;
        this.port = port;
        this.path = path;
        this.pk = pk;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPk() {
        return pk;
    }

    public void setPk(String pk) {
        this.pk = pk;
    }
}

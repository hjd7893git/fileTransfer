package com.fileTransfer.custom.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by nano on 18-4-22.
 */
@XmlRootElement
public class FileMonitor implements Serializable {
    private String fileName;
    private int size;
    private int total;
    private int finish;
    private int speed;
    private int status;

    public FileMonitor() {
        super();
    }

    public FileMonitor(String fileName, int size, int total, int finish, int speed, int status) {
        super();
        this.fileName = fileName;
        this.size = size;
        this.total = total;
        this.finish = finish;
        this.speed = speed;
        this.status = status;
    }

    @XmlElement(name="fileName")
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @XmlElement(name="size")
    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @XmlElement(name="total")
    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    @XmlElement(name="finish")
    public int getFinish() {
        return finish;
    }

    public void setFinish(int finish) {
        this.finish = finish;
    }

    @XmlElement(name="speed")
    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    @XmlElement(name="status")
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}

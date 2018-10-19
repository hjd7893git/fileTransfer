package com.fileTransfer.custom.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by nano on 18-4-28.
 */
@XmlRootElement
public class SingleResponse implements Serializable {
    private String status;

    public SingleResponse() {
        super();
    }
    public SingleResponse(String status) {
        super();
        this.status = status;
    }

    @XmlElement(name="status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

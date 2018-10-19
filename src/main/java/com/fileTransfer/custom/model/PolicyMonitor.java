package com.fileTransfer.custom.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

/**
 * Created by nano on 18-4-22.
 */
@XmlRootElement
public class PolicyMonitor implements Serializable {
    private Long policyId;
    private List<FileMonitor> file;

    public PolicyMonitor() {
        super();
    }

    public PolicyMonitor(Long policyId, List<FileMonitor> file) {
        super();
        this.policyId = policyId;
        this.file = file;
    }

    @XmlElement(name="policyId")
    public Long getPolicyId() {
        return policyId;
    }

    public void setPolicyId(Long policyId) {
        this.policyId = policyId;
    }

    @XmlElementWrapper(name="files")
    public List<FileMonitor> getFile() {
        return file;
    }

    public void setFile(List<FileMonitor> file) {
        this.file = file;
    }
}

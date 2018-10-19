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
public class Monitor implements Serializable {
    private String nodeName;
    private List<PolicyMonitor> policy;

    public Monitor() {
        super();
    }

    public Monitor(String nodeName, List<PolicyMonitor> policy) {
        super();
        this.nodeName = nodeName;
        this.policy = policy;
    }

    @XmlElement(name="nodeName")
    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    @XmlElementWrapper(name="policys")
    public List<PolicyMonitor> getPolicy() {
        return policy;
    }

    public void setPolicy(List<PolicyMonitor> policy) {
        this.policy = policy;
    }
}

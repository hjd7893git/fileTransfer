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
public class Policy implements Serializable {
    private Long policyId;
    private List<Node> node;
    private String srcPath;
    private boolean encTag;
    private boolean compressTag;
    private boolean signTag;
    private String times;
    private String extensions;

    public Policy() {
        super();
    }

    public Policy(Long policyId, List<Node> node, String srcPath, boolean encTag, boolean compressTag, boolean signTag, String times, String extensions) {
        super();
        this.policyId = policyId;
        this.node = node;
        this.srcPath = srcPath;
        this.encTag = encTag;
        this.compressTag = compressTag;
        this.signTag = signTag;
        this.times = times;
        this.extensions = extensions;
    }

    @XmlElementWrapper(name="nodes")
    public List<Node> getNode() {
        return node;
    }

    public void setNode(List<Node> node) {
        this.node = node;
    }

    @XmlElement(name="srcPath")
    public String getSrcPath() {
        return srcPath;
    }

    public void setSrcPath(String srcPath) {
        this.srcPath = srcPath;
    }

    @XmlElement(name="encTag")
    public boolean isEncTag() {
        return encTag;
    }

    public void setEncTag(boolean encTag) {
        this.encTag = encTag;
    }

    @XmlElement(name="compressTag")
    public boolean isCompressTag() {
        return compressTag;
    }

    public void setCompressTag(boolean compressTag) {
        this.compressTag = compressTag;
    }

    @XmlElement(name="signTag")
    public boolean isSignTag() {
        return signTag;
    }

    public void setSignTag(boolean signTag) {
        this.signTag = signTag;
    }

    @XmlElement(name="times")
    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }

    @XmlElement(name="extensions")
    public String getExtensions() {
        return extensions;
    }

    public void setExtensions(String extensions) {
        this.extensions = extensions;
    }

    @XmlElement(name="policyId")
    public Long getPolicyId() {
        return policyId;
    }

    public void setPolicyId(Long policyId) {
        this.policyId = policyId;
    }
}

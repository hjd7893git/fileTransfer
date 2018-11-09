package com.fileTransfer.custom.model;

import com.fileTransfer.custom.service.TcpService;
import com.rest.frame.exception.UsualException;
import sviolet.smcrypto.SM3Digest;
import sviolet.smcrypto.util.ByteUtils;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

/**
 * Created by nano on 18-4-11.
 */
@XmlRootElement
public class NodeConf implements Serializable {

    private String ssid;
    private String nodeName;
    private String nodeIp;
    private int nodePort;
    private String nodeVk;
    private String nodeCert;
    private String nodePk;
    private int maxSpeed;
    private List<Policy> policy;
    private String xmlSign;

    public NodeConf() {
        super();
    }
    public NodeConf(String nodeName, String nodeIp, int nodePort, String nodeVk, String nodeCert, String nodePk, int maxSpeed, List<Policy> policy) {
        super();
        this.nodeName = nodeName;
        this.nodeIp = nodeIp;
        this.nodePort = nodePort;
        this.nodeVk = nodeVk;
        this.nodeCert = nodeCert;
        this.nodePk = nodePk;
        this.maxSpeed = maxSpeed;
        this.policy = policy;
    }

    @XmlElement(name="nodeName")
    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    @XmlElement(name="nodeIp")
    public String getNodeIp() {
        return nodeIp;
    }

    public void setNodeIp(String nodeIp) {
        this.nodeIp = nodeIp;
    }

    @XmlElementWrapper(name="policys")
    public List<Policy> getPolicy() {
        return policy;
    }

    public void setPolicy(List<Policy> policy) {
        this.policy = policy;
    }

    @XmlElement(name="nodePort")
    public int getNodePort() {
        return nodePort;
    }

    public void setNodePort(int nodePort) {
        this.nodePort = nodePort;
    }

    @XmlElement(name="nodeVk")
    public String getNodeVk() {
        return nodeVk;
    }

    public void setNodeVk(String nodeVk) {
        this.nodeVk = nodeVk;
    }

    @XmlElement(name="maxSpeed")
    public int getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(int maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    @XmlElement(name="nodeCert")
    public String getNodeCert() {
        return nodeCert;
    }

    public void setNodeCert(String nodeCert) {
        this.nodeCert = nodeCert;
    }

    @XmlElement(name="nodePk")
    public String getNodePk() {
        return nodePk;
    }

    public void setNodePk(String nodePk) {
        this.nodePk = nodePk;
    }

    public void calcSsid() {
        StringBuilder temp = new StringBuilder().append(nodeName).append(nodeIp).append(nodePort).append(nodeVk).append(nodePk).append(maxSpeed);
        for (Policy p : policy) {
            temp.append(p.getPolicyId());
            for (Node n : p.getNode()) {
                temp.append(n.getIp()).append(n.getPort()).append(n.getPath()).append(n.getPk());
            }
            temp.append(p.getSrcPath()).append(p.getTimes()).append(p.getExtensions()).append(p.isEncTag()).append(p.isCompressTag()).append(p.isSignTag());
        }
        //拼装后的数据进行摘要计算，生成摘要数据
        SM3Digest digest = new SM3Digest();
        digest.update(temp.toString().getBytes());
        this.ssid = ByteUtils.bytesToHex(digest.doFinal()).toUpperCase();
    }

    public void calcSign() throws UsualException {
        TcpService hsm = new TcpService();
        //对摘要数据进行sm2签名计算，得到签名数据(节点+传输通道等XML数据)
        this.xmlSign = hsm.calcSm2Sign(this.ssid);
        hsm.close();
    }

    @XmlElement(name="ssid")
    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    @XmlElement(name="xmlSign")
    public String getXmlSign() {
        return xmlSign;
    }

    public void setXmlSign(String xmlSign) {
        this.xmlSign = xmlSign;
    }
}

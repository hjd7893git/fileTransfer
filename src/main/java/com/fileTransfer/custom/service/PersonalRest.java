package com.fileTransfer.custom.service;

import com.fileTransfer.custom.model.Node;
import com.fileTransfer.custom.model.NodeConf;
import com.fileTransfer.custom.model.Policy;
import com.rest.frame.exception.UsualException;
import com.rest.frame.model.Data;
import com.rest.frame.model.Field;
import com.rest.frame.model.RestResponse;
import com.rest.frame.model.Table;
import com.rest.frame.service.InternalCache;
import com.rest.frame.service.UniversalDAO;
import com.rest.frame.service.UniversalRestInterface;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.ehcache.Cache;
import sviolet.smcrypto.util.ByteUtils;

import javax.ws.rs.Path;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by nano on 18-4-19.
 */
@Path("/crud")
public class PersonalRest implements UniversalRestInterface {

    @Override
    public RestResponse before(RestResponse response, String tableName, String action) throws UsualException {
        // 新增/修改节点信息时，计算节点签名证书
        if ("node".equals(tableName) && ("Insert".equals(action) || "Update".equals(action))) {
            TcpService hsm = new TcpService();
            List<List<Data>> datas = response.getDatas();
            for (int i = 0; i < datas.size(); i ++) {
                // 新增节点时，首先随机产生节点公私钥
                if ("Insert".equals(action)) {
                    String key = hsm.randomSm2KeyPair();
                    datas.get(i).forEach(data -> {
                        if ("nodevk".equals(data.getKey()))
                            data.setValue(key.substring(0, key.length() - 128));
                        if ("nodepk".equals(data.getKey()))
                            data.setValue(key.substring(key.length() - 128));
                    });
                }
                datas.set(i, this.calcNodeSignCert(hsm, datas.get(i)));
            }
            hsm.close();
        }
        // 修改节点配置，直接返回null
        if ("config".equals(tableName) && "Update".equals(action))
            return null;
        // 创建节点配置文件，保存节点配置版本号SSID
        if ("config".equals(tableName) && "Insert".equals(action)) {
            InternalCache cache = InternalCache.getInstance();
            UniversalDAO dao = UniversalDAO.getInstance();
            Cache<String, Table> tables = cache.getTables();
            Table nodeTable = tables.get("node");
            Table policyTable = tables.get("policy");
            Table policynodeTable = tables.get("policynode");

            List<List<Data>> datas = response.getDatas();
            for (int i = 0; i < datas.size(); i ++) {
                int len = datas.get(i).size();
                Long nodeId = 0L;
                for (int j = 0; j < len; j ++){
                    if ("nodeid".equals(datas.get(i).get(j).getKey()))
                        nodeId = Long.parseLong(datas.get(i).get(j).getValue().toString());
                }
                Field idField = new Field(0L, "id", "", "", "number", nodeId, nodeId, "", true, true, true, true, true, true, true, false, true, true, 0L, 0L, 0L);
                List<Field> searchModeFields = Collections.singletonList(idField);
                List<List<Data>> result = dao.selectList(nodeTable, 10, 1, 1, true, searchModeFields);
                if (result.size() != 0) {
                    List<Data> nodeDatas = result.get(0);
                    NodeConf conf = new NodeConf();
                    nodeDatas.forEach(d -> {
                        switch (d.getKey()) {
                            case "name" : conf.setNodeName((String) d.getValue()); break;
                            case "ip" : conf.setNodeIp((String) d.getValue()); break;
                            case "port" : conf.setNodePort(((BigDecimal) d.getValue()).intValue()); break;
                            case "nodevk" : conf.setNodeVk((String) d.getValue()); break;
                            case "nodepk" : conf.setNodePk((String) d.getValue()); break;
                            case "cert" : conf.setNodeCert((String) d.getValue()); break;
                            case "speed" : conf.setMaxSpeed(((BigDecimal) d.getValue()).intValue()); break;
                        }
                    });
                    //    Step2: 查询Policy
                    Field nodeIdField = new Field(0L, "nodeid", "", "", "number", nodeId, nodeId, "", true, true, true, true, true, true, true, false, true, true, 0L, 0L, 0L);
                    List<Field> searchPolicyFields = Collections.singletonList(nodeIdField);
                    //    此处继续使用了封装好的分页查询, 每页数量为Int最大值
                    List<List<Data>> resultPolicys = dao.selectList(policyTable, Integer.MAX_VALUE, 1, 1, true, searchPolicyFields);
                    List<Policy> policys = new ArrayList<>();
                    resultPolicys.forEach(p -> {
                        Policy policy = new Policy();
                        p.forEach(d -> {
                            switch (d.getKey()) {
                                case "id" : policy.setPolicyId(((BigDecimal) d.getValue()).longValue()); break;
                                case "path" : policy.setSrcPath((String) d.getValue()); break;
                                case "enc" : policy.setEncTag(((BigDecimal) d.getValue()).intValue() != 0); break;
                                case "compress" : policy.setCompressTag(((BigDecimal) d.getValue()).intValue() != 0); break;
                                case "sign" : policy.setSignTag(((BigDecimal) d.getValue()).intValue() != 0); break;
                                case "times" : policy.setTimes((String) d.getValue()); break;
                                case "extensions" : policy.setExtensions((String) d.getValue()); break;
                            }

                            //    Step3: 查询Policy -> Node
                            Field policynodeIdField = new Field(0L, "policyid", "", "", "number", policy.getPolicyId(), policy.getPolicyId(), "", true, true, true, true, true, true, true, false, true, true, 0L, 0L, 0L);
                            List<Field> searchPolicyNodeFields = Collections.singletonList(policynodeIdField);
                            List<List<Data>> resultPolicyNodes = dao.selectList(policynodeTable, Integer.MAX_VALUE, 1, 1, true, searchPolicyNodeFields);
                            List<Node> nodes = new ArrayList<>();
                            resultPolicyNodes.forEach(pn -> {
                                Node node = new Node();
                                long dstNodeId = 0L;
                                for(int j = 0; j < pn.size(); j++) {
                                    switch (pn.get(j).getKey()) {
                                        case "nodeid" : dstNodeId = ((BigDecimal) pn.get(j).getValue()).longValue(); break;
                                        case "path" : node.setPath((String) pn.get(j).getValue()); break;
                                    }
                                }
                                Field dstNodeIdField = new Field(0L, "id", "", "", "number", dstNodeId, dstNodeId, "", true, true, true, true, true, true, true, false, true, true, 0L, 0L, 0L);
                                List<Field> searchDstNodeFields = Collections.singletonList(dstNodeIdField);
                                List<List<Data>> resultDstNodes = dao.selectList(nodeTable, Integer.MAX_VALUE, 1, 1, true, searchDstNodeFields);
                                List<Data> resultDstNode = resultDstNodes.get(0);
                                resultDstNode.forEach(dn -> {
                                    switch (dn.getKey()) {
                                        case "ip" : node.setIp((String) dn.getValue()); break;
                                        case "port" : node.setPort(((BigDecimal) dn.getValue()).intValue()); break;
                                        case "nodepk" : node.setPk((String) dn.getValue()); break;
                                    }
                                });
                                nodes.add(node);
                            });
                            policy.setNode(nodes);
                        });
                        policys.add(policy);
                    });
                    conf.setPolicy(policys);
                    conf.calcSsid();
                    ByteArrayOutputStream os = new ByteArrayOutputStream();
                    ObjectOutputStream oos = null;
                    try {
                        oos = new ObjectOutputStream(os);
                        oos.writeObject(conf);
                    } catch (IOException e) {
                        throw new UsualException("无法持久化节点配置信息");
                    }
                    String data = ByteUtils.bytesToHex(os.toByteArray()).toUpperCase();
                    String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                    datas.get(i).forEach(d -> {
                        if ("data".equals(d.getKey()))
                            d.setValue(data);
                        if ("createtime".equals(d.getKey()))
                            d.setValue(time);
                        if ("status".equals(d.getKey()))
                            d.setValue("0");
                    });
                    return response;
                } else throw new UsualException("未找到该节点配置信息");
            }
        }
        return response;
    }

    @Override
    public void after(RestResponse response, String tableName, String action) throws UsualException {
        if (!"Select".equals(action)) {
            Subject subject = SecurityUtils.getSubject();
            if (subject.isAuthenticated()) {
                Session session = subject.getSession();
                long uid = (Long) session.getAttribute("uid");
                String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                List<List<Data>> datas = response.getDatas();
                InternalCache cache = InternalCache.getInstance();
                Table table = cache.getTable(tableName);
                PersonalDAO dao = new PersonalDAO();
                for (int i = 0; i < datas.size(); i ++) {
                    StringBuffer commentBuf = new StringBuffer();
                    commentBuf.append("{");
                    List<Data> ds = datas.get(i);
                    for (int j = 0; j < ds.size(); j ++) {
                        if (j != 0)
                            commentBuf.append(", ");
                        for (int k = 0; k < table.getFields().size(); k ++) {
                            if (table.getFields().get(k).getDb_name().equals(ds.get(j).getKey()))
                                commentBuf.append("\"" + table.getFields().get(k).getDb_view() + "\":\"" + ds.get(j).getValue() + "\"");
                        }
                    }
                    commentBuf.append("}");
                    dao.insertHistory(uid, table.getDb_view(), action, commentBuf.toString(), time);
                }
            }
        }
    }

    /**
     * 计算节点签名证书
     * @param hsm
     * @param args
     * @return
     */
    private List<Data> calcNodeSignCert(TcpService hsm, List<Data> args) throws UsualException {
        List<Data> args1 = args;
        StringBuffer certData = new StringBuffer().append("02");
        StringBuffer vk = new StringBuffer();
        args1.forEach(a -> {
            if ("name".equals(a.getKey())) {
                certData.append(String.format("%04d", ((String) a.getValue()).length()));
                certData.append((String) a.getValue());
            }
            if ("nodevk".equals(a.getKey()))
                vk.append((String) a.getValue());
        });
        args1.forEach(a -> {
            if ("nodepk".equals(a.getKey())) {
                certData.append((String) a.getValue());
            }
        });
        String sign = hsm.calcSm2SignWithSm3(vk.toString(), certData.toString());
        certData.append(sign);
        args1.forEach(a -> {
            if ("cert".equals(a.getKey()))
                a.setValue(certData.toString());
        });
        return args1;
    }


}

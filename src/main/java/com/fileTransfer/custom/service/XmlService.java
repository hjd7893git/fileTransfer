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
import org.ehcache.Cache;

import javax.annotation.security.PermitAll;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by nano on 18-4-11.
 */
@Path("/xml")
public class XmlService {

    private InternalCache cache;
    private PersonalDAO dao;
    private Cache<String, Table> tables;

    public  XmlService() throws UsualException {
        super();
        this.cache = InternalCache.getInstance();
        this.dao = new PersonalDAO();
        this.tables = cache.getTables();
    }

    @GET
    @Path("/Download/{nodeName:[A-Za-z0-9]{4,16}}.xml")
    @Produces(MediaType.APPLICATION_XML)
    public Response nodeConf(@PathParam("nodeName") String nodeName) throws Exception {

        Table nodeTable = tables.get("node");
        Table policyTable = tables.get("policy");
        Table policynodeTable = tables.get("policynode");
        Field nodeNameField = null;
        for (int i=0; i < nodeTable.getFields().size(); i++) {
            if (nodeTable.getFields().get(i).getDb_name().equals("name")) {
                nodeNameField = nodeTable.getFields().get(i);
            }
        }
        if (nodeNameField != null) {
            //    Step1: 查询Node
            nodeNameField.setDb_regex(nodeName);
            List<Field> searchModeFields = Collections.singletonList(nodeNameField);
            List<List<Data>> result = dao.selectList(nodeTable, 10, 1, 1, true, searchModeFields);
            if (result.size() != 0) {
                List<Data> nodeDatas = result.get(0);
                NodeConf conf = new NodeConf();
                long nodeId = Long.parseLong(nodeDatas.get(0).getValue().toString());
                nodeDatas.forEach(d -> {
                    switch (d.getKey()) {
                        case "name" : conf.setNodeName((String) d.getValue()); break;
                        case "ip" : conf.setNodeIp((String) d.getValue()); break;
                        case "port" : conf.setNodePort((int) Long.parseLong(d.getValue().toString())); break;
                        case "nodevk" : conf.setNodeVk((String) d.getValue()); break;
                        case "nodepk" : conf.setNodePk((String) d.getValue()); break;
                        case "cert" : conf.setNodeCert((String) d.getValue()); break;
                        case "speed" : conf.setMaxSpeed((int)Long.parseLong(d.getValue().toString())); break;
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
                            case "id" : policy.setPolicyId(Long.parseLong(d.getValue().toString())); break;
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
                                    case "nodeid" : dstNodeId =Long.parseLong(pn.get(j).getValue().toString()); break;
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
                                    case "port" : node.setPort((int) Long.parseLong(dn.getValue().toString())); break;
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
                return Response.status(Response.Status.OK).header("Access-Control-Allow-Origin", "*").entity(conf).build();
            } else throw new Exception("EEEEEEEEE");
        } else throw new Exception("EEEEEEEEE");
    }

    /**
     * 在线更新节点配置
     * @param request
     * @return Response
     */
    @POST
    @Path("/Update/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response nodeMonitor(RestResponse request) {
        List<List<Data>> datas = request.getDatas();
        long id = 0;
        long nodeid = 0;
        String xmlData = "";
        for (int i = 0; i < datas.size(); i ++) {
            for (int j = 0; j < datas.get(i).size(); j ++) {
                if ("id".equals(datas.get(i).get(j).getKey()))
                    id = Long.parseLong(datas.get(i).get(j).getValue().toString());
                if ("nodeid".equals(datas.get(i).get(j).getKey()))
                    nodeid = Long.parseLong(datas.get(i).get(j).getValue().toString());
                if ("data".equals(datas.get(i).get(j).getKey()))
                    xmlData = datas.get(i).get(j).getValue().toString();
            }
        }
        Table nodeTable = tables.get("node");
        Field nodeIdField = new Field(0L, "id", "", "", "number", nodeid, nodeid, "", true, true, true, true, true, true, true, false, true, true, 0L, 0L, 0L);
        List<Field> searchNodeFields = Collections.singletonList(nodeIdField);
        List<List<Data>> resultNodes = dao.selectList(nodeTable, Integer.MAX_VALUE, 1, 1, true, searchNodeFields);
        for (int i = 0; i < resultNodes.size(); i ++) {
            List<Data> n = resultNodes.get(i);
            Node node = new Node();
            n.forEach(d -> {
                switch (d.getKey()) {
                    case "ip" : node.setIp((String) d.getValue()); break;
                    case "port" : node.setPort(((BigDecimal) d.getValue()).intValue()); break;
                    case "nodepk" : node.setPk((String) d.getValue()); break;
                }
            });
            try {
                // 与对应节点建立连接并更新配置
                TcpService tcp = new TcpService(node.getIp(), node.getPort(), 0);
                tcp.updateConfig(xmlData);
                tcp.close();
            } catch (UsualException e) {
                return Response.status(Response.Status.BAD_REQUEST).header("Access-Control-Allow-Origin", "*").entity("{\"message\":\"" + e.getMessage() + "\"}").build();
            }
        }
        dao.updateNodeConfig(id, nodeid);
        return Response.status(Response.Status.OK).header("Access-Control-Allow-Origin", "*").entity("{}").build();
    }

    /**
     * 浏览器对于非常规类型请求，会自动发送OPTIONS请求获取允许的报文类型
     * @return Response
     */
    @OPTIONS
    @Path("/Update/")
    @PermitAll
    @Consumes(MediaType.APPLICATION_JSON)
    public Response nodeOption(@PathParam("nodeName") String nodeName) {
        return Response.status(Response.Status.NO_CONTENT).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Headers", "content-type").build();
    }
}

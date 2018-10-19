package com.fileTransfer.custom.service;

import com.rest.frame.exception.UsualException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by nano on 18-4-19.
 */
public class PersonalListener implements ServletContextListener {

    private Timer nodeStatusTimer = null;
    // 监控间隔时间(s)
    private static int MONITOR_INTERVAL = 5;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        this.nodeStatusTimer = new Timer(true);
        NodeStatusTimer nsTimer = new NodeStatusTimer();
        nodeStatusTimer.schedule(nsTimer, 0, PersonalListener.MONITOR_INTERVAL * 1000);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        this.nodeStatusTimer.cancel();
    }


    /**
     * 传输节点监控定时任务
     */
    private class NodeStatusTimer extends TimerTask {
        private Logger logger = LoggerFactory.getLogger(NodeStatusTimer.class);
        private PersonalDAO dao;

        @Override
        public void run() {
            logger.info("监控定时任务启动中...");
            try {
                this.dao = new PersonalDAO();
                logger.info("监控定时任务启动成功");
            } catch (UsualException e) {
                logger.error("监控定时任务启动失败: " + e.getMessage());
            }
            List<LinkedHashMap<String, Object>> nodes = this.dao.selectAllNodes();
            nodes.forEach(node -> {
                Thread nodeThread = new Thread(new MonitorThread(node, dao));
                nodeThread.start();
            });
        }
    }

    private class MonitorThread implements Runnable {
        private Logger logger = LoggerFactory.getLogger(MonitorThread.class);
        private Long id;
        private String ip;
        private int port;
        private boolean status;
        private boolean newStatus = status;
        private PersonalDAO dao;

        public MonitorThread(LinkedHashMap<String, Object> node, PersonalDAO dao) {
            this.id = ((BigDecimal) node.get("id")).longValue();
            this.ip = "" + node.get("ip");
            this.port = ((BigDecimal) node.get("port")).intValue();
            this.status = ((BigDecimal) node.get("status")).intValue() != 0;
            this.dao = dao;
        }

        @Override
        public void run() {

            try {
                TcpService tcp = new TcpService(this.ip, this.port, 0);
                if (!tcp.send("NC").startsWith("ND")) {

                    throw new UsualException("传输节点[\" + this.ip + \":\" + this.port + \"]NC返回报文不正常");
                }
                if (!this.status) {
                    //String monitorRes = tcp.send("");
                    // 监控信息收集部分


                }
                this.newStatus = true;
                tcp.close();
            } catch (UsualException e) {
                this.newStatus = false;
                logger.warn("监控任务[" + this.ip + ":" + this.port + "]:" + e.getMessage());
            } finally {
                if (this.status != this.newStatus)
                    this.dao.updateNodeStatus(id, this.newStatus);
            }

        }
    }
}

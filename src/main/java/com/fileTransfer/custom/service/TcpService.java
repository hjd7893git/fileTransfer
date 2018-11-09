package com.fileTransfer.custom.service;

import com.fileTransfer.custom.model.NodeConf;
import com.fileTransfer.custom.util.EBCDIC;
import com.rest.frame.exception.UsualException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sviolet.smcrypto.SM3Digest;
import sviolet.smcrypto.util.ByteUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Properties;
import java.util.UUID;

/**
 * Created by nano on 18-4-29.
 */
public class TcpService {
    private static Logger logger = LoggerFactory.getLogger(TcpService.class);

    private String ip = "";
    private int port = 0;
    private Socket sock = null;
    private OutputStream os = null;
    private InputStream is = null;
    private int headLength = 0;
    private String encode = "ASCII";
    private String vk;
    private String pk;

    public TcpService() throws UsualException {
        String profilePath = TcpService.class.getResource("/").getPath() + "hsm.properties";
        Properties prop = new Properties();
        InputStream in = TcpService.class.getClassLoader().getResourceAsStream("hsm.properties");
        try {
            prop.load(in);
        } catch (IOException e) {
            throw new UsualException("配置文件[" + profilePath + "]读取失败");
        }
        ip = prop.getProperty("ip");
        //获取address的值
        port = Integer.parseInt(prop.getProperty("port"));

        /**
         * 创建连接
         */
        this.explorer();

        this.vk = prop.getProperty("vk", "");
        this.pk = prop.getProperty("pk", "");
        if ("".equals(this.vk) || "".equals(this.pk)) {
            try {
                OutputStream out = new FileOutputStream(profilePath);
                String newKey = this.randomSm2KeyPair();
                prop.setProperty("vk", newKey.substring(0, newKey.length() - 128));
                prop.setProperty("pk", newKey.substring(newKey.length() - 128));
                prop.store(out, "");
                out.close();
                this.vk = newKey.substring(0, newKey.length() - 128);
                this.pk = newKey.substring(newKey.length() - 128);
            } catch (IOException e) {
                throw new UsualException("配置文件更新失败");
            }
        }
        try {
            in.close();
        } catch (IOException e) {
            throw new UsualException("配置文件[" + profilePath + "]关闭失败");
        }
    }

    public TcpService(String ip, int port, int head) throws UsualException {
        this.ip = ip;
        this.port = port;
        this.headLength = head;
        try {
            //创建连接
            this.sock = new Socket();
            this.sock.connect(new InetSocketAddress(ip, port), 1000);
            this.os = this.sock.getOutputStream();
            this.is = this.sock.getInputStream();
        } catch (IOException e) {
            throw new UsualException("TCP[" + ip + ":" + port + "]连接不可用");
        }
    }

    private void explorer() throws UsualException {
        try {
            //创建连接
            this.sock = new Socket();
            this.sock.connect(new InetSocketAddress(ip, port), 1000);
            this.os = this.sock.getOutputStream();
            this.is = this.sock.getInputStream();
        } catch (IOException e) {
            throw new UsualException("TCP[" + ip + ":" + port + "]连接不可用");
        }

        //探测密码机头长和编码
        String sendStr = "0000000000000000000000000000000000000000000000000000000000000000";
        String backStr = this.send(sendStr);
        this.headLength = backStr.indexOf("1") - 1;
        if (!"60".equals(backStr.substring(this.headLength + 2, this.headLength + 4)))
            this.encode = "EBCDIC";
    }

    //发送
    public String send(String sendStr) throws UsualException {
        String ssid = UUID.randomUUID().toString().replaceAll("-", "");
        String str = "EBCDIC".equals(this.encode) ? EBCDIC.ASCIIToEBCDIC(sendStr) : sendStr;
        String a = ssid.substring(0, this.headLength) + str;
        try {
            logger.info("发送:[{}]{}", a.length(), a);
            byte[] sendBytes = this.add2BytesLen(a);
            byte[] b = new byte[2];
            this.os.write(sendBytes);
            this.os.flush();
            this.is.read(b);
            int len = this.byteArrayToInt(b);
            byte[] c = new byte[len];
            this.is.read(c);
            String res = "EBCDIC".equals(this.encode) ? EBCDIC.EBCDICToASCII(new String(c, "ISO-8859-1")).substring(this.headLength) : new String(c, "ISO-8859-1").substring(this.headLength);
            logger.info("接收:[{}]{}", len, res);
            return res;
        } catch (UnsupportedEncodingException e1) {
            throw new UsualException("未知的编码错误[ISO-8859-1]");
        } catch (IOException e) {
            throw new UsualException("通讯错误，目标[" + this.ip + ":" + this.port + "]");
        }
    }

    public void close() {
        try {
            if (os != null)
                os.close();
            if (is != null)
                is.close();
            if (sock != null)
                sock.close();
        } catch (IOException e) {
            logger.warn("TCP[" + ip + ":" + port + "]连接关闭失败");
        }
    }

    private byte[] add2BytesLen(String a) throws UnsupportedEncodingException {
        byte[] body = a.getBytes("ISO-8859-1");
        int bodyLen = body.length;
        byte[] len = new byte[]{(byte) (bodyLen / 256), (byte) (bodyLen % 256)};
        byte[] b = new byte[bodyLen + 2];
        System.arraycopy(len, 0, b, 0, 2);
        System.arraycopy(body, 0, b, 2, bodyLen);
        return b;
    }

    private int byteArrayToInt(byte[] b) {
        return (int) (b[0] & 0xFF) << 8 | (b[1] & 0xFF);
    }

    /**
     * 产生随机SM2密钥对    224 + 128
     * @return vk + pk
     * @throws UsualException 通用错误
     */
    String randomSm2KeyPair() throws UsualException {
        try {
            String cmd = "K10256399";
            String backStr = this.send(cmd);
            return ByteUtils.bytesToHex(backStr.substring(8).getBytes("ISO-8859-1")).toUpperCase();
        } catch (UnsupportedEncodingException e) {
            throw new UsualException("未知的编码错误[ISO-8859-1]");
        }
    }

    public String calcSm2Sign(String data) throws UsualException {
        return this.calcSm2Sign(this.vk, data);
    }

    public String calcSm2SignWithSm3(String data) throws UsualException {
        return this.calcSm2SignWithSm3(this.vk, data);
    }

    /**
     * 计算SM2私钥签名
     * @param vk   私钥
     * @param data 数据
     * @return 签名值
     * @throws UsualException 通用错误
     */
    String calcSm2Sign(String vk, String data) throws UsualException {
        try {
            String cmd = "K3990112" + new String(ByteUtils.hexToBytes(vk), "ISO-8859-1") + "01" + String.format("%04d", data.length() / 2) + new String(ByteUtils.hexToBytes(data), "ISO-8859-1");
            String backStr = this.send(cmd);
            return ByteUtils.bytesToHex(backStr.substring(4).getBytes("ISO-8859-1")).toUpperCase();
        } catch (UnsupportedEncodingException e) {
            throw new UsualException("未知的编码错误[ISO-8859-1]");
        }
    }

    /**
     * 计算SM2私钥签名(SM3)
     * @param vk   私钥
     * @param data 数据
     * @return 签名值
     * @throws UsualException 通用错误
     */
    String calcSm2SignWithSm3(String vk, String data) throws UsualException {
        try {
            String cmd = "K3990112" + new String(ByteUtils.hexToBytes(vk), "ISO-8859-1") + "0200161234567812345678" + String.format("%04d", data.length()) + data;
            String backStr = this.send(cmd);
            return ByteUtils.bytesToHex(backStr.substring(4).getBytes("ISO-8859-1")).toUpperCase();
        } catch (UnsupportedEncodingException e) {
            throw new UsualException("未知的编码错误[ISO-8859-1]");
        }
    }

    void updateConfig(String data) throws UsualException {
        String profilepath = TcpService.class.getResource("/").getPath() + "hsm.properties";
        Properties prop = new Properties();
        InputStream in = TcpService.class.getClassLoader().getResourceAsStream("hsm.properties");
        try {
            prop.load(in);
        } catch (IOException e) {
            throw new UsualException("配置文件[" + profilepath + "]读取失败");
        }
        this.vk = prop.getProperty("vk", "");
        this.pk = prop.getProperty("pk", "");

        try {
            //从对象流中读取得到数据
            ByteArrayInputStream is = new ByteArrayInputStream(ByteUtils.hexToBytes(data));
            ObjectInputStream ois = new ObjectInputStream(is);
            NodeConf conf = (NodeConf) ois.readObject();
            conf.calcSign(); //ssid进行sm2签名 得到XML签名值
            logger.info("xmlSign:{}", conf.getXmlSign());

            JAXBContext context = JAXBContext.newInstance(conf.getClass());
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "utf-8");
            StringWriter writer = new StringWriter();
            marshaller.marshal(conf, writer);
            String xmlString = writer.toString();

            // 开始一个新的配置文件
            if (!this.send("X0").startsWith("X1"))
                throw new UsualException("传输节点[" + ip + ":" + port + "]异常，无法推送新的配置文件");
            // 设置单次传输最大长度
            int maxLength = 9999;
            int count = xmlString.length() / maxLength + 1;
            // 分次发送配置信息(拼组)
            for (int i = 1; i <= count; i ++) {
                String send = "XM" + String.format("%04d", (i < count) ? maxLength : (xmlString.length() % maxLength)) + ((i < count) ? xmlString.substring((i - 1) * maxLength, i * maxLength) : xmlString.substring((i - 1) * maxLength));
                String res = this.send(send);
                logger.info("返回值:{}", res);
                if (!"XN00".equals(res))
                    throw new UsualException("传输节点[" + ip + ":" + port + "]异常，配置文件推送失败");
            }
            // 使用新的配置重启节点服务
            SM3Digest digest = new SM3Digest();
            digest.update(xmlString.getBytes());
            String fileSign = ByteUtils.bytesToHex(digest.doFinal()).toUpperCase();
            this.send("RS" + fileSign);
        } catch (IOException e) {
            throw new UsualException("配置信息生成失败:读取配置信息失败");
        } catch (ClassNotFoundException e) {
            throw new UsualException("配置信息生成失败:未找到NodeConf类");
        } catch (JAXBException e) {
            throw new UsualException("配置信息生成失败:无法创建JAXBContext");
        }
    }
}

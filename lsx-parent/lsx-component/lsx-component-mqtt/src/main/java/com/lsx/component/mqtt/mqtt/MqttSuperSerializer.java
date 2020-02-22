package com.lsx.component.mqtt.mqtt;

import io.swagger.models.auth.In;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.ip.tcp.serializer.AbstractPooledBufferByteArraySerializer;
import org.springframework.integration.ip.tcp.serializer.ByteArrayStxEtxSerializer;
import org.springframework.integration.ip.tcp.serializer.SoftEndOfStreamException;
import org.springframework.integration.mapping.MessageMappingException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


//mqtt 的序列化 和 反序列化
public class MqttSuperSerializer extends AbstractPooledBufferByteArraySerializer {


    Logger logger = LoggerFactory.getLogger(MqttSuperSerializer.class);

    /*
    *           控制报文结构
    *       +--------------+--------------+------------------+
    *      ｜ mqtt 固定报头 ｜ mqtt 可变报头 ｜ payload 有效载荷 ｜
    *       +--------------+--------------+------------------+
    *
    *           可变报文结构          需要的报文  3～11   PUBLISH（Qos > 0）  PUBACK  PUBREC PUBREL PUBCOMP SUBSCRIBE UNSUBSCRIBE UNSUBACK
    *       两个字节的  报文标识符字段
    *       +----+------+
    *      ｜MSB ｜ LSB ｜
    *       +----+------+
    *
    *
    * */

    private static final int CONNECT = 1;

    // 反序列化器 相关
    boolean framing = false; //同步
    int fixHeadControlType;// 控制报文类型
    int fixHeadControlFlag;// 控制报文类型的标志位
    int fixHeadLeftLength; //剩余长度   包括可变长度 和 payload

    int qos;
    boolean packetIdentifierFlag = false;//标志
    int packetIdentifier;//报文标识符

    int level;//版本
    int connectFlag; //连接标志
    int keepAlive;//保持连接时间

    int protocolNameLength;//协议名长度
    String protocolName;//协议名


    private String convertControlTypeFlag(int controlType, int controlFlag){
        String str = "";

        switch (controlType){
            case  0: str += "名字：  Reserved       报文流动方向 ： 禁止              描述： 保留"; break;
            case  1: str += "名字：  CONNECT        报文流动方向 ： 客户端到服务端     描述： 客户端请求连接服务端"; break;
            case  2: str += "名字：  CONNACK        报文流动方向 ： 服务端到客户端     描述： 连接报文确认"; break;
            case  3: str += "名字：  PUBLISH        报文流动方向 ： 两个方向都允许     描述： 发布消息"; break;
            case  4: str += "名字：  PUBACK         报文流动方向 ： 两个方向都允许     描述： QoS 1 消息发布收到确认"; break;
            case  5: str += "名字：  PUBREC         报文流动方向 ： 两个方向都允许     描述： 发布收到 （保证交付第一步）"; break;
            case  6: str += "名字：  PUBREL         报文流动方向 ： 两个方向都允许     描述： 发布释放 （保证交付第二步）"; break;
            case  7: str += "名字：  PUBCOMP        报文流动方向 ： 两个方向都允许     描述： Qos 2 消息发布完成 （保证交互第三步）"; break;
            case  8: str += "名字：  SUBSCRIBE      报文流动方向 ： 客户端到服务端     描述： 客户端订阅请求"; break;
            case  9: str += "名字：  SUBACK         报文流动方向 ： 服务端到客户端     描述： 订阅请求报文确认"; break;
            case 10: str += "名字：  UNSUBSCRIBE    报文流动方向 ： 客户端到服务端     描述： 客户端取消订阅请求"; break;
            case 11: str += "名字：  UNSUBACK       报文流动方向 ： 服务端到客户端     描述： 取消订阅报文确认"; break;
            case 12: str += "名字：  PINGREQ        报文流动方向 ： 客户端到服务端     描述： 心跳请求"; break;
            case 13: str += "名字：  PINGRESP       报文流动方向 ： 服务端到客户端     描述： 心跳响应"; break;
            case 14: str += "名字：  DISCONNECT     报文流动方向 ： 客户端到服务端     描述： 客户端断开连接"; break;
            case 15: str += "名字：  Reserved       报文流动方向 ： 禁止              描述： 保留"; break;
        }
        if(controlType == 3){
            str+= "\n标志：    DUP = " + (controlFlag >> 3 & 0x01) + "    Qos = " +  (controlFlag >> 1 & 0x3) + "    RETAIN= " + (controlFlag & 0x01);
        }
        return str;
    }



    // 计算长度   参数2 为 迭代层数   第一层为 1
    private int lenEncoder(InputStream inputStream, Integer layer) throws IOException{


        int length = 0;

        if (layer == null)
            layer = 1;


        if (layer > 4){
            throw new IOException("超过当前协议的最大传输大小：256MB = 268435455");
        }

        int bite = inputStream.read();
        if (bite < 0) {
            throw new SoftEndOfStreamException("Stream closed between payloads");
        }

        if(bite > 127){
            length = bite & 0x7f;

            int len = lenEncoder(inputStream, layer+1);

            length += len << (7 * layer);

        }else{
            length = bite;
        }
        return length;
    }


    private int packetIdentifierEncode(InputStream inputStream) throws IOException{
        int identifier = 0;

        int bite = inputStream.read();
        if (bite < 0) {
            throw new SoftEndOfStreamException("Stream closed between payloads");
        }

        identifier = bite << 8;

        bite = inputStream.read();
        if (bite < 0) {
            throw new SoftEndOfStreamException("Stream closed between payloads");
        }

        identifier += bite;

        return identifier;
    }

    boolean mqttCharEncoder(InputStream inputStream) throws IOException{
        boolean debug = logger.isDebugEnabled();
        byte [] mqtt = new byte[4];
        mqtt[0] = 'M';
        mqtt[1] = 'Q';
        mqtt[2] = 'T';
        mqtt[3] = 'T';

        for (int i=0; i<4; ++i){
            int bite = inputStream.read();
            if (bite < 0) {
                throw new SoftEndOfStreamException("Stream closed between payloads");
            }
            if (bite != mqtt[i]){
                if (debug){
                    logger.debug("need "+String.valueOf(mqtt[i]) +" get " + String.valueOf(bite));
                }
                return false;
            }
        }
        return true;
    }

    private int getByte(InputStream inputStream) throws IOException{
        int bite = inputStream.read();
        if (bite < 0) {
            throw new SoftEndOfStreamException("Stream closed between payloads");
        }
        return bite;
    }


    private int getKeepAlive(InputStream inputStream) throws IOException{
        int value = 0;
        value = getByte(inputStream) <<8;
        value += getByte(inputStream);
        return value;
    }

    private void getProtocolName(InputStream inputStream) throws IOException{
        boolean debug = logger.isDebugEnabled();

        int length = 0;
        length += getByte(inputStream)* 256;
        length += getByte(inputStream);

        protocolNameLength = length;
        if (debug){
            logger.debug("协议名长度 = "+protocolNameLength);
        }

        byte[] name = new byte[protocolNameLength];

        for (int i=0; i<protocolNameLength ; ++i){
            name[i] = (byte)getByte(inputStream);
        }

        protocolName =  new String(name);

        if (debug){
            logger.debug("协议名 ：" + protocolName);
        }

    }


    /**
     * A single reusable instance.
     */
    public static final ByteArrayStxEtxSerializer INSTANCE = new ByteArrayStxEtxSerializer();

    public static final int STX = 0x02;

    public static final int ETX = 0x03;

    /**
     * Reads the data in the inputStream to a byte[]. Data must be prefixed
     * with an ASCII STX character, and terminated with an ASCII ETX character.
     * Throws a {@link SoftEndOfStreamException} if the stream
     * is closed immediately before the STX (i.e. no data is in the process of
     * being read).
     *
     */
    //反序列化
    @Override
    public byte[] doDeserialize(InputStream inputStream, byte[] buffer) throws IOException {

        boolean debug = logger.isDebugEnabled();

        if (debug){
            logger.debug("doDeserialize");
        }

        //mqtt 解析 交付给 应用层处理，不再 在此解码器中执行

        int bite = inputStream.read();
        checkClosure(bite);
        buffer[0] = (byte) bite;
//        fixHeadLeftLength = lenEncoder(inputStream, null);
        if (debug){
            logger.debug("协议头：" + bite);
        }

        int fix_size = 1;
        fixHeadLeftLength = 0;
        for (int i=0; i<4; ++i){
            fix_size++;
            bite = inputStream.read();
            checkClosure(bite);
            buffer[i+1] = (byte)bite;
            if (bite < 127){
                fixHeadLeftLength += bite << (7 * (i));
                break; //结束
            }
            bite &= 0x7f;//去掉首位
            fixHeadLeftLength += bite << (7 * (i));
        }
        if (debug)
            logger.debug("剩余长度："+fixHeadLeftLength);
        if (bite > 127){
            throw new IOException("超过当前协议的最大传输大小：256MB = 268435455");
        }

        for (int i=0; i<fixHeadLeftLength; ++i){
            bite = inputStream.read();
            checkClosure(bite);
            buffer[fix_size+i] = (byte)bite;
        }

        return copyToSizedArray(buffer, fixHeadLeftLength + fix_size);
//
//        int n = 0;
//        try {
////            if (bite != STX) {
////                throw new MessageMappingException("Expected STX to begin message");
////            }
//            while ((bite = inputStream.read()) != ETX) {
//
//                buffer[n++] = (byte) bite;
//                if (n >= getMaxMessageSize()) {
//                    throw new IOException("ETX not found before max message length: "
//                            + getMaxMessageSize());
//                }
//            }
//            return copyToSizedArray(buffer, n);
//        }
//        catch (IOException e) {
//            publishEvent(e, buffer, n);
//            throw e;
//        }
//        catch (RuntimeException e) {
//            publishEvent(e, buffer, n);
//            throw e;
//        }
//
//
//
//
//
//
//        boolean debug = logger.isDebugEnabled();
//
//        int bite = inputStream.read();
//        if (bite < 0) {
//            throw new SoftEndOfStreamException("Stream closed between payloads");
//        }
//
//
//        if (framing == false){
//            fixHeadControlType = (bite >>4) & 0x0f;
//            fixHeadControlFlag = bite & 0x0f;
//        }
//
//        if (debug){
//            logger.debug("固定报文 - 控制报文的类型 ： " + convertControlTypeFlag(fixHeadControlType,fixHeadControlFlag));
//        }
//
//
//        qos = fixHeadControlFlag >> 1 & 0x3;
//
//
//        fixHeadLeftLength = lenEncoder(inputStream, null);
//
//
//        packetIdentifierFlag = false;
//        if (fixHeadControlType >= 3 && fixHeadControlType <= 11){
//            if (fixHeadControlType == 3){
//                //检查qos
//                if (qos > 0){
//                    //存在标识符
//                    packetIdentifierFlag = true;
//                }
//            }else{
//                //存在标识符
//                packetIdentifierFlag = true;
//            }
//
//        }
//
//
//        if (debug){
//            logger.debug("剩余报文长度 : " + fixHeadLeftLength);
//        }
//
//        if (packetIdentifierFlag){
//            packetIdentifier = packetIdentifierEncode(inputStream);
//            fixHeadLeftLength -= 2;
//            if (debug){
//                logger.debug("唯一标识符：" + packetIdentifier);
//            }
//        }else{
//            if (debug){
//                logger.debug("唯一标识符：不需要");
//            }
//        }
//
//
//        if (debug){
//            logger.debug("剩余报文长度 : " + fixHeadLeftLength);
//        }
//
//
//        if (fixHeadControlType == CONNECT){
//
//            getProtocolName(inputStream);
//
////            //接下来4个字节为mqtt
////            if (mqttCharEncoder(inputStream) != true){
////                throw new IOException("CONNECT MQTT character 不匹配");
////            }
//
//
//            //level
//            level = getByte(inputStream);
//
//            if (level != 4 && level != 3){
//                throw new IOException("当前仅支持 3.1.1 版本协议");
//                //还需要返回一个不支持的版本的报文 MQTT-3。1。1-CN page 21
//                // ToDo : mqtt 版本错误的应答  未实现
//            }
//
//            connectFlag = getByte(inputStream);
//
//            if (new Integer(connectFlag & 0x1) != 0){
//                //ToDo: mqtt 这个字段最后一位 不为0 直接关闭连接
//            }
//
//            if(new Integer(connectFlag & 0x80) == 0x80){
//                //用户名标志
//                //有效载荷包含 用户名
//            }else{
//                //有效载荷不包含用户名
//            }
//
//            if (new Integer(connectFlag & 0x40) == 0x40){
//                //密码标志
//                //有效载荷包含 密码
//            }else{
//                //有效载荷不包含 密码
//            }
//
//
//            //ToDo: 还有其他位 没有实现   P23
//
//
//
//            keepAlive = getKeepAlive(inputStream);
//
//            if (debug){
//                logger.debug("keep-alive = "+keepAlive + " 秒");
//            }
//
//
//            fixHeadLeftLength -= 6+protocolName.length(); // connect 报文的 可变长度位 10
//
//        }
//
//
//        int n = 0;
//        try {
//
//            while (n < fixHeadLeftLength) {
//                bite = inputStream.read();
//                checkClosure(bite);
//                buffer[n++] = (byte) bite;
//            }
//
//            byte[] payload = copyToSizedArray(buffer, n);
//
//            if (debug){
//                logger.debug("payload = "+ new String(payload));
//            }
//
//            return payload;
//        }
//        catch (IOException e) {
//            publishEvent(e, buffer, n);
//            throw e;
//        }
//        catch (RuntimeException e) {
//            publishEvent(e, buffer, n);
//            throw e;
//        }
    }


    /**
     * Writes the byte[] to the stream, prefixed by an ASCII STX character and
     * terminated with an ASCII ETX character.
     */
    //序列化
    @Override
    public void serialize(byte[] bytes, OutputStream outputStream) throws IOException {
        outputStream.write(STX);
        outputStream.write(bytes);
        outputStream.write(ETX);
    }



}

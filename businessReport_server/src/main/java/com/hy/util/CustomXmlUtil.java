package com.hy.util;

import com.hy.entity.base.Tx;
import com.hy.entity.base.TxBody;
import com.hy.entity.base.TxRequestHeader;
import com.hy.entity.base.TxResponseHeader;
import com.sun.xml.internal.bind.marshaller.CharacterEscapeHandler;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import lombok.extern.slf4j.Slf4j;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * .
 * Created by of liaoxg
 * date: 2019-07-11
 * user: lxg
 * package_name: com.hy.util
 */
@Slf4j
public final class CustomXmlUtil {

    /**
     * .
     * XStream 格式化类
     */
    private static XStream xStream = new XStream(new DomDriver());

    static {
        xStream.autodetectAnnotations(true);
    }

    /**
     * .
     * 工具类隐藏构造函数
     */
    private CustomXmlUtil() {

    }

    /**
     * .
     * 解包
     *
     * @param xmlStr 解包文本
     * @param tClass <T> 泛型
     * @param <T>    泛型
     * @return T 泛型
     * @throws Exception Exception
     */
    public static <T> Map<String, T> unpack(
        final String xmlStr, final Class<T> tClass)
        throws Exception {
        Tx tx = (Tx) CustomXmlUtil.convertToGraphModel(xmlStr, Tx.class);
        String uuid = tx.getTxHeader().getSysEvtTraceId();
        String encodeStr = tx.getTxBody().getMsg();
        log.debug("加密报文:\n{}", encodeStr);
        String decodeStr = decode(encodeStr);
        decodeStr = decodeStr.replace("<msgEntity>", "");
        decodeStr = decodeStr.replace("</msgEntity>", "");
        XStream xStream1 = new XStream(new DomDriver());
        xStream1.processAnnotations(tClass);
        Map<String, T> resMap = new HashMap<>();
        T obj = (T) xStream1.fromXML(decodeStr);
        resMap.put(uuid, obj);
        return resMap;
    }

    /**
     * .
     * 封包
     *
     * @param data        数据对象
     * @param type        BusinessType 业务类型
     * @param messageType 报文类型
     * @param uuid        全局唯一编号
     * @return xml文本
     * @throws Exception Exception
     */
    public static String pack(final Object data,
                              final BusinessType type,
                              final MessageType messageType,
                              final String uuid)
        throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Tx tx = new Tx();
        if (messageType == MessageType.request) {
            TxRequestHeader header = new TxRequestHeader();
            header.setSysTxCode(type.getName());
            header.setSysEvtTraceId(uuid);
            header.setSysSndSerialNo(uuid);
            header.setSysReqTime(sdf.format(new Date()));
            tx.setTxHeader(header);
        } else if (messageType == MessageType.response) {
            TxResponseHeader header = new TxResponseHeader();
            //返回对应唯一标识 应该从请求处获取
            header.setSysEvtTraceId(uuid);
            header.setSysSndSerialNo(type.getName());
            header.setSysRespTime(sdf.format(new Date()));
            //返回状态为0
            header.setSysTxStatus("00");
            //返回描述
            header.setSysRespDesc("响应成功");
            tx.setTxHeader(header);
        }
        String item = xStream.toXML(data);
        item = "<msgEntity>" + item + "</msgEntity>";
        TxBody txBody = new TxBody(encode(item));
        tx.setTxBody(txBody);
        String xmlStr = CustomXmlUtil.convertToXML(tx, Tx.class);
        log.debug("本地打包加密报文:\n{}", xmlStr);
        return xmlStr;
    }

    /**
     * .
     * 加密
     *
     * @param xmlStr xml加密字符串
     * @return 解密后结果
     * @throws Exception Exception
     */
    private static String encode(final String xmlStr)
        throws Exception {
        return CharactersEncryptionUtil.encryption(xmlStr);
    }

    /**
     * .
     * 解密
     *
     * @param xmlStr 原始xml字符串
     * @return 加密后结果
     * @throws Exception Exception
     */
    private static String decode(final String xmlStr)
        throws Exception {
        return CharactersEncryptionUtil.decryption(xmlStr);
    }


    /**
     * .
     * xml转对象
     *
     * @param xml   xml字符串
     * @param model 对象类
     * @return 返回对象实例
     * @throws JAXBException JAXBException
     */
    private static Object convertToGraphModel(final String xml,
                                              final Class model)
        throws JAXBException {
        StringReader reader = new StringReader(xml);
        JAXBContext jaxbContext = JAXBContext.newInstance(model);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        return jaxbUnmarshaller.unmarshal(reader);
    }

    /**
     * .
     * 将对象转为流程XML
     *
     * @param obj   对象实例
     * @param model 对象类
     * @return xml字符串
     * @throws JAXBException JAXBException
     */
    public static String convertToXML(final Object obj, final Class model)
        throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(model);
        Marshaller marshaller = jaxbContext.createMarshaller();
        //控制字符串不转译
        marshaller.setProperty(CharacterEscapeHandler.class.getName(),
            (CharacterEscapeHandler) (ch, start, length, isAttVal, writer)
                -> writer.write(ch, start, length));
        marshaller.setListener(new MarshallerListener());
        StringWriter writer = new StringWriter();
        marshaller.marshal(obj, writer);
        return writer.toString();
    }
}

package com.hui.gmall.payment.mq;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQMessage;
import org.apache.activemq.command.ActiveMQTextMessage;

import javax.jms.*;
import javax.lang.model.element.VariableElement;

/**
 * @author shenhui
 * @version 1.0
 * @date 2020/7/1 20:54
 */
public class ProducerTest {
    /*
    创建连接工厂
    创建连接
    打开连接
    创建session
    创建队列
    创建消息提供者
    发送消息
    关闭
     */
    public static void main(String[] args) throws JMSException {
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory("tcp://192.168.255.128:61616");
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Queue queue = session.createQueue("shenhui");
        MessageProducer producer = session.createProducer(queue);
        ActiveMQTextMessage activeMQTextMessage = new ActiveMQTextMessage();
        activeMQTextMessage.setText("shenhui da shuai b");
        producer.send(activeMQTextMessage);
        producer.close();
        session.close();
        connection.close();
    }
}

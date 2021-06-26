package com.example.messagingrabbitmq;

import com.example.messagingrabbitmq.models.Sms;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Sender {
 
    private static RabbitTemplate rabbitTemplate;
    private ObjectMapper mapper = new ObjectMapper();
    private static Sender instance = null;


    private Sender(RabbitTemplate _rabbitTemplate) {
        rabbitTemplate = _rabbitTemplate;
    }

    public static void initialize(RabbitTemplate _rabbitTemplate) {
        if(_rabbitTemplate != null && instance == null) {
            instance = new Sender(_rabbitTemplate);
        }
    }

    public static Sender getInstance() {
        return instance;
    }


    public void addSmsToQueue(Sms sms) throws JsonProcessingException {

        String smsJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(sms);
        rabbitTemplate.convertAndSend(MessagingRabbitmqApplication.topicExchangeName,
            "foo.bar.baz", 
            smsJson);
    }
}

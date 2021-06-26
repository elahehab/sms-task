package com.example.messagingrabbitmq;

import java.util.concurrent.TimeUnit;

import com.example.messagingrabbitmq.models.Response;
import com.example.messagingrabbitmq.models.Sms;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


@RestController
@RequestMapping(path="/sms")
public class SmsController {

    private ObjectMapper mapper = new ObjectMapper();

    public SmsController(RabbitTemplate rabbitTemplate) {
        Sender.initialize(rabbitTemplate);
    }

    
    @GetMapping("/send")
	String send(@RequestParam("number") String number, @RequestParam("text") String text) throws JsonProcessingException {
        //verify number format
        
        Sms newSms = new Sms();
        newSms.setNumber(number);
        newSms.setText(text);
        newSms.setSmsStatus(Sms.STATUS.SMS_NEW);

        Sender.getInstance().addSmsToQueue(newSms);
        Response response = new Response();
        response.setStatus(Response.RESPONSE_STATUS.SUCCESS);
        response.setBody("Message were added to the queue");

        String responseJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(response);
        return responseJson;
    }
}

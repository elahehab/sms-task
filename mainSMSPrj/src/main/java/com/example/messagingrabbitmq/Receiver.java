package com.example.messagingrabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.messagingrabbitmq.models.Sms;
import com.example.messagingrabbitmq.models.Response;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonParseException;
import org.springframework.context.annotation.Bean;


@Component
public class Receiver {

	ObjectMapper mapper = new ObjectMapper();

	private String sendURL1 = "http://localhost:8081/sms/send";
	private String sendURL2 = "http://localhost:8082/sms/send";

	private boolean usingUrl1 = false;
	private boolean usingUrl2 = false;

	private static final Logger log = LoggerFactory.getLogger(Receiver.class);


	public void receiveMessage(String smsJson) {
		try {

			usingUrl1 = false;
			usingUrl2 = false;

			Sms sms = mapper.readValue(smsJson, Sms.class);
			sms.setSmsStatus(Sms.STATUS.SMS_QUEUED);
			
			String lastNumber = sms.getNumber().substring(sms.getNumber().length() - 1);
			
			String url = "";
			if(Integer.parseInt(lastNumber)%2 == 0) {
				url = sendURL1;
				usingUrl1 = true;
			} else {
				url = sendURL2;
				usingUrl2 = true;
			}

			sendSmsUsingUrlAddr(sms, url);
		
		} catch(JsonParseException e) {
			log.error("Wrong input sms format: " + smsJson);
		} catch(JsonMappingException e) {
			log.error("Wrong input sms format: " + smsJson);
		} catch(JsonProcessingException e) {
			log.error("Wrong input sms format: " + smsJson);
		} 
	}

	private void sendSmsUsingUrlAddr(Sms sms, String url) {
		RestTemplate builder = (new RestTemplateBuilder()).build();
		try {
			String responseStr = builder.getForObject(url + "?number=" + sms.getNumber() + "&body=" + sms.getText(), String.class);
			Response response = mapper.readValue(responseStr, Response.class);
			
			if(response.getStatus() == Response.RESPONSE_STATUS.SUCCESS) {
				log.info("sending sms using " + url + " response: success");
				sms.setSmsStatus(Sms.STATUS.SMS_SEND_SUCCESS);
				return;
			} else {
				log.info("sending sms using " + url + " response: failure");
				onSmsFailed(sms);
			}
		} catch(Exception e) {
			log.error(e.getMessage());
			onSmsFailed(sms);
		}
	}

	private void onSmsFailed(Sms sms) {

		if(usingUrl1 == true && usingUrl2 == true) {
			sms.setSmsStatus(Sms.STATUS.SMS_SEND_FAILED);
			//insert sms back to queue after a time passed
			new java.util.Timer().schedule(new java.util.TimerTask() {
				@Override
				public void run() {
					try {
						Sender.getInstance().addSmsToQueue(sms);
					} catch (Exception e) {
						log.error(e.getMessage());
					}
				}
			}, 10000);
			return;

		} else if (usingUrl1 == true) {
			usingUrl2 = true;
			sendSmsUsingUrlAddr(sms, sendURL2);
		} else if (usingUrl2 == true) {
			usingUrl1 = true;
			sendSmsUsingUrlAddr(sms, sendURL1);
		}

	}

}

package ir.sms.main;

import java.util.Random;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ir.sms.main.models.Response;

@RestController
@RequestMapping(path="/sms")
public class SmsController {
    
    private ObjectMapper mapper = new ObjectMapper();

    @GetMapping("/send")
	String send(@RequestParam("number") String number, @RequestParam("body") String text) throws JsonProcessingException{
        float randomNum = (new Random()).nextFloat();
        Response response = new Response();
        response.setBody("test");
        if(randomNum < 0.5) {
            response.setStatus(Response.RESPONSE_STATUS.SUCCESS);
        } else {
            response.setStatus(Response.RESPONSE_STATUS.FAILURE);
        }
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(response);
    }
}

package lol.gilliard.smsdashboard;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@Controller
public class SMSController {

    public static class MessageDetails {
        public List<String> numbers;
        public String message;
    }

    @Value("${phoneNumber:+13473912842}")
    private String myTwilioPhoneNumber;

    @Autowired
    public SMSController(
        @Value("${twilioAccountSid:AC06f3c3bd1ca7192e7035bab214c97777}") String twilioAccountSid,
        @Value("${twilioAuthToken:a012669905f230a49ca0e2d97f29e991}") String twilioAuthToken) {
//    	twilioAccountSid="AC06f3c3bd1ca7192e7035bab214c97777";
//    	twilioAuthToken="a012669905f230a49ca0e2d97f29e991";
        Twilio.init(twilioAccountSid, twilioAuthToken);
    }
    
    @PostMapping("/send-messages")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void sendMessages(@RequestBody MessageDetails messageDetails) {

        messageDetails.numbers.stream().forEach( number -> {
            Message message = Message.creator(
                new PhoneNumber(number),
                new PhoneNumber(myTwilioPhoneNumber),
                messageDetails.message).create();
            System.out.println("Sent message w/ sid: " + message.getSid());
        });
    }
}

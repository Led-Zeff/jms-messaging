package courses.microservices.jmsmessaging.sender;

import java.util.UUID;

import javax.jms.JMSException;
import javax.jms.Message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import courses.microservices.jmsmessaging.config.JsmConfig;
import courses.microservices.jmsmessaging.model.HelloWorldMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class HellowSender {

  private final JmsTemplate jmsTemplate;
  private final ObjectMapper objectMapper;

  @Scheduled(fixedRate = 2000)
  public void sendMessage() {
    log.info("Sending message");
    HelloWorldMessage msg = new HelloWorldMessage(UUID.randomUUID(), "Hallo, Ja, eine katze und ein hund");
    jmsTemplate.convertAndSend(JsmConfig.JMS_QUEUE, msg);
    log.info("Message sent");
  }

  @Scheduled(fixedRate = 2000)
  public void sendAndReceiveMessage() throws JMSException {
    HelloWorldMessage msg = new HelloWorldMessage(UUID.randomUUID(), "Tee oder milch?");
    Message received = jmsTemplate.sendAndReceive(JsmConfig.JMS_SEND_AND_RECEIVE_QUEUE, session -> {
      Message helloMsg;
      try {
        helloMsg = session.createTextMessage(objectMapper.writeValueAsString(msg));
        helloMsg.setStringProperty("_type", "courses.microservices.jmsmessaging.model.HelloWorldMessage");
        return helloMsg;
      } catch (JsonProcessingException e) {
        log.error(e.getMessage(), e);
        throw new JMSException("Error sending message");
      }
    });

    log.info(received.getBody(String.class));
  }
  
}

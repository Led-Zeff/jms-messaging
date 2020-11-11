package courses.microservices.jmsmessaging.listener;

import java.util.UUID;

import javax.jms.JMSException;
import javax.jms.Message;

import org.springframework.jms.JmsException;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import courses.microservices.jmsmessaging.config.JsmConfig;
import courses.microservices.jmsmessaging.model.HelloWorldMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class HelloListener {

  private final JmsTemplate jmsTemplate;

  @JmsListener(destination = JsmConfig.JMS_QUEUE)
  public void listen(@Payload HelloWorldMessage helloMessage, @Headers MessageHeaders headers, Message message) {
    log.info("Got a message: {}", helloMessage);
    // throw new RuntimeException("Resend please");
  }

  @JmsListener(destination = JsmConfig.JMS_SEND_AND_RECEIVE_QUEUE)
  public void listenAndSend(@Payload HelloWorldMessage helloMessage, @Headers MessageHeaders headers, Message message) throws JmsException, JMSException {
    jmsTemplate.convertAndSend(message.getJMSReplyTo(), new HelloWorldMessage(UUID.randomUUID(), "Tee bitte, Danke!"));
  }

}

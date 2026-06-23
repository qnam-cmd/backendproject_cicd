package org.spring.backendprojectex.websocket.config;

import org.spring.backendprojectex.websocket.rabbitmq.dto.Answer;
import org.spring.backendprojectex.websocket.rabbitmq.dto.Question;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class Receiver {
    // 웹소켓을 통해서 클라이언트에게 메시지를 전달하는 도구
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    // rabbitmq 큐를 지속적으로 감시하는 메서드
    // 설정한 컨테이너 myFactory를 통해 JSON메시지를 Question객체로 자동변환함
    @RabbitListener(queues = "${rabbitmq.queue.name}", containerFactory = "myFactory")
    public void receiveMessage(Question question) throws Exception {
        Thread.sleep(100);
        LocalDateTime today = LocalDateTime.now();
        String formattedTime = today.format(DateTimeFormatter.ofPattern("a H:mm"));
        // 요청 data를 전달~
        String responseText = question.getContent()+ "에 대한 답장입니다(RabbitMQ).";
        String src="";
        System.out.println(question.getContent()+"<<<< question");

        src= "<div class='msg bot flex'>" +
                "<div class='icon'>" +
                "<img src='/images/chat2.png' th:alt=\"#{chat}\" style='width: 40px; height: 40px;' />" +
                "</div>" +
                "<div class='message'>" +
                "<div class='part'>" +
                "<p>" + responseText + "</p>" +
                "<img src='"+ src +"' th:alt=\"#{src}\" />" +
                "</div>" +
                "<div class='time'>" +
                formattedTime +
                "</div>" + "</div>" + "</div>";
        // 웹소켓 응답 전송
        // 클라이언트가 구독중인 /topic/question 채널로 결과를 전달
        simpMessagingTemplate.convertAndSend("/topic/question", Answer.builder().message(src).build());
    }
}

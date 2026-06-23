package org.spring.backendprojectex.websocket.chatbot.controller;

import lombok.RequiredArgsConstructor;
import org.spring.backendprojectex.member.entity.MemberEntity;
import org.spring.backendprojectex.member.repository.MemberRepository;
import org.spring.backendprojectex.websocket.chatbot.dto.BotMessage;
import org.spring.backendprojectex.websocket.chatbot.dto.ClientMessage;
import org.spring.backendprojectex.websocket.rabbitmq.dto.Question;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ChatbotController {

    private final MemberRepository memberRepository;

    @MessageMapping("/hellow")  // /app/hellow 로 전송받고
    @SendTo("/topic/greetings") // /topic/greetings로 응답
    public BotMessage greetings(ClientMessage message) throws Exception {
        Thread.sleep(50);
        LocalDateTime today = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");
        String formatterDay = today.format(formatter);
        String formattedTime = today.format(DateTimeFormatter.ofPattern("a H:mm"));

        String firstMessage =
                " <div class='msg'> <div class='content'> " + message.getContent() + "</div>" +
                        "<div class='head-img'> <img src='/images/chat.png' style='width: 40px; height: 40px;'> </div>" +
                        "<div class='message'> 안녕하세요, 챗봇(WebSocket)입니다. <br>" +
                        "궁금한점은 저에게물어보세요. <br> " +
                        "<div class='time'>" + formatterDay + formattedTime + "</div> </div>";
        return new BotMessage(firstMessage);
    }

    @MessageMapping("/message")     // /app/message 로 전송받고
    @SendTo("/topic/message")       // /topic/message 로 응답
    public BotMessage message(ClientMessage message) throws Exception {
        Thread.sleep(50);
        LocalDateTime today = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");
        String formatterDay = today.format(formatter);
        String formattedTime = today.format(DateTimeFormatter.ofPattern("a H:mm"));

        String responseText = "";
        String searchData = message.getContent().trim();
        System.out.println("입력메시지" + searchData);
        // 이메일조회
        String searchEmail = searchData.replace("이메일:", "").trim();
        System.out.println("이메일" + searchEmail);

        if (searchData.startsWith("이메일:")) {
            // DB조회 O
            // 이메일로 조회
            Optional<MemberEntity> optionalMemberEntity = memberRepository.findByUserEmail(searchEmail);
            // 출력
            if (optionalMemberEntity.isPresent()) {
                MemberEntity memberEntity = optionalMemberEntity.get();
                responseText = "이메일: " + memberEntity.getUserEmail() + "이름: " + memberEntity.getUserName();
            } else {
                responseText = searchEmail + "이메일을 찾을 수 없습니다.";
            }
        } else {
            // DB조회 X
            responseText = message.getContent() + "에 대한 응답입니다.";
        }
        String firstMessage = "<div class='msg'>" +
                "<div class='head-img'><img src='/images/chat.png' style='width: 40px; height: 40px;' ></div>" +
                "<div class='message'>" + responseText + "</div>" +
                "<div class='time'>" + formatterDay + formattedTime + "</div></div>";
        return new BotMessage(firstMessage);
    }

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.name}")
    private String exchange;    // 경로설정(어디로보낼것인가)

    @Value("${rabbitmq.question.routing.key}")
    private String routingKey;  // 어떤조건으로 보낼것인가


    @MessageMapping("/bot")
    public void rabbitChat(Question message) throws Exception {
        System.out.println("프로젝트 컨트롤러 도달 완료 - message: " + message);
        // 직접응답하지않고, RabbitMQ로 이동, Receiver가 처리
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
    }


}

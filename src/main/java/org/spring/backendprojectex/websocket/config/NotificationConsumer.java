package org.spring.backendprojectex.websocket.config;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Component
public class NotificationConsumer {
    // RabbitMQ 수신데이터 -> 가공 -> Websocket 전송 데이터 -> 프론트
    private final SimpMessagingTemplate simpMessagingTemplate;

    public NotificationConsumer(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate= simpMessagingTemplate;
    }

    // RabbitMq Config에 지정한 컨버터 기반 팩토리(myFactory)를 이용하여 메시지 청취
    @RabbitListener(queues = "${rabbitmq.queue.notification}", containerFactory = "myFactory")
    public void receiveNotification(Message message) {
        try {
            // 1. UTF-8 문자열 데이터로 수신
            String messageHtml = new String(message.getBody(), StandardCharsets.UTF_8);
            // 2. Jackson 컨버터를 거치며 앞뒤에 들어간 이중 따옴표(") 정제 가공
            if (messageHtml.startsWith("\"") && messageHtml.endsWith("\"")) {
                messageHtml = messageHtml.substring(1, messageHtml.length() - 1);
            }

            // 3. 문자열 내부에 유니코드 형태나 남아있는 이모지(\uD83D\uDD14)가 있다면 완전히 강제 제거 및 청소
            messageHtml = messageHtml.replace("\\uD83D\\uDD14", "")
                    .replace("종", "");
            System.out.println("[Consumer 작동중] 깨짐없는 알림 수신 완료 -> 웹소켓 전송");

            // 4. 자바스크립트 측 JSON 파싱 규칙(body.message)에 맞게 변환 Map 매핑
            Map<String, String> response = new HashMap<>();
            response.put("message", messageHtml);

            // 5. 프론트의 /topic/notification 채널로 웹소켓 브로드캐스팅 전송
            simpMessagingTemplate.convertAndSend("/topic/notification", response);
        } catch (Exception e) {
            System.out.println("알림 문자 전송실패" + e.getMessage());
        }
    }
}

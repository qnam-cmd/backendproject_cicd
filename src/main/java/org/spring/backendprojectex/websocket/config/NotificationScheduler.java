package org.spring.backendprojectex.websocket.config;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class NotificationScheduler {
    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.name:ec2.exchange}")
    private String exchange;

    @Value("${rabbitmq.notification.routing.key:ec2.notification.#}")
    private String routingKey;

    // RabbitTemplate 주입
    public NotificationScheduler(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }
    // 30초마다 실행되는 스케쥴러
    @Scheduled(cron = "*/30 * * * * *")
    public void sendDailyNotification() {
        try {
            String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("a h:mm"));
            // 프론트엔드 화면에 그려질 HTML 구조
            String messageHtml = "<div class='msg'>" +
                    "<div class='head-img'><img src='/images/bell.png' style='width: 40px; height: 40px;'> </div>" +
                    "<div class='message'>오늘의 알림입니다.</div>" +
                    "<div class='audio'>" +
                    "<audio controls autoplay muted id='alarmAudio'><source src='/audio/alarm.ogg' type='audio/ogg'>" +
                    "<source src='/audio/alarm.mp3' type='audio/mpeg'> </audio></div>" +
                    "<div class='time'>" + time + "</div>" +
                    "</div>";
            // 와일드카드(#) 문자 -> 실제 라우팅 경로이름으로 설정
            String actualRoutingKey = routingKey.contains("#") ? routingKey.replace("#", "daily") : "ec2.notification.daily";
            // RabbitMq Exchange 로 메시지 전송
            rabbitTemplate.convertAndSend(exchange, actualRoutingKey, messageHtml);
            System.out.println("RabbitMQ로 알림 전송완료! 시간: "+time);
        } catch (Exception e) {
            System.err.println("발송실패(에러):" +e.getMessage());
        }
    }
}

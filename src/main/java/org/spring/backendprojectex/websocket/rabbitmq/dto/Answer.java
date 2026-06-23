package org.spring.backendprojectex.websocket.rabbitmq.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Answer {
    // 응답
    private String answer;
    private String message;
}

package org.spring.backendprojectex.websocket.rabbitmq.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Question {
    private Long key;
    private String name;
    private String question;
    private String content;
}

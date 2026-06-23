package org.spring.backendprojectex.websocket.config;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RabbitMqConfig {
    private final ConnectionFactory connectionFactory;

    @Value("${rabbitmq.exchange.name}")
    private String topicExchangeName;

    @Value("${rabbitmq.queue.name}")
    private String queueName;

    @Value("${rabbitmq.question.routing.key}")
    private String routingKey;

    @Value("${rabbitmq.queue.notification}")
    private String queueNameNotification;

    @Value("${rabbitmq.notification.routing.key}")
    private String routingKeyNotification;

    @Bean
    public Queue notificationQueue() {
        return new Queue(queueNameNotification, true);
    }
    @Bean
    public Binding notificationBinding() {
        return BindingBuilder.bind(notificationQueue())
                .to(exchange())
                .with(routingKeyNotification);
    }


    // RabbitMq 구성요소
    @Bean
    Queue queue() {
        return new Queue(queueName, true);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(topicExchangeName);    // 메시지가 머무는통로(queue), true: 서버 재시작되어도 큐가 유지됨
    }

    @Bean
    MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();  // JSON 메시지를 Java객체로 바꿔주는 도구
    }

    // 큐(queue)와 교환기(exchange)를 라우팅키로 연결
    @Bean
    Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(routingKey);
    }

    // 챗봇용 수동 리스너 컨테이너 역할
    @Bean
    public SimpleMessageListenerContainer container (ConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queueName); // 챗봇용 큐 감시
        container.setMessageListener(listenerAdapter);      // 도착하면 어댑터실행
        container.setMissingQueuesFatal(false);             // 큐가 잠시 없어도 서버가 뻗지않고 대기함
        return container;
    }

    // 챗봇용 수동 메시지 어댑터
    @Bean
    public MessageListenerAdapter listenerAdapter(Receiver receiver) {
        MessageListenerAdapter listenerAdapter
                = new MessageListenerAdapter();
        listenerAdapter.setMessageConverter(messageConverter());
        return listenerAdapter;
    }

    // 여러 리스너를 만들 때 공통 설정을 해주는 공장(Factory)
    @Bean
    public SimpleRabbitListenerContainerFactory myFactory(SimpleRabbitListenerContainerFactoryConfigurer configurer,
                                                          MessageConverter messageConverter) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        configurer.configure(factory, connectionFactory);
        factory.setMessageConverter(messageConverter);
        return factory;
    }

}

package miniproject.infra;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import miniproject.UserApplication;
import miniproject.config.kafka.KafkaProcessor;
import org.springframework.beans.BeanUtils;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.MimeTypeUtils;

import java.io.Serializable;
import java.util.Date;
//<<< Clean Arch / Outbound Adaptor
@Getter
@Setter
public abstract class AbstractEvent implements Serializable {

    private String eventType;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date timestamp;

    public AbstractEvent() {
        this.eventType = this.getClass().getSimpleName();
        this.timestamp = new Date();
    }

    public AbstractEvent(Object aggregate) {
        this();
        // 필요한 경우 하위 클래스에서 BeanUtils.copyProperties 사용
        BeanUtils.copyProperties(aggregate, this);
    }

    public void publish() {
        KafkaProcessor processor = UserApplication.applicationContext.getBean(KafkaProcessor.class);
        MessageChannel outputChannel = processor.outboundTopic();

        outputChannel.send(
            MessageBuilder
                .withPayload(this)
                .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                .setHeader("type", getEventType())
                .build()
        );
    }

    public void publishAfterCommit() {
        TransactionSynchronizationManager.registerSynchronization(
            new TransactionSynchronizationAdapter() {
                @Override
                public void afterCompletion(int status) {
                    AbstractEvent.this.publish();
                }
            }
        );
    }

    public boolean validate() {
        return getEventType().equals(getClass().getSimpleName());
    }

    public String toJson() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON format exception", e);
        }
    }
}
//>>> Clean Arch / Outbound Adaptor

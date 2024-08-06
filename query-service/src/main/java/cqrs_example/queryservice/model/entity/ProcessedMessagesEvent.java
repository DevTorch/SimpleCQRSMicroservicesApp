package cqrs_example.queryservice.model.entity;

import cqrs_example.kafkacore.events.EventTypeEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "processed_messages_events")
public class ProcessedMessagesEvent {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    private String messageId;
    private Long entityId;
    private EventTypeEnum eventType;
    private String eventBody;
    private LocalDateTime receivedTimeStamp;

    public ProcessedMessagesEvent(String messageId,
                                  Long entityId,
                                  EventTypeEnum eventType,
                                  String eventBody,
                                  LocalDateTime receivedTimeStamp) {
        this.messageId = messageId;
        this.entityId = entityId;
        this.eventType = eventType;
        this.eventBody = eventBody;
        this.receivedTimeStamp = receivedTimeStamp;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
               "id = " + id + ", " +
               "messageId = " + messageId + ", " +
               "entityId = " + entityId + ", " +
               "eventType = " + eventType + ", " +
               "eventBody = " + eventBody + ", " +
               "receivedTimeStamp = " + receivedTimeStamp + ")";
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        ProcessedMessagesEvent that = (ProcessedMessagesEvent) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}

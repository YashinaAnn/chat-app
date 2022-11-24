package ru.yasha.webchat.entity;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "messages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String text;
    @CreationTimestamp
    private LocalDateTime time;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Version
    private Long version;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ChatMessage chatMessage = (ChatMessage) o;
        return id != null && Objects.equals(id, chatMessage.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

package ru.yasha.chat.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.yasha.chat.entity.ChatMessage;

import java.util.List;
import java.util.Optional;

public interface ChatMessageRepository extends PagingAndSortingRepository<ChatMessage, Long> {

    @Query("SELECT m FROM ChatMessage m ORDER BY m.time DESC")
    List<ChatMessage> findLast(Pageable pageable);

    Optional<ChatMessage> findByText(String text);
}

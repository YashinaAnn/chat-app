package ru.yasha.webchat.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import ru.yasha.webchat.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends PagingAndSortingRepository<User, Long> {

    Optional<User> findByName(String name);

    List<User> findByActive(boolean active);
}

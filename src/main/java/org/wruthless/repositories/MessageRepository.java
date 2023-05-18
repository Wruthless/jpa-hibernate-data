package org.wruthless.repositories;

import org.springframework.data.repository.CrudRepository;
import org.wruthless.Message;

// A repository of Message entities with a Long identifier.
public interface MessageRepository extends CrudRepository<Message, Long> {

}
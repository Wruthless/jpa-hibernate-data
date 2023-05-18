package org.wruthless;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.wruthless.configuration.SpringDataConfiguration;
import org.wruthless.repositories.MessageRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

// Extend with SpringExtension. This is to integrate the Sprint test
// context with the JUnit 5 Jupiter test.
@ExtendWith(SpringExtension.class)
// The context configuration is the SpringDataConfiguration class in the
// configuration package.
@ContextConfiguration(classes = {SpringDataConfiguration.class})
public class HelloWorldSpringDataJPATest {

    // MessageRepository bean injected by Spring through autowiring. This is
    // accomplished through the org.wruthless.repositories package where
    // MessageRepository is located.
    @Autowired
    private MessageRepository messageRepository;

    @Test
    public void storeLoadMessage() {
        Message message = new Message();
        message.setText("Hello World from Spring Data JPA!");

        // Persist the message object. The save method is inherited from the
        // CrudRepository interface.
        messageRepository.save(message);

        // Retrieve the messages from the repository. findAll is inherited from
        // the CrudRepository interface.
        List<Message> messages = (List<Message>) messageRepository.findAll();

        assertAll(
                () -> assertEquals(1, messages.size()),
                () -> assertEquals("Hello World from Spring Data JPA!", messages.get(0).getText())
        );

    }
}
package edu.uoc.epcsd.notification.services;

import edu.uoc.epcsd.notification.pojos.Category;
import edu.uoc.epcsd.notification.pojos.Show;
import edu.uoc.epcsd.notification.pojos.User;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class NotificationService {

    @Autowired
    private UserService userService;    // mock service

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    String kafkaTopic = "java_in_use_topic";

    public void notifyShowCreation(Show show) {

        for (Category category : show.getCategories()) {
            for (User user : userService.getUsersByFavouriteCategory(category)) {
                notifyUser(user, show);
            }
        }
    }

    // mock notification
    private void notifyUser(User user, Show show) {
        // send email / push notification / etc.
        log.info("Show \"" + show.getName() + "\" added!. Notifying the user \"" + user.getFullName() + "\"");
    }

    public void send(String message) {

        kafkaTemplate.send(kafkaTopic, message);
    }


}

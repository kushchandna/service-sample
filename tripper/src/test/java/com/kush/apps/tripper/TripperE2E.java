package com.kush.apps.tripper;

import static com.kush.apps.tripper.api.Duration.duration;
import static java.util.Arrays.asList;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.kush.apps.tripper.api.Duration;
import com.kush.apps.tripper.api.TripPlan;
import com.kush.lib.service.remoting.auth.User;
import com.kush.messaging.content.TextContent;
import com.kush.messaging.message.Message;

public class TripperE2E extends BaseTripperE2E {

    @Test
    public void e2e() throws Exception {
        User firstUser = user(0);
        User secondUser = user(1);
        User thirdUser = user(2);

        updateNameAndEmail(firstUser, "First User", "firstuser@domain.com");
        updateNameAndEmail(secondUser, "Second User", "seconduser@domain.com");
        updateNameAndEmail(thirdUser, "Third User", "thirduser@domain.com");

        runAuthenticatedOperation(firstUser, () -> {
            TripPlan tripPlan = createTripPlan();

            Map<String, Set<Object>> userFilter = ImmutableMap.of(FIELD_EMAIL,
                    new HashSet<>(asList(EMAILS_IN_CONTACTS)));
            addTripMembers(tripPlan, userFilter);
        });

        runAuthenticatedOperation(secondUser, () -> {
            tripperMessagingService.registerMessageHandler((msg) -> {
                System.out.println("Second User got message " + msg);
            });
        });

        runAuthenticatedOperation(thirdUser, () -> {
            TripPlan tripPlan = fetchFirstTripPlan();
            tripperMessagingService.sendMessage(tripPlan.getId(), new TextContent("Test Message"));
        });

        runAuthenticatedOperation(firstUser, () -> {
            TripPlan tripPlan = fetchFirstTripPlan();
            List<Message> messages = tripperMessagingService.getMessages(tripPlan.getId());
            System.out.println("First User got messages " + messages);
        });

        runAuthenticatedOperation(firstUser, () -> {
            TripPlan tripPlan = fetchFirstTripPlan();

            Duration suggestion1 = duration()
                .from(LocalDateTime.parse("2019-02-01 22:00", FORMATTER))
                .to(LocalDateTime.parse("2019-02-05 21:00", FORMATTER))
                .build();
            tripperPlanningService.proposeDuration(tripPlan.getId(), suggestion1);

            Duration suggestion2 = duration()
                .from(LocalDateTime.parse("2019-02-08 22:00", FORMATTER))
                .to(LocalDateTime.parse("2019-02-12 21:00", FORMATTER))
                .build();
            tripperPlanningService.proposeDuration(tripPlan.getId(), suggestion2);
        });

        TimeUnit.SECONDS.sleep(1);
    }
}

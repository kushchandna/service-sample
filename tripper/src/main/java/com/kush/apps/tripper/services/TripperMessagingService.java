package com.kush.apps.tripper.services;

import static java.util.Collections.singleton;

import java.util.Set;

import com.kush.apps.tripper.api.TripPlan;
import com.kush.apps.tripper.persistors.TripPlanPersistor;
import com.kush.lib.persistence.api.PersistorOperationFailedException;
import com.kush.messaging.content.Content;
import com.kush.messaging.destination.Destination;
import com.kush.messaging.destination.GroupIdBasedDestination;
import com.kush.messaging.message.Message;
import com.kush.messaging.push.MessageHandler;
import com.kush.messaging.services.MessagingService;
import com.kush.service.BaseService;
import com.kush.service.annotations.ServiceMethod;
import com.kush.service.auth.AuthenticationRequired;
import com.kush.utils.id.Identifier;

public class TripperMessagingService extends BaseService {

    @AuthenticationRequired
    @ServiceMethod
    public Message sendMessage(Identifier tripPlanId, Content content) throws PersistorOperationFailedException {
        TripPlanPersistor tripPlanPersistor = getTripPlanPersistor();
        TripPlan tripPlan = tripPlanPersistor.fetch(tripPlanId);
        Identifier tripGroupId = tripPlan.getTripGroup().getId();
        MessagingService messagingService = getMessagingService();
        Set<Destination> destinations = singleton(new GroupIdBasedDestination(tripGroupId));
        return messagingService.sendMessage(content, destinations);
    }

    @AuthenticationRequired
    @ServiceMethod
    public void registerMessageHandler(MessageHandler messageHandler) {
        getMessagingService().registerMessageHandler(messageHandler);
    }

    @AuthenticationRequired
    @ServiceMethod
    public void unregisterMessageHandler(MessageHandler messageHandler) {
        getMessagingService().unregisterMessageHandler(messageHandler);
    }

    @Override
    protected void processContext() {
        checkContextHasValueFor(MessagingService.class);
    }

    private TripPlanPersistor getTripPlanPersistor() {
        return getInstance(TripPlanPersistor.class);
    }

    private MessagingService getMessagingService() {
        return getInstance(MessagingService.class);
    }
}

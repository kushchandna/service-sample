package com.kush.apps.tripper.services;

import static java.util.Collections.singleton;

import java.util.List;
import java.util.Set;

import com.kush.apps.tripper.api.TripPlan;
import com.kush.apps.tripper.api.TripPlanGroupBasedDestination;
import com.kush.apps.tripper.persistors.TripPlanPersistor;
import com.kush.lib.persistence.api.PersistorOperationFailedException;
import com.kush.messaging.content.Content;
import com.kush.messaging.destination.Destination;
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
        TripPlan tripPlan = getTripPlan(tripPlanId);
        Set<Destination> destinations = singleton(new TripPlanGroupBasedDestination(tripPlan));
        return getMessagingService().sendMessage(content, destinations);
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

    @AuthenticationRequired
    @ServiceMethod
    public List<Message> getMessages(Identifier tripPlanId) throws PersistorOperationFailedException {
        Identifier tripPlanGroupId = getTripPlanGroupId(tripPlanId);
        return getMessagingService().getMessagesInGroup(tripPlanGroupId);
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

    private Identifier getTripPlanGroupId(Identifier tripPlanId) throws PersistorOperationFailedException {
        TripPlan tripPlan = getTripPlan(tripPlanId);
        return tripPlan.getTripGroup().getId();
    }

    private TripPlan getTripPlan(Identifier tripPlanId) throws PersistorOperationFailedException {
        TripPlanPersistor tripPlanPersistor = getTripPlanPersistor();
        return tripPlanPersistor.fetch(tripPlanId);
    }
}

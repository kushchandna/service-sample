package com.kush.apps.tripper;

import static com.google.common.collect.Sets.newHashSet;
import static com.kush.lib.persistence.helpers.InMemoryPersistor.forType;
import static java.time.Month.APRIL;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.kush.apps.tripper.api.Duration;
import com.kush.apps.tripper.api.TripPlan;
import com.kush.apps.tripper.api.TripPlanMember;
import com.kush.apps.tripper.api.TripPlanPlace;
import com.kush.apps.tripper.persistors.DefaultTripPlanPersistor;
import com.kush.apps.tripper.persistors.TripPlanPersistor;
import com.kush.apps.tripper.services.TripPlannerService;
import com.kush.lib.location.api.Place;
import com.kush.lib.location.api.PlaceFinder;
import com.kush.lib.location.services.PlaceService;
import com.kush.lib.persistence.api.Persistor;
import com.kush.lib.service.remoting.auth.User;
import com.kush.service.BaseServiceTest;
import com.kush.utils.id.Identifier;

public class TripperServiceTest extends BaseServiceTest {

    private TripPlannerService tripPlannerService;
    private PlaceService placeService;

    @Before
    public void beforeEachTest() throws Exception {
        Persistor<TripPlan> tripPlanPersistor = forType(TripPlan.class);
        Persistor<TripPlanPlace> tripPlanPlacePersistor = forType(TripPlanPlace.class);
        Persistor<TripPlanMember> tripPlanMemberPersistor = forType(TripPlanMember.class);
        TripPlanPersistor finalPersistor =
                new DefaultTripPlanPersistor(tripPlanPersistor, tripPlanPlacePersistor, tripPlanMemberPersistor);
        addToContext(TripPlanPersistor.class, finalPersistor);
        addToContext(PlaceFinder.class, new DummyPlaceFinder());
        tripPlannerService = registerService(TripPlannerService.class);
        placeService = registerService(PlaceService.class);
    }

    @Test
    public void createdTripPlan_CanBeRetrievedByCreator() throws Exception {
        String user1TripPlan1 = "First Trip Plan";
        String user2TripPlan2 = "Second Trip Plan";
        String user1TripPlan3 = "Third Trip Plan";

        User[] users = getUsers();
        User user1 = users[0];
        User user2 = users[1];

        runAuthenticatedOperation(user1, () -> {
            tripPlannerService.createTripPlan(user1TripPlan1);
            tripPlannerService.createTripPlan(user1TripPlan3);
        });

        runAuthenticatedOperation(user2, () -> {
            tripPlannerService.createTripPlan(user2TripPlan2);
        });

        runAuthenticatedOperation(user1, () -> {
            List<TripPlan> user1CreatedTripPlans = tripPlannerService.getTripPlans();
            assertThat(user1CreatedTripPlans, hasSize(2));
            assertThat(user1CreatedTripPlans.get(0).getTripPlanName(), is(equalTo(user1TripPlan1)));
            assertThat(user1CreatedTripPlans.get(0).getCreatedBy(), is(equalTo(user1.getId())));
            assertThat(user1CreatedTripPlans.get(1).getTripPlanName(), is(equalTo(user1TripPlan3)));
            assertThat(user1CreatedTripPlans.get(1).getCreatedBy(), is(equalTo(user1.getId())));
        });

        runAuthenticatedOperation(user2, () -> {
            List<TripPlan> user2CreatedTripPlans = tripPlannerService.getTripPlans();
            assertThat(user2CreatedTripPlans, hasSize(1));
            assertThat(user2CreatedTripPlans.get(0).getTripPlanName(), is(equalTo(user2TripPlan2)));
            assertThat(user2CreatedTripPlans.get(0).getCreatedBy(), is(equalTo(user2.getId())));
        });
    }

    @Test
    public void addPlacesToCreatedTripPlan() throws Exception {
        runAuthenticatedOperation(() -> {
            TripPlan tripPlan = tripPlannerService.createTripPlan("Test Trip Plan");
            assertThat(tripPlannerService.getPlacesInTripPlan(tripPlan.getId()), is(empty()));

            Place place1 = placeService.findPlace("Place1");
            Place place2 = placeService.findPlace("Place2");
            tripPlannerService.addPlacesToTripPlan(tripPlan.getId(), asList(place1, place2));

            List<TripPlan> tripPlans = tripPlannerService.getTripPlans();
            List<Place> savedPlacesToVisit = tripPlannerService.getPlacesInTripPlan(tripPlans.get(0).getId());
            assertThat(savedPlacesToVisit, hasSize(2));
            assertThat(savedPlacesToVisit.get(0).getName(), is(equalTo("Place1")));
            assertThat(savedPlacesToVisit.get(1).getName(), is(equalTo("Place2")));
        });
    }

    @Test
    public void planATrip() throws Exception {
        // decide dates for a trip plan
        LocalDateTime tripPlanStartTime = LocalDateTime.of(LocalDate.of(2018, APRIL, 10), LocalTime.of(22, 0));
        LocalDateTime tripPlanEndTime = LocalDateTime.of(LocalDate.of(2018, APRIL, 14), LocalTime.of(2, 0));
        Duration duration = Duration.during(tripPlanStartTime, tripPlanEndTime);

        // decide places for trip plan
        Place place1 = placeService.findPlace("Place1");
        Place place2 = placeService.findPlace("Place2");
        Place place3 = placeService.findPlace("Place3");
        List<Place> places = asList(place1, place2, place3);

        // add members to trip plan
        User[] users = getUsers();
        User tripPlanOwner = users[0];
        Set<Identifier> memberUserIds = newHashSet(users[1].getId(), users[2].getId(), users[3].getId());

        runAuthenticatedOperation(tripPlanOwner, () -> {
            TripPlan tripPlan = tripPlannerService.createTripPlan("Test Trip Plan");
            tripPlannerService.setTripPlanDuration(tripPlan.getId(), duration);
            tripPlannerService.addPlacesToTripPlan(tripPlan.getId(), places);
            tripPlannerService.addMembersToTripPlan(tripPlan.getId(), memberUserIds);
        });
    }
}

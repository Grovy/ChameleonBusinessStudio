package com.compilercharisma.chameleonbusinessstudio.service;

import com.compilercharisma.chameleonbusinessstudio.dto.*;
import com.compilercharisma.chameleonbusinessstudio.exception.ExternalServiceException;
import com.compilercharisma.chameleonbusinessstudio.exception.AppointmentNotFound;
import com.compilercharisma.chameleonbusinessstudio.exception.UserNotRegisteredException;
import com.compilercharisma.chameleonbusinessstudio.repository.AppointmentRepository;
import com.compilercharisma.chameleonbusinessstudio.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final AppointmentRepository appointmentRepository;

    public UserService(UserRepository userRepository, AppointmentRepository appointmentRepository) {
        this.userRepository = userRepository;
        this.appointmentRepository = appointmentRepository;
    }

    /**
     * This method gets all the users in Vendia
     *
     * @return {@link UserResponse}
     */
    public Mono<UserResponse> getAllUsers() {
        return userRepository.findAllUsers();
    }

    /**
     * Returns a Boolean of whether the user is already registered
     *
     * @param email email of the user
     * @return {@link Boolean}
     */
    public Mono<Boolean> isRegistered(String email) {
        return userRepository.findAllUsers()
                .map(UserResponse::getUsers)
                .map(users -> users.stream()
                        .anyMatch(u -> u.getEmail().equalsIgnoreCase(email)));
    }

    /**
     * Get a single user from Vendia based on their email, null if it doesn't exist
     *
     * @param email User's email
     * @return {@link User}
     */
    public Mono<User> getUser(String email) {
        return getAllUsers()
                .map(UserResponse::getUsers)
                .filter(CollectionUtils::isNotEmpty)
                .switchIfEmpty(Mono.error(new UserNotRegisteredException(
                        "User with email [%s] is not registered".formatted(email))))
                .mapNotNull(list -> list.stream()
                        .filter(u -> u.getEmail().equalsIgnoreCase(email))
                        .findFirst().orElse(null));
    }

    /**
     * Creates a user in Vendia Share
     *
     * @param user the user that will be created in Vendia
     * @return {@link User}
     */
    public Mono<User> createUser(User user) {
        return userRepository.isUserRegistered(user.getEmail())
                .filter(Boolean.FALSE::equals)
                .switchIfEmpty(Mono.error(new ExternalServiceException(
                        "User with email [%s] already exists".formatted(user.getEmail()), HttpStatus.CONFLICT)))
                .flatMap(u -> userRepository.createUser(user));
    }

    /**
     * Edits a user's info in Vendia
     *
     * @param user the user whose info will be edited in Vendia
     * @return {@link User}
     */
    public Mono<User> updateUser(User user) {
        return userRepository.findUserByEmail(user.getEmail())
                .mapNotNull(list -> list.getUsers().stream().findFirst().orElse(null))
                .flatMap(u -> userRepository.updateUser(user, u.get_id()));
    }

    /**
     * Deletes a user from Vendia
     *
     * @param email The email of user to be deleted.
     * @return {@link UserResponse}
     */
    public Mono<DeletionResponse> deleteUser(String email) {
        return userRepository.findUserByEmail(email)
                .mapNotNull(list -> {
                    if (CollectionUtils.isEmpty(list.getUsers())) {
                        log.error("User with email [{}] was not found in Vendia", email);
                        throw new UserNotRegisteredException("User with email [%s] was not found".formatted(email));
                    }
                    return list.getUsers().stream().findFirst().orElse(null);
                })
                .flatMap(u -> userRepository.deleteUser(u.get_id()));
    }

    /**
     * Fetches all the Appointments for a user in Vendia
     *
     * @param id The id to look up.
     * @return {@link UserAppointments}
     */
    public Mono<UserAppointmentsResponse> getUserAppointments(String id) {
        return userRepository.getUserAppointments(id)
                .flatMapIterable(UserAppointments::getAppointments)
                .flatMap(appointmentRepository::getAppointment)
                .collectList()
                .map(UserAppointmentsResponse::new);
    }

    /**
     * Adds multiple appointment IDs to a User's appointment list.
     * Note that this method does not validate the appointment IDs, and throws
     * an UnsupportedOperationException if this would add no new appointments to
     * their array.
     * 
     * Calling this method is preferered over multiple calls to multiple calls
     * to addNewAppointmentForUser, as the latter is vulnerable to race 
     * conditions and performs more requests.
     */
    public Mono<User> addNewAppointmentsForUser(
            String userId, 
            List<String> appointmentIds
    ){
        log.info("Adding the following appointments for user with id [{}]:", userId);
        for (var appt : appointmentIds) {
            log.info("* [{}]", appt);
        }
        return userRepository.getUserAppointments(userId)
                .map(UserAppointments::getAppointments)
                .filter(oldAppts -> containsNewAppointments(oldAppts, appointmentIds))
                .switchIfEmpty(Mono.error(new UnsupportedOperationException("must add at least 1 new appointment")))
                .map(oldAppts -> merge(oldAppts, appointmentIds))
                .map(this::stringifyAppointmentList)
                .flatMap(apptsString -> userRepository.updateAppointmentsForUser(userId, apptsString));
    }

    private boolean containsNewAppointments(List<String> oldAppts, List<String> newAppts){
        var oldSet = Set.copyOf(oldAppts);
        return !oldSet.containsAll(newAppts);
    }

    private List<String> merge(List<String> list1, List<String> list2){
        var merged = new HashSet<String>(list1);
        list2.forEach(merged::add);
        return merged.stream().toList();
    }

    private String stringifyAppointmentList(List<String> appointmentList){
        return appointmentList.stream()
                .map(s -> String.format("\"%s\"", s))
                .collect(Collectors.joining(",", "[", "]"));
    }

    /**
     * Adds a new appointment to the user's appointments array when they are booked
     *
     * @param userId        id of the user
     * @param appointmentId id of the appointment
     * @return {@link Mono} of a {@link User}
     */
    public Mono<User> addNewAppointmentForUser(String userId, String appointmentId) {
        return userRepository.getUserAppointments(userId)
                .filter(u -> !u.getAppointments().contains(appointmentId))
                .switchIfEmpty(Mono.error(new AppointmentNotFound(
                        "User already is booked for appointment with id [%s]".formatted(appointmentId))))
                .map(appointments -> {
                    appointments.getAppointments().add(appointmentId);
                    return stringifyAppointmentList(appointments.getAppointments());
                })
                .flatMap(apptsString -> userRepository.updateAppointmentsForUser(userId, apptsString));
    }

    /**
     * Removes an appointment id from the user's appointments array when they are unbooked
     *
     * @param userId        Id of the user
     * @param appointmentId Id of the appointment
     * @return {@link Mono} of a {@link User}
     */
    public Mono<User> removeAppointmentForUser(String userId, String appointmentId) {
        return userRepository.getUserAppointments(userId)
                .filter(u -> u.getAppointments().contains(appointmentId))
                .switchIfEmpty(Mono.error(new AppointmentNotFound(
                        "User with id [%s] is not booked for appointment with id [%s]".formatted(userId, appointmentId))))
                .map(appointments -> {
                    var userAppointments = appointments.getAppointments();
                    userAppointments.remove(appointmentId);
                    return CollectionUtils.isNotEmpty(userAppointments) ?
                            userAppointments.stream()
                            .map(s -> String.format("\"%s\"", s))
                            .collect(Collectors.joining(",", "[", "]")) :
                            "[]";
                })
                .flatMap(apptsString -> userRepository.updateAppointmentsForUser(userId, apptsString));
    }

}

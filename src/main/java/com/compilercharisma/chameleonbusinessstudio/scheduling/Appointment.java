package com.compilercharisma.chameleonbusinessstudio.scheduling;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

/**
 * This is object representation of an appointment
 * 
 * @author Matt Crow <mattcrow19@gmail.com>
 */
public class Appointment {
    @Getter
    private final int id;
    
    @Getter
    private final LocalDateTime startTime; // LDT is immutable, so this is OK
    
    @Getter
    private final LocalDateTime endTime;
    
    @Getter
    private final String title;
    
    @Getter
    private final String location;
    
    @Getter
    private final String description;
    
    @Getter
    private final String restrictions;
    
    @Setter
    @Getter
    private boolean isCanceled;
    
    @Getter
    private final int totalSlots;
    
    private final Map<String, Set<String>> tags;
    private final Set<String> registeredUsers;
    
    public Appointment(
            int id, LocalDateTime startTime, LocalDateTime endTime, String title,
            String location, String description, String restrictions, 
            boolean isCanceled, int availableSlots
    ){
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.title = title;
        this.location = location;
        this.description = description;
        this.restrictions = restrictions;
        this.isCanceled = isCanceled;
        this.totalSlots = availableSlots;
        tags = new HashMap<>();
        registeredUsers = new HashSet<>();
    }
    
    
    public boolean isSlotAvailable(){
        return totalSlots > registeredUsers.size();
    }
    
    public void addTag(String tag, String value){
        if(!tags.containsKey(tag)){
            tags.put(tag, new HashSet<>());
        }
        tags.get(tag).add(value);
    }
    
    public Set<String> getValuesForTag(String tag){
        return new HashSet<>(tags.getOrDefault(tag, new HashSet<>()));
    }
    
    public Set<String> getTags(){
        return new HashSet<>(tags.keySet());
    }
    
    public Map<String, Set<String>> getTagValues(){
        Map<String, Set<String>> ret = new HashMap<>();
        getTags().forEach((t)->ret.put(t, getValuesForTag(t)));
        return ret;
    }
    
    public boolean hasAnyValueForTag(String tag){
        return tags.containsKey(tag) && !tags.get(tag).isEmpty();
    }
    
    public boolean hasValueForTag(String tag, String value){
        return tags.containsKey(tag) && tags.get(tag).contains(value);
    }
    
    public boolean isAnyUserRegistered(){
        return !registeredUsers.isEmpty();
    }
    
    public boolean isUserRegistered(String user){
        return registeredUsers.contains(user);
    }
    
    public void registerUser(String user){
        if(!isSlotAvailable()){
            throw new UnsupportedOperationException(String.format("Cannot register \"%s\" in appointment#%d: no slots available", user, id));
        }
        registeredUsers.add(user);
    }
    
    /**
     * @return a copy of the users signed up for this appointment
     */
    public Set<String> getRegisteredUsers(){
        return new HashSet<>(registeredUsers);
    }
}

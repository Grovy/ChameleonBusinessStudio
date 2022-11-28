package com.compilercharisma.chameleonbusinessstudio.repository;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.compilercharisma.chameleonbusinessstudio.dto.Schedule;
import com.compilercharisma.chameleonbusinessstudio.webconfig.ApplicationFolder;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Temporary implementation of ScheduleRepository that stores schedules in a
 * JSON file
 */
@Slf4j
@Repository // comment this out, then add to VendiaScheduleRepository once we use that
public class JsonScheduleRepository implements ScheduleRepository {
    public static final String DEFAULT_STORE_NAME = "schedules.json";

    private final ApplicationFolder folder;
    private final String storeName;

    @Autowired // allows Spring to detect ctor it needs to use
    public JsonScheduleRepository(ApplicationFolder folder){
        this(folder, DEFAULT_STORE_NAME);
    }

    public JsonScheduleRepository(ApplicationFolder folder, String storeName){
        this.folder = folder;
        this.storeName = storeName;
    }

    /**
     * If no schedules are stored on the local file system, creates test data
     */
    public void createAbsentSchedules() {
        getAllSchedules()
            .collectList()
            .handle((schedules, dummy)->{
                if(schedules.isEmpty()){
                    log.info("no schedules found, storing test schedule data...");
                    storeTestData();
                    log.info("... done storing test data schedule");
                }
            })
            .subscribe(); // does not run if no subscribers
    }

    private void storeTestData(){
        var schedules = new ArrayList<Schedule>();
        // add your test data here!
        setSchedules(schedules)
            .subscribe(); // need to subscribe to ensure it runs
    }

    @Override
    public Mono<Void> storeSchedule(Schedule s) {
        setIds(s);
        return getAllSchedules()
            .collectList()
            .map(oldSchedules->{
                // create new array instead of mutating old one, as this way is
                // more in-line with functional programming principles
                var newSchedules = new ArrayList<Schedule>(oldSchedules);
                newSchedules.add(s);
                return newSchedules;
            }).flatMap(this::setSchedules);
    }

    @Override
    public Mono<Void> storeSchedules(List<Schedule> schedules){
        /*
         * cannot just add to a JSON file, need to do the following:
         * 1. take all scheds from the old file
         * 2. remove any scheds that have been updated
         * 3. add all the new scheds
         * 4. save the result in the JSON file
         */

        var updatedIds = schedules.stream()
            .filter(sched->sched.get_id() != null)
            .map(sched->sched.get_id())
            .collect(Collectors.toSet());
        
        schedules.forEach(this::setIds);
        
        return getAllSchedules() // step 1.
            .filter(sched->!updatedIds.contains(sched.get_id())) // step 2.
            .collectList()
            .map(oldSchedsThatAreNotUpdated->{
                var all = new ArrayList<>(oldSchedsThatAreNotUpdated);
                all.addAll(schedules); // step 3.
                return all;
            })
            .flatMap(this::setSchedules); // step 4.
    }
    

    @Override
    public Mono<Schedule> getScheduleById(String id) {
        return getAllSchedules()
            .filter(sched->sched.get_id().equals(id))
            .next(); // returns first element
    }

    @Override
    public Flux<Schedule> getAllSchedulesWhereEnabledEquals(boolean b) {
        return getAllSchedules().filter(sched -> sched.getIsEnabled() == b);
    }

    @Override
    public Flux<Schedule> getAllSchedules() {
        Flux<Schedule> schedules = Flux.empty();

        var file = folder.getFile(ApplicationFolder.SCHED_DIR, storeName);
        if(!file.exists() || !file.isFile()){
            return Flux.just(new Schedule[0]);
        }
        
        try (var stream = Files.lines(file.toPath())) {
            var text = stream.collect(Collectors.joining("\n"));
            if(!(text.startsWith("[") && text.endsWith("]"))){ // not JSON array
                text = '[' + text + ']';
            }
            //                                           https://stackoverflow.com/a/14891237
            var obj = makeMapper().readValue(text, new TypeReference<List<Schedule>>(){});
            schedules = Flux.fromIterable(obj);
        } catch (IOException e) {
            schedules = Flux.error(e);
        }

        return schedules;
    }

    @Override
    public Mono<Void> deleteScheduleById(String id) {
        return getAllSchedules()
            .filter(sched->!sched.get_id().equals(id)) // remove sched with that ID from stream
            .collectList()
            .flatMap(this::setSchedules);
    }

    private void setIds(Schedule s){
        // sorry for the pyramid of doom guys :(
        // set missing IDs of s and all data therein
        if(s.get_id() == null){
            s.set_id(UUID.randomUUID().toString());
            for(var ra : s.getAppointments()) {
                if(ra.get_id() == null){
                    ra.set_id(UUID.randomUUID().toString());
                }
                if(ra.getAppointment().get_id() == null){
                    ra.getAppointment().set_id(UUID.randomUUID().toString());
                }
            }
        }
    }

    private Mono<Void> setSchedules(List<Schedule> schedules){
        schedules.forEach(this::setIds);

        Mono<Void> result = Mono.empty();
        var f = folder.getFile(ApplicationFolder.SCHED_DIR, storeName);

        try {
            var asStr = makeMapper().writeValueAsString(schedules);
            Files.write(f.toPath(), asStr.getBytes());
        } catch(IOException ex){
            result = Mono.error(ex);
        }

        return result;
    }

    private ObjectMapper makeMapper(){
        // https://github.com/FasterXML/jackson-modules-java8
        return new ObjectMapper()
            .registerModule(new JavaTimeModule());
    }
}

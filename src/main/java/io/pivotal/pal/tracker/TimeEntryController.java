package io.pivotal.pal.tracker;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Time;
import java.util.List;

@RestController
@RequestMapping("/time-entries")
public class TimeEntryController {
    TimeEntryRepository repository;
    private final DistributionSummary timeEntrySummary;
    private final Counter actionCounter;

    public TimeEntryController(TimeEntryRepository repository, MeterRegistry meterRegistry) {
        this.repository=repository;
        timeEntrySummary =meterRegistry.summary("timeEntry.summary");
        actionCounter = meterRegistry.counter("timeEntry.actionCounter");
    }

    @PostMapping
    public ResponseEntity<TimeEntry> create(@RequestBody TimeEntry entry){
        TimeEntry entry1=repository.create(entry);
        actionCounter.increment();
        timeEntrySummary.record(repository.list().size());
        return ResponseEntity.status(HttpStatus.CREATED).body(entry1);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TimeEntry> read(@PathVariable Long id){
        TimeEntry entry1=repository.find(id);
        if(entry1==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(entry1);
        }
        actionCounter.increment();
        return ResponseEntity.ok(entry1);
    }

    @GetMapping
    public ResponseEntity<List<TimeEntry>> list(){
        actionCounter.increment();
        return ResponseEntity.status(HttpStatus.OK).body(repository.list());
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable Long id, @RequestBody TimeEntry timeEntry){
        TimeEntry entry=repository.update(id,timeEntry);
        if(entry == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(entry);
        }
        actionCounter.increment();
        return ResponseEntity.ok(entry);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TimeEntry> delete(@PathVariable Long id){
        repository.delete(id);
        actionCounter.increment();
        timeEntrySummary.record(repository.list().size());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(repository.find(id));
    }
}

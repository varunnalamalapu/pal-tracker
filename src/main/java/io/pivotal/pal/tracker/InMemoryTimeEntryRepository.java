package io.pivotal.pal.tracker;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTimeEntryRepository implements TimeEntryRepository {
    Map<Long, TimeEntry> map = new HashMap<Long, TimeEntry>();
    List<TimeEntry> theList;
    Long id = 1L;

    @Override
    public TimeEntry create(TimeEntry timeEntry) {
        timeEntry.setId(id);
        map.put(timeEntry.getId(), timeEntry);
        id += 1L;
        return timeEntry;
    }

    @Override
    public TimeEntry find(Long id) {
        return map.get(id);
    }

    @Override
    public List<TimeEntry> list() {
        theList=new ArrayList<TimeEntry>(map.values());
        return theList;
    }

    @Override
    public TimeEntry update(Long id, TimeEntry entry) {
        if(!map.containsKey(id)){
            return null;
        }
        entry.setId(id);
        map.put(id, entry);
        return map.get(id);
    }


    @Override
    public void delete(Long id) {
        map.remove(id);
    }
}


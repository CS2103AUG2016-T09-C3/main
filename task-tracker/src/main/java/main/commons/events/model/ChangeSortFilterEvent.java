//@@author A0144132W
package main.commons.events.model;

import main.commons.events.BaseEvent;
import main.model.filter.SortCriteria;

public class ChangeSortFilterEvent extends BaseEvent {

    private SortCriteria filter;
    
    public ChangeSortFilterEvent(String param) {
        assert "date".equals(param) || "name".equals(param);
        matchToFilter(param);
    }

    private void matchToFilter(String param) {
        if ("date".equals(param)) {
            filter = SortCriteria.TIME;
        }
        else {
            filter = SortCriteria.NAME;
        }
    }
    
    public SortCriteria getSortCriteria() {
        return filter;
    }

    @Override
    public String toString() {
        return filter.name();
    }

}

//@@author A0144132W
package main.model.filter;

import java.util.Comparator;
import main.model.task.Task;

public class SortFilter {
    

    //Sort by comparing deadlines and end date of events
    private Comparator<Task> byTime = (t1, t2) -> t1.compareTime(t2);

    //Sort lexicographically, case insensitive
    private Comparator<Task> byName = (t1, t2) -> t1.getMessage().toLowerCase().compareTo(t2.getMessage().toLowerCase());

    
    private Comparator<Task> sortCriteria;
    private boolean isReversed = false;
    
    public SortFilter(SortCriteria criteria) {
        switch (criteria) {
            case TIME:
                sortCriteria = byTime.thenComparing(byName);
                break;
            case NAME:
                sortCriteria = byName.thenComparing(byTime); 
                break;
            default:
                sortCriteria = byTime.thenComparing(byName);
                break;
        }
    }
    
    public Comparator<Task> getComparator() {
        if (isReversed) return sortCriteria.reversed();
        else return sortCriteria;
    }
    
    public void setReverse(boolean reverse) {
        isReversed = reverse;
    }
}

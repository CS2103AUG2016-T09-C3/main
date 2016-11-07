//@@author A0144132W
package main.commons.events.ui;

import main.commons.events.BaseEvent;

public class AutoCompleteEvent extends BaseEvent{
    
    // the start and end index of the input to be replaced
    private int start;
    private int end;
    
    private String suggestion;
    
    public AutoCompleteEvent(int start, int end, String suggestion) {
        this.suggestion = suggestion;
        this.start = start;
        this.end = end;
    }
    
    public String getSuggestion() {
        return suggestion;
    }
    
    public int getStart() {
        return start;
    }
    
    public int getEnd() {
        return end;
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

}

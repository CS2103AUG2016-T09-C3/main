//@@author A0144132W
package main.commons.events.ui;

import main.commons.events.BaseEvent;

public class KeyPressEvent extends BaseEvent{
    private String input;

    public KeyPressEvent(String input) {
        this.input = input;
    }
    
    public String getInput() {
        return input;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
    

}

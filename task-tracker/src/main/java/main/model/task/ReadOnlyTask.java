package main.model.task;

import java.util.Date;

/**
 * A read-only immutable interface for a Person in the addressbook.
 * Implementations should guarantee: details are present and not null, field values are validated.
 */
public interface ReadOnlyTask {
	
    String getMessage();
    Date getStartTime();
    Date getEndTime();
    Date getDeadline();
    boolean getIsFloating();
    boolean getIsEvent();
    

    /**
     * Returns true if both have the same state. (interfaces cannot override .equals)
     */
    default boolean isSameStateAs(ReadOnlyTask other) {
        return other == this // short circuit if same object
                || (other != null // this is first to avoid NPE below
                && other.getMessage().equals(this.getMessage()) // state checks here onwards
                && other.getStartTime().equals(this.getStartTime())
                && other.getEndTime().equals(this.getEndTime())
                && other.getDeadline().equals(this.getDeadline())
                && other.getIsFloating() == this.getIsFloating());
                
                //&& other.getIsFloating().equals(this.getIsFloating()));
    }

    /**
     * Formats the Task as text, showing all details.
     */
    default String getAsText() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getMessage())
                .append(getStartTime())
                .append(getEndTime())
                .append(getDeadline())
                .append(getIsFloating());
                
                
        return builder.toString();
    }

  
}

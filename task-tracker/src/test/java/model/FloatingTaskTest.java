//@@author A0139750B-unused
//FloatingTask class was unused so the test is half done and unused
package model;

import main.model.task.PriorityType;
import main.model.task.Task;
import main.model.task.TaskType;


import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class FloatingTaskTest {
	Task floatingTask;
	
	@Before
	public void setup() throws Exception{
		floatingTask = new Task("", PriorityType.NORMAL);
	}
	
	@Test
	public void checkParameters() throws IllegalArgumentException{		
		assert floatingTask.getMessage() != null;
		assert floatingTask.getPriority() != null;
		assertEquals(PriorityType.NORMAL, floatingTask.getPriority());
		assertFalse(floatingTask.getIsDone());
		assertFalse(floatingTask.getIsInferred());
		assertSame(TaskType.FLOATING, floatingTask.getType());
		
	}
	
	
	@Test
	public void nullPriority() throws Exception{
		try {
			floatingTask.setPriority(null);
		} catch (NullPointerException e){
			System.out.println("Exception thrown " + e);
			e.printStackTrace();
		}
	}
	
	
}

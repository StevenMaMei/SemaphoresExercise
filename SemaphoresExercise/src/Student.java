import java.util.Random;
import java.util.concurrent.Semaphore;

public class Student implements Runnable{
	
	private TeacherAssistant teacherAssistant;
	private WaitingRoom waitingRoom;
	private int id;
	private boolean isProgramming;
	
	public Student(TeacherAssistant t, WaitingRoom w, int id) {
		teacherAssistant = t;
		waitingRoom = w;
		isProgramming = true;
		this.id = id;
	}
	
	@Override
	public void run() {
		Random randGenerator = new Random();
		while(true) {
			if( isProgramming && !waitingRoom.isThereAStudentWaiting() && teacherAssistant.getBusySemaphore().tryAcquire()) {
				teacherAssistant.setCurrentlyAttending(true);
				
				teacherAssistant.setCurrentlyStudent(this);
				
			}else if(isProgramming && waitingRoom.getQueueEditingSemaphore().tryAcquire()) {
				
				if(waitingRoom.howMuchStudentsAreWaiting() <3) {
					waitingRoom.addStudentToTheQueue(this);		
					isProgramming = false;
				}
				waitingRoom.getQueueEditingSemaphore().release();
			}
			
			try {
				Thread.sleep(randGenerator.nextInt(2)*1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isProgramming() {
		return isProgramming;
	}

	public void setProgramming(boolean isProgramming) {
		this.isProgramming = isProgramming;
	}
	
	
	
	
	
}

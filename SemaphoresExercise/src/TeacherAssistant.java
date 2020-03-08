import java.util.Random;
import java.util.concurrent.Semaphore;

public class TeacherAssistant implements Runnable{

	
	private Semaphore busySemaphore;
	private boolean isCurrentlyAttending;
	private Student currentlyStudent;
	private WaitingRoom waitingRoom;
	
	public TeacherAssistant(WaitingRoom room) {
		busySemaphore = new Semaphore(1, true);
		isCurrentlyAttending= false;
		waitingRoom = room;
		
	}
	public void attendStudent(int id) {
		currentlyStudent.setProgramming(false);
		System.out.println("Teacher assistant is currently student with id "+id );
	}
	public void dispatchStudent(int id) {
		System.out.println("Teacher assistant dispatch student with id "+id);
		currentlyStudent.setProgramming(true);
	}

	@Override
	public void run() {
		
		int count = 0;
		while(true) {
			Random rand = new Random();
			if(isCurrentlyAttending) {
//				System.out.println("curr Atend");
				attendStudent(currentlyStudent.getId());
				
				try {
					Thread.sleep(rand.nextInt(2)*1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				dispatchStudent(currentlyStudent.getId());
				isCurrentlyAttending = false;
				busySemaphore.release();
			}else {
				//System.out.println("searching");
				if(waitingRoom.isThereAStudentWaiting()) {
					try {
						waitingRoom.getQueueEditingSemaphore().acquire();
						if(busySemaphore.tryAcquire()) {
							
//						System.out.println(count++);
							isCurrentlyAttending = true;
							currentlyStudent = waitingRoom.getOneStudent();							
						}
						waitingRoom.getQueueEditingSemaphore().release();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else {
					
					
					try {
						Thread.sleep(rand.nextInt(2)*1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				
			}
		}
	}
	
	public Semaphore getBusySemaphore() {
		return busySemaphore;
	}
	public void setBusySemaphore(Semaphore busySemaphore) {
		this.busySemaphore = busySemaphore;
	}
	public boolean isCurrentlyAttending() {
		return isCurrentlyAttending;
	}
	public void setCurrentlyAttending(boolean isCurrentlyAttending) {
		this.isCurrentlyAttending = isCurrentlyAttending;
	}
	public Student getCurrentlyStudent() {
		return currentlyStudent;
	}
	public void setCurrentlyStudent(Student currentlyStudent) {
		this.currentlyStudent = currentlyStudent;
	}
	
	
}

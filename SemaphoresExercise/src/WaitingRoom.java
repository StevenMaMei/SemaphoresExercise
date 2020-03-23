import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

public class WaitingRoom{

	private Queue<Student> studentsQueue;
	private Semaphore queueEditingSemaphore;
	
	public WaitingRoom() {
		studentsQueue = new LinkedList<Student>();
		queueEditingSemaphore = new Semaphore(1,true);
	}
	
	public boolean isThereAStudentWaiting() {
		return studentsQueue.size()>0;
	}
	
	public Student getOneStudent() {
		System.out.println("Student with id "+studentsQueue.peek().getId() +" is removed from the queue");
		return studentsQueue.poll();
	}
	
	public int howMuchStudentsAreWaiting() {
		return studentsQueue.size();
	}
	

	public void addStudentToTheQueue(Student s) {
		studentsQueue.add(s);
		System.out.println("----------------->");
		System.out.println("Student with id "+s.getId()+ " added to the queue"+ " there are: "+ studentsQueue.size());
		System.out.println("<-----------------");
	}

	public Semaphore getQueueEditingSemaphore() {
		return queueEditingSemaphore;
	}

	public void setQueueEditingSemaphore(Semaphore queueEditingSemaphore) {
		this.queueEditingSemaphore = queueEditingSemaphore;
	}
	

	
	
}

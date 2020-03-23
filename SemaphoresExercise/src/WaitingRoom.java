import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

/**
 * 
 * Clase que representa la sala de espera de las monitorías.
 *
 */

public class WaitingRoom{

	/**
	 * Cola donde esperan los estudiantes por ser atendidos. No pueden haber más de 3 estudiantes en la cola.
	 */
	private Queue<Student> studentsQueue;
	
	/**
	 * Semaforo para lograr sincronizar los cambios que se vayan a realizar sobre la sala de espera.
	 */
	private Semaphore queueEditingSemaphore;
	
	public WaitingRoom() {
		studentsQueue = new LinkedList<Student>();
		
		/*Se indica que el semáforo solo va a permitir que el recurso sea atendido por máximo 1 hilo al tiempo,
			y asegurando que los permisos se den en el orden que fueron solicitados.*/
		queueEditingSemaphore = new Semaphore(1,true);
	}
	
	/**
	 * Método que indica si hay estudiantes en la sala de espera, es decir, si hay elementos en la cola
	 * @return true en caso de que hayan, false cuando esté vacía
	 */
	public boolean isThereAStudentWaiting() {
		return studentsQueue.size()>0;
	}
	
	/**
	 * Método que saca y retorna el estudiante que esté en la cabeza de la cola. Imprime su id informando que se retiró de la cola.
	 * @return El Estudiante que fue sacado de la cola.
	 */
	public Student getOneStudent() {
		System.out.println("Student with id "+studentsQueue.peek().getId() +" is removed from the queue");
		return studentsQueue.poll();
	}
	
	/**
	 * Método que indica cuantos estudiantes están en la sala de espera
	 * @return Entero entre 0 y 3
	 */
	public int howMuchStudentsAreWaiting() {
		return studentsQueue.size();
	}
	

	/**
	 * Método que agrega a la cola al estudiante pasado por parámetro e imprime esa información.
	 * Este método solo puede ser usado cuando la cola tenga menos de 3 estudiantes.
	 * @param s El estudiante a agregar
	 */
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

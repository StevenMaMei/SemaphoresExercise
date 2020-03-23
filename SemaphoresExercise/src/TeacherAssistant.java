import java.util.Random;
import java.util.concurrent.Semaphore;

/**
 * Clase que representa al monitor dormilón (zzZZZzzz...)
 *
 */
public class TeacherAssistant implements Runnable{

	/**
	 * El semaforo para que se haga una correcta sincronización de las modificaciones que se hagan al estado
	 * del monitor.
	 */
	private Semaphore busySemaphore;
	
	/**
	 * Parametro que indica si el monitor está actualmente atendiendo a algún estudiante (true) o no (false)
	 */
	private boolean isCurrentlyAttending;
	
	/**
	 * Estudiante al cual está atendiendo el monitor en este momento (null si no lo está haciendo)
	 */
	private Student currentlyStudent;
	
	/**
	 * Sala de espera donde entran los estudiantes para ser atendidos por el monitor.
	 */
	private WaitingRoom waitingRoom;
	
	public TeacherAssistant(WaitingRoom room) {
		/*Se indica que el semáforo solo va a permitir que el recurso sea atendido por máximo 1 hilo al tiempo,
		y asegurando que los permisos se den en el orden que fueron solicitados.*/
		
		busySemaphore = new Semaphore(1, true);
		isCurrentlyAttending= false;
		waitingRoom = room;
		
	}
	
	/**
	 * Método con el que se inicia a atender a un estudiante: se indica que el estudiante actual ya no está
	 * programando y se imprime que el monitor empezó a atenderlo.
	 * @param id id del estudiante a atender
	 */
	public void attendStudent(int id) {
		currentlyStudent.setProgramming(false);
		System.out.println("Teacher assistant is currently student with id "+id );
	}
	
	/**
	 * Método para despachar al estudiante de la sala del monitor: su duda ha sido resuelta. Se imprime que
	 * el monitor despacha al estudiante, el cual vuelve al estado de programar.
	 * @param id id del estudiante a atender
	 */
	public void dispatchStudent(int id) {
		System.out.println("Teacher assistant dispatch student with id "+id);
		currentlyStudent.setProgramming(true);
	}

	/**
	 * Instrucciones que ejecutará el hilo del monitor.
	 */
	@Override
	public void run() {
		
		int count = 0;
		while(true) {
			Random rand = new Random();
			/*
			 * En caso de que el monitor esté atendiendo un estudiante, se llama al método de atención,
			 * en el que se especifica que el estudiante no está programando y se imprime que está recibiendo atención.
			 */
			if(isCurrentlyAttending) {
				attendStudent(currentlyStudent.getId());
				
				//El hilo duerme un tiempo aleatorio, que es lo que se demora en ser atendido el estudiante.
				try {
					Thread.sleep(rand.nextInt(2)*1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				//Se despacha al estudiante, se indica que el monitor ya no está atendiendo a nadie y se libera el semáforo.
				dispatchStudent(currentlyStudent.getId());
				isCurrentlyAttending = false;
				busySemaphore.release();
			
			//En caso de que el monitor no esté atendiendo a nadie actualmente, entra a este hilo.
			}else {
				
				//Verifica si hay algún estudiante esperando en la sala, para pasar a atenderlo.
				if(waitingRoom.isThereAStudentWaiting()) {
					try {
						//Antes de modificar la cola de espera, primero debe de adquirir el sémaforo de la sala para cambiar su estado de forma segura
						waitingRoom.getQueueEditingSemaphore().acquire();
						
						/*
						 * De igual forma, como va a modificar su propio estado, debe verificar si su semáforo
						 * está disponible, para así adquirirlo e indicar que ya está atendiendo a un estudiante
						 * (e indicar cual es).
						 */
						if(busySemaphore.tryAcquire()) {
							
							isCurrentlyAttending = true;
							currentlyStudent = waitingRoom.getOneStudent();							
						}
						/*
						 * Libera el semáforo de la sala de espera para que otros puedan accederlo.
						 */
						waitingRoom.getQueueEditingSemaphore().release();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				/*
				 * En caso de que no esté nadie en la sala de espera, se pone a dormir un tiempo aleatorio para
				 * después volver a ejecutar todo el ciclo.
				 */
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

import java.util.Random;
import java.util.concurrent.Semaphore;

/**
 * Clase que representa a un estudiante.
 */
public class Student implements Runnable{
	
	/**
	 * El monitor hacia el cual el estudiante puede resolver sus dudas.
	 */
	private TeacherAssistant teacherAssistant;
	
	/**
	 * La sala de espera a la que debe ir en caso de que el monitor est� ocupado 
	 */
	private WaitingRoom waitingRoom;
	
	/**
	 * Id que identifica al estudiante.
	 */
	private int id;
	
	/**
	 * true en caso de que el estudiante est� programando, false en caso contrario (recibiendo o esperando ayuda del monitor)
	 */
	private boolean isProgramming;
	
	public Student(TeacherAssistant t, WaitingRoom w, int id) {
		teacherAssistant = t;
		waitingRoom = w;
		isProgramming = true;
		this.id = id;
	}
	
	/**
	 * Instrucciones que ejecutar� el hilo de cada estudiante
	 */
	@Override
	public void run() {
		Random randGenerator = new Random();
		while(true) {
			/*
			 * Esta condici�n es para pedir ayuda: para hacerlo, se debe verificar que efectivamente el
			 * estudiante se encuentra programando, no haya ning�n estudiante en la sala de espera y 
			 * el monitor se encuentre libre (Es decir, su sem�foro est� disponible)
			 */
			if( isProgramming && !waitingRoom.isThereAStudentWaiting() && teacherAssistant.getBusySemaphore().tryAcquire()) {
				/*
				 * En caso de que las condiciones anteriores ocurran, el estudiante va directamente donde el monitor
				 * a despertarlo para que lo atienda.
				 */
				teacherAssistant.setCurrentlyAttending(true);
				
				teacherAssistant.setCurrentlyStudent(this);
				
				/*
				 * En caso que no, se verifica si el estudiante est� programando y pueda modificar el
				 * estado de la sala de espera.
				 */
			}else if(isProgramming && waitingRoom.getQueueEditingSemaphore().tryAcquire()) {
				
				/*
				 * Si hay menos de 3 personas en la cola, deja de programar y entra a la sala de espera, en
				 * caso contrario continua programando.
				 */
				if(waitingRoom.howMuchStudentsAreWaiting() <3) {
					waitingRoom.addStudentToTheQueue(this);		
					isProgramming = false;
				}
				//Libera el sem�foro de la sala de espera, ya que no har� nada m�s.
				waitingRoom.getQueueEditingSemaphore().release();
			}
			
			try {
				//Contin�a con la actividad que est� realizando por un tiempo aleatorio.
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

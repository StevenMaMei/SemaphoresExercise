/**
 * Tarea Monitor Dormilón
 * @author Steven Ma Mei - Daniel Galvis Torres
 */

import java.util.ArrayList;


/**
 * 
 * Clase principal donde se ejecuta el programa.
 * Aquí, se define el ambiente en el que se simulará el domintor dormilón.
 *
 */
public class Environment {

	public static void main(String[] args) {
		
		WaitingRoom wr = new WaitingRoom();
		
		TeacherAssistant ta = new TeacherAssistant(wr);
		
		//Se crea e inicializa el hilo que representa al monitor.
		Thread taThread = new Thread(ta);
		taThread.start();
		
		int n = 30;
		ArrayList<Thread> students = new ArrayList<Thread>();
		//Se crean los estudiantes (En este caso, son n = 30) y se inicializan los hilos de cada uno.
		for(int i = 0; i<n;i++) {
			Student s = new Student(ta, wr, i);
			Thread t = new Thread(s);
			students.add(t);
			t.start();
		}
	}

}

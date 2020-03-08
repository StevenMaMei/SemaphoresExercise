import java.util.ArrayList;

public class Environment {

	public static void main(String[] args) {
		
		WaitingRoom wr = new WaitingRoom();
		
		TeacherAssistant ta = new TeacherAssistant(wr);
		
		Thread taThread = new Thread(ta);
		taThread.start();
		
		int n = 30;
		ArrayList<Thread> students = new ArrayList<Thread>();
		for(int i = 0; i<n;i++) {
			Student s = new Student(ta, wr, i);
			Thread t = new Thread(s);
			students.add(t);
			t.start();
		}
	}

}

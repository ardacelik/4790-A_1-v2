import java.net.*;
import java.io.*;
 
public class Server {
    public static void main(String[] args) {
         
        if (args.length != 1) {
            System.err.println("Usage: java EchoServer <port number>");
            System.exit(1);
        }
         
        int portNumber = Integer.parseInt(args[0]);
        Server es = new Server();
        es.run(portNumber);
     }

     public void run(int portNumber) {
        try {
            ServerSocket serverSocket = new ServerSocket(portNumber);
            while(true) {
               Socket client = serverSocket.accept();
               Connection cc = new Connection(client);
            }
        } catch(Exception e) {
           System.out.println("Exception: "+e);
        }
    }
         
  
}

class Connection extends Thread {
    private Socket client = null;
    private PrintWriter out = null;
    private BufferedReader in = null;
    private ObjectInputStream ois = null;
    private ObjectOutputStream oos = null;

    public Connection(Socket s) { // constructor
       client = s;
    
       try {
            out = new PrintWriter(client.getOutputStream(), true);                   
            in = new BufferedReader(
                    new InputStreamReader(client.getInputStream()));
            ois = new ObjectInputStream(client.getInputStream());
            oos = new ObjectOutputStream(client.getOutputStream());
       } catch (Exception e) {
           try {
             client.close();
           } catch (Exception ex) {
             System.out.println("Error while getting socket streams.."+ex);
           }
           return;
       }
        this.start(); // Thread starts here...this start() will call run()
    }
 
    public void run() {
        Student x = null;
        //Student y = null;
        String studentId = null;
        String semester = null;
        String courses[] = null;
        String successMessage = null;
        try {
            x = (Student) ois.readObject();
            studentId = x.getStudentId();
            semester = x.getSemester();
            courses = x.getCourses();
            successMessage = x.notifySuccessfullRegistration();
            oos.writeUTF(successMessage);
            oos.flush();
            ois.close();
            oos.close();
            System.out.println(successMessage);
            System.out.println("Student ID: " + studentId);
            System.out.println("Semester: " + semester);
            for(int i = 0; i < courses.length; i++) {
                System.out.println(courses[i]);
            }
            client.close(); 
         } catch(Exception e) {
            System.out.println("Exception caught...");
            System.out.println(e.getMessage());
        }
    }
}
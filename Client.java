import java.io.*;
import java.net.*;
 
public class Client {
    public static void main(String[] args) {
         
        // if (args.length != 2) {
        //     System.err.println(
        //         "Usage: java Client <host name> <port number>");
        //     System.exit(1);
        // }
 
        // String hostName = args[0];
        // int portNumber = Integer.parseInt(args[1]);

        // ------ During development only!!! --------
        String hostName = "127.0.0.1";
        int portNumber = 5000;

        String studentId = "100596185";
        String semester = "Fall";
        String courses[] = {"1010", "1020", "1030"};
        String successMessage = null;

        try {
            Socket socket = new Socket(hostName, portNumber);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            
            Student so1 = new Student();
            so1.setStudentId(studentId);
            so1.setSemester(semester);
            so1.setCourses(courses);

            oos.writeObject(so1);
            oos.flush();
            
            successMessage = ois.readLine();

            System.out.println(successMessage);
            System.out.println("Student ID: " + studentId);
            System.out.println("Semester: " + semester);
            System.out.println("Courses: " + courses);

            oos.close();
            ois.close();
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                hostName);
            System.exit(1);
        } 
    }
}
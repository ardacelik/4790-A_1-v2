import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.ArrayList;
 
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

        String studentId;
        String semester;
        String registeredCourses[];
        ArrayList<String> offeredCourses = new ArrayList<String>();
        String successMessage;

        try {
            Socket socket = new Socket(hostName, portNumber);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Scanner usrIn = new Scanner(System.in);

            System.out.println("Welcome to class registration!\nPlease select enter your student ID: ");
            while(true) {
                studentId = usrIn.next();
                if(studentId.length() != 9) {
                    System.out.println("Incorrect student ID, please enter the 9-digit student number");
                    continue;
                }
                break;
            }

            Student student = new Student();
            student.setStudentId(studentId);

            System.out.println("Welcome " + studentId + ". Now please select the semester to see what classes are offered (Type Fall or Winter): ");
            while(true) {
                semester = usrIn.next();
                if(!semester.equals("Fall") && !semester.equals("Winter")) {
                    System.out.println("Please try again. Type \"Fall\" or \"Winter\"");
                    continue;
                }
                break;
            }

            student.setSemester(semester);
            oos.writeUTF(semester);
            System.out.println("You are registering for the " + semester + " semester.\nHere is the list of courses offered in " + semester + ":\n");
            offeredCourses = (ArrayList) ois.readObject();
            // while(offeredCourses.size() == 0) {
                
            //     if(offeredCourses.size() < 7) {
            //         continue;
            //     }
            //     break;
            // }
            for(int i = 0; i < offeredCourses.size(); i++) {
                System.out.println(i + "." + offeredCourses.get(i).toString() + "\n");
            }


            
            // out.println(year);
            // System.out.println("You entered: " + year + " as your year.\nNow please select the semester by typing either 1 or 2 on the console: \n");
            
            // Student student = new Student();
            // so1.setStudentId(studentId);
            // so1.setSemester(semester);
            // so1.setCourses(courses);

            // oos.writeObject(so1);
            // oos.flush();

            // successMessage = ois.readLine();

            // System.out.println(successMessage);
            // System.out.println("Student ID: " + studentId);
            // System.out.println("Semester: " + semester);
            // System.out.println("Courses: " + courses);

            // oos.close();
            // ois.close();
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                hostName);
            System.exit(1);
        } catch(ClassNotFoundException c){
            System.out.println("Class not found");
            c.printStackTrace();
            return;
          }
    }
}
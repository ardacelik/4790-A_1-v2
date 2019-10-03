import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.ArrayList;
 
public class Client {
    public static void main(String[] args) {
         
        if (args.length != 2) {
            System.err.println(
                "Usage: java Client <host name> <port number>");
            System.exit(1);
        }
 
         String hostName = args[0];
         int portNumber = Integer.parseInt(args[1]);

        // ------ During development only!!! --------
        //String hostName = "127.0.0.1";
        //int portNumber = 5000;

        String studentId;
        String semester;
        ArrayList<String> registeredCourses = new ArrayList<String>();
        ArrayList<String> offeredCourses = new ArrayList<String>();
        String successMessage;
        boolean isRegistrationComplete = false;
        int selectOfferedClass;
        String courseToRegister = null;

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
            out.println(semester);
            System.out.println("You are registering for the " + semester + " semester.\nHere is the list of courses offered in " + semester + ":\n");
            offeredCourses = (ArrayList) ois.readObject();

            for(int i = 0; i < offeredCourses.size(); i++) {
                System.out.println(i + "." + offeredCourses.get(i).toString() + "\n");
            }

            while(isRegistrationComplete == false) {
                if(registeredCourses.size() == offeredCourses.size()) {
                    System.out.println("You are registered for all your " + semester + " classes! Your registration process is now complete");
                    isRegistrationComplete = true;
                    break;
                }
                System.out.println("\nPlease enter the class you would like to register by typing 0 - " + (offeredCourses.size() - 1));
                selectOfferedClass = usrIn.nextInt();
                if((selectOfferedClass > offeredCourses.size() - 1) || (selectOfferedClass < 0)) {
                    System.out.println("Invalid input. Please only enter a number between 0 - " + (offeredCourses.size() - 1));
                    continue;
                    
                }
                courseToRegister = offeredCourses.get(selectOfferedClass);
                if(registeredCourses.contains(courseToRegister)) {
                    System.out.println("You are already in this class! Please try another one");
                    continue;
                }
                registeredCourses.add(courseToRegister);
                System.out.println(registeredCourses);
                System.out.println("You registered for " + courseToRegister + ". Would you like to register for another class? Enter 1 for Yes, 0 for No\n");
                System.out.println("0.No\n1.Yes\n");
                int isContinuingToRegister = usrIn.nextInt();
                if(isContinuingToRegister == 0) {
                    isRegistrationComplete = true;
                    break;
                }
                for(int i = 0; i < offeredCourses.size(); i++) {
                    System.out.println(i + "." + offeredCourses.get(i).toString() + "\n");
                }
                System.out.println(registeredCourses);
            }
            student.setCourses(registeredCourses);
            oos.writeObject(student);
            oos.flush();
            successMessage = ois.readLine();
            System.out.println(successMessage);
            System.out.println("Student ID: " + studentId);
            System.out.println("Semester: " + semester);
            for(int i = 0; i < registeredCourses.size(); i++) {
                System.out.println(registeredCourses.get(i));
            }
            
            oos.close();
            ois.close();
            
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
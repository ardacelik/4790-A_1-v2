import java.net.*;
import java.util.ArrayList;
import java.io.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.*;
import javax.swing.*;

 
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

    private static final String path = "./StudentInfo/";
    
    ArrayList<String> fallClasses = new ArrayList<String>();
    ArrayList<String> winterClasses = new ArrayList<String>();

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
        ArrayList<String> courses = null;
        String successMessage = null;
        try {

            while(true) {
                semester = in.readLine();
                if (semester.equals(null)) {
                    continue;
                }
                if(semester.equals("Fall")) {
                    fallClasses.add("COMM 1050U - Technical Communications");
                    fallClasses.add("ENGR 1015U - Introduction to Engineering");
                    fallClasses.add("MATH 1010U - Calculus I");
                    fallClasses.add("MATH 1850U - Linear Algebra for Engineers");
                    fallClasses.add("PHY 1010U - Physics I");
                    oos.writeObject(fallClasses);
                } else if(semester.equals("Winter")) {
                    winterClasses.add("CHEM 1800U - Chemistry for Engineers");
                    winterClasses.add("ENGR 1025U - Engineering Design");
                    winterClasses.add("ENGR 1200U - Introduction to Programming for Engineers");
                    winterClasses.add("MATH 1020U - Calculus II");
                    winterClasses.add("PHY 1020U - Physics II");
                    winterClasses.add("SSCI 1470U - Impact of Science and Technology on Society");
                    oos.writeObject(winterClasses);
                } else {
                    continue;
                }
                break;
            }
            
            x = (Student) ois.readObject();
            studentId = x.getStudentId();
            semester = x.getSemester();
            courses = x.getCourses();
            successMessage = x.notifySuccessfullRegistration();
            oos.writeUTF(successMessage);
            oos.flush();
            System.out.println(successMessage);
            System.out.println("Student ID: " + studentId);
            System.out.println("Semester: " + semester);
            for(int i = 0; i < courses.size(); i++) {
                System.out.println(courses.get(i));
            }
            saveStudentToFile(x);
            ois.close();
            oos.close();
            client.close(); 
         } catch(Exception e) {
            System.out.println("Exception caught...");
            System.out.println(e.getMessage());
        }
    }

    public void saveStudentToFile(Student s) {
        try {

            File file = new File(path);
            ObjectOutputStream oosf = new ObjectOutputStream(new FileOutputStream(path + s.getStudentId() + ".txt"));
            oosf.writeObject(s);
            oosf.close();
            new ImageViewer();
                        
        } catch (Exception e) {
            //TODO: handle exception
        }
        
    }
}

class ImageViewer {
    public ImageViewer() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame("Image Viewer");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                BufferedImage img = null;

                try {
                    img = ImageIO.read(getClass().getResource("DR.Q.jpg"));
                } catch (IOException e) {
                    e.printStackTrace();
                    System.exit(1);
                }

                ImageIcon imgIcon = new ImageIcon(img);
                JLabel lbl = new JLabel();
                lbl.setIcon(imgIcon);
                frame.getContentPane().add(lbl, BorderLayout.CENTER);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }
}
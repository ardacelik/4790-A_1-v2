import java.io.*;
import java.util.*;

public class Student implements Serializable {
   String studentId = null;
   String semester = null;
   String courses[] = null;

   public Student() {
   }

   public void setStudentId(String studentId) {
       this.studentId = studentId;
   }

   public void setSemester(String semester) {
       this.semester = semester;
   }

   public void setCourses(String courses[]) {
       this.courses = courses;
   }

   public String getStudentId() {
       return studentId;
   }

   public String getSemester() {
       return semester;
   }

   public String[] getCourses() {
       return courses;
   }

   public String notifySuccessfullRegistration() {
       return "Congratulations! You are now registered in all your classes!";
   }

}
package dev.coms4156.project.individualproject;

import java.io.Serial;
import java.io.Serializable;


/**
* Constructs a new Course object with the given parameters. Initial count starts at 0.
*/
public class Course implements Serializable {

  /**
  * Constructs a new Course object with the given parameters. Initial count starts at 0.
  *
  * @param instructorName     The name of the instructor teaching the course.
  * @param courseLocation     The location where the course is held.
  * @param timeSlot           The time slot of the course.
  * @param capacity           The maximum number of students that can enroll in the course.
  */
  public Course(String instructorName, String courseLocation, String timeSlot, int capacity) {
    this.courseLocation = courseLocation;
    this.instructorName = instructorName;
    this.courseTimeSlot = timeSlot;
    this.enrollmentCapacity = capacity;
    this.enrolledStudentCount = 0;
  }

  /**
  * Enrolls a student in the course if there is space available.
  *
  * @return true if the student is successfully enrolled, false otherwise.
  */
  public boolean enrollStudent() {
    if (!isCourseFull()) {
      enrolledStudentCount++;
      return true;
    }
    return false;
  }

  /**
  * Drops a student from the course if a student is enrolled.
  *
  * @return true if the student is successfully dropped, false otherwise.
  */
  public boolean dropStudent() {
    if (enrolledStudentCount > 0) {
      enrolledStudentCount--;
      return true;
    }
    return false;
  }

  /**
  * Gets course location.
  *
  * @return the course location.
  */
  public String getCourseLocation() {
    return this.courseLocation;
  }

  /**
  * Gets instructor name.
  *
  * @return the instructor's name.
  */
  public String getInstructorName() {
    return this.instructorName;
  }

  /**
  * Gets course time slot.
  *
  * @return the course time slot.
  */
  public String getCourseTimeSlot() {
    return this.courseTimeSlot;
  }

  /**
  * Converts the above info to string format.
  *
  * @return a string representation of the course info.
  */
  public String toString() {
    return "\nInstructor: " + instructorName +  "; Location: "
            + courseLocation +  "; Time: " + courseTimeSlot;
  }

  /**
  * Reassigns instructor.
  *
  * @param newInstructorName is the new instructor.
  */
  public void reassignInstructor(String newInstructorName) {
    this.instructorName = newInstructorName;
  }

  /**
  * Reassigns location.
  *
  * @param newLocation is the new location.
  */
  public void reassignLocation(String newLocation) {
    this.courseLocation = newLocation;
  }

  /**
  * Reassigns time.
  *
  * @param newTime is the new course time.
  */
  public void reassignTime(String newTime) {
    this.courseTimeSlot = newTime;
  }

  /**
  * Sets enrolled student count.
  *
  * @param count is the number of enrolled students.
  */
  public void setEnrolledStudentCount(int count) {
    if (count >= 0) {
      this.enrolledStudentCount = count;
    } else {
      System.out.println("Cannot set count to a negative value.");
    }
  }

  /**
  * Checks if course is full.
  *
  * @return true if course is at or past capacity, false otherwise.
  */
  public boolean isCourseFull() {
    return enrolledStudentCount >= enrollmentCapacity;
  }

  /**
  * Gets enrolled student count. Added by DT.
  *
  * @return enrolled student count for testing purposes.
  */

  public int getEnrolledStudentCount() {
    return this.enrolledStudentCount;
  }

  @Serial
  private static final long serialVersionUID = 123456L;
  private final int enrollmentCapacity;
  private int enrolledStudentCount;
  private String courseLocation;
  private String instructorName;
  private String courseTimeSlot;
}

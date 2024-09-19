package dev.coms4156.project.individualproject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.HashMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


/**
 * Department Unit Tests.
 */
public class DepartmentUnitTests {
  private Department department;
  private Course course;

  /**
   * Set Up.
   */
  @BeforeEach
  public void setUp() {
    HashMap<String, Course> courses = new HashMap<>();
    String[] times = {"11:40-12:55", "4:10-5:25", "10:10-11:25", "2:40-3:55"};
    String[] locations = {"417 IAB", "309 HAV", "301 URIS"};
    var coms1004 = new Course("Adam Cannon", locations[0], times[0], 400);
    coms1004.setEnrolledStudentCount(249);
    courses.put("coms1004", coms1004);
    department = new Department("COMS", courses, "Luca Carloni", 2700);
  }

  /**
   * Test getCourseSelection.
   */
  @Test
  public void test_getCourseSelection() {
    assertNotNull(department.getCourseSelection());
    assertNotNull(department.getCourseSelection().get("coms1004"));
  }

  /**
   * Test getNumberOfMajors.
   */
  @Test
  public void test_getNumberOfMajors() {
    assertEquals(2700, department.getNumberOfMajors());
  }

  /**
   * Test getDepartmentChair.
   */
  @Test
  public void test_getDepartmentChair() {
    assertEquals("Luca Carloni", department.getDepartmentChair());
  }

  /**
   * Test addPersonToMajor.
   */
  @Test
  public void test_addPersonToMajor() {
    department.addPersonToMajor();
    assertEquals(2701, department.getNumberOfMajors());
  }

  /**
   * Test dropPersonFromMajor.
   */
  @Test
  public void test_dropPersonFromMajor() {
    department.dropPersonFromMajor();
    assertEquals(2699, department.getNumberOfMajors());
  }

  /**
   * Test dropPersonFromMajor when there are no majors to check it doesn't go below zero.
   */
  @Test
  public void test_dropPersonFromMajor_WithZeroMajors() {
    var emptyDepartment = new Department("COMS", new HashMap<>(),
            "Luca Carloni", 0);
    emptyDepartment.dropPersonFromMajor();
    assertEquals(0, emptyDepartment.getNumberOfMajors(),
            "Number of majors should not be negative.");
  }

  /**
   * Test addCourse to check it adds a course to the department.
   */
  @Test
  public void test_addCourse() {
    String[] times = {"11:40-12:55", "4:10-5:25", "10:10-11:25", "2:40-3:55"};
    String[] locations = {"417 IAB", "309 HAV", "301 URIS"};
    var newCourse = new Course("Brian Borowski", locations[2], times[1], 250);
    newCourse.setEnrolledStudentCount(242);
    department.addCourse("coms3134", newCourse);

    assertEquals(newCourse, department.getCourseSelection().get("coms3134"));
  }

  /**
   * Test createCourse to check it creates and adds a course to the department.
   */
  @Test
  public void test_createCourse() {
    department.createCourse("coms3157",
            "Jae Lee", "417 IAB", "4:10-5:25", 400);
    var createdCourse = department.getCourseSelection().get("coms3157");
    createdCourse.setEnrolledStudentCount(311);

    assertNotNull(createdCourse);
    assertEquals("Jae Lee", createdCourse.getInstructorName());
    assertEquals("417 IAB", createdCourse.getCourseLocation());
    assertEquals("4:10-5:25", createdCourse.getCourseTimeSlot());
  }

  /**
   * Test toString method to check it returns the correct string representation.
   */
  @Test
  public void test_toString() {
    String expected =
            "COMS coms1004: \nInstructor: Adam Cannon; Location: 417 IAB; Time: 11:40-12:55\n";
    String actual = department.toString();
    assertEquals(expected, actual);
  }
}



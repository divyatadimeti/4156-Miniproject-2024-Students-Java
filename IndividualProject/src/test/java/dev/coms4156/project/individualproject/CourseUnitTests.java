package dev.coms4156.project.individualproject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

/**
* Course Unit Tests.
*/
@SpringBootTest
@ContextConfiguration

public class CourseUnitTests {
  private Course testCourse;

  /**
  * Set up Course instance for testing.
  */
  @BeforeEach
  public void setUp() {
    testCourse = new Course("Griffin Newbold", "417 IAB", "11:40-12:55", 250);
  }

  /**
  * Test enrollment functionality when course is not full.
  */
  @Test
  public void test_enrollStudent() {
    testCourse.setEnrolledStudentCount(249); // Setting count to one less than capacity
    assertTrue(testCourse.enrollStudent(), "Enrollment should succeed.");
    assertEquals(250, testCourse.getEnrolledStudentCount(),
            "Enrolled student count should be updated.");
    assertTrue(testCourse.isCourseFull(),
            "Course should be now be full after enrolling one more student.");
  }

  /**
  * Test enrollment functionality when course is full.
  */
  @Test
  public void test_enrollStudent_WhenFull() {
    testCourse.setEnrolledStudentCount(250); // Setting count to capacity
    assertFalse(testCourse.enrollStudent(),
            "Enrollment should fail when course is full.");
    assertEquals(250, testCourse.getEnrolledStudentCount(),
            "Enrolled student count should remain the same.");
  }

  /**
  * Test dropping a student.
  */
  @Test
  public void test_dropStudent() {
    testCourse.setEnrolledStudentCount(1); // Setting count to make sure a student can be dropped
    assertTrue(testCourse.dropStudent(), "Dropping a student should work.");
    assertEquals(0, testCourse.getEnrolledStudentCount(),
            "Enrolled student count should be 0.");
  }

  /**
  * Test dropping a student when no students enrolled.
  */
  @Test
  public void test_dropStudent_WhenNoneEnrolled() {
    testCourse.setEnrolledStudentCount(0);
    assertFalse(testCourse.dropStudent(),
            "Dropping a student when no students are enrolled should fail.");
  }

  /**
  * Test isCourseFull when enrolled count is at capacity.
  */
  @Test
  public void test_isCourseFull_WhenFull() {
    testCourse.setEnrolledStudentCount(250); // Setting count equal to capacity
    assertTrue(testCourse.isCourseFull(),
            "Course should be full when enrolled count matches capacity.");
  }

  /**
  * Test isCourseFull when enrolled count is less than capacity.
  */
  @Test
  public void test_isCourseFull_WhenNotFull() {
    testCourse.setEnrolledStudentCount(249); // Setting count one less than capacity
    assertFalse(testCourse.isCourseFull(),
            "Course should not be full since enrolled count is less than capacity.");
  }

  /**
  * Test reassigning the instructor.
  */
  @Test
  public void test_reassignInstructor() {
    testCourse.reassignInstructor("Jennifer Blazet");
    assertEquals("Jennifer Blazet",
            testCourse.getInstructorName(), "Instructor name should be reassigned.");
  }

  /**
  * Test reassigning the location.
  */
  @Test
  public void test_reassignLocation() {
    testCourse.reassignLocation("202 HAV");
    assertEquals("202 HAV",
            testCourse.getCourseLocation(), "Course location should be reassigned.");
  }

  /**
  * Test reassigning the time slot.
  */
  @Test
  public void test_reassignTime() {
    testCourse.reassignTime("2:10-4:00");
    assertEquals("2:10-4:00", testCourse.getCourseTimeSlot(),
            "Course time slot should be reassigned.");
  }

  /**
  * Test setting enrolled student count to a negative value.
  */
  @Test
  public void tes_setEnrolledStudentCount_Negative() {
    testCourse.setEnrolledStudentCount(-10);
    assertEquals(0, testCourse.getEnrolledStudentCount(),
            "Enrolled student count should not be negative and should stay at 0.");
  }

  /**
  * Test setting enrolled student count to a value greater than capacity.
  */
  @Test
  public void test_setEnrolledStudentCount_AboveCapacity() {
    testCourse.setEnrolledStudentCount(300); // Setting count above capacity
    assertEquals(300, testCourse.getEnrolledStudentCount(),
            "Enrolled student count should now be 300.");
  }

  /**
   * Test toString method.
   */
  @Test
  public void test_toString() {
    String expectedResult = "\nInstructor: Griffin Newbold; Location: 417 IAB; Time: 11:40-12:55";
    assertEquals(expectedResult, testCourse.toString(), "Strings should match.");
  }
}
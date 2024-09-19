package dev.coms4156.project.individualproject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.HashMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


/**
 * RouteController Tests.
 */
public class RouteControllerTests {

  @Mock
  private MyFileDatabase myFileDatabase;

  private RouteController routeController;

  private HashMap<String, Department> departmentMapping;
  private HashMap<String, Course> courseMapping;

  private static final String DEPT_NOT_FOUND = "Department Not Found";
  private static final String COURSE_NOT_FOUND = "Course Not Found";

  /**
   * Set up for Route Controller tests.
   *
   * @throws Exception if necessary.
   */
  @BeforeEach
  void setUp() throws Exception {
    MockitoAnnotations.openMocks(this);

    routeController = new RouteController();

    // Manually setting static field for IndividualProjectApplication.myFileDatabase
    setStaticField(IndividualProjectApplication.class, "myFileDatabase", myFileDatabase);

    // Mock courses
    courseMapping = new HashMap<>();

    Course fullCourse = new Course("Adam Cannon", "417 IAB", "11:40-12:55", 400);
    fullCourse.setEnrolledStudentCount(400);
    courseMapping.put("1004", fullCourse);

    Course notFullCourse = new Course("Brian Borowski", "301 URIS", "4:10-5:25", 250);
    notFullCourse.setEnrolledStudentCount(242);
    courseMapping.put("3134", notFullCourse);

    // Mock departments
    Department comsDepartment = new Department("COMS", courseMapping, "Luca Carloni", 2700);

    HashMap<String, Course> econCourseMapping = new HashMap<>();

    Department econDepartment = new Department("ECON", econCourseMapping, "Michael Woodford", 2345);

    departmentMapping = new HashMap<>();
    departmentMapping.put("COMS", comsDepartment);
    departmentMapping.put("ECON", econDepartment);

    when(myFileDatabase.getDepartmentMapping()).thenReturn(departmentMapping);
  }

  // Helper to set static fields
  private void setStaticField(Class<?> class1, String fieldName, Object value) throws Exception {
    Field field = class1.getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(null, value);
  }

  /**
   * Test retrieving department when the department exists.
   */
  @Test
  void test_retrieveDepartment_DepartmentExists() {
    ResponseEntity<?> response = routeController.retrieveDepartment("COMS");
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(departmentMapping.get("COMS").toString(), response.getBody());
  }

  /**
   * Test retrieving department when the department does not exist.
   */
  @Test
  void test_retrieveDepartment_DepartmentNotFound() {
    ResponseEntity<?> response = routeController.retrieveDepartment("SCIENCE");
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals(DEPT_NOT_FOUND, response.getBody());
  }

  /**
   * Test retrieving course when the course does exist.
   */
  @Test
  void test_retrieveCourse_CourseExists() {
    ResponseEntity<?> response = routeController.retrieveCourse("COMS", 1004);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(
            "\nInstructor: Adam Cannon; Location: 417 IAB; Time: 11:40-12:55", response.getBody());
  }

  /**
   * Test retrieving course when the department does not exist.
   */
  @Test
  void test_retrieveCourse_DepartmentNotFound() {
    ResponseEntity<?> response = routeController.retrieveCourse("SCIENCE", 1004);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals(DEPT_NOT_FOUND, response.getBody());
  }

  /**
   * Test retrieving course when the course does not exist.
   */
  @Test
  void test_retrieveCourse_CourseNotFound() {
    ResponseEntity<?> response = routeController.retrieveCourse("COMS", 9999);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals(COURSE_NOT_FOUND, response.getBody());
  }

  /**
   * Test checking if course is full when it is full.
   */
  @Test
  void test_isCourseFull_CourseExists_IsFull() {
    ResponseEntity<?> response = routeController.isCourseFull("COMS", 1004);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(true, response.getBody());
  }

  /**
   * Test checking if course is full when it is NOT full.
   */
  @Test
  void test_isCourseFull_CourseExists_IsNotFull() {
    ResponseEntity<?> response = routeController.isCourseFull("COMS", 3134);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(false, response.getBody());
  }

  /**
   * Test checking if course is full when the course does not exist.
   */
  @Test
  void test_isCourseFull_CourseNotFound() {
    ResponseEntity<?> response = routeController.isCourseFull("COMS", 9999);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals(COURSE_NOT_FOUND, response.getBody());
  }

  /**
   * Test getting the major count from a department when the department is valid.
   */
  @Test
  void test_getMajorCtFromDept_DepartmentExists() {
    ResponseEntity<?> response = routeController.getMajorCtFromDept("COMS");
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("There are: 2700 majors in the department", response.getBody());
  }

  /**
   * Test getting major count of a department that does not exist.
   */
  @Test
  void test_getMajorCtFromDept_DepartmentDoesNotExist() {
    ResponseEntity<?> response = routeController.getMajorCtFromDept("SCIENCE");
    assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    assertEquals(DEPT_NOT_FOUND, response.getBody());
  }

  /**
   * Test identifying a department chair of a department that exists.
   */
  @Test
  void test_identifyDeptChair_DepartmentExists() {
    ResponseEntity<?> response = routeController.identifyDeptChair("COMS");
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Luca Carloni is the department chair.", response.getBody());
  }

  /**
   * Test identifying a department chair of a department that does not exist.
   */
  @Test
  void test_identifyDeptChair_DepartmentDoesNotExist() {
    ResponseEntity<?> response = routeController.identifyDeptChair("SCIENCE");
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals(DEPT_NOT_FOUND, response.getBody());
  }

  /**
   * Test finding the course location of an existing course.
   */
  @Test
  void test_findCourseLocation_CourseExists() {
    ResponseEntity<?> response = routeController.findCourseLocation("COMS", 1004);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("417 IAB is where the course is located.", response.getBody());
  }

  /**
   * Test finding the course location of a nonexistent course.
   */
  @Test
  void test_findCourseLocation_CourseDoesNotExist() {
    ResponseEntity<?> response = routeController.findCourseLocation("COMS", 9999);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals(COURSE_NOT_FOUND, response.getBody());
  }

  /**
   * Test finding the course instructor of an existing course.
   */
  @Test
  void test_findCourseInstructor_CourseExists() {
    ResponseEntity<?> response = routeController.findCourseInstructor("COMS", 1004);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Adam Cannon is the instructor for the course.", response.getBody());
  }

  /**
   * Test finding the course instructor of a nonexistent course.
   */
  @Test
  void test_findCourseInstructor_CourseDoesNotExist() {
    ResponseEntity<?> response = routeController.findCourseInstructor("COMS", 9999);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals(COURSE_NOT_FOUND, response.getBody());
  }

  /**
   * Test finding the time of an existing course.
   */
  @Test
  void test_findCourseTime_CourseExists() {
    ResponseEntity<?> response = routeController.findCourseTime("COMS", 1004);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("The course meets at: 11:40-12:55", response.getBody());
  }

  /**
   * Test finding the time on a nonexistent course.
   */
  @Test
  void test_findCourseTime_CourseDoesNotExist() {
    ResponseEntity<?> response = routeController.findCourseTime("COMS", 9999);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals(COURSE_NOT_FOUND, response.getBody());
  }

  /**
   * Test adding a major to an existing department.
   */
  @Test
  void test_addMajorToDept_DepartmentExists() {
    ResponseEntity<?> response = routeController.addMajorToDept("COMS");
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Attribute was updated successfully", response.getBody());
    assertEquals(2701, departmentMapping.get("COMS").getNumberOfMajors());
  }

  /**
   * Test adding a major to a nonexistent department.
   */
  @Test
  void test_addMajorToDept_DepartmentDoesNotExist() {
    ResponseEntity<?> response = routeController.addMajorToDept("SCIENCE");
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals(DEPT_NOT_FOUND, response.getBody());
  }

  /**
   * Test removing a major from an existing department.
   */
  @Test
  void test_removeMajorFromDept_DepartmentExists() {
    ResponseEntity<?> response = routeController.removeMajorFromDept("COMS");
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Attribute was updated or is at minimum", response.getBody());
    assertEquals(2699, departmentMapping.get("COMS").getNumberOfMajors());
  }

  /**
   * Test removing a major from a nonexistent department.
   */
  @Test
  void test_removeMajorFromDept_DepartmentDoesNotExist() {
    ResponseEntity<?> response = routeController.removeMajorFromDept("SCIENCE");
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals(DEPT_NOT_FOUND, response.getBody());
  }

  /**
   * Test dropping a student from an existing course.
   */
  @Test
  void test_dropStudent_CourseExists() {
    ResponseEntity<?> response = routeController.dropStudent("COMS", 3134);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Student has been dropped.", response.getBody());
    assertEquals(241, courseMapping.get("3134").getEnrolledStudentCount());
  }

  /**
   * Test dropping a student from a nonexistent course.
   */
  @Test
  void test_dropStudent_CourseDoesNotExist() {
    ResponseEntity<?> response = routeController.dropStudent("COMS", 9999);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals(COURSE_NOT_FOUND, response.getBody());
  }

  /**
   * Test setting enrollment count of an existing course.
   */

  @Test
  void test_setEnrollmentCount_CourseExists() {
    ResponseEntity<?> response = routeController.setEnrollmentCount("COMS", 3134, 250);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Attribute was updated successfully.", response.getBody());
    assertEquals(250, courseMapping.get("3134").getEnrolledStudentCount());
  }

  /**
   * Test setting enrollment count of a nonexistent course.
   */
  @Test
  void test_setEnrollmentCount_CourseDoesNotExist() {
    ResponseEntity<?> response = routeController.setEnrollmentCount("COMS", 9999, 50);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals(COURSE_NOT_FOUND, response.getBody());
  }

  /**
   * Test changing the course time of an existing course.
   */
  @Test
  void test_changeCourseTime_CourseExists() {
    ResponseEntity<?> response = routeController.changeCourseTime("COMS", 1004, "2:00-3:15");
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Attribute was updated successfully.", response.getBody());
    assertEquals("2:00-3:15", courseMapping.get("1004").getCourseTimeSlot());
  }

  /**
   * Test changing the course time of a nonexistent course.
   */
  @Test
  void test_changeCourseTime_CourseDoesNotExist() {
    ResponseEntity<?> response = routeController.changeCourseTime("COMS", 9999, "1:00-2:15");
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals(COURSE_NOT_FOUND, response.getBody());
  }

  /**
   * Test changing the teacher of an existing course.
   */
  @Test
  void test_changeCourseTeacher_CourseExists() {
    ResponseEntity<?> response = routeController.changeCourseTeacher("COMS", 1004, "Gail Kaiser");
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Attribute was updated successfully.", response.getBody());
    assertEquals("Gail Kaiser", courseMapping.get("1004").getInstructorName());
  }

  /**
   * Test changing the time of a nonexistent course.
   */
  @Test
  void test_changeCourseTeacher_CourseDoesNotExist() {
    ResponseEntity<?> response = routeController.changeCourseTeacher("COMS", 9999, "Gail Kaiser");
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals(COURSE_NOT_FOUND, response.getBody());
  }

  /**
   * Test changing the location of an existing course.
   */
  @Test
  void test_changeCourseLocation_CourseExists() {
    ResponseEntity<?> response = routeController.changeCourseLocation("COMS", 1004, "309 HAV");
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Attribute was updated successfully.", response.getBody());
    assertEquals("309 HAV", courseMapping.get("1004").getCourseLocation());
  }

  /**
   * Test changing the location of a nonexistent course.
   */
  @Test
  void test_changeCourseLocation_CourseDoesNotExist() {
    ResponseEntity<?> response = routeController.changeCourseLocation("COMS", 9999, "309 HAV");
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals(COURSE_NOT_FOUND, response.getBody());
  }
}

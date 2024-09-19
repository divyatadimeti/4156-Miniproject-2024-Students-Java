package dev.coms4156.project.individualproject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * MyFileDatabase Tests.
 */
public class MyFileDatabaseTests {

  private MyFileDatabase myFileDatabase;
  private File tempFile;

  /**
   * Set up.
   *
   * @throws IOException thrown if necessary.
   */
  @BeforeEach
  public void setUp() throws IOException {
    tempFile = File.createTempFile("testfile", ".db");
    tempFile.deleteOnExit();
    myFileDatabase = new MyFileDatabase(0, tempFile.getAbsolutePath());
  }

  @AfterEach
  public void deleteTempFile() {
    tempFile.delete();
  }

  /**
   * Test when non-zero flag is provided.
   */
  @Test
  public void test_NonzeroFlag() {
    Exception thrown = assertThrows(
            IllegalArgumentException.class,
            () -> new MyFileDatabase(1, tempFile.getAbsolutePath()),
            "Expected MyFileDatabase to throw error but it did not."
    );

    assertEquals("Flag must be zero.", thrown.getMessage());
  }

  /**
   * Test setting department mapping.
   */
  @Test
  public void test_setMapping() {
    HashMap<String, Course> courses = new HashMap<>();
    Course course = new Course("Adam Cannon",
            "417 IAB", "11:40-12:55", 400);
    courses.put("coms1004", course);

    Department department = new Department("COMS", courses,
            "Luca Carloni", 2700);
    HashMap<String, Department> departments = new HashMap<>();
    departments.put("COMS", department);
    myFileDatabase.setMapping(departments);

    assertEquals(departments, myFileDatabase.getDepartmentMapping());
  }


  /**
   * Test serialization and deserialization of deparments.
   */
  @Test
  public void test_serialize_deserialize() {
    HashMap<String, Course> courses = new HashMap<>();
    Course course = new Course("Waseem Noor",
            "309 HAV", "2:40-3:55", 210);
    courses.put("econ1105", course);

    Department department = new Department("ECON",
            courses, "Michael Woodford", 2345);
    HashMap<String, Department> departments = new HashMap<>();
    departments.put("ECON", department);
    myFileDatabase.setMapping(departments);

    myFileDatabase.saveContentsToFile();

    MyFileDatabase newDatabase = new MyFileDatabase(0, tempFile.getAbsolutePath());
    HashMap<String, Department> deserializedDepartments = newDatabase.getDepartmentMapping();

    assertNotNull(deserializedDepartments, "Deserialized depts shouldn't be null.");
    assertEquals(departments.size(), deserializedDepartments.size(),
            "Dept size should match.");

    Department deserializedDepartment = deserializedDepartments.get("ECON");
    assertNotNull(deserializedDepartment, "Deserialized dept shouldn't be null.");
    assertEquals("Michael Woodford", deserializedDepartment.getDepartmentChair(),
            "Dept chair should match.");
    assertEquals(2345, deserializedDepartment.getNumberOfMajors(),
            "Num majors should match.");
  }

  /**
   * Test saving contents to a file with an empty mapping.
   */
  @Test
  public void test_saveContentsToFile_EmptyMapping() {
    myFileDatabase.setMapping(new HashMap<>());
    myFileDatabase.saveContentsToFile();

    // Check that file not empty
    assertTrue(tempFile.length() > 0);
  }

  /**
   * Test department mapping with invalid file path.
   */
  @Test
  public void test_getDeptMapping_FilePathHandling() {
    MyFileDatabase databaseWithInvalidPath = new MyFileDatabase(0, "/invalid/path/to/file");
    assertEquals(new HashMap<>(), databaseWithInvalidPath.getDepartmentMapping(),
            "Dept mapping should be empty hashmap for invalid file path.");
  }


  /**
   * Test toString method.
   */
  @Test
  public void test_toString() {
    HashMap<String, Course> courses = new HashMap<>();
    Course course = new Course("Uday Menon",
            "627 MUDD", "11:40-12:55", 50);
    courses.put("ieor2500", course);

    Department department = new Department("IEOR", courses,
            "Jay Sethuraman", 67);
    HashMap<String, Department> departments = new HashMap<>();
    departments.put("IEOR", department);
    myFileDatabase.setMapping(departments);

    String expected = "For the IEOR department: " + "\nIEOR ieor2500: " + course.toString() + "\n";
    assertEquals(expected, myFileDatabase.toString());
  }
}
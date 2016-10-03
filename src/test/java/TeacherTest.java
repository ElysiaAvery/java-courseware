import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;

public class TeacherTest {
  private Teacher teacher1;
  private Teacher teacher2;

  @Before
  public void setUp() {
    DB.sql2o = new Sql2o("jdbc:postgresql://localhost:5432/courseware_test", null, null);
    teacher1 = new Teacher("Brian");
    teacher2 = new Teacher("Karen");
  }

  @Test
  public void Teacher_instantiates_true() {
    assertEquals(true, teacher1 instanceof Teacher);
  }

  @Test
  public void save_returnsId_true() {
    assertTrue(teacher1.getId()>1);
  }

  @Test
  public void getName_returnsCorrectName_true() {
    assertEquals("Brian", teacher1.getName());
  }

  @Test
  public void setName_updatesNameInDatabase_true() {
    teacher1.setName("Todd");
    assertEquals("Todd", teacher1.getName());
  }

  @Test
  public void find_returnsCorrectTeacher_true() {
    assertTrue(Teacher.find(teacher1.getId()).equals(teacher1));
  }

  @Test
  public void all_returnsAllInstances_true() {
    assertTrue(Teacher.all().size()>1);
  }

  @Test
  public void delete_deletesCorrectTeacher_null() {
    int tempId = teacher2.getId();
    teacher2.delete();
    assertEquals(null, Teacher.find(tempId));
  }

  @After
  public void tearDown() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "DELETE FROM teachers *;";
      con.createQuery(sql).executeUpdate();
      sql = "DELETE FROM users *;";
      con.createQuery(sql).executeUpdate();
    }
  }
}
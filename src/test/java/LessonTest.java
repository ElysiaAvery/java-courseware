import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;

public class LessonTest {
  Lesson testLesson;

  @Rule
    public DatabaseRule database = new DatabaseRule();

  @Before
  public void setUp() {
    testLesson = new Lesson("Basket Weaving With Reeds", "I <3 reeds by Reed Reedchards, chapters 1-5", "lorem reedsum", 1);
  }

  @Test
  public void Lesson_instantiates_true() {
    assertEquals(true, testLesson instanceof Lesson);
  }

  @Test
  public void equals_returnsTrueIfPropertiesAreSame_true(){
    Lesson testLesson2 = new Lesson("Basket Weaving With Reeds", "I <3 reeds by Reed Reedchards, chapters 1-5", "lorem reedsum", 1);
    assertTrue(testLesson.equals(testLesson2));
  }


  @Test
  public void save_insertsLessonIntoDatabase_Lesson() {
    testLesson.save();
    Lesson testLesson2 = null;
    try(Connection con = DB.sql2o.open()){
      testLesson2 = con.createQuery("SELECT * FROM lessons WHERE name='Basket Weaving With Reeds'")
                       .executeAndFetchFirst(Lesson.class);
    }
    assertTrue(testLesson2.equals(testLesson));
  }

  @Test
  public void find_returnsLessonWithSameId_secondLesson() {
    testLesson.save();
    Lesson testLesson2 = new Lesson("Basket Weaving With Palm Fronds", "Fronds Are Your Friends, by Palm Palmerson chapter 7", "palms palms palms palmitty palms", 1);
    testLesson2.save();
    assertEquals(Lesson.find(testLesson2.getId()), testLesson2);
  }

  @Test(expected=IndexOutOfBoundsException.class)
  public void find_throwsExceptionIfLessonNotFound() {
    Lesson.find(1);
  }

  @Test
  public void all_returnsAllInstancesOfLesson_true() {
    testLesson.save();
    Lesson testLesson2 = new Lesson("Basket Weaving With Palm Fronds", "Fronds Are Your Friends, by Palm Palmerson chapter 7", "palms palms palms palmitty palms", 1);
    testLesson2.save();
    assertEquals(true, Lesson.all().get(0).equals(testLesson));
    assertEquals(true, Lesson.all().get(1).equals(testLesson2));
  }

  @Test public void getUnfinishedAssigments_returnsListOfUnsubmittedAssignments_ArrayList() {
    testLesson.save();
    Student student = new Student("Selena");
    Assignment testAssignment = new Assignment("Weave a Basket", "Step one, weave basket. Take a picture, and turn it in.", testLesson.getId());
    testAssignment.save();
    Assignment testAssignment2 = new Assignment("Weave a Basket", "Here's my basket, hope it's the best.", testLesson.getId(), student.getId());
    testAssignment2.turnIn();
    assertTrue(testLesson.getUnfinishedAssigments().get(0).equals(testAssignment));
    assertFalse(testLesson.getUnfinishedAssigments().contains(testAssignment2));
  }

  @Test public void getFinishedAssigments_returnsListOfSubmittedAssignments_ArrayList() {
    testLesson.save();
    Student student = new Student("Selena");
    Assignment testAssignment = new Assignment("Weave a Basket", "Step one, weave basket. Take a picture, and turn it in.", testLesson.getId());
    testAssignment.save();
    Assignment testAssignment2 = new Assignment("Weave a Basket", "Here's my basket, hope it's the best.", testLesson.getId(), student.getId());
    testAssignment2.turnIn();
    assertFalse(testLesson.getFinishedAssigments().get(0).equals(testAssignment));
    assertTrue(testLesson.getFinishedAssigments().contains(testAssignment2));
  }
}
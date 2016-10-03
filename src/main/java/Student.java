import org.sql2o.*;
import java.util.List;

public class Student {

  private String name;
  private int id;
  private int user_id;

  public Student (String name){
    this.name = name;
    this.save();
  }

  public int getId () {
    return id;
  }

  public String getName () {
    return name;
  }

  public void setName (String name) {
     this.name = name;
  }

  public static List<Student> all() {
    try(Connection con =DB.sql2o.open()) {
      String sql = "SELECT * FROM users INNER JOIN students ON (students.user_id = users.id)";
      return con.createQuery(sql)
        .executeAndFetch(Student.class);
    }
  }

  public static Student find(int id) {
  String sql = "SELECT * FROM users INNER JOIN students ON (students.user_id = users.id) WHERE students.id = :id";
  try(Connection con = DB.sql2o.open()) {
    return con.createQuery(sql)
      .addParameter("id", id)
      .executeAndFetchFirst(Student.class);
    }
  }

  public void delete() {
      String sql = "DELETE FROM students WHERE id=:id";
      try(Connection con = DB.sql2o.open()){
        con.createQuery(sql)
        .addParameter("id", id)
        .executeUpdate();
      }
    }

  public void save() {
    String sql = "INSERT INTO users (name) VALUES (:name)";
    try(Connection con = DB.sql2o.open()) {
      this.user_id = (int) con.createQuery (sql, true)
      .addParameter("name", name)
      .executeUpdate()
      .getKey();
      sql = "INSERT INTO students (user_id) VALUES (:user_id)";
      this.id = (int) con.createQuery (sql, true)
      .addParameter("user_id", user_id)
      .executeUpdate()
      .getKey();
    }
  }

  @Override
  public boolean equals(Object otherStudent) {
    if (!(otherStudent instanceof Student)) {
      return false;
    } else {
      Student newStudent = (Student) otherStudent;
      return this.getId() == newStudent.getId();
    }
  }
}
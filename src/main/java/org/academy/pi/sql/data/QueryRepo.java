package org.academy.pi.sql.data;

import java.util.List;
import lombok.experimental.UtilityClass;
import org.academy.pi.sql.models.QueryCategory;
import org.academy.pi.sql.models.SqlNamedQuery;

@UtilityClass
public class QueryRepo {

  private static final List<SqlNamedQuery> SAMPLE_QUERIES = List.of(
      // SELECT QUERIES
      SqlNamedQuery.builder()
          .title("Find All")
          .query("SELECT * FROM STUDENTS;")
          .category(QueryCategory.SELECT_QUERIES)
          .build(),
      SqlNamedQuery.builder()
          .title("Find By Name")
          .query("SELECT USERNAME, FIRST_NAME, LAST_NAME FROM STUDENTS WHERE FIRST_NAME = 'Nanny';")
          .category(QueryCategory.SELECT_QUERIES)
          .build(),
      SqlNamedQuery.builder()
          .title("Find Like Name")
          .query("""
          SELECT USERNAME, FIRST_NAME, LAST_NAME, EMAIL FROM STUDENTS
          WHERE FIRST_NAME LIKE 'Nan%';
          """)
          .category(QueryCategory.SELECT_QUERIES)
          .build(),
      SqlNamedQuery.builder()
          .title("Find Distinct Values")
          .query("SELECT DISTINCT IS_GOOD FROM STUDENTS;")
          .category(QueryCategory.SELECT_QUERIES)
          .build(),
      SqlNamedQuery.builder()
          .title("Order By Name Ascending")
          .query("""
           SELECT FIRST_NAME, LAST_NAME, EMAIL FROM STUDENTS
           ORDER BY LAST_NAME ASC
           LIMIT 15;
           """)
          .category(QueryCategory.SELECT_QUERIES)
          .build(),
      SqlNamedQuery.builder()
          .title("Order By Multiple Columns")
          .query("""
           SELECT FIRST_NAME, LAST_NAME, SHIRT_SIZE FROM STUDENTS
           WHERE SHIRT_SIZE IS NOT NULL
           ORDER BY SHIRT_SIZE ASC, LAST_NAME DESC
           LIMIT 20;
           """)
          .category(QueryCategory.SELECT_QUERIES)
          .build(),
      SqlNamedQuery.builder()
          .title("Find With AND Condition")
          .query("""
           SELECT FIRST_NAME, LAST_NAME, GENDER, IS_GOOD FROM STUDENTS
           WHERE GENDER = 'Female' AND IS_GOOD = 'Daily'
           LIMIT 10;
           """)
          .category(QueryCategory.SELECT_QUERIES)
          .build(),
      SqlNamedQuery.builder()
          .title("Find With OR Condition")
          .query("""
           SELECT FIRST_NAME, LAST_NAME, SHIRT_SIZE FROM STUDENTS
           WHERE SHIRT_SIZE = 'XS' OR SHIRT_SIZE = 'XL'
           ORDER BY SHIRT_SIZE, LAST_NAME
           LIMIT 15;
           """)
          .category(QueryCategory.SELECT_QUERIES)
          .build(),
      SqlNamedQuery.builder()
          .title("Find With IN Clause")
          .query("""
           SELECT FIRST_NAME, LAST_NAME, IS_GOOD FROM STUDENTS
           WHERE IS_GOOD IN ('Daily', 'Weekly', 'Monthly')
           ORDER BY IS_GOOD, LAST_NAME
           LIMIT 20;
           """)
          .category(QueryCategory.SELECT_QUERIES)
          .build(),
      SqlNamedQuery.builder()
          .title("Find NULL Values")
          .query("""
           SELECT USERNAME, FIRST_NAME, LAST_NAME, EMAIL FROM STUDENTS
           WHERE EMAIL IS NULL
           LIMIT 10;
           """)
          .category(QueryCategory.SELECT_QUERIES)
          .build(),
      SqlNamedQuery.builder()
          .title("Find NOT NULL With Filter")
          .query("""
           SELECT USERNAME, FIRST_NAME, LAST_NAME, SHIRT_SIZE FROM STUDENTS
           WHERE SHIRT_SIZE IS NOT NULL AND GENDER = 'Male'
           ORDER BY SHIRT_SIZE
           LIMIT 15;
           """)
          .category(QueryCategory.SELECT_QUERIES)
          .build(),
      SqlNamedQuery.builder()
          .title("Combined AND and OR")
          .query("""
           SELECT FIRST_NAME, LAST_NAME, GENDER, IS_GOOD FROM STUDENTS
           WHERE (GENDER = 'Female' OR GENDER = 'Male')
           AND IS_GOOD = 'Daily'
           ORDER BY LAST_NAME
           LIMIT 15;
           """)
          .category(QueryCategory.SELECT_QUERIES)
          .build(),
      SqlNamedQuery.builder()
          .title("Count All")
          .query("SELECT COUNT(*) AS TOTAL_STUDENTS FROM STUDENTS;")
          .category(QueryCategory.SELECT_QUERIES)
          .build(),
      SqlNamedQuery.builder()
          .title("Count With Shirt Size")
          .query("""
          SELECT COUNT(*) AS STUDENTS_WITH_SHIRT_SIZE FROM STUDENTS
          WHERE SHIRT_SIZE IS NOT NULL;
          """)
          .category(QueryCategory.SELECT_QUERIES)
          .build(),
      SqlNamedQuery.builder()
          .title("Count By Group")
          .query("""
          SELECT
            IS_GOOD, COUNT(*) AS GOOD_STUDENTS
          FROM STUDENTS
          GROUP BY IS_GOOD
          ORDER BY GOOD_STUDENTS DESC;
          """)
          .category(QueryCategory.SELECT_QUERIES)
          .build(),
      SqlNamedQuery.builder()
          .title("Count By 2 Groups")
          .query("""
          SELECT
            SHIRT_SIZE, IS_GOOD, COUNT(*) AS GOOD_STUDENTS_WITH_SHIRT_SIZE
          FROM STUDENTS
          WHERE SHIRT_SIZE IS NOT NULL
          GROUP BY SHIRT_SIZE, IS_GOOD
          ORDER BY SHIRT_SIZE ASC, IS_GOOD DESC;
          """)
          .category(QueryCategory.SELECT_QUERIES)
          .build(),
      SqlNamedQuery.builder()
          .title("Count Grouped With HAVING")
          .query("""
           SELECT
             SHIRT_SIZE, COUNT(*) AS STUDENT_COUNT
           FROM STUDENTS
           WHERE SHIRT_SIZE IS NOT NULL
           GROUP BY SHIRT_SIZE
           HAVING COUNT(*) > 50
           ORDER BY STUDENT_COUNT DESC;
           """)
          .category(QueryCategory.SELECT_QUERIES)
          .build(),
      SqlNamedQuery.builder()
          .title("Average, Min, Max Examples")
          .query("""
           SELECT
             COUNT(*) AS TOTAL,
             COUNT(SHIRT_SIZE) AS WITH_SIZE,
             COUNT(DISTINCT SHIRT_SIZE) AS UNIQUE_SIZES
           FROM STUDENTS;
           """)
          .category(QueryCategory.SELECT_QUERIES)
          .build(),
      SqlNamedQuery.builder()
          .title("Count By Category")
          .query("""
           SELECT
             F.CATEGORY, COUNT(*) AS FAVORITES_COUNT
           FROM FAVORITES F
           GROUP BY F.CATEGORY
           ORDER BY FAVORITES_COUNT DESC;
           """)
          .category(QueryCategory.SELECT_QUERIES)
          .build(),
      SqlNamedQuery.builder()
          .title("Popular Favorite Colors")
          .query("""
           SELECT
             F.FAVORITE AS COLOR, COUNT(*) AS STUDENT_COUNT
           FROM FAVORITES F
           WHERE F.CATEGORY = 'COLOR'
           GROUP BY F.FAVORITE
           ORDER BY STUDENT_COUNT DESC
           LIMIT 10;
           """)
          .category(QueryCategory.SELECT_QUERIES)
          .build(),
      SqlNamedQuery.builder()
          .title("Join Favorites Filter Name")
          .query("""
           SELECT
             S.ID, S.FIRST_NAME, F.CATEGORY, F.FAVORITE
           FROM STUDENTS S
           JOIN FAVORITES F ON S.ID = F.STUDENT_ID
           WHERE S.FIRST_NAME = 'Thor';
           """)
          .category(QueryCategory.SELECT_QUERIES)
          .build(),
      SqlNamedQuery.builder()
          .title("Join Favorites Filter Color w/ Limit")
          .query("""
           SELECT
             S.ID, S.FIRST_NAME, F.CATEGORY, F.FAVORITE
           FROM STUDENTS S
           JOIN FAVORITES F ON S.ID = F.STUDENT_ID
           WHERE F.CATEGORY = 'COLOR'
           AND F.FAVORITE = 'Fuscia'
           ORDER BY S.FIRST_NAME
           LIMIT 10;
           """)
          .category(QueryCategory.SELECT_QUERIES)
          .build(),
      SqlNamedQuery.builder()
          .title("Left Join All Students")
          .query("""
           SELECT
             S.ID, S.FIRST_NAME, S.LAST_NAME, F.CATEGORY, F.FAVORITE
           FROM STUDENTS S
           LEFT JOIN FAVORITES F ON S.ID = F.STUDENT_ID
           WHERE S.FIRST_NAME = 'Thor'
           ORDER BY F.CATEGORY;
           """)
          .category(QueryCategory.SELECT_QUERIES)
          .build(),
      SqlNamedQuery.builder()
          .title("Students Without Favorites")
          .query("""
           SELECT
             S.ID, S.FIRST_NAME, S.LAST_NAME
           FROM STUDENTS S
           LEFT JOIN FAVORITES F ON S.ID = F.STUDENT_ID
           WHERE F.STUDENT_ID IS NULL
           LIMIT 10;
           """)
          .category(QueryCategory.SELECT_QUERIES)
          .build(),

      // INSERT/UPDATE/DELETE QUERIES
      SqlNamedQuery.builder()
          .title("Insert New Student")
          .query("""
           INSERT INTO STUDENTS (ID, USERNAME, FIRST_NAME, LAST_NAME, EMAIL, GENDER)
           VALUES ('new-student-id-123', 'jdoe123', 'John', 'Doe', 'jdoe@example.com', 'Male');
           """)
          .category(QueryCategory.INSERT_UPDATE_DELETE)
          .build(),
      SqlNamedQuery.builder()
          .title("Insert Multiple Students")
          .query("""
           INSERT INTO STUDENTS (ID, USERNAME, FIRST_NAME, LAST_NAME, EMAIL, GENDER)
           VALUES
             ('student-id-001', 'asmith', 'Alice', 'Smith', 'asmith@example.com', 'Female'),
             ('student-id-002', 'bjones', 'Bob', 'Jones', 'bjones@example.com', 'Male');
           """)
          .category(QueryCategory.INSERT_UPDATE_DELETE)
          .build(),
      SqlNamedQuery.builder()
          .title("Update By Name")
          .query("""
           UPDATE STUDENTS SET FIRST_NAME = 'NotNanny'
           WHERE FIRST_NAME = 'Nanny';
           """)
          .category(QueryCategory.INSERT_UPDATE_DELETE)
          .build(),
      SqlNamedQuery.builder()
          .title("Update Multiple Fields")
          .query("""
           UPDATE STUDENTS SET
             USERNAME = 'new_nserot0',
             FIRST_NAME = 'new_Nanny',
             LAST_NAME = 'new_Serot'
           WHERE FIRST_NAME = 'Nanny';
           """)
          .category(QueryCategory.INSERT_UPDATE_DELETE)
          .build(),
      SqlNamedQuery.builder()
          .title("Delete Record(s)")
          .query("""
           DELETE FROM STUDENTS WHERE FIRST_NAME = 'Nanny';
           """)
          .category(QueryCategory.INSERT_UPDATE_DELETE)
          .build(),
      SqlNamedQuery.builder()
          .title("Delete With Subquery (FK Safe)")
          .query("""
           DELETE FROM FAVORITES
           WHERE STUDENT_ID IN (
             SELECT ID FROM STUDENTS WHERE FIRST_NAME = 'Nanny'
           );
           """)
          .category(QueryCategory.INSERT_UPDATE_DELETE)
          .build(),
      SqlNamedQuery.builder()
          .title("Delete Specific Student")
          .query("""
           DELETE FROM STUDENTS
           WHERE ID = 'new-student-id-123';
           """)
          .category(QueryCategory.INSERT_UPDATE_DELETE)
          .build(),

      // TABLE OPERATIONS (DDL)
      SqlNamedQuery.builder()
          .title("Create New Table")
          .query("""
           CREATE TABLE PROJECTS (
             ID VARCHAR(40) PRIMARY KEY,
             STUDENT_ID VARCHAR(40),
             PROJECT_NAME VARCHAR(100),
             GRADE VARCHAR(10),
             COMPLETED_DATE TIMESTAMP,
             FOREIGN KEY (STUDENT_ID) REFERENCES STUDENTS(ID)
           );
           """)
          .category(QueryCategory.TABLE_OPERATIONS)
          .build(),
      SqlNamedQuery.builder()
          .title("Create Simple Table")
          .query("""
           CREATE TABLE ASSIGNMENTS (
             ID INT PRIMARY KEY,
             TITLE VARCHAR(200),
             DUE_DATE DATE,
             POINTS INT
           );
           """)
          .category(QueryCategory.TABLE_OPERATIONS)
          .build(),
      SqlNamedQuery.builder()
          .title("Drop Table")
          .query("""
           DROP TABLE IF EXISTS PROJECTS;
           """)
          .category(QueryCategory.TABLE_OPERATIONS)
          .build(),
      SqlNamedQuery.builder()
          .title("Delete All Records (Like Truncate)")
          .query("""
           DELETE FROM FAVORITES;
           """)
          .category(QueryCategory.TABLE_OPERATIONS)
          .build(),
      SqlNamedQuery.builder()
          .title("Alter Table Add Column")
          .query("""
           ALTER TABLE STUDENTS
           ADD COLUMN GRADE_LEVEL VARCHAR(10);
           """)
          .category(QueryCategory.TABLE_OPERATIONS)
          .build(),
      SqlNamedQuery.builder()
          .title("Alter Table Drop Column")
          .query("""
           ALTER TABLE STUDENTS
           DROP COLUMN GRADE_LEVEL;
           """)
          .category(QueryCategory.TABLE_OPERATIONS)
          .build(),
      SqlNamedQuery.builder()
          .title("Show All Tables")
          .query("""
           SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES
           WHERE TABLE_SCHEMA = 'PUBLIC';
           """)
          .category(QueryCategory.TABLE_OPERATIONS)
          .build(),
      SqlNamedQuery.builder()
          .title("Show Table Columns")
          .query("""
           SELECT COLUMN_NAME, DATA_TYPE, IS_NULLABLE
           FROM INFORMATION_SCHEMA.COLUMNS
           WHERE TABLE_NAME = 'STUDENTS';
           """)
          .category(QueryCategory.TABLE_OPERATIONS)
          .build()
  );

  public static List<String> getTableNames() {
    return List.of("favorites", "students");
  }

  public static List<SqlNamedQuery> getSampleQueries() {
    return SAMPLE_QUERIES;
  }
}

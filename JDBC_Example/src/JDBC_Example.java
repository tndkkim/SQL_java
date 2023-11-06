import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
//import java.sql.Statement;
import java.util.Scanner;

public class JDBC_Example{
	static final String DB_URL = "jdbc:mysql://localhost/database?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
	static final String USER = "root"; // user name
	static final String PASS = "2264826"; // user password
	//static final String QUERY = "select * from student where dept_name = 'History'"; // input query
	
	public static void main(String[] args) {
		
		Connection conn = null;
		PreparedStatement studentStmt = null;
		PreparedStatement sectionStmt = null;
		ResultSet studentRs = null;
		ResultSet sectionRs = null;
		
		try {
			conn = DriverManager.getConnection(DB_URL, USER, PASS); 
			
			String studentQuery = "SELECT ID, name, dept_name, tot_cred FROM student WHERE ID = ?";
			studentStmt= conn.prepareStatement(studentQuery);

			
			Scanner scanner = new Scanner(System.in);
			System.out.print("Enter ID: ");
			String stdID = scanner.nextLine();
			scanner.close();
			
			studentStmt.setString(1, stdID);
			studentRs = studentStmt.executeQuery();
			
			
			String courseQuery = "SELECT s.course_id, c.title, s.sec_id, s.year, s.semester, t.grade "+
								"FROM takes t "+
								"NATURAL JOIN section s "+
								"NATURAL JOIN course c "+
								"WHERE t.ID = ?";			
			sectionStmt = conn.prepareStatement(courseQuery);
			
			sectionStmt.setString(1, stdID);
            sectionRs = sectionStmt.executeQuery();
			
			
			if (studentRs.next()) {
				System.out.println();
				System.out.println("-Student Basic Info-");
				System.out.println("Student ID: " + studentRs.getString("ID"));
                System.out.println("Student Name: " + studentRs.getString("name"));
                System.out.println("Department: " + studentRs.getString("dept_name"));
                System.out.println("Total Credits: " + studentRs.getInt("tot_cred"));
                System.out.println();
                System.out.println("-Student Grade Info-");

                
                
                while (sectionRs.next()) {
                	System.out.println("Course ID: " + sectionRs.getString("course_id"));
                    System.out.println("Course Title: " + sectionRs.getString("title"));
                    System.out.println("Section ID: " + sectionRs.getString("sec_id"));
                    System.out.println("Year: " + sectionRs.getInt("year"));
                    System.out.println("Semester: " + sectionRs.getString("semester"));
                    System.out.println("Grade: " + sectionRs.getString("grade"));
                    System.out.println();
                	
                }
                
                
                
			} else {
				System.out.println("There is no ID " + stdID);
			}
			
			
			
			
			} catch (SQLException e) {
				System.out.println("SQLException: " + e);
				} finally {
					try {
						studentRs.close();
						sectionRs.close();
						studentStmt.close();
						sectionStmt.close();
						conn.close();
					} catch (SQLException e) {
						e.printStackTrace();
						}
					}
		}

	}

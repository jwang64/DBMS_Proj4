import java.sql.*;
import java.util.*;
import java.io.*;

public class MySQLConnector
{
	private static final int STUDENT_TABLE = 0;
	private static final int PROFESSOR_TABLE = 1;
	private static final int COURSE_TABLE = 2;
	private static final int TEACHING_TABLE = 3;
	private static final int TRANSCRIPT_TABLE = 4;

	private static Connection connection;
	private static Comparable[][][] data;
	private static PreparedStatement statement;
	
	
	private static void createTables()
	{
		TupleGenerator test = new TupleGeneratorImpl ();
        test.addRelSchema ("Student",
                           "id name address status",
                           "Integer String String String",
                           "id",
                           null);
        
        test.addRelSchema ("Professor",
                           "id name deptId",
                           "Integer String String",
                           "id",
                           null);
        
        test.addRelSchema ("Course",
                           "crsCode deptId crsName descr",
                           "String String String String",
                           "crsCode",
                           null);
        
        test.addRelSchema ("Teaching",
                           "crsCode semester profId",
                           "String String Integer",
                           "crcCode semester",
                           new String [][] {{ "profId", "Professor", "id" },
                                            { "crsCode", "Course", "crsCode" }});
        
        test.addRelSchema ("Transcript",
                           "studId crsCode semester grade",
                           "Integer String String String",
                           "studId crsCode semester",
                           new String [][] {{ "studId", "Student", "id"},
                                            { "crsCode", "Course", "crsCode" },
                                            { "crsCode semester", "Teaching", "crsCode semester" }});
        
        int tups [] = new int [] { 10000, 10000, 10000, 10000, 1000 };
        
        data = test.generate(tups);
	}
	
	private static void createConnection()
	{
		Properties databaseProperties = new Properties();
		try {
			File config = new File("config.properties");
			FileInputStream fileInput = new FileInputStream(config);
			databaseProperties.load(fileInput);
			fileInput.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String databaseUrl = databaseProperties.getProperty("jdbc.url");
		String databaseDriver = databaseProperties.getProperty("jdbc.driver");
		String databaseUsername = databaseProperties.getProperty("jdbc.username");
		String databasePassword = databaseProperties.getProperty("jdbc.password");
		
		try {
			Class.forName(databaseDriver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		try {
			connection = DriverManager.getConnection(databaseUrl, databaseUsername, databasePassword);
			System.out.println("Connected!");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	private static void createAndPopulateStudentTables() throws SQLException
	{
		System.out.println("Student Table being created");
		connection.prepareStatement("CREATE TABLE IF NOT EXISTS student ("
				+ "id integer PRIMARY KEY,"
				+ "name varchar(64),"
				+ "address varchar(128),"
				+ "status varchar(16)"
				+ ");").execute();
		Comparable[][] sTable = data[STUDENT_TABLE];
		int studentId;
		String studentName;
		String studentAddress;
		String studentStatus;
		for(int i = 0 ; i< sTable.length; i++)
		{
			statement = connection.prepareStatement("INSERT INTO student (id, name, address, status) "
					+ "VALUES (?, ?, ?, ?);");
			Comparable[] studentTuple;
			studentTuple = sTable[i];
			studentId = (int) studentTuple[0];
			studentName = (String) studentTuple[1];
			studentAddress = (String) studentTuple[2];
			studentStatus = (String) studentTuple[3];
			statement.setInt(1, studentId);
			statement.setString(2, studentName);
			statement.setString(3, studentAddress);
			statement.setString(4, studentStatus);
			
			statement.executeUpdate();
			
		}
		System.out.println("Student Table finished");
		}
	

	private static void createAndPopulateProfessorTables() throws SQLException
	{
		System.out.println("Professor Table being created");
		connection.prepareStatement("CREATE TABLE IF NOT EXISTS professor ("
				+ "id integer PRIMARY KEY,"
				+ "name varchar(64),"
				+ "deptId varchar(16)"
				+ ");").execute();
		Comparable[][] pTable = data[PROFESSOR_TABLE];
		int professorId;
		String professorName;
		String professorDeptId;
		
		for(int i = 0 ; i< pTable.length; i++)
		{
			statement = connection.prepareStatement("INSERT INTO professor (id, name, deptId) "
					+ "VALUES (?, ?, ?);");
			Comparable[] professorTuple;
			professorTuple = pTable[i];
			professorId = (int) professorTuple[0];
			professorName = (String) professorTuple[1];
			professorDeptId = (String) professorTuple[2];
			statement.setInt(1, professorId);
			statement.setString(2, professorName);
			statement.setString(3, professorDeptId);
			
			statement.executeUpdate();
			
		}
		System.out.println("Professor Table Finished");
	}
	
	private static void createAndPopulateCourseTables() throws SQLException
	{
		System.out.println("Course Table being created");
		connection.prepareStatement("CREATE TABLE IF NOT EXISTS course ("
				+ "crsCode varchar(64) PRIMARY KEY,"
				+ "deptId varchar(128),"
				+ "crsName varchar(128),"
				+ "descr varchar(256)"
				+ ");").execute();
		Comparable[][] cTable = data[COURSE_TABLE];
		String crsCode;
		String deptId;
		String crsName;
		String descr;
		
		for(int i = 0 ; i< cTable.length; i++)
		{
			statement = connection.prepareStatement("INSERT INTO course (crsCode, deptId, crsName, descr) "
					+ "VALUES (?, ?, ?, ?);");
			
			Comparable[] courseTuple;
			courseTuple = cTable[i];
			crsCode = (String) courseTuple[0];
			deptId = (String) courseTuple[1];
			crsName = (String) courseTuple[2];
			descr = (String) courseTuple[3];
			statement.setString(1, crsCode);
			statement.setString(2, deptId);
			statement.setString(3, crsName);
			statement.setString(4, descr);
			
			statement.executeUpdate();
			
		}
		System.out.println("Course Table Finished");
	}
	
	private static void createAndPopulateTeachingTables() throws SQLException
	{
		System.out.println("Teaching Table being created");
		connection.prepareStatement("CREATE TABLE IF NOT EXISTS teaching ("
				+ "crsCode varchar(16),"
				+ "semester varchar(16),"
				+ "profId integer,"
				+ "CONSTRAINT crsId PRIMARY KEY (crsCode,semester),"
				+ "CONSTRAINT prof FOREIGN KEY (profId) REFERENCES Professor (id),"
				+ "CONSTRAINT crs FOREIGN KEY (crsCode) REFERENCES Course (crsCode)"
				+ ");").execute();
		Comparable[][] tTable = data[TEACHING_TABLE];
		String crsCode;
		String semester;
		int profId;

		
		for(int i = 0 ; i< tTable.length; i++)
		{
			statement = connection.prepareStatement("INSERT INTO teaching (crsCode, semester, profId) "
					+ "VALUES (?, ?, ?);");

			Comparable[] teachingTuple;
			teachingTuple = tTable[i];
			crsCode = (String) teachingTuple[0];
			semester = (String) teachingTuple[1];
			profId = (int) teachingTuple[2];
			statement.setString(1, crsCode);
			statement.setString(2, semester);
			statement.setInt(3, profId);
			
			statement.executeUpdate();
			
		}
		System.out.println("Teaching Table Finished");
	}
	
	private static void createAndPopulateTranscriptTables() throws SQLException
	{
		System.out.println("Transcript Table being created");
		connection.prepareStatement("CREATE TABLE IF NOT EXISTS transcript ("
				+ "studId integer,"
				+ "crsCode varchar(16),"
				+ "semester varchar(16),"
				+ "grade varchar(16),"
				+ "CONSTRAINT trs_grad PRIMARY KEY (studId,crsCode,semester),"
				+ "CONSTRAINT trs_stud FOREIGN KEY (studId) REFERENCES Student (id),"
				+ "CONSTRAINT trs_crs FOREIGN KEY (crsCode) REFERENCES Course (crsCode),"
				+ "CONSTRAINT trs_section FOREIGN KEY (crsCode,semester) REFERENCES Teaching (crsCode,semester)"
				+ ");").execute();
		Comparable[][] tTable = data[TRANSCRIPT_TABLE];
		int studId;
		String crsCode;
		String semester;
		String grade;
		
		for(int i = 0 ; i< tTable.length; i++)
		{
			statement = connection.prepareStatement("INSERT INTO transcript (studId, crsCode, semester, grade) "
					+ "VALUES (?, ?, ?, ?);");	
			Comparable[] transcriptTuple;
			transcriptTuple = tTable[i];
			studId = (int) transcriptTuple[0];
			crsCode = (String) transcriptTuple[1];
			semester = (String) transcriptTuple[2];
			grade = (String) transcriptTuple[3];
			statement.setInt(1, studId);
			statement.setString(2, crsCode);
			statement.setString(3, semester);
			statement.setString(4,  grade);
			
			statement.executeUpdate();
			
		}
		System.out.println("Transcript Table Finished");
	}	

	private static void clearTables() throws SQLException
	{
		System.out.println("Clearing tables...");
		connection.prepareStatement("DELETE FROM transcript ").executeUpdate();
		connection.prepareStatement("DELETE FROM teaching ").executeUpdate();
		connection.prepareStatement("DELETE FROM course ").executeUpdate();
		connection.prepareStatement("DELETE FROM professor ").executeUpdate();
		connection.prepareStatement("DELETE FROM student ").executeUpdate();
	}
	
	public static void main(String[] args)
	{
		createConnection();
		if(connection != null) 
		{
			createTables();
			
			try
			{
				createAndPopulateStudentTables();
				createAndPopulateProfessorTables();
				createAndPopulateCourseTables();
				createAndPopulateTeachingTables();
				createAndPopulateTranscriptTables();
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}
	
}
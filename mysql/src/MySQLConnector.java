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
	
	
	public static Table[] createTables(int numberOfTables)
	{
		// code from the TestTupleGenerator
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

        String [] tables = { "Student", "Professor", "Course", "Teaching", "Transcript" };
		int tups [] = new int [] { numberOfTables, numberOfTables , numberOfTables, numberOfTables , numberOfTables };
		Comparable [][][] resultTest = test.generate (tups);
		Table professor = new Table("Professor", "id name deptId", "Integer String String", "id");
		Table courses = new Table("Course", "crsCode deptId crsName descr", "String String String String", "crsCode");
		Table student = new Table("Student", "id name address status", "Integer String String String", "id");
		for(Comparable[] tuple : resultTest[1])
		{
			professor.insert(tuple);
		}
		for(Comparable[] tuple : resultTest[2])
		{
			courses.insert(tuple);
		}
    for(Comparable[] tuple : resultTest[3])
    {
      student.insert(tuple);
    }
		
		Table tableList[] = {professor, courses, student};
		return tableList;
	}
	
	private static void createConnetion()
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
	
	
}
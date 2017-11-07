public class PerformanceTest
{
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

	public static void main(String[] args)
	{
		// timers
		long initialTime;
		long endingTime;
		double duration;
		Table temp;

		// mock data
		int numberToGenerate = 10000;
		Table createdTables[] = createTables(numberToGenerate);
		
		System.out.println("--------------- POINT SELECT ---------------");
		int professorID = (int)createdTables[0].getTuple(6786)[0];
		initialTime = System.nanoTime();
		for(int i = 0; i < numberToGenerate; i++)
		{
			if((int)createdTables[0].getTuple(i)[0] == professorID)
			{
				break; // if the id is the same then get out of the loop
			}
		}
		endingTime = System.nanoTime();
		duration = (endingTime - initialTime) / 1e6;
		System.out.println("Sequential: " + duration + " milliseconds");
		
		initialTime = System.nanoTime();
		temp = createdTables[0].select(new KeyType(professorID)); // LINHASH_MAP
		endingTime = System.nanoTime();
		duration = (endingTime - initialTime) / 1e6;
		System.out.println("Indexed using " + createdTables[0].getMapType() + ": " + duration + " milliseconds");


		System.out.println("--------------- RANGE SELECT ---------------");
		// implemented by Anurag Banerjee
		initialTime = System.nanoTime();
			int sequentialRangeSelectCount = 0;
			for (int i = 0; i < numberToGenerate; i++) {
				int profId = (int)createdTables[0].getTuple(i)[0];
				// get all professors with ids 1000 < id < 10000
				if (profId >= 1000 && profId <= 10000) {
					sequentialRangeSelectCount++;
				}
			}
		endingTime = System.nanoTime();
		duration = (endingTime - initialTime) / 1e6;
		System.out.println("Sequential: " + duration + " milliseconds; " + sequentialRangeSelectCount + " tuples");

		initialTime = System.nanoTime();
			// get all professors with ids 1000 < id < 10000
			Table indexRangeSelect = createdTables[0].select (t -> ((int)t[0] >= 1000) && ((int)t[0] <= 10000));
		endingTime = System.nanoTime();
		duration = (endingTime - initialTime) / 1e6;
		System.out.println("Indexed using " + createdTables[0].getMapType() + ": " + duration + " milliseconds");
		// end implemented by Anurag Banerjee

		System.out.println("--------------- JOIN ---------------");
		initialTime = System.nanoTime();
		temp = createdTables[0].join("id", "id", createdTables[2]);
		endingTime = System.nanoTime();
		duration = (endingTime - initialTime) / 1e6;
		System.out.println("Sequential using Nested Loop: " + duration +" milliseconds");

		// initialTime = System.nanoTime();
		// todo: implement sequential hash join
		// endingTime = System.nanoTime();
		// duration = (endingTime - initialTime) / 1e6;
		System.out.println("Sequential using Hash Join: "); // todo

		initialTime = System.nanoTime();
		temp = createdTables[0].i_join("id", "id", createdTables[2]);		
		endingTime = System.nanoTime();
		duration = (endingTime - initialTime) / 1e6;
		System.out.println("Indexed using " + createdTables[0].getMapType() + ": "+ duration +" milliseconds"); 
   
    /*System.out.println("Indexed Join");
		System.out.println("---------------");
   
    long initialTime3;
    long endingTime3;
    double duration3;
    initialTime3 = System.nanoTime();
    createdTables[0].join_Index(createdTables[1]);
    endingTime3 = System.nanoTime();
    duration3 = (endingTime3-initialTime3);
    System.out.println("Duration for Indexed Join of MapType: " +createdTables[0].getMapType());
    System.out.println(duration3 / 1000000 +" milliseconds\n");
    */
	}
	
}
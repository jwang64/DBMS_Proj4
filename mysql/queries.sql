SET @v1 = 685;
SET @v2 = 114;
SET @v3 = 557;
SET @v4 = 3;
SET @v5 = ;
SET @v6 = ;
SET @v7 = ;
SET @v8 = ;

-- List the name of the student with id equal to v1 (id). --
SELECT name
FROM Student
WHERE id = @v1;

-- List the names of students with id in the range of v2 (id) to v3 (inclusive). --
SELECT name
FROM Student
WHERE id >= @v2 AND id <= @v3;

-- List the names of students who have taken course v4 (crsCode). --
SELECT name
FROM Student
WHERE id IN ( SELECT studID
			  FROM Transcript
			  WhERE crsCode = @v4
			) ;

-- List the names of students who have taken a course taught by professor v5 (name). -

-- List the names of students who have taken a course from department v6 (deptId), but not v7. --

-- List the names of students who have taken all courses offered by department v8 (deptId). --
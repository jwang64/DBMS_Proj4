SET @v1 = 685;
SET @v2 = 114;
SET @v3 = 557;
SET @v4 = 'crsCode123456';
SET @v5 = 'name10743';
SET @v6 = 'deptId123456';
SET @v7 = 'deptId234567';
SET @v8 = 'deptId345678';

-- List the name of the student with id equal to v1 (id). --
SELECT name
FROM Student
WHERE id = @v1;
-- 3.3ms --

-- List the names of students with id in the range of v2 (id) to v3 (inclusive). --
SELECT name
FROM Student
WHERE id >= @v2 AND id <= @v3;
-- 3ms --

-- List the names of students who have taken course v4 (crsCode). --
SELECT name 
FROM Student 
JOIN transcript 
WHERE crsCode IN (SELECT crsCode 
				  FROM transcript 
				  WHERE crsCode=@v3);
-- .8ms --

-- List the names of students who have taken a course taught by professor v5 (name). -
SELECT s.name
FROM Student as s
	JOIN Transcript as Trans
		ON Trans.studId = s.id 
	JOIN Teaching as Te
		ON Te.crsCode = Trans.crsCode AND Te.semester = Trans.semester
	JOIN Professor as p
		ON p.id = Te.profId
WHERE p.name = @v5;
-- 9.7ms --

-- List the names of students who have taken a course from department v6 (deptId), but not v7. --
SELECT Student.name
FROM Student, Transcript, Course
WHERE Transcript.crsCode= Course.crsCode AND Student.id = Transcript.studID
AND Course.deptId = @v6 and Course.deptId <> @v7;
-- .8ms --

-- List the names of students who have taken all courses offered by department v8 (deptId). --
SELECT s.name 
FROM Student as s
JOIN Transcript as Trans
	ON s.id = Trans.studId
		WHERE crsCode IN
		(SELECT crsCode FROM Course 
		 WHERE deptId = @v8 AND crsCode 
		 IN (SELECT crsCode 
		     FROM Teaching))
		GROUP BY studId
		HAVING COUNT(*) = 
			(SELECT COUNT(*) FROM Course WHERE deptId = @v8 AND crsCode IN (SELECT crsCode FROM Teaching));
-- 5.2ms --			

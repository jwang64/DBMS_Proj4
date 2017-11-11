set "variables.v1" = 5502;
set "variables.v2" = 0;
set "variables.v3" = 1013;
set "variables.v4" = 'crsCode389452';
set "variables.v5" = 'name119557'; -- name of professor
set "variables.v6" = 'deptId785607';
set "variables.v7" = 'deptId145704';
set "variables.v8" = 'deptId785607'; -- todo: use better deptid


-- List the name of the student with id equal to v1 (id). --
SELECT name
FROM Student
WHERE id = cast(current_setting('variables.v1') as integer);
-- 3.3ms --

-- List the names of students with id in the range of v2 (id) to v3 (inclusive). --
SELECT name
FROM Student
WHERE id >= cast(current_setting('variables.v2') as integer) AND id <= @cast(current_setting('variables.v3') as integer);
-- 3ms --

-- List the names of students who have taken course v4 (crsCode). --
SELECT name 
FROM Student 
JOIN transcript 
WHERE crsCode IN (SELECT crsCode 
				  FROM transcript 
				  WHERE crsCode=cast(current_setting('variables.v4') as text));
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
WHERE p.name = cast(current_setting('variables.v5') as text);
-- 9.7ms --

-- List the names of students who have taken a course from department v6 (deptId), but not v7. --
SELECT Student.name
FROM Student, Transcript, Course
WHERE Transcript.crsCode= Course.crsCode AND Student.id = Transcript.studID
AND Course.deptId = cast(current_setting('variables.v6') as text) and Course.deptId <> cast(current_setting('variables.v7') as text);
-- .8ms --

-- List the names of students who have taken all courses offered by department v8 (deptId). --
SELECT s.name 
FROM Student as s
JOIN Transcript as Trans
	ON s.id = Trans.studId
		WHERE crsCode IN
		(SELECT crsCode FROM Course 
		 WHERE deptId = cast(current_setting('variables.v8') as text) AND crsCode 
		 IN (SELECT crsCode 
		     FROM Teaching))
		GROUP BY studId
		HAVING COUNT(*) = 
			(SELECT COUNT(*) FROM Course WHERE deptId = cast(current_setting('variables.v8') as text) AND crsCode IN (SELECT crsCode FROM Teaching));
-- 5.2ms --			

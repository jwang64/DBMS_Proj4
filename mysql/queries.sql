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
-- .6ms --

-- List the names of students who have taken course v4 (crsCode). --
SELECT name
FROM Student, Transcript
WHERE Student.id = Transcript.StudID AND Transcript.crsCode = @v4;
-- .7ms --


-- List the names of students who have taken a course taught by professor v5 (name). -
SELECT Student.name
FROM Student, Transcript, Teaching, Professor
WHERE Professor.name = @v5 AND Transcript.crsCode = Teaching.crsCode 
AND Student.id = Transcript.studID AND Teaching.profID = Professor.ID;
-- 12.7ms --


-- List the names of students who have taken a course from department v6 (deptId), but not v7. --
SELECT Student.name
FROM Student, Transcript, Course
WHERE Transcript.crsCode= Course.crsCode AND Student.id = Transcript.studID
AND Course.deptId = @v6 and Course.deptId <> @v7;
-- .8ms --

-- List the names of students who have taken all courses offered by department v8 (deptId). --
SELECT name
FROM Student, Transcript 
WHERE Transcript.studID = Student.id
AND ALL Transcript.crsCode = Course.crsCode 
AND Course.deptId = @v8;
-- --
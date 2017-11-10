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
FROM Student, Transcript
WHERE Student.id = Transcirpt.StudID AND Transcirpt.crsCode = @v4;


-- List the names of students who have taken a course taught by professor v5 (name). -
SELECT name
FROM Student, Transcript, Teaching, Professor
WHERE Professor.name = @v5 AND Transcript.crsCode = Teaching.crsCode 
AND Student.id = Transcript.studID AND Teaching.profID = Professor.ID;


-- List the names of students who have taken a course from department v6 (deptId), but not v7. --
SELECT name
FROM Student, Transcript, Course
WHERE Course.deptIdTranscript.crsCode= Course.crsCode AND Student.id = Transcript.studID
AND Course.deptId = @v6 and Course.deptId <> @v7;


-- List the names of students who have taken all courses offered by department v8 (deptId). --
SELECT name
FROM Student, Transcript, Course
WHERE Transcript.studID = Student.id
AND ALL Transcript.crsCode = Course.crsCode AND Course.deptId = @v8;
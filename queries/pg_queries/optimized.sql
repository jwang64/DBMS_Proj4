set "variables.v1" = 5502;
set "variables.v2" = 0;
set "variables.v3" = 1013;
set "variables.v4" = 'crsCode389452';
set "variables.v5" = 'name119557'; -- name of professor
set "variables.v6" = 'deptId785607';
set "variables.v7" = 'deptId145704';

-- IMPORTANT!!! make sure @v8 matches in the where clause
set "variables.v8" = 'deptId785607';
CREATE VIEW DEPT_COURSES AS (
	SELECT crsCode
	FROM course
	WHERE deptId = 'deptId345678'
);

-- List the name of the student with id equal to v1 (id). --
SELECT name
FROM Student
WHERE id = cast(current_setting('variables.v1') as integer);

-- List the names of students with id in the range of v2 (id) to v3 (inclusive). --
SELECT name
FROM Student
WHERE id >= cast(current_setting('variables.v2') as integer)
	AND id <= @cast(current_setting('variables.v3') as integer);

-- List the names of students who have taken course v4 (crsCode). --

SELECT DISTINCT(student.name)
FROM student, transcript
WHERE student.id = transcript.studId
	AND transcript.crsCode = cast(current_setting('variables.v4') as text)
;

-- List the names of students who have taken a course taught by professor v5 (name). -
SELECT DISTINCT(s.name)
FROM student as s, professor as p, teaching as t, transcript as ts
WHERE s.id = ts.studId
	AND ts.crsCode = t.crsCode
	AND t.profId = p.id
	AND p.name = cast(current_setting('variables.v5') as text)
;

-- List the names of students who have taken a course from department v6 (deptId), but not v7. --
SELECT Student.name
FROM Student, Transcript, Course
WHERE Transcript.crsCode= Course.crsCode AND Student.id = Transcript.studID
	AND Course.deptId = cast(current_setting('variables.v6') as text)
	AND Course.deptId <> cast(current_setting('variables.v7') as text);

-- List the names of students who have taken all courses offered by department v8 (deptId). --

SELECT DISTINCT(s.name)
FROM student as s, transcript as ts, course as c
WHERE s.id = ts.studId
	AND ts.crsCode = ALL (SELECT * FROM DEPT_COURSES)
;

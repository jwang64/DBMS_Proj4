SET @v1 = 685;
SET @v2 = 114;
SET @v3 = 557;
SET @v4 = 'crsCode123456';
SET @v5 = 'name10743';
SET @v6 = 'deptId123456';
SET @v7 = 'deptId234567';


SET @v8 = 'deptId345678';
-- IMPORTANT!!! make sure @v8 matches in the where clause
CREATE VIEW DEPT_COURSES AS (
	SELECT crsCode
	FROM course
	WHERE deptId = 'deptId345678'
);

-- List the name of the student with id equal to v1 (id). --
SELECT name
FROM student
WHERE id = @v1;

-- List the names of students with id in the range of v2 (id) to v3 (inclusive). --
SELECT name
FROM student
WHERE id >= @v2 AND id <= @v3;

-- List the names of students who have taken course v4 (crsCode). --

SELECT DISTINCT(student.name)
FROM student, transcript
WHERE student.id = transcript.studId
	AND transcript.crsCode = @v4
;

-- List the names of students who have taken a course taught by professor v5 (name). -
SELECT DISTINCT(s.name)
FROM student as s, professor as p, teaching as t, transcript as ts
WHERE s.id = ts.studId
	AND ts.crsCode = t.crsCode
	AND t.profId = p.id
	AND p.name = @v5
;

-- List the names of students who have taken a course from department v6 (deptId), but not v7. --
SELECT DISTINCT(s.name)
FROM student as s, transcript as ts, course as c
WHERE s.id = ts.studId
	AND ts.crsCode = c.crsCode
	AND c.deptId = @v6
	AND c.deptId <> @v7
;

-- List the names of students who have taken all courses offered by department v8 (deptId). --
SELECT DISTINCT(s.name)
FROM student as s, transcript as ts
WHERE s.id = ts.studId
	AND ts.crsCode = ALL (SELECT * FROM DEPT_COURSES)
;
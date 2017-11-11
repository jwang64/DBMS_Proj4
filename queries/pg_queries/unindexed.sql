set "variables.v1" = 5502;
set "variables.v2" = 0;
set "variables.v3" = 1013;
set "variables.v4" = 'crsCode389452';
set "variables.v5" = 'name119557'; -- name of professor
set "variables.v6" = 'deptId785607';
set "variables.v7" = 'deptId145704';
set "variables.v8" = 'deptId785607'; -- todo: use better deptid


-- 1) List the name of the student with id equal to v1 (id).

SELECT student.name
FROM student
WHERE student.id = cast(current_setting('variables.v1') as integer)
;

-- should return:
	-- name872287

-- 2) List the names of students with id in the range of v2 (id) to v3 (inclusive).

SELECT student.name
FROM student
WHERE student.id
	BETWEEN cast(current_setting('variables.v2') as integer)
		AND cast(current_setting('variables.v3') as integer)
;

-- should return:
	-- name523005
	-- name362652
	-- name457567
	-- name6989
	-- name52780
	-- name178108
	-- name537005
	-- name261599


-- 3) List the names of students who have taken course v4 (crsCode).

SELECT DISTINCT(student.name)
FROM student, transcript
WHERE student.id = transcript.studId
	AND transcript.crsCode = cast(current_setting('variables.v4') as text)
;

-- should return:
	--  name523005

-- 4) List the names of students who have taken a course taught by professor v5 (name).

SELECT DISTINCT(s.name)
FROM student as s, professor as p, teaching as t, transcript as ts
WHERE s.id = ts.studId
	AND ts.crsCode = t.crsCode
	AND t.profId = p.id
	AND p.name = cast(current_setting('variables.v5') as text)
;

-- should return:
	-- name523005

-- 5) List the names of students who have taken a course from department v6 (deptId), but not v7 (deptId).

SELECT DISTINCT(s.name)
FROM student as s, transcript as ts, course as c
WHERE s.id = ts.studId
	AND ts.crsCode = c.crsCode
	AND c.deptId = cast(current_setting('variables.v6') as text)
	AND c.deptId <> cast(current_setting('variables.v7') as text)
	-- returns
	--  name72596
	--  name591558
;

-- 6) List the names of students who have taken all courses offered by department v8 (deptId).

SELECT DISTINCT(s.name)
FROM student as s, transcript as ts, course as c
WHERE s.id = ts.studId
	AND ts.crsCode = ALL (
		SELECT crsCode
		FROM course
		WHERE course.deptId = cast(current_setting('variables.v8') as text)
	)
;






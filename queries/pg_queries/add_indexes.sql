DROP INDEX IF EXISTS transcriptStudentIndex;
CREATE INDEX transcriptStudentIndex 
ON transcript (studId);

DROP INDEX IF EXISTS transcriptCourseCodeIndex;
CREATE INDEX transcriptCourseCodeIndex
ON transcript (crsCode);

DROP INDEX IF EXISTS teachingProfIndex;
CREATE INDEX teachingProfIndex
ON teaching (profId);

DROP INDEX IF EXISTS profNameIndex;
CREATE INDEX profNameIndex
ON professor (name);

DROP INDEX IF EXISTS courseDeptIndex;
CREATE INDEX courseDeptIndex
ON course (deptId);
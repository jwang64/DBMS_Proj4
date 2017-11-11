ALTER TABLE transcript DROP INDEX transcriptStudentIndex;
CREATE INDEX transcriptStudentIndex 
ON transcript (studId);

ALTER TABLE transcript DROP INDEX transcriptCourseCodeIndex;
CREATE INDEX transcriptCourseCodeIndex
ON transcript (crsCode);

ALTER TABLE teaching DROP INDEX teachingProfIndex;
CREATE INDEX teachingProfIndex
ON teaching (profId);

ALTER TABLE professor DROP INDEX profNameIndex;
CREATE INDEX profNameIndex
ON professor (name);

ALTER TABLE course DROP INDEX courseDeptIndex;
CREATE INDEX courseDeptIndex
ON course (deptId);
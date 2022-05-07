SELECT Student_fk, AVG(ExamMark), AVG(PracticalMark)
FROM Student_Takes_Course
GROUP BY Student_fk
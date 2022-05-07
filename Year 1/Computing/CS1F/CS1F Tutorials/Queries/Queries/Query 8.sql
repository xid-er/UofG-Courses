SELECT Student_fk, Student.Name_Forename, Student.Name_Surname, AVG(ExamMark), AVG(PracticalMark)
FROM Student_Takes_Course, Student
WHERE Student_Takes_Course.Student_fk = Student.MatricNo AND (Student.Name_Surname = 'Smith' OR Student.Name_Surname = 'Saunders')
GROUP BY Student_fk
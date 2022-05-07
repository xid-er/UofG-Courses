SELECT Name_Forename, Name_Surname, ExamMark
FROM Student_Takes_Course, Course, Student
WHERE Course.Title = 'CS-1Q' AND Student_Takes_Course.Course_fk = Course.CourseID AND Student.MatricNo = Student_Takes_Course.Student_fk
ORDER BY Name_Surname ASC
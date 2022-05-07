SELECT Name_Forename, Name_Surname
FROM Tutor, Staff, Tutorial_Group_2
WHERE Tutor.StaffID_fk = Staff.StaffNo AND Tutorial_Group_2.Tutor_fk = Tutor.StaffID_fk AND Staff.JobTitle <> 'lecturer' AND Tutorial_Group_2.TutorialRoom = 11
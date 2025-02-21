// IMyMySchoolInterface.aidl
package com.example.demodatabase;

// Declare any non-default types here with import statements

import com.example.demodatabase.Student;
import com.example.demodatabase.Subject;
import java.util.List;

parcelable Student;
parcelable Subject;

interface IMyMySchoolInterface {
    List<Student> getFirst100Students();
}
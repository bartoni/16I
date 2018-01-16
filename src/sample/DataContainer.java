package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;


public class DataContainer {

    protected ObservableList<Student> students;

    public ObservableList<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = FXCollections.observableArrayList(students);
    }

    public DataContainer() {
        students = FXCollections.observableArrayList();
    }
}
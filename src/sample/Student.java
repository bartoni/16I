package sample;

public class Student {

    protected String name;
    protected String surname;
    protected String pesel;
    protected String idx;
    protected Double grade;
    protected String gradeDetailed;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPesel() {
        return pesel;
    }

    public void setPesel(String pesel) {
        this.pesel = pesel;
    }

    public String getIdx() {
        return idx;
    }

    public void setIdx(String idx) {
        this.idx = idx;
    }

    public Double getGrade() {
        return grade;
    }

    public void setGrade(Double grade) {
        this.grade = grade;
    }

    public String getGradeDetailed() {
        return gradeDetailed;
    }

    public void setGradeDetailed(String gradeDetailed) {
        this.gradeDetailed = gradeDetailed;
    }
}
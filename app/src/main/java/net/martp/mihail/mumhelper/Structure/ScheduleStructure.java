package net.martp.mihail.mumhelper.Structure;

public class ScheduleStructure {
    String date;
    String time;
    String subject;
    String teacher;
    String classroom;
    String typelesson;

    public ScheduleStructure() {
    }

    public ScheduleStructure(String date, String time, String subject, String teacher, String classroom, String typelesson) {
        this.date = date;
        this.time = time;
        this.subject = subject;
        this.teacher = teacher;
        this.classroom = classroom;
        this.typelesson = typelesson;
    }

    public ScheduleStructure(String date, String time, String subject, String teacher, String classroom) {
        this.date = date;
        this.time = time;
        this.subject = subject;
        this.teacher = teacher;
        this.classroom = classroom;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getSubject() {
        return subject;
    }

    public String getTeacher() {
        return teacher;
    }

    public String getClassroom() {
        return classroom;
    }

    public String getTypelesson() {
        return typelesson;
    }

}

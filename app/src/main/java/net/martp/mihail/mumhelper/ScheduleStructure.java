package net.martp.mihail.mumhelper;

/**
 * Created by Mihail on 24.12.2014.
 */
public class ScheduleStructure {
    String date;
    String time;
    String subject;
    String teacher;
    String classroom;

    public ScheduleStructure() {
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
}

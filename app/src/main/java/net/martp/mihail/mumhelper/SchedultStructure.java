package net.martp.mihail.mumhelper;

/**
 * Created by Mihail on 24.12.2014.
 */
public class SchedultStructure {
    String date;
    String time;
    String nameLesson;
    String teacher;
    String classroom;

    public SchedultStructure() {
    }

    public SchedultStructure(String date, String time, String nameLesson, String teacher, String classroom) {
        this.date = date;
        this.time = time;
        this.nameLesson = nameLesson;
        this.teacher = teacher;
        this.classroom = classroom;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getNameLesson() {
        return nameLesson;
    }

    public String getTeacher() {
        return teacher;
    }

    public String getClassroom() {
        return classroom;
    }
}

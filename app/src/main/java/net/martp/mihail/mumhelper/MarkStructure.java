package net.martp.mihail.mumhelper;

/**
 * Created by Mihail on 24.12.2014.
 */
public class MarkStructure {
    boolean status;
    String nameOfDiscipline;
    String formOfControl;
    String mark;
    String date;

    public MarkStructure(boolean status, String nameOfDiscipline, String formOfControl, String mark, String date) {
        this.status = status;
        this.nameOfDiscipline = nameOfDiscipline;
        this.formOfControl = formOfControl;
        this.mark = mark;
        this.date = date;
    }

    public boolean getStatud() {
        return status;
    }

    public String getNameOfDiscipline() {
        return nameOfDiscipline;
    }

    public String getFormOfControl() {
        return formOfControl;
    }

    public String getMark() {
        return mark;
    }

    public String getDate() {
        return date;
    }
}

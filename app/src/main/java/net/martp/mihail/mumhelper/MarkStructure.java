package net.martp.mihail.mumhelper;

/**
 * Created by Mihail on 24.12.2014.
 */
public class MarkStructure {
    String nameOfDiscipline;
    String formOfControl;
    String mark;
    String date;

    public MarkStructure(String nameOfDiscipline, String formOfControl, String mark, String date) {
        this.nameOfDiscipline = nameOfDiscipline;
        this.formOfControl = formOfControl;
        this.mark = mark;
        this.date = date;
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

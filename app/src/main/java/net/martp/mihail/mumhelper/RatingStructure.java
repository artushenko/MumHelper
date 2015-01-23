package net.martp.mihail.mumhelper;

/**
 * Created by Mihail on 23.01.2015.
 */
public class RatingStructure {
    private String number;
    private String surname;
    private String name;
    private String avarege;

    public RatingStructure(String number, String surname, String name, String avarege) {
        this.number = number;
        this.surname = surname;
        this.name = name;
        this.avarege = avarege;
    }

    public String getNumber() {
        return number;
    }

    public String getSurname() {
        return surname;
    }

    public String getName() {
        return name;
    }

    public String getAvarege() {
        return avarege;
    }
}

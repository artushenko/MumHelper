package net.martp.mihail.mumhelper;

/**
 * Created by Mihail on 26.01.2015.
 */
public class OrderStructure {

    String courese;
    String numberOrder;
    String textOrder;

    public OrderStructure(String courese, String numberOrder, String textOrder) {
        this.courese = courese;
        this.numberOrder = numberOrder;
        this.textOrder = textOrder;
    }

    public String getCourese() {
        return courese;
    }

    public String getNumberOrder() {
        return numberOrder;
    }

    public String getTextOrder() {
        return textOrder;
    }
}

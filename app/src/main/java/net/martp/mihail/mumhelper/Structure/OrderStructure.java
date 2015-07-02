package net.martp.mihail.mumhelper.Structure;

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

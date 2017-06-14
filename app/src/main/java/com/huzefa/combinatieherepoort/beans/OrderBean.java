package com.huzefa.combinatieherepoort.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rashida on 04/06/17.
 */

public class OrderBean {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<OrderBean> ITEMS = new ArrayList<OrderBean>();
    public static final int COUNT = 10;

    public String partyNumber;

    public OrderBean(String partyNumber, String description) {
        this.partyNumber = partyNumber;
        this.description = description;
    }

    public String description;


    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem(i));
        }
    }

    private static void addItem(OrderBean item) {
        ITEMS.add(item);
    }

    private static OrderBean createDummyItem(int position) {
        return new OrderBean("ParjiNumber "+position, "Item Description" + position);
    }
}

package com.techpark.lastfmclient.adapters;

/**
 * Created by andrew on 29.10.14.
 */
public class NavMenuItem implements NavDrawerItem {

    private int id;
    public static final int ITEM_TYPE = 1;
    private String label;

    private NavMenuItem(){}

    public static NavDrawerItem getInstance(int id, String label) {
        NavMenuItem s = new NavMenuItem();
        s.id = id;
        s.setLabel(label);
        return s;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public int getType() {
        return ITEM_TYPE;
    }

    @Override
    public boolean updateActionBarTitle() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public boolean isUserItem() {
        return false;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}

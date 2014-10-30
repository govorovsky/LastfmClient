package com.techpark.lastfmclient.adapters;

/**
 * Created by andrew on 29.10.14.
 */
public class NavMenuSection implements NavDrawerItem {

    private int id;
    public static final int SECTION_TYPE = 0;
    private String label;

    private NavMenuSection() {
    }

    public static NavMenuSection getInstance(int id, String label) {
        NavMenuSection s = new NavMenuSection();
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
        return SECTION_TYPE;
    }

    @Override
    public boolean updateActionBarTitle() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public String getLabel() {
        return label;

    }

    public void setLabel(String label) {
        this.label = label;
    }


    @Override
    public boolean isUserItem() {
        return false;
    }
}

package com.ttn.googlehackathon.models;

public class NavDrawerItem {

    private String title;
    private int icon;
    private int activeIcon;
    private String count = "0";
    // boolean to set visiblity of the counter
    private boolean isCounterVisible = false;

    public NavDrawerItem() {
    }

    public NavDrawerItem(String title, int icon, int activeIcon) {
        this.title = title;
        this.icon = icon;
        this.activeIcon = activeIcon;
    }

    public NavDrawerItem(String title, int icon, int activeIcon, boolean isCounterVisible, String count) {
        this.title = title;
        this.icon = icon;
        this.activeIcon = activeIcon;
        this.isCounterVisible = isCounterVisible;
        this.count = count;
    }

    public int getActiveIcon() {
        return activeIcon;
    }

    public void setActiveIcon(int activeIcon) {
        this.activeIcon = activeIcon;
    }

    public boolean isCounterVisible() {
        return isCounterVisible;
    }

    public void setIsCounterVisible(boolean isCounterVisible) {
        this.isCounterVisible = isCounterVisible;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIcon() {
        return this.icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getCount() {
        return this.count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public boolean getCounterVisibility() {
        return this.isCounterVisible;
    }

    public void setCounterVisibility(boolean isCounterVisible) {
        this.isCounterVisible = isCounterVisible;
    }
}

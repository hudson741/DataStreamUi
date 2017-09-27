package com.yss.utils;

import java.util.HashMap;
import java.util.List;

/**
 * allows for paged results on the display screens
 */
public class SortedSet {
    private String orderByField = null;
    private String orderByDirection = "asc";
    private List itemList;
    private HashMap<String, String> filterMap = new HashMap<>();

    public SortedSet() {
        
    }
    
    public SortedSet(String orderByField){
        this.orderByField =  orderByField;
    }
    

    public String getOrderByField() {

        if (orderByField != null) {
            return orderByField.replaceAll("[^0-9,a-z,A-Z,\\_,\\.]", "");
        }
        return null;

    }

    public void setOrderByField(String orderByField) {
        this.orderByField = orderByField;
    }


    public String getOrderByDirection() {
        if ("asc".equalsIgnoreCase(orderByDirection)) {
            return "asc";
        } else {
            return "desc";
        }
    }

    public void setOrderByDirection(String orderByDirection) {
        this.orderByDirection = orderByDirection;
    }

    public List getItemList() {
        return itemList;
    }

    public void setItemList(List itemList) {
        
        this.itemList = itemList;
    }

    public HashMap<String, String> getFilterMap() {
        return filterMap;
    }

    public void setFilterMap(HashMap<String, String> filterMap) {
        this.filterMap = filterMap;
    }
}

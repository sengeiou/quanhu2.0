package com.rz.circled.modle;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by xiayumo on 16/8/17.
 */
public class AreaModel implements Serializable {

    public String name;
    public String code;
    public boolean isChecked = false;
    public ArrayList<CityModel> children;


    public static class CityModel implements Serializable {
        public String name;
        public String code;
        private String parent_code;
        public boolean isChecked = false;
    }

}

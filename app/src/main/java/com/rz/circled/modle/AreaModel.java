package com.rz.circled.modle;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by xiayumo on 16/8/17.
 */
public class AreaModel implements Serializable{

    public String name;
    public boolean isChecked = false;
    public ArrayList<CityModel > cities;


    public static class CityModel implements Serializable{
         public String name;
         public boolean isChecked = false;
    }

}

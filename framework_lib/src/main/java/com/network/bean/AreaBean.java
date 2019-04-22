package com.network.bean;

import com.bean.Entity;

import java.util.List;

/**
 * @author yangsk
 * @class describe  {@link #}
 * @time 06/08/2018 3:01 PM
 */

public class AreaBean extends Entity {

    public List<ProvincesBean> provinces;
    public List<CitiesBean> cities;
    public List<CountiesBean> counties;

    public static class ProvincesBean {
        public int id;
        public String name;

    }

    public static class CitiesBean {
        public int id;
        public String name;
        public String lat;
        public String lng;
        public int province_id;
    }

    public static class CountiesBean {
        public int id;
        public String name;
        public int city_id;
        public String area_code;
        public String postcode;
        public String lat;
        public String lng;
    }
}

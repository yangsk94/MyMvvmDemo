package com.network.bean

import com.bean.Entity

/**
 * @author yangsk
 * @class describe  [.]
 * @time 06/08/2018 3:01 PM
 */

class AreaBean : Entity() {

    var provinces: List<ProvincesBean>? = null
    var cities: List<CitiesBean>? = null
    var counties: List<CountiesBean>? = null

    class ProvincesBean {
        var id: Int = 0
        var name: String? = null

    }

    class CitiesBean {
        var id: Int = 0
        var name: String? = null
        var lat: String? = null
        var lng: String? = null
        var province_id: Int = 0
    }

    class CountiesBean {
        var id: Int = 0
        var name: String? = null
        var city_id: Int = 0
        var area_code: String? = null
        var postcode: String? = null
        var lat: String? = null
        var lng: String? = null
    }
}

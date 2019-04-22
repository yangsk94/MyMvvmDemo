package com.network.bean

import com.bean.Entity

/**
 * @author yangsk
 * @class describe  [.]
 * @time 27/07/2018 4:15 PM
 */

class UserInfo : Entity() {
    var token: String? = null
    var user: UserBean? = null

    class UserBean {

        var id: Int = 0
        var mobile: String? = null
        var name: String? = null
        var avatar: AvatarBean? = null
        var avatar_url: String? = null
        var avatar_id: Int = 0
        var memo: String? = null
        var status: Int = 0
        var address: Int = 0
        var id_number: String? = null
        var hz_company_id: Int = 0//仅货主
        var pic_card1: String? = null
        var pic_card2: String? = null
        var pic_card_id1: Int = 0
        var pic_card_id2: Int = 0
        var hz_company_status: Int = 0// 0:审核通过；1:新增，待审核；2：审核未通过；-1：block；-2：未绑定公司  仅货主

        var pic_driver_license: String? = null//仅司机
        var pic_driver_license_id: Int = 0

        var truck: TruckBean? = null//司机关联的车辆，如果为空，则为企业司机，如果不为空，则为挂靠司机，仅司机
        var wl_company_list: List<WlCompanyBean>? = null//司机关联的物流公司列表，仅司机

        var wl_company: WlCompanyBean? = null
        var wl_company_status: String? = null//仅经纪人
        var wl_company_id: String? = null//仅经纪人
        var address_count: Int = 0
        var company_count: Int = 0
        var focus_area_count: Int = 0
        var driver_count: Int = 0
        var truck_count: Int = 0
        var bind_company_name: String? = null

        class AvatarBean {

            var id: Int = 0
            var filename: String? = null
            var url: String? = null
            var width: Int = 0
            var height: Int = 0
            var create_time: String? = null
            var type: String? = null
            var title: Any? = null
        }

        class WlCompanyBean {

            var id: Int = 0
            var name: String? = null
            var province_name: String? = null
            var city_name: String? = null
            var county_name: String? = null
            var address: String? = null
            var contactor: String? = null
            var contact_phone: String? = null
            var bank: String? = null
            var fax: String? = null
            var is_super_company: Int = 0//超级公司
            var status: Int = 0
            var creator: Int = 0
            var ctime: String? = null
            var mtime: String? = null
            var pic_company_cert1: Any? = null
            var reg_from: String? = null
            var tax_number: String? = null
            var bank_account: String? = null
            var truck_number: Int = 0
            var driver_number: Int = 0
        }


        class TruckBean {

            var id: Int = 0
            var number: String? = null
            var vin: String? = null
            var model: String? = null
            var memo: String? = null
            var driver_admin_id: Any? = null
            var wl_company_id: Int = 0
            var truck_type_id: Int = 0
            var pic_license_id1: Int = 0
            var pic_license_id2: Int = 0
            var pic_permit_id: Int = 0
            var check_expire: String? = null
            var permit_expire: String? = null
            var status: Int = 0
            var pic_license1: PicLicenseBean? = null
            var pic_license2: PicLicenseBean? = null
            var pic_permit: PicLicenseBean? = null
            var truck_type: TruckTypeBean? = null
            var isCheck: Boolean = false


            class PicLicenseBean {

                var id: Int = 0
                var filename: String? = null
                var url: String? = null
                var width: Int = 0
                var height: Int = 0
                var create_time: String? = null
                var type: String? = null
                var title: Any? = null
            }

            class TruckTypeBean {

                var id: Int = 0
                var type: String? = null
                var length: Double = 0.toDouble()

            }
        }


    }
}

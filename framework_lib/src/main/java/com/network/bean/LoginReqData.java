package com.network.bean;

/**
 * @author 容联•云通讯-zhulk
 * @class describe  {@link #}
 * @time 2019/4/19 下午6:47
 */
public class LoginReqData {
    public String mobile;
    public String password;
    public int type;

    public LoginReqData(String mobile, String password, int type) {
        this.mobile = mobile;
        this.password = password;
        this.type = type;
    }
}

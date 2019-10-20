package com.example.demo.web;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.common.AuthUtils;
import com.example.demo.entity.WxUser;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URLEncoder;

/**
 * 微信开放平台
 * @author wangqiang
 * @date 2019/10/14 15:46
 */
@RestController
public class LoginPcController {

    /**
     * 微信登录授权接口
     * 第一步：用户授权并获取code
     */
    @RequestMapping("/wx-login-pc")
    public void login(){
        String backUrl = "http://localhost:8081/wx-project/callback-pc";//注意：这个回调地址，必须在公网上面能够访问

        String urls = "navigateTo(\"https://open.weixin.qq.com/connect/qrconnect?appid="+ AuthUtils.APPID +
                "&redirect_uri=" + URLEncoder.encode(backUrl) +
                "&response_type=code" +
                "&scope=snsapi_login" +
                "&state=state#wechat_redirect";
    }

    /**
     * 第二步：用户同意后，将调用重定向的接口
     * @param code
     * @throws IOException
     */
    @RequestMapping("/callback-pc")
    public WxUser  callBack(String code) throws IOException {
        //使用code换取access_token
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + AuthUtils.APPID +
                "&secret=" + AuthUtils.APPSECRET +
                "&code=1111" +
                "&grant_type=authorization_code";
        JSONObject jsonObject = AuthUtils.doGetJson(url);
        String openId = jsonObject.getString("openid");
        String token = jsonObject.getString("access_token");
        //通过access_token、openid获取用户信息
        String infoUrl = "https://api.weixin.qq.com/sns/userinfo?access_token=" + token +
                "&openid=" + openId +
                "&lang=zh_ch";
        JSONObject userInfo = AuthUtils.doGetJson(infoUrl);
        System.out.println(userInfo);

        WxUser wxUser = new WxUser();
        //解析用户信息
        wxUser.setOpenid(userInfo.getString("openid"));
        wxUser.setNickname(userInfo.getString("nickname"));
        wxUser.setSex(Integer.parseInt(userInfo.getString("nickname")));
        wxUser.setProvince(userInfo.getString("province"));
        wxUser.setCity(userInfo.getString("city"));
        wxUser.setCountry(userInfo.getString("country"));
        wxUser.setHeadimgurl(userInfo.getString("headimgurl"));
        wxUser.setPrivilege(userInfo.getString("privilege"));
        wxUser.setUnionid(userInfo.getString("unionid"));

        // 1、使用微信用户信息直接登录无需注册和绑定
        //保存用户信息
       // return  userInfo ;

        //2、将微信和当前系统进行绑定起来
        /**
         * 1、使用openid来查询数据库是否当前系统时候已经绑定
         * 2、如果没有绑定则跳转到登录页面进行绑定，如果绑定了则直接跳转
         */
        return wxUser;
    }
}

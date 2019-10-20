package com.example.demo.web;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.common.AuthUtils;
import com.example.demo.entity.User;
import com.example.demo.entity.WxUser;
import com.example.demo.service.TokenService;
import com.example.demo.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Objects;

/**
 * @author wangqiang
 * @date 2019/10/14 15:46
 */
@RestController
@Api("微信登录授权接口")
public class LoginController {

    @Autowired
    UserService userService;

    @Autowired
    TokenService tokenService;

    /**
     * 微信登录授权接口
     * 第一步：用户授权并获取code
     */
    @RequestMapping("/wxLogin")
    @ApiOperation("微信登录授权接口")
    public void login(){
        String backUrl = "http://localhost:8081/wx-project/callback";//注意：这个回调地址，必须在公网上面能够访问
        String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + AuthUtils.APPID +
                "&redirect_uri=" + URLEncoder.encode(backUrl) +
                "&response_type=code" +
                "&scope=snsapi_userinfo&state=STATE#wechat_redirect";

    }

    /**
     * 第二步：用户同意后，将调用重定向的接口
     * @param code
     * @throws IOException
     */
    @RequestMapping("/callback")
    @ApiOperation("用户同意后，将调用重定向的接口")
    public JSONObject  callBack(String code,HttpServletResponse response) throws IOException {
        JSONObject jsonObj = new JSONObject();
        //使用code换取access_token
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + AuthUtils.APPID +
                "&secret=" + AuthUtils.APPSECRET +
                "&code=" + code +
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

        //解析用户信息   --可以不要，只是看看用户信息而已
        WxUser wxUser = new WxUser();

        wxUser.setOpenid(userInfo.getString("openid"));
        wxUser.setNickname(userInfo.getString("nickname"));
        wxUser.setSex(Integer.parseInt(userInfo.getString("nickname")));
        wxUser.setProvince(userInfo.getString("province"));
        wxUser.setCity(userInfo.getString("city"));
        wxUser.setCountry(userInfo.getString("country"));
        wxUser.setHeadimgurl(userInfo.getString("headimgurl"));
        wxUser.setPrivilege(userInfo.getString("privilege"));
        wxUser.setUnionid(userInfo.getString("unionid"));

        jsonObj = gettoken(userInfo.getString("openid"),userInfo.getString("nickname"),response);
        jsonObj.put("userInfo", wxUser);
        jsonObj.put("openid", userInfo.getString("openid"));

        return jsonObj;
    }

    /**
     * 处理token
     * @param openid
     * @param name
     * @param response
     * @return
     */
    public JSONObject gettoken(String openid,String name,HttpServletResponse response){
        JSONObject jsonObj = new JSONObject();
        /**
         * 将用户信息保存起来
         */
        User user = new User();
        user.setId(openid);
        user.setName(name);
        /*通过id查询当前用户是否存在*/
        User userDetail = userService.findUserById(user.getId());
        if(Objects.isNull(userDetail)){
            jsonObj.put("message", "登录失败,密码错误");
            return jsonObj;
        }
        String token = tokenService.getToken(userDetail);
        jsonObj.put("token", token);

        Cookie cookie = new Cookie("token", token);
        cookie.setPath("/");
        response.addCookie(cookie);
        return jsonObj;
    }


}

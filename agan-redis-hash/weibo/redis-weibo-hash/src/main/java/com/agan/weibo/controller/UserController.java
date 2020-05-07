package com.agan.weibo.controller;


import com.agan.weibo.entity.User;
import com.agan.weibo.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
/**
 * @author 阿甘
 * @see https://study.163.com/provider/1016671292/course.htm?share=1&shareId=1016481220
 * @version 1.0
 * 注：如有任何疑问欢迎阿甘老师微信：agan-java 随时咨询老师。
 */
@Api(description = "用户接口")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;


    @ApiOperation(value="微博注册")
    @PostMapping(value = "/createUser")
    public void createUser(@RequestBody UserVO userVO) {
        User user=new User();
        BeanUtils.copyProperties(userVO,user);
        userService.createUser(user);
    }
}

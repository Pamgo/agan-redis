package com.agan.weibo.controller;


import com.agan.weibo.service.RelationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;
/**
 * @author 阿甘
 * @see https://study.163.com/provider/1016671292/course.htm?share=1&shareId=1016481220
 * @version 1.0
 * 注：如有任何疑问欢迎阿甘老师微信：agan-java 随时咨询老师。
 */
@Api(description = "微博关系接口")
@RestController
@RequestMapping("/relation")
public class RelationController {

    @Autowired
    private RelationService relationService;

    /**
     * 阿甘关注了雷军，阿甘即使雷军的粉丝 follower
     * userId=阿甘
     * followeeId=雷军
     */
    @ApiOperation(value="关注")
    @PostMapping(value = "/follow")
    public void follow(Integer userId,Integer followeeId) {
        relationService.follow(userId,followeeId);
    }



    @ApiOperation(value="查看我的粉丝")
    @GetMapping(value = "/myFollower")
    public List<UserVO> myFollower(Integer userId){
        return this.relationService.myFollower(userId);
    }


    @ApiOperation(value="查看我的关注")
    @GetMapping(value = "/myFollowee")
    public List<UserVO> myFollowee(Integer userId){

        return this.relationService.myFollowee(userId);
    }



    @ApiOperation(value="求2个用户的关注交集")
    @GetMapping(value = "/intersect")
    public List<UserVO> intersect(Integer userId1, Integer userId2){
        return  this.relationService.intersect(userId1,userId2);
    }

}

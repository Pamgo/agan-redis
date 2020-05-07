package com.agan.weibo.controller;


import com.agan.weibo.common.PageResult;
import com.agan.weibo.entity.Content;
import com.agan.weibo.service.PushContentService;
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
@Api(description = "微博内容：push功能")
@RestController
@RequestMapping("/push-content")
public class PushContentController {

    @Autowired
    private PushContentService contentService;


    @ApiOperation(value="用户发微博")
    @PostMapping(value = "/post")
    public void post(@RequestBody ContentVO contentVO) {
        Content content=new Content();
        BeanUtils.copyProperties(contentVO,content);
        contentService.post(content);
    }



}

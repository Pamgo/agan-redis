package com.agan.weibo.controller;



import com.agan.weibo.common.PageResult;
import com.agan.weibo.entity.Content;
import com.agan.weibo.service.PullContentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(description = "微博内容：pull功能")
@RestController
@RequestMapping("/pull-content")
public class PullContentController {

    @Autowired
    private PullContentService contentService;

    @ApiOperation(value="用户发微博")
    @PostMapping(value = "/post")
    public void post(@RequestBody ContentVO contentVO) {
        Content content=new Content();
        BeanUtils.copyProperties(contentVO,content);
        contentService.post(content);
    }

    @ApiOperation(value="获取个人列表")
    @GetMapping(value = "/homeList")
    public PageResult<Content> getHomeList(Integer userId, int page, int size){
        return  this.contentService.homeList(userId,page,size);
    }

    @ApiOperation(value="获取关注列表")
    @GetMapping(value = "/attentionList")
    public PageResult<Content> attentionList(Integer userId, int page, int size){
        return  this.contentService.attentionList(userId,page,size);
    }
}

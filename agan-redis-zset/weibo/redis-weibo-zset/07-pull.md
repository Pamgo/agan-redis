### 3.案例实战:基于pull技术，实现微博个人列表
PullContentController
``` 

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

}

```
PullContentService
``` 
@Slf4j
@Service
public class PullContentService extends ContentService{

    /**
     * 用户发微博
     */
    public void post(Content obj){
        Content temp=this.addContent(obj);

        this.addMyPostBox(temp);
    }


    /**
     * 发布微博的时候，加入到我的个人列表
     */
    public void addMyPostBox(Content obj){
        String key= Constants.CACHE_MY_POST_BOX_ZSET_KEY+obj.getUserId();
        //按秒为单位
        long score=obj.getCreateTime().getTime()/1000;
        this.redisTemplate.opsForZSet().add(key,obj.getId(),score);
    }

    /**
     * 获取个人列表
     */
    public PageResult<Content> homeList(Integer userId, int page, int size){
        PageResult<Content> pageResult=new PageResult();
        List<Integer> list=new ArrayList<>();
        long start = (page - 1) * size;
        long end = start + size - 1;
        try {
            String key= Constants.CACHE_MY_POST_BOX_ZSET_KEY+userId;
            //1.设置总数
            long total=this.redisTemplate.opsForZSet().zCard(key);
            pageResult.setTotal(total);

            //2.分页查询
            //redis ZREVRANGE
            Set<ZSetOperations.TypedTuple<Integer>> rang= this.redisTemplate.opsForZSet().reverseRangeWithScores(key,start,end);
            for (ZSetOperations.TypedTuple<Integer> obj:rang){
                list.add(obj.getValue());
                log.info("个人post集合value={},score={}",obj.getValue(),obj.getScore());
            }

            //3.去拿明细数据
            List<Content> contents=this.getContents(list);
            pageResult.setRows(contents);
        }catch (Exception e){
            log.error("异常",e);
        }
        return pageResult;
    }

}

```

### 4.案例实战:基于pull技术，实现微博关注列表
``` 

    /**
     * 刷新拉取用户关注列表
     * 用户第一次刷新或定时刷新 触发
     */
    private void refreshAttentionBox(int userId){
        //获取刷新的时间
        String refreshkey=Constants.CACHE_REFRESH_TIME_KEY+userId;
        Long ago=(Long) this.redisTemplate.opsForValue().get(refreshkey);
        //如果时间为空，取2天前的时间
        if (ago==null){
            //当前时间
            long now=System.currentTimeMillis()/1000;
            //当前时间减去2天
            ago=now-60*60*24*2;
        }

        //提取该用户的关注列表
        String followerkey=Constants.CACHE_KEY_FOLLOWEE+userId;
        Set<Integer> sets= redisTemplate.opsForSet().members(followerkey);
        log.debug("用户={}的关注列表={}",followerkey,sets);

        //当前时间
        long now=System.currentTimeMillis()/1000;
        String attentionkey= Constants.CACHE_MY_ATTENTION_BOX_ZSET_KEY+userId;
        for (Integer id:sets){
            //去关注人的个人主页，拿最新微博
            String key= Constants.CACHE_MY_POST_BOX_ZSET_KEY+id;
            Set<ZSetOperations.TypedTuple<Integer>> rang= this.redisTemplate.opsForZSet().rangeByScoreWithScores(key,ago,now);
            if(!CollectionUtils.isEmpty(rang)){
                //加入我的关注post集合 就是通过上次刷新时间计算出最新的微博，写入关注zset集合；再更新刷新时间
                this.redisTemplate.opsForZSet().add(attentionkey,rang);
            }

        }

        //关注post集合 只留1000个
        //计算post集合，总数
        long count=this.redisTemplate.opsForZSet().zCard(attentionkey);
        //如果大于1000，就剔除多余的post
        if(count>1000){
            long end=count-1000;
            //redis ZREMRANGEBYRANK
            this.redisTemplate.opsForZSet().removeRange(attentionkey,0,end);
        }
        long days=this.redisTemplate.getExpire(attentionkey,TimeUnit.DAYS);
        if(days<10){
            //设置30天过期
            this.redisTemplate.expire(attentionkey,30,TimeUnit.DAYS);
        }

    }

```





### 体验


用户和id的数据库对应关系
阿甘->id=1  
雷军->id=2  
王石->id=3  
潘石岂->id=4  

关注顺序
阿甘关注：雷军 王石 潘石岂
雷军关注：王石 潘石岂
王石关注: 雷军
潘石岂关注：王石

1.雷军发微博：
第一条微博：小米手机涨价了100
第二条微博：小米手机降价1000
第三条微博：小米手机免费送啦  哈哈哈
2.体验个人列表，用雷军账户查看


3.体验关注列表，用阿甘账户查看




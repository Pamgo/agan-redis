-- 为某个接口的请求IP设置计数器，例如,当IP127.0.0.1请求商品接口时，key=product:127.0.0.1
local times = redis.call('incr',KEYS[1])
-- 当某个ip第一次请求时，为该ip的key设置超时时间。
if times == 1 then
    redis.call('expire',KEYS[1], ARGV[1])
end
-- tonumber就是把某个字符串转换为数字，
-- 例如 某个IP 30秒内，请求次数大于10，就返回0，反则 返回1
if times > tonumber(ARGV[2]) then
    return 0
end
return 1

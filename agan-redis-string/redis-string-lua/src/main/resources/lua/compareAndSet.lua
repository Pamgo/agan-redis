-- 如果redis没找到，就把值写进去
if redis.call('get', KEYS[1]) == nil then
   redis.call('set', KEYS[1], ARGV[1]);
   return 1
end

-- 如果旧值不等于新增，就把新增设置进去
if redis.call('get', KEYS[1]) ~= ARGV[1]  then
   redis.call('set', KEYS[1], ARGV[1]);
   return 1
else
   return 0
end
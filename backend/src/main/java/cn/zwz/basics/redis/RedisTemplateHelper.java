package cn.zwz.basics.redis;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;


@ApiOperation(value = "Redis Utility Class")  //Redis工具类
@Component
public class RedisTemplateHelper {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @ApiOperation(value = "Scan implementation")  //scan实现
    private void scan(String wayForScan, Consumer<byte[]> consumableList) {
        redisTemplate.execute((RedisConnection connection) -> {
            try (Cursor<byte[]> cursor = connection.scan(ScanOptions.scanOptions().count(Long.MAX_VALUE).match(wayForScan).build())) {
                cursor.forEachRemaining(consumableList);
                return null;
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        });
    }

    @ApiOperation(value = "Get keys that meet the criteria using scan") //scan获取符合条件的key
    public Set<String> scan(String pattern) {

        Set<String> keys = new HashSet<>();
        this.scan(pattern, item -> {
            String key = new String(item, StandardCharsets.UTF_8);
            keys.add(key);
        });
        return keys;
    }

    @ApiOperation(value = "Delete all by wildcard expression") //通过通配符表达式删除所有
    public void deleteByPattern(String pattern) {
        Set<String> keys = this.scan(pattern);
        redisTemplate.delete(keys);
    }

    @ApiOperation(value = "Delete a key") //删除key
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    @ApiOperation(value = "Bulk delete keys") //批量删除key
    public void delete(Collection<String> keys) {
        redisTemplate.delete(keys);
    }

    @ApiOperation(value = "Serialize a key") //序列化key
    public byte[] dump(String key) {
        return redisTemplate.dump(key);
    }

    @ApiOperation(value = "Check if a key exists") //是否存在key
    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    @ApiOperation(value = "Set key expiration time") //设置过期时间
    public Boolean expire(String key, long timeout, TimeUnit unit) {
        return redisTemplate.expire(key, timeout, unit);
    }

    @ApiOperation(value = "Set key expiration at specific date") //设置过期时间
    public Boolean expireAt(String key, Date date) {
        return redisTemplate.expireAt(key, date);
    }

    @ApiOperation(value = "Find keys matching a pattern") //查找匹配的key
    public Set<String> keys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    @ApiOperation(value = "Move a key to another database") //将当前数据库的 key 移动到给定的数据库 db 当中
    public Boolean move(String key, int dbIndex) {
        return redisTemplate.move(key, dbIndex);
    }

    @ApiOperation(value = "Remove key's expiration time, making the key persistent") //移除 key 的过期时间，key 将持久保持
    public Boolean persist(String key) {
        return redisTemplate.persist(key);
    }

    @ApiOperation(value = "Retrieve remaining expiration time of a key") //返回 key 的剩余的过期时间
    public Long getExpire(String key, TimeUnit unit) {
        return redisTemplate.getExpire(key, unit);
    }

    @ApiOperation(value = "Retrieve remaining expiration time of a key") //返回 key 的剩余的过期时间
    public Long getExpire(String key) {
        return redisTemplate.getExpire(key);
    }

    @ApiOperation(value = "Randomly retrieve a key from the current database") //从当前数据库中随机返回一个 key
    public String randomKey() {
        return redisTemplate.randomKey();
    }

    @ApiOperation(value = "Rename a key") //修改 key 的名称
    public void rename(String oldKey, String newKey) {
        redisTemplate.rename(oldKey, newKey);
    }

    @ApiOperation(value = "Rename a key if the new key does not exist") //仅当 newkey 不存在时，将 oldKey 改名为 newkey
    public Boolean renameIfAbsent(String oldKey, String newKey) {
        return redisTemplate.renameIfAbsent(oldKey, newKey);
    }

    @ApiOperation(value = "Get the type of value stored by a key") //返回 key 所储存的值的类型
    public DataType type(String key) {
        return redisTemplate.type(key);
    }

    /** -------------------string相关操作--------------------- */

    @ApiOperation(value = "Set the value of a specific key") //设置指定 key 的值
    public void set(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @ApiOperation(value = "Associate value with key and set the expiration time of key to timeout") //将值 value 关联到 key ，并将 key 的过期时间设为 timeout
    public void set(String key, String value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    @ApiOperation(value = "Get the value of a specific key") //获取指定 key 的值
    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @ApiOperation(value = "Return a substring value of the key") //返回 key 中字符串值的子字符
    public String getRange(String key, long start, long end) {
        return redisTemplate.opsForValue().get(key, start, end);
    }

    @ApiOperation(value = "Set the value of a given key to value and return the old value of the key") //将给定 key 的值设为 value ，并返回 key 的旧值(old value)
    public String getAndSet(String key, String value) {
        return redisTemplate.opsForValue().getAndSet(key, value);
    }

    @ApiOperation(value = "Get the bit value of the string stored at the specified offset for a key") //对 key 所储存的字符串值，获取指定偏移量上的位(bit)
    public Boolean getBit(String key, long offset) {
        return redisTemplate.opsForValue().getBit(key, offset);
    }

    @ApiOperation(value = "Batch retrieve") //批量获取
    public List<String> multiGet(Collection<String> keys) {
        return redisTemplate.opsForValue().multiGet(keys);
    }

    @ApiOperation(value = "Set ASCII code. The ASCII code for the string 'a' is 97, and its binary representation is '01100001'. This method sets the bit at the specified offset to the value",
            notes = "offset position, value: true for 1, false for 0")
    //设置ASCII码, 字符串'a'的ASCII码是97, 转为二进制是'01100001', 此方法是将二进制第offset位值变为value",notes = "offset 位置, value: 值,true为1, false为0
    public boolean setBit(String key, long offset, boolean value) {
        return redisTemplate.opsForValue().setBit(key, offset, value);
    }

    @ApiOperation(value = "Set the value of a key only if the key does not exist",
            notes = "Return false if it previously existed, true if it did not exist")
    //只有在 key 不存在时设置 key 的值",notes = "之前已经存在返回false, 不存在返回true
    public boolean setIfAbsent(String key, String value) {
        return redisTemplate.opsForValue().setIfAbsent(key, value);
    }

    @ApiOperation(value = "Override the string value stored at the specified key with the value parameter starting from offset",
            notes = "offset: Start overwriting from the specified position")
    //用 value 参数覆写给定 key 所储存的字符串值，从偏移量 offset 开始",notes = "offset:从指定位置开始覆写
    public void setRange(String key, String value, long offset) {
        redisTemplate.opsForValue().set(key, value, offset);
    }

    @ApiOperation(value = "Get the length of the string") //获取字符串的长度
    public Long size(String key) {
        return redisTemplate.opsForValue().size(key);
    }

    @ApiOperation(value = "Batch addition") //批量添加
    public void multiSet(Map<String, String> maps) {
        redisTemplate.opsForValue().multiSet(maps);
    }

    @ApiOperation(value = "Set one or more key-value pairs only when none of the given keys exist") //同时设置一个或多个 key-value 对，当且仅当所有给定 key 都不存在
    public boolean multiSetIfAbsent(Map<String, String> maps) {
        return redisTemplate.opsForValue().multiSetIfAbsent(maps);
    }

    @ApiOperation(value = "Increase (increment). If negative, it's a decrement") //增加(自增长), 负数则为自减
    public Long incrBy(String key, long increment) {
        return redisTemplate.opsForValue().increment(key, increment);
    }

    @ApiOperation(value = "Increase (increment)") //增加(自增长)
    public Double incrByFloat(String key, double increment) {
        return redisTemplate.opsForValue().increment(key, increment);
    }

    @ApiOperation(value = "Append to the end") //追加到末尾
    public Integer append(String key, String value) {
        return redisTemplate.opsForValue().append(key, value);
    }

    // hash表
    @ApiOperation(value = "Retrieve the value of a specified field stored in a hash table") //获取存储在哈希表中指定字段的值
    public Object hGet(String key, String field) {
        return redisTemplate.opsForHash().get(key, field);
    }

    @ApiOperation(value = "Get the values of all given fields") //获取所有给定字段的值
    public Map<Object, Object> hGetAll(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    @ApiOperation(value = "Get the values of all given fields") //获取所有给定字段的值
    public List<Object> hMultiGet(String key, Collection<Object> fields) {
        return redisTemplate.opsForHash().multiGet(key, fields);
    }

    @ApiOperation(value = "Add a single entry") //添加单个
    public void hPut(String key, String hashKey, String value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    @ApiOperation(value = "Add a collection") //添加集合
    public void hPutAll(String key, Map<String, String> maps) {
        redisTemplate.opsForHash().putAll(key, maps);
    }

    @ApiOperation(value = "Set only if hashKey does not exist") //仅当hashKey不存在时才设置
    public Boolean hPutIfAbsent(String key, String hashKey, String value) {
        return redisTemplate.opsForHash().putIfAbsent(key, hashKey, value);
    }

    @ApiOperation(value = "Delete one or more fields from the hash table") //删除一个或多个哈希表字段
    public Long hDelete(String key, Object... fields) {
        return redisTemplate.opsForHash().delete(key, fields);
    }

    @ApiOperation(value = "Check if a specified field exists in the hash table key") //查看哈希表 key 中，指定的字段是否存在
    public boolean hExists(String key, String field) {
        return redisTemplate.opsForHash().hasKey(key, field);
    }

    @ApiOperation(value = "Add an increment to the integer value of a specified field in hash table key")
    //为哈希表 key 中的指定字段的整数值加上增量 increment
    public Long hIncrBy(String key, Object field, long increment) {
        return redisTemplate.opsForHash().increment(key, field, increment);
    }

    @ApiOperation(value = "Add an increment to the integer value of a specified field in hash table key")
    //为哈希表 key 中的指定字段的整数值加上增量 increment
    public Double hIncrByFloat(String key, Object field, double delta) {
        return redisTemplate.opsForHash().increment(key, field, delta);
    }

    @ApiOperation(value = "Get all fields from the hash table") //获取所有哈希表中的字段
    public Set<Object> hKeys(String key) {
        return redisTemplate.opsForHash().keys(key);
    }

    @ApiOperation(value = "Get the number of fields in the hash table") //获取哈希表中字段的数量
    public Long hSize(String key) {
        return redisTemplate.opsForHash().size(key);
    }

    @ApiOperation(value = "Get all values from the hash table") //获取哈希表中所有值
    public List<Object> hValues(String key) {
        return redisTemplate.opsForHash().values(key);
    }

    @ApiOperation(value = "Iterate over key-value pairs in the hash table") //迭代哈希表中的键值对
    public Cursor<Map.Entry<Object, Object>> hScan(String key, ScanOptions options) {
        return redisTemplate.opsForHash().scan(key, options);
    }

    // list operations
    @ApiOperation(value = "Get an element from the list by its index") //通过索引获取列表中的元素
    public String lIndex(String key, long index) {
        return redisTemplate.opsForList().index(key, index);
    }

    @ApiOperation(value = "Get elements within a specific range from the list") //获取列表指定范围内的元素
    public List<String> lRange(String key, long start, long end) {
        return redisTemplate.opsForList().range(key, start, end);
    }

    @ApiOperation(value = "Store at the head of the list") //存储在list头部
    public Long lLeftPush(String key, String value) {
        return redisTemplate.opsForList().leftPush(key, value);
    }
    public Long lLeftPushAll(String key, String... value) {
        return redisTemplate.opsForList().leftPushAll(key, value);
    }

    public Long lLeftPushAll(String key, Collection<String> value) {
        return redisTemplate.opsForList().leftPushAll(key, value);
    }

    @ApiOperation(value = "Push only when the list exists") //当list存在的时候才加入
    public Long lLeftPushIfPresent(String key, String value) {
        return redisTemplate.opsForList().leftPushIfPresent(key, value);
    }

    @ApiOperation(value = "Push in front of the pivot if it exists") //如果pivot存在,再pivot前面添加
    public Long lLeftPush(String key, String pivot, String value) {
        return redisTemplate.opsForList().leftPush(key, pivot, value);
    }
    public Long lRightPush(String key, String value) {
        return redisTemplate.opsForList().rightPush(key, value);
    }

    public Long lRightPushAll(String key, String... value) {
        return redisTemplate.opsForList().rightPushAll(key, value);
    }

    public Long lRightPushAll(String key, Collection<String> value) {
        return redisTemplate.opsForList().rightPushAll(key, value);
    }
    @ApiOperation(value = "Add to an existing list") //为已存在的列表添加值
    public Long lRightPushIfPresent(String key, String value) {
        return redisTemplate.opsForList().rightPushIfPresent(key, value);
    }

    @ApiOperation(value = "Push to the right of the pivot element") //在pivot元素的右边添加值
    public Long lRightPush(String key, String pivot, String value) {
        return redisTemplate.opsForList().rightPush(key, pivot, value);
    }

    @ApiOperation(value = "Set the value of a list element by its index") //通过索引设置列表元素的值
    public void lSet(String key, long index, String value) {
        redisTemplate.opsForList().set(key, index, value);
    }

    @ApiOperation(value = "Remove and get the first element of the list") //移出并获取列表的第一个元素
    public String lLeftPop(String key) {
        return redisTemplate.opsForList().leftPop(key);
    }

    @ApiOperation(value = "Remove and get the first element of the list. If the list is empty, it will block until an item is available or a timeout occurs")
    //移出并获取列表的第一个元素， 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止
    public String lBLeftPop(String key, long timeout, TimeUnit unit) {
        return redisTemplate.opsForList().leftPop(key, timeout, unit);
    }

    @ApiOperation(value = "Remove and get the last element of the list") //移除并获取列表最后一个元素
    public String lRightPop(String key) {
        return redisTemplate.opsForList().rightPop(key);
    }

    @ApiOperation(value = "Remove and get the last element of the list. If the list is empty, it will block until an item is available or a timeout occurs")
    //移出并获取列表的最后一个元素， 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止
    public String lBRightPop(String key, long timeout, TimeUnit unit) {
        return redisTemplate.opsForList().rightPop(key, timeout, unit);
    }

    @ApiOperation(value = "Pop a value from a list, push it to another list and return it")
    //移除列表的最后一个元素，并将该元素添加到另一个列表并返回
    public String lRightPopAndLeftPush(String sourceKey, String destinationKey) {
        return redisTemplate.opsForList().rightPopAndLeftPush(sourceKey, destinationKey);
    }

    @ApiOperation(value = "Pop a value from a list and push it to another, blocking if necessary until an element is available or a timeout occurs")
    //从列表中弹出一个值，将弹出的元素插入到另外一个列表中并返回它； 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止
    public String lBRightPopAndLeftPush(String sourceKey, String destinationKey, long timeout, TimeUnit unit) {
        return redisTemplate.opsForList().rightPopAndLeftPush(sourceKey, destinationKey, timeout, unit);
    }

    @ApiOperation(value = "Remove elements from the list equal to the given value", notes = "index=0, remove all elements equal to value; index>0, remove first element from the head that equals value; index<0, remove the first element from the tail that equals value")
   //删除集合中值等于value的元素",notes = "index=0, 删除所有值等于value的元素; index>0, 从头部开始删除第一个值等于value的元素;index<0, 从尾部开始删除第一个值等于value的元素
    public Long lRemove(String key, long index, String value) {
        return redisTemplate.opsForList().remove(key, index, value);
    }

    @ApiOperation(value = "Trim the list") //裁剪list
    public void lTrim(String key, long start, long end) {
        redisTemplate.opsForList().trim(key, start, end);
    }

    @ApiOperation(value = "Get the length of the list") //获取列表长度
    public Long lLen(String key) {
        return redisTemplate.opsForList().size(key);
    }

    // set
    @ApiOperation(value = "Add elements to a set") //set添加元素
    public Long sAdd(String key, String... values) {
        return redisTemplate.opsForSet().add(key, values);
    }

    @ApiOperation(value = "Remove elements from a set") //set移除元素
    public Long sRemove(String key, Object... values) {
        return redisTemplate.opsForSet().remove(key, values);
    }

    @ApiOperation(value = "Remove and return a random element from the set") //移除并返回集合的一个随机元素
    public String sPop(String key) {
        return redisTemplate.opsForSet().pop(key);
    }

    @ApiOperation(value = "Move element 'value' from one set to another set") //将元素value从一个集合移到另一个集合
    public Boolean sMove(String key, String value, String destKey) {
        return redisTemplate.opsForSet().move(key, value, destKey);
    }

    @ApiOperation(value = "Get the size of the set") //获取集合的大小
    public Long sSize(String key) {
        return redisTemplate.opsForSet().size(key);
    }

    @ApiOperation(value = "Check if the set contains the value") //判断集合是否包含value
    public Boolean sIsMember(String key, Object value) {
        return redisTemplate.opsForSet().isMember(key, value);
    }

    @ApiOperation(value = "Get the intersection of two sets")  //获取两个集合的交集
    public Set<String> sIntersect(String key, String otherKey) {
        return redisTemplate.opsForSet().intersect(key, otherKey);
    }

    @ApiOperation(value = "Get the intersection of a set with multiple sets") //获取key集合与多个集合的交集
    public Set<String> sIntersect(String key, Collection<String> otherKeys) {
        return redisTemplate.opsForSet().intersect(key, otherKeys);
    }

    @ApiOperation(value = "Store the intersection of the set 'key' and set 'otherKey' into 'destKey'")
    //key集合与otherKey集合的交集存储到destKey集合中
    public Long sIntersectAndStore(String key, String otherKey, String destKey) {
        return redisTemplate.opsForSet().intersectAndStore(key, otherKey, destKey);
    }

    @ApiOperation(value = "Store the intersection of set 'key' with multiple sets into 'destKey'")
    //key集合与多个集合的交集存储到destKey集合中
    public Long sIntersectAndStore(String key, Collection<String> otherKeys,String destKey) {
        return redisTemplate.opsForSet().intersectAndStore(key, otherKeys, destKey);
    }

    @ApiOperation(value = "Get the union of two sets") //获取两个集合的并集
    public Set<String> sUnion(String key, String otherKeys) {
        return redisTemplate.opsForSet().union(key, otherKeys);
    }

    @ApiOperation(value = "Get the union of a set with multiple sets") //获取key集合与多个集合的并集
    public Set<String> sUnion(String key, Collection<String> otherKeys) {
        return redisTemplate.opsForSet().union(key, otherKeys);
    }

    @ApiOperation(value = "Store the union of the set 'key' and set 'otherKey' into 'destKey'")
    //key集合与otherKey集合的并集存储到destKey中
    public Long sUnionAndStore(String key, String otherKey, String destKey) {
        return redisTemplate.opsForSet().unionAndStore(key, otherKey, destKey);
    }

    @ApiOperation(value = "Store the union of set 'key' with multiple sets into 'destKey'")
    //key集合与多个集合的并集存储到destKey中
    public Long sUnionAndStore(String key, Collection<String> otherKeys,String destKey) {
        return redisTemplate.opsForSet().unionAndStore(key, otherKeys, destKey);
    }

    @ApiOperation(value = "Get the difference between two sets") //获取两个集合的差集
    public Set<String> sDifference(String key, String otherKey) {
        return redisTemplate.opsForSet().difference(key, otherKey);
    }

    @ApiOperation(value = "Get the difference of a set with multiple sets") //获取key集合与多个集合的差集
    public Set<String> sDifference(String key, Collection<String> otherKeys) {
        return redisTemplate.opsForSet().difference(key, otherKeys);
    }

    @ApiOperation(value = "Store the difference between the set 'key' and set 'otherKey' into 'destKey'")
    //key集合与otherKey集合的差集存储到destKey中
    public Long sDifference(String key, String otherKey, String destKey) {
        return redisTemplate.opsForSet().differenceAndStore(key, otherKey, destKey);
    }

    @ApiOperation(value = "Store the difference of set 'key' with multiple sets into 'destKey'")
    //key集合与多个集合的差集存储到destKey中
    public Long sDifference(String key, Collection<String> otherKeys,String destKey) {
        return redisTemplate.opsForSet().differenceAndStore(key, otherKeys,destKey);
    }

    @ApiOperation(value = "Return specified elements from the set") //返回集合指定元素
    public Set<String> setMembers(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    @ApiOperation(value = "Randomly get an element from the set") // 随机获取集合中的一个元素
    public String sRandomMember(String key) {
        return redisTemplate.opsForSet().randomMember(key);
    }

    @ApiOperation(value = "Randomly get 'count' elements from the set") //随机获取集合中count个元素
    public List<String> sRandomMembers(String key, long count) {
        return redisTemplate.opsForSet().randomMembers(key, count);
    }

    @ApiOperation(value = "Randomly get 'count' elements from the set and remove duplicates") //随机获取集合中count个元素并且去除重复的
    public Set<String> sDistinctRandomMembers(String key, long count) {
        return redisTemplate.opsForSet().distinctRandomMembers(key, count);
    }

    @ApiOperation(value = "Scan and return specified keys") //scan扫描返回指定key
    public Cursor<String> sScan(String key, ScanOptions options) {
        return redisTemplate.opsForSet().scan(key, options);
    }

    // zSet
    @ApiOperation(value = "Add element, the sorted set is ordered by the score value from smallest to largest") //添加元素,有序集合是按照元素的score值由小到大排列
    public Boolean zAdd(String key, String value, double score) {
        return redisTemplate.opsForZSet().add(key, value, score);
    }

    @ApiOperation(value = "Add set") //添加集合
    public Long zAdd(String key, Set<ZSetOperations.TypedTuple<String>> values) {
        return redisTemplate.opsForZSet().add(key, values);
    }

    @ApiOperation(value = "Remove") //移除
    public Long zRemove(String key, Object... values) {
        return redisTemplate.opsForZSet().remove(key, values);
    }

    @ApiOperation(value = "Increase the score value of an element and return the increased value") //增加元素的score值，并返回增加后的值
    public Double zIncrementScore(String key, String value, double delta) {
        return redisTemplate.opsForZSet().incrementScore(key, value, delta);
    }

    @ApiOperation(value = "Return the ranking of the element in the set, the sorted set is ordered by the score value from smallest to largest")
    //返回元素在集合的排名,有序集合是按照元素的score值由小到大排列
    public Long zRank(String key, Object value) {
        return redisTemplate.opsForZSet().rank(key, value);
    }

    @ApiOperation(value = "Return the ranking of the element in the set, ordered by score value from largest to smallest")
    //返回元素在集合的排名,按元素的score值由大到小排列
    public Long zReverseRank(String key, Object value) {
        return redisTemplate.opsForZSet().reverseRank(key, value);
    }

    @ApiOperation(value = "Get the elements of the set, ordered from smallest to largest")
    //获取集合的元素, 从小到大排序
    public Set<String> zRange(String key, long start, long end) {
        return redisTemplate.opsForZSet().range(key, start, end);
    }

    @ApiOperation(value = "Get the elements of the set and also retrieve the score value")
    //获取集合元素, 并且把score值也获取
    public Set<ZSetOperations.TypedTuple<String>> zRangeWithScores(String key, long start, long end) {
        return redisTemplate.opsForZSet().rangeWithScores(key, start, end);
    }

    @ApiOperation(value = "Query set elements based on Score value") //根据Score值查询集合元素
    public Set<String> zRangeByScore(String key, double min, double max) {
        return redisTemplate.opsForZSet().rangeByScore(key, min, max);
    }


    @ApiOperation(value = "Query set elements by Score value, ordered from smallest to largest")
    //根据Score值查询集合元素, 从小到大排序
    public Set<ZSetOperations.TypedTuple<String>> zRangeByScoreWithScores(String key, double min, double max) {
        return redisTemplate.opsForZSet().rangeByScoreWithScores(key, min, max);
    }

    @ApiOperation(value = "Query set elements by Score value and specified position, ordered from smallest to largest")
    //根据Score值和指定位置查询集合元素, 从小到大排序
    public Set<ZSetOperations.TypedTuple<String>> zRangeByScoreWithScores(String key, double min, double max, long start, long end) {
        return redisTemplate.opsForZSet().rangeByScoreWithScores(key, min, max, start, end);
    }

    @ApiOperation(value = "Get the set elements, ordered from largest to smallest")
    //获取集合的元素, 从大到小排序
    public Set<String> zReverseRange(String key, long start, long end) {
        return redisTemplate.opsForZSet().reverseRange(key, start, end);
    }

    @ApiOperation(value = "Get the set elements, ordered from largest to smallest, and also return the score value")
    //获取集合的元素, 从大到小排序, 并返回score值
    public Set<ZSetOperations.TypedTuple<String>> zReverseRangeWithScores(String key, long start, long end) {
        return redisTemplate.opsForZSet().reverseRangeWithScores(key, start, end);
    }

    @ApiOperation(value = "Query set elements by Score value, ordered from largest to smallest")
    //根据Score值查询集合元素, 从大到小排序
    public Set<String> zReverseRangeByScore(String key, double min, double max) {
        return redisTemplate.opsForZSet().reverseRangeByScore(key, min, max);
    }

    @ApiOperation(value = "Query set elements by Score value, ordered from largest to smallest")
    //根据Score值查询集合元素, 从大到小排序
    public Set<ZSetOperations.TypedTuple<String>> zReverseRangeByScoreWithScores(String key, double min, double max) {
        return redisTemplate.opsForZSet().reverseRangeByScoreWithScores(key, min, max);
    }

    @ApiOperation(value = "Query set elements by Score value and specified position, ordered from largest to smallest")
    //根据Score值和指定位置查询集合元素, 从大到小排序
    public Set<String> zReverseRangeByScore(String key, double min, double max, long start, long end) {
        return redisTemplate.opsForZSet().reverseRangeByScore(key, min, max, start, end);
    }

    @ApiOperation(value = "Get the number of set elements by score value")
    //根据score值获取集合元素数量
    public Long zCount(String key, double min, double max) {
        return redisTemplate.opsForZSet().count(key, min, max);
    }

    @ApiOperation(value = "Get the size of the set")
    //获取集合大小
    public Long zSize(String key) {
        return redisTemplate.opsForZSet().size(key);
    }

    @ApiOperation(value = "Get the size of the set")//获取集合大小
    public Long zZCard(String key) {
        return redisTemplate.opsForZSet().zCard(key);
    }

    @ApiOperation(value = "Get the score value of the value element in the set") //获取集合中value元素的score值
    public Double zScore(String key, Object value) {
        return redisTemplate.opsForZSet().score(key, value);
    }

    @ApiOperation(value = "Remove members at specified index positions") //移除指定索引位置的成员
    public Long zRemoveRange(String key, long start, long end) {
        return redisTemplate.opsForZSet().removeRange(key, start, end);
    }

    @ApiOperation(value = "Remove members by specified score value range") //根据指定的score值的范围来移除成员
    public Long zRemoveRangeByScore(String key, double min, double max) {
        return redisTemplate.opsForZSet().removeRangeByScore(key, min, max);
    }

    @ApiOperation(value = "Get the union of key and otherKey and store in destKey") //获取key和otherKey的并集并存储在destKey中
    public Long zUnionAndStore(String key, String otherKey, String destKey) {
        return redisTemplate.opsForZSet().unionAndStore(key, otherKey, destKey);
    }

    @ApiOperation(value = "Get the union of key and otherKeys and store in destKey") //获取key和otherKey的并集并存储在destKey中
    public Long zUnionAndStore(String key, Collection<String> otherKeys, String destKey) {
        return redisTemplate.opsForZSet().unionAndStore(key, otherKeys, destKey);
    }

    @ApiOperation(value = "Intersection") //交集
    public Long zIntersectAndStore(String key, String otherKey, String destKey) {
        return redisTemplate.opsForZSet().intersectAndStore(key, otherKey, destKey);
    }

    @ApiOperation(value = "Intersection") //交集
    public Long zIntersectAndStore(String key, Collection<String> otherKeys, String destKey) {
        return redisTemplate.opsForZSet().intersectAndStore(key, otherKeys, destKey);
    }

    @ApiOperation(value = "Scan specified keys") //scan扫描指定key
    public Cursor<ZSetOperations.TypedTuple<String>> zScan(String key, ScanOptions options) {
        return redisTemplate.opsForZSet().scan(key, options);
    }

    @ApiOperation(value = "Get the connection factory") //获得连接工厂
    public RedisConnectionFactory getConnectionFactory() {
        return redisTemplate.getConnectionFactory();
    }


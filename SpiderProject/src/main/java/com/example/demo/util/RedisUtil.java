package com.example.demo.util;//package com.example.spider;
//
//import redis.clients.jedis.Jedis;
//import redis.clients.jedis.Pipeline;
//import redis.clients.jedis.Response;
//import redis.clients.jedis.Transaction;
//
//import java.io.IOException;
//import java.util.Set;
//
//public class RedisUtil {
//    private static final String FUTURE_URLS = "future_urls";
//    private static final String OK_URLS = "ok_urls";
//    private static final String FAIL_URLS = "fail_urls";
//    private static final String DOWNLOADING_URLS = "downloading_urls";
//
//    public static Jedis redis = new Jedis("127.0.0.1",6379);
//
//    public static Transaction beginTx(){
//        Transaction tx = redis.multi();
//        return tx;
//    }
//    public static Pipeline pipeline(){
//        return redis.pipelined();
//    }
//
//    /**
//     * 添加到将来集合
//     * sorted_set：不重复，有序
//     * 以系统时间作为score，实现队列的效果，并且去重
//     * 集合中的key是http://www.sohu.com/1/1.html#n，n表示链接级别
//     */
//    public static void addToFutures(String url, int n){
//        String key = getKeyName(url, RedisUtil.FUTURE_URLS);
//        redis.zadd(key, System.currentTimeMillis(), url + "#" + n);
//    }
//
//    /**
//     * 操作组合url
//     * @param combUrl
//     */
//    public static void addToFutures(String combUrl){
//        String url = UrlUtil.extractUrl(combUrl);
//        int level = UrlUtil.extractLevel(combUrl);
//        addToFutures(url, level);
//    }
//
//
//    /**
//     * 添加到ok集合
//     * Set：不重复，无序
//     */
//    public static void addToOks(String url){
//        String key = getKeyName(url, RedisUtil.OK_URLS);
//        redis.sadd(key, url);
//    }
//
//    /**
//     * 在失败结合中累加1
//     * hash：key(url) - value(次数)
//     */
//    public static Long incrFails(String url){
//        String key = getKeyName(url, RedisUtil.FAIL_URLS);
//        return redis.hincrBy(key, url, 1);
//    }
//
//    /**
//     * 添加到下载集合
//     * Set：不重复，无序
//     */
//    public static void addToDownloadings(String url,int level){
//        String key = getKeyName(url, RedisUtil.DOWNLOADING_URLS);
//        redis.sadd(key, url + "#" + level);
//
//    }
//
//    /**
//     *从将来集中剪切出下一个comboUrl地址
//     */
//    public static String getNextUrl(String url){
//        String key = getKeyName(url, RedisUtil.FUTURE_URLS);
//        Response<Set<String>> list = null;
//        //启动事务
//        Transaction tx = redis.multi();
//        //查询最上面的元素
//        list = tx.zrange(key, 0, 0);
//        //将其删除
//        tx.zremrangeByRank(key, 0, 0);
//        //提交事务
//        tx.exec();
//        //get()需要在exec之后访问
//        Set<String> urls = list.get();
//        if(urls != null && !urls.isEmpty()){
//            return urls.iterator().next();
//        }
//        return null;
//    }
//
//    /**
//     * 获得key的名称
//     */
//    public static String getKeyName(String url, String keyType){
//        String domain = SiteUtil.getDomainName(url);
//        return domain + "_" + keyType;
//    }
//
//    /**
//     * 判断将来集是否存在指定url
//     */
//    public static boolean existsInFutures(String url){
//        String key = getKeyName(url, FUTURE_URLS);
//        return redis.zrank(key, url) != null;
//    }
//
//    /**
//     * 判断ok集中是否存在指定url
//     */
//    public static boolean existsInOks(String url){
//        String key = getKeyName(url, OK_URLS);
//        return redis.sismember(key, url);
//    }
//
//    /**
//     * 判断失败集是否存在指定url
//     */
//    public static boolean existsInFails(String url){
//        String key = getKeyName(url, FAIL_URLS);
//        return redis.hexists(key, url);
//    }
//
//    /**
//     * 判断下载集是否存在指定url
//     */
//    public static boolean existsDownloadings(String url){
//        String key = getKeyName(url, DOWNLOADING_URLS);
//        return redis.sismember(key, url);
//    }
//
//    /**
//     * 获取指定url的下载失败次数
//     */
//    public static int getFailCounts(String url) {
//        String key = getKeyName(url, FAIL_URLS);
//        String count = redis.hget(key, url);
//        return (count == null) ? 0 : Integer.parseInt(count);
//    }
//
//    /**
//     * 从下载集中删除url
//     * @param url
//     */
//    public static void delFromDownloadings(String url, int level) {
//        String key = getKeyName(url, DOWNLOADING_URLS);
//        redis.srem(key, url + "#" + level);
//    }
//}

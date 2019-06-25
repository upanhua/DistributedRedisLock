package com.xuecheng.manage_cms.dao;

import org.redisson.Redisson;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

public class RedissonManager {
    private static final String RAtomicName = "genId_";

    private static Config config = new Config();

    private static RedissonClient redisson = null;

    public static void init(){
        try{
            /*config.useClusterServers()
                    .setScanInterval(200000)//设置集群状态扫描间隔
                    .setMasterConnectionPoolSize(10000)//设置对于master节点的连接池中连接数最大为10000
                    .setSlaveConnectionPoolSize(10000)//设置对于slave节点的连接池中连接数最大为500
                    .setIdleConnectionTimeout(10000)//如果当前连接池里的连接数量超过了最小空闲连接数，而同时有连接空闲时间超过了该数值，那么这些连接将会自动被关闭，并从连接池里去掉。时间单位是毫秒。
                    .setConnectTimeout(30000)//同任何节点建立连接时的等待超时。时间单位是毫秒。
                    .setTimeout(3000)//等待节点回复命令的时间。该时间从命令发送成功时开始计时。
                    .setRetryInterval(3000)//当与某个节点的连接断开时，等待与其重新建立连接的时间间隔。时间单位是毫秒。
                    .addNodeAddress("127.0.0.1:7001","127.0.0.1:7002","127.0.0.1:7003","127.0.0.1:7004","127.0.0.1:7005","127.0.0.1:7006");
*/
            config.useSingleServer().setAddress("127.0.0.1:6379").setPassword("");
            redisson = Redisson.create(config);

            RAtomicLong atomicLong = redisson.getAtomicLong(RAtomicName);
            atomicLong.set(1);//自增设置为从1开始
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static RedissonClient getRedisson(){
        if(redisson == null){
            RedissonManager.init(); //初始化
        }
        return redisson;
    }

    /** 获取redis中的原子ID */
    public static Long nextID(){
        RAtomicLong atomicLong = getRedisson().getAtomicLong(RAtomicName);
        atomicLong.incrementAndGet();
        return atomicLong.get();
    }

}

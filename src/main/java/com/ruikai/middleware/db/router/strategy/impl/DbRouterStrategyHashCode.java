package com.ruikai.middleware.db.router.strategy.impl;

import com.ruikai.middleware.db.router.DbContextHolder;
import com.ruikai.middleware.db.router.DbRouterConfig;
import com.ruikai.middleware.db.router.strategy.IDbRouterStrategy;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ruikai
 * @version 1.0
 * @date 2023/4/14 16:05
 */
@Slf4j
public class DbRouterStrategyHashCode implements IDbRouterStrategy {

    private DbRouterConfig dbRouterConfig;

    public DbRouterStrategyHashCode(DbRouterConfig dbRouterConfig) {
        this.dbRouterConfig = dbRouterConfig;
    }

    @Override
    public void doRouter(String dbKeyAttr) {
        int size = dbRouterConfig.getDbCount() * dbRouterConfig.getTbCount();
        //扰动函数,JDK hashMap中为使元素哈希散列更加均匀，添加了扰动给函数
        int idx = (size - 1) & (dbKeyAttr.hashCode() ^ (dbKeyAttr.hashCode() >>> 16));

        // 库表索引
        int dbIdx = idx / dbRouterConfig.getTbCount() + 1;
        int tbIdx = idx - dbRouterConfig.getTbCount() * (dbIdx - 1);

        setDbKey(dbIdx);
        setTbKey(tbIdx);
        log.debug("数据库路由 dbIdx： {}  tbIdx: {}", dbIdx, tbIdx);
    }

    @Override
    public void setDbKey(int dbIdx) {
        DbContextHolder.setDbKey(String.format("%02d", dbIdx));
    }

    @Override
    public void setTbKey(int tbIdx) {
        DbContextHolder.setTbKey(String.format("%03d", tbIdx));
    }

    @Override
    public int dbCount() {
        return dbRouterConfig.getDbCount();
    }

    @Override
    public int tbCount() {
        return dbRouterConfig.getTbCount();
    }

    @Override
    public void clear() {
        DbContextHolder.clearDbKey();
        DbContextHolder.clearTbKey();
    }
}

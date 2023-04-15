package com.ruikai.middleware.db.router.strategy;

/**
 * @author ruikai
 * @version 1.0
 * @date 2023/4/14 15:36
 * 路由策略
 */
public interface IDbRouterStrategy {

    /**
     * 路由计算
     * @param dbKeyAttr 路由字段
     */
    void doRouter(String dbKeyAttr);

    /**
     * 手动设置分库路由
     * @param dbIdx 路由库
     */
    void setDbKey(int dbIdx);

    /**
     * 手动设置分表路由
     * @param tbIdx 路由表
     */
    void setTbKey(int tbIdx);

    /**
     * 获取分库数
     * @return 数量
     */
    int dbCount();

    /**
     * 获取分表数
     * @return 数量
     */
    int tbCount();

    /**
     * 清楚路由
     */
    void clear();
}

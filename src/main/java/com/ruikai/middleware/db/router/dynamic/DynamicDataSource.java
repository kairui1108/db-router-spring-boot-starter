package com.ruikai.middleware.db.router.dynamic;

import com.ruikai.middleware.db.router.DbContextHolder;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @author ruikai
 * @version 1.0
 * @date 2023/4/14 17:13
 * 动态数据源获取，切换数据源时从这个里面获取
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        return "db" + DbContextHolder.getDbKey();
    }
}

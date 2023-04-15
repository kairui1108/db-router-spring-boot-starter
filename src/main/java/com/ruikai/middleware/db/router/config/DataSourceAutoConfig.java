package com.ruikai.middleware.db.router.config;

import com.ruikai.middleware.db.router.DbRouterJoinPoint;
import com.ruikai.middleware.db.router.DbRouterConfig;
import com.ruikai.middleware.db.router.dynamic.DynamicDataSource;
import com.ruikai.middleware.db.router.dynamic.DynamicMybatisPlugin;
import com.ruikai.middleware.db.router.strategy.IDbRouterStrategy;
import com.ruikai.middleware.db.router.strategy.impl.DbRouterStrategyHashCode;
import com.ruikai.middleware.db.router.util.PropertyUtil;
import org.apache.ibatis.plugin.Interceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author ruikai
 * @version 1.0
 * @date 2023/4/14 15:25
 */
@Configuration
public class DataSourceAutoConfig  implements EnvironmentAware {

    /**
     * 数据源配置组
     */
    private Map<String, Map<String, Object>> dataSourceMap = new HashMap<>();

    /**
     * 默认数据源配置
     */
    private Map<String, Object> defaultDatasourceConfig;

    /**
     * 分库数量
     */
    private int dbCount;

    /**
     * 分表数量
     */
    private int tbCount;

    /**
     * 路由字段
     */
    private String routeKey;

    @Bean(name = "db-router-point")
    @ConditionalOnMissingBean
    public DbRouterJoinPoint point(DbRouterConfig dbRouterConfig, IDbRouterStrategy dbRouterStrategy) {
        return new DbRouterJoinPoint(dbRouterConfig, dbRouterStrategy);
    }

    @Bean
    public DbRouterConfig dbRouterConfig() {
        return new DbRouterConfig(dbCount, tbCount, routeKey);
    }

    @Bean
    public IDbRouterStrategy dbRouterStrategy(DbRouterConfig dbRouterConfig) {
        return new DbRouterStrategyHashCode(dbRouterConfig);
    }

    @Bean
    public Interceptor plugin() {
        return new DynamicMybatisPlugin();
    }

    @Bean
    public DataSource dataSource() {
        Map<Object, Object> targetDataSources = new HashMap<>();
        for (String dbInfo : dataSourceMap.keySet()) {
            Map<String, Object> objMap = dataSourceMap.get(dbInfo);
            targetDataSources.put(dbInfo,
                    new DriverManagerDataSource(objMap.get("url").toString(), objMap.get("username").toString(), objMap.get("password").toString()));
        }
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        dynamicDataSource.setTargetDataSources(targetDataSources);
        dynamicDataSource.setDefaultTargetDataSource(new DriverManagerDataSource(
                defaultDatasourceConfig.get("url").toString(), defaultDatasourceConfig.get("username").toString(), defaultDatasourceConfig.get("password").toString()));

        return dynamicDataSource;
    }

    @Bean
    public TransactionTemplate transactionTemplate(DataSource dataSource) {
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
        dataSourceTransactionManager.setDataSource(dataSource);

        TransactionTemplate transactionTemplate = new TransactionTemplate();
        transactionTemplate.setTransactionManager(dataSourceTransactionManager);
        transactionTemplate.setPropagationBehaviorName("PROPAGATION_REQUIRED");

        return transactionTemplate;
    }

    /**
     *
     * @param environment
     */
    @Override
    public void setEnvironment(Environment environment) {
        String prefix = "mini-db-router.jdbc.datasource.";

        dbCount = Integer.parseInt(Objects.requireNonNull(environment.getProperty(prefix + "dbCount")));
        tbCount = Integer.parseInt(Objects.requireNonNull(environment.getProperty(prefix + "tbCount")));
        routeKey = environment.getProperty(prefix + "routerKey");

        // 分库分表数据源
        String dataSource = environment.getProperty(prefix + "list");
        assert dataSource != null;
        for (String dbInfo : dataSource.split(",")) {
            Map<String, Object> dataSourceProps = PropertyUtil.handle(environment, prefix + dbInfo, Map.class);
            dataSourceMap.put(dbInfo, dataSourceProps);
        }

        // 默认数据源
        String defaultData = environment.getProperty(prefix + "default");
        defaultDatasourceConfig = PropertyUtil.handle(environment, prefix + defaultData, Map.class);
    }
}

package com.ruikai.middleware.db.router;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author ruikai
 * @version 1.0
 * @date 2023/4/14 15:32
 * 数据路由配置
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DbRouterConfig {

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
    private String routerKey;

}

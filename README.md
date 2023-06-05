# Db-Router-Starter

## 微型分库分表，数据库路由组件

**项目介绍**：基于AOP、Spring动态数据源切换、Mybatis插件开发、散列算法等技术，实现的SpringBoot Starter 数据库路由组件

**核心技术**：AOP,AbstractRoutingDataSource,MyBatis Plugin StatementHandler,扰动函数,哈希散列,ThreadLocal

**职责**:
- 设计分库分表数据库路由组件的架构模型结构，运用设计模式对这块组件进行功能的分治和实现
- 调研平方散列、除法散列、乘法散列、哈希散列以及斐波那契散列，并结合雪崩测试，选择适合散列算法，并做功能开发实现
- 引入Mybatis Plugin插件开发功能，动态修改sql语句，做到执行对应表的策略设计

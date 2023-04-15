package com.ruikai.middleware.db.router;

/**
 * @author ruikai
 * @version 1.0
 * @date 2023/4/14 15:59
 * 数据源上下文
 */
public class DbContextHolder {

    private static final ThreadLocal<String> dbKey = new ThreadLocal<>();
    private static final ThreadLocal<String> tbKey = new ThreadLocal<>();

    public static void setDbKey(String dbKeyIdx) {
        dbKey.set(dbKeyIdx);
    }

    public static String getDbKey() {
        return dbKey.get();
    }

    public static void setTbKey(String tbKeyIdx) {
        tbKey.set(tbKeyIdx);
    }

    public static String getTbKey() {
         return tbKey.get();
    }

    public static void clearDbKey() {
        dbKey.remove();
    }

    public static void clearTbKey() {
        tbKey.remove();
    }
}

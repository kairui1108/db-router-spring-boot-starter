package com.ruikai.middleware.test;

import com.ruikai.middleware.db.router.annotation.DbRouter;
import org.junit.Test;

import java.lang.reflect.Method;

/**
 * @author ruikai
 * @version 1.0
 * @date 2023/4/14 17:40
 */
public class ApiTest {
    public static void main(String[] args) {

    }

    @Test
    public void test_db_hash() {
        String key = "tanruikai";
        int dbCount = 2, tbCount = 4;
        int size = dbCount * tbCount;
        // 散列
        int idx = (size - 1) & (key.hashCode() ^ (key.hashCode() >>> 16));

        int dbIdx = idx / tbCount + 1;
        int tbIdx = idx - tbCount * (dbIdx - 1);

        System.out.println(dbIdx);
        System.out.println(tbIdx);
    }

    @Test
    public void test_annotation() throws NoSuchMethodException {
        Class<IUserDao> iUserDaoClass = IUserDao.class;
        Method method = iUserDaoClass.getMethod("insertUser", String.class);

        DbRouter dbRouter = method.getAnnotation(DbRouter.class);

        System.out.println(dbRouter.key());

    }

    @Test
    public void test_route() {
        String key = "Ukdli109op811d";

    }


}

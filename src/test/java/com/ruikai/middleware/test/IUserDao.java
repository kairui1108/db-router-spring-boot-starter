package com.ruikai.middleware.test;

import com.ruikai.middleware.db.router.annotation.DbRouter;

/**
 * @author ruikai
 * @version 1.0
 * @date 2023/4/14 17:39
 */
public interface IUserDao {

    @DbRouter(key = "userId")
    void insertUser(String req);
}

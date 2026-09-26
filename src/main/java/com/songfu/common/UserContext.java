package com.songfu.common;

/*
* 本地线程绑定
* */
public class UserContext {
    private static final ThreadLocal<Integer> USER_ID = new ThreadLocal<>();
    private static final ThreadLocal<String> USERNAME = new ThreadLocal<>();
    private static final ThreadLocal<Integer> ROLE = new ThreadLocal<>();

    public static void set(Integer id, String username, Integer role){
        USER_ID.set(id);
        USERNAME.set(username);
        ROLE.set(role);

    }
    public static Integer getUserId() {
        return USER_ID.get();
    }
    public static String getUsername() {
        return USERNAME.get();
    }
    public static Integer getRole() { return ROLE.get(); }

    public static void remove() {
        USER_ID.remove();
        USERNAME.remove();
        ROLE.remove();
    }
}

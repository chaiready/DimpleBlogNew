package com.dimple.common.constant;

/**
 * @className: Constants
 * @description: 通用常量信息
 * @auther: Dimple
 * @Date: 2019/3/13
 * @Version: 1.1
 */
public class Constants {
    /**
     * UTF-8 字符集
     */
    public static final String UTF8 = "UTF-8";

    /**
     * 通用成功标识
     */
    public static final String SUCCESS = "0";

    /**
     * 通用失败标识
     */
    public static final String FAIL = "1";

    /**
     * 登录成功
     */
    public static final String LOGIN_SUCCESS = "Success";

    /**
     * 注销
     */
    public static final String LOGOUT = "Logout";

    /**
     * 登录失败
     */
    public static final String LOGIN_FAIL = "Error";

    /**
     * 自动去除表前缀
     */
    public static String AUTO_REOMVE_PRE = "true";

    /**
     * 当前记录起始索引
     */
    public static String PAGE_NUM = "pageNum";

    /**
     * 每页显示记录数
     */
    public static String PAGE_SIZE = "pageSize";

    /**
     * 排序列
     */
    public static String ORDER_BY_COLUMN = "orderByColumn";

    /**
     * 排序的方向 "desc" 或者 "asc".
     */
    public static String IS_ASC = "isAsc";
    
    /**
     * 博客菜单数量
     */
    public static int BLOG_FUNC_COUNT = 3;
    
    /**
     * 博主角色key
     */
    public static String BLOG_ROLE_KEY = "blogger";
    
    public static int BLOG_PAGE_SIZE = 10;
    
    //博客首页图片数
    public static int BLOG_INDEX_PIC_COUNT = 3;
    
    //博客首页默认图片
    public static String BLOG_INDEX_PIC_URL="/front/images/touploadimg.jpg";


    public static String FILE_ITEM_ENTITYTYPE_BLOG = "blog";//附件实体类型-博客


    public static String STATUS_NORMAL = "1";
    public static String STATUS_DEL = "0";


    public static String ADMIN_LOGINNAME = "admin";
    
    //pdf在线预览路径
    public static String PDF_VIEWER = "/public/king/pdfjs_2_2_228/web/viewer.html";
    
    public static String EXAM_INDEX_PAGE = "/kaoshi";
}

package com.hss.modules.system.monitorThing;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * 分页工具类
 *
 * @author ruoyi
 */
public class PageUtils extends Page {
    /**
     * 设置请求分页数据
     */
    public static void startPage()
    {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
        Boolean reasonable = pageDomain.getReasonable();
        Page.of(pageNum,pageSize);
    }

//    /**
//     * 清理分页的线程变量
//     */
//    public static void clearPage()
//    {
//        Page.clearPage();
//    }
}

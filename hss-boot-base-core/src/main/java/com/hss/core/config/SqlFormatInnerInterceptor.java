package com.hss.core.config;

import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * sql格式化拦截器,用于将表名大写转换为小写
 * 之前是oracle, oracle表名默认大写，mysql表名默认小写，所以需要转换
 */

@Slf4j
@Component
public class SqlFormatInnerInterceptor implements InnerInterceptor {

    private static final Pattern TABLE_PATTERN = Pattern.compile(
            "\\b(FROM|JOIN|UPDATE|INSERT\\s+INTO|DELETE\\s+FROM)\\s+([A-Z_][A-Z0-9_]*)\\b",
            Pattern.CASE_INSENSITIVE
    );

    /**
     * 在执行SQL语句前进行预处理
     *
     * @param sh StatementHandler的实例，用于操作SQL语句
     * @param connection 数据库连接对象，可用于获取数据库相关信息
     * @param transactionTimeout 事务超时时间，可用于控制事务执行时间
     */
    @Override
    public void beforePrepare(StatementHandler sh, Connection connection, Integer transactionTimeout) {
        try {
            // 获取当前StatementHandler绑定的SQL语句和参数
            BoundSql boundSql = sh.getBoundSql();
            String originalSql = boundSql.getSql();

            // 如果原始SQL为空，则无需处理
            if (originalSql == null || originalSql.trim().isEmpty()) {
                return;
            }

            // 使用正则表达式匹配SQL中的表名，并进行小写及反引号处理
            Matcher matcher = TABLE_PATTERN.matcher(originalSql);
            StringBuffer newSql = new StringBuffer();

            // 处理匹配到的表名
            while (matcher.find()) {
                String keyword = matcher.group(1);
                String tableName = matcher.group(2);
                matcher.appendReplacement(newSql, keyword + " `" + tableName.toLowerCase() + "`");
            }
            matcher.appendTail(newSql);

            // 使用MetaObject修改BoundSql中的SQL语句
            MetaObject metaObject = SystemMetaObject.forObject(boundSql);
            metaObject.setValue("sql", newSql.toString());

            // 记录原始SQL和格式化后的SQL日志
//            log.info("Original SQL: {}", originalSql);
//            log.info("Formatted SQL: {}", newSql);

        } catch (Exception e) {
            // 记录SQL处理失败的错误日志
            log.error("SQL处理失败", e);
        }
    }
}

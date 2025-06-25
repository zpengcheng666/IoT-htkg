package com.hss.modules.util;

import cn.hutool.core.lang.func.VoidFunc0;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * @author 26060
 */
@Slf4j
public class TransactionUtil {

    public static void  transactionAfter(VoidFunc0 fun) {
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    try {
                        fun.call();
                    } catch (Exception e) {
                        log.error("事务回调错误", e);
                    }
                }
            });
        } else {
            try {
                fun.call();
            } catch (Exception e) {
                log.error("事务回调错误", e);
            }
        }
    }
}

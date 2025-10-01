package com.ada.genealogyapp.transaction;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.data.neo4j.core.transaction.Neo4jTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@Aspect
@Component
@Slf4j
public class TransactionAspect {

    private final PlatformTransactionManager transactionManager;

    public TransactionAspect(Neo4jTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }


    @Around("@annotation(com.ada.genealogyapp.transaction.TransactionalInNeo4j)")
    public Object manageTransaction(ProceedingJoinPoint joinPoint) throws Throwable {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName(joinPoint.getSignature().getName());
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

        TransactionStatus status = transactionManager.getTransaction(def);
        log.info("Transaction started for: {}", joinPoint.getSignature().getName());
        Object result;
        try {
            result = joinPoint.proceed();
            transactionManager.commit(status);
            log.info("Transaction committed for: {}", joinPoint.getSignature().getName());
        } catch (Throwable ex) {
            transactionManager.rollback(status);
            log.info("Transaction rolled back for: {} due to: {}", joinPoint.getSignature().getName(), ex.getMessage());
            throw ex;
        }
        return result;
    }
}

package com.ada.genealogyapp.transaction;

import lombok.extern.slf4j.Slf4j;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.neo4j.driver.SessionConfig;
import org.neo4j.driver.Transaction;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Slf4j
@Service
public class TransactionService {

    private final Driver driver;
    private Session currentSession;
    private Transaction currentTransaction;

    public TransactionService(Driver driver) {
        this.driver = driver;
    }

    public Transaction startTransactionAndSession() {
        if (currentSession == null || !currentSession.isOpen()) {
            currentSession = driver.session(SessionConfig.builder().withDatabase("neo4j").build());
        }
        if (currentTransaction == null || !currentTransaction.isOpen()) {
            currentTransaction = currentSession.beginTransaction();
        }
        log.info("Started transaction: {}", currentTransaction.toString());
        return currentTransaction;
    }

    public void commitChanges() {
        if (currentTransaction != null && currentTransaction.isOpen()) {
            try {
                currentTransaction.commit();
                log.info("Committed transaction: {}", currentTransaction);
            } finally {
                closeTransactionAndSession();
            }
        } else {
            log.warn("No active transaction to commit");
        }
    }

    public void rollbackChanges() {
        if (currentTransaction != null && currentTransaction.isOpen()) {
            try {
                log.info("Rolling back transaction...");
                currentTransaction.rollback();
                log.info("Transaction rolled back");
            } finally {
                closeTransactionAndSession();
            }
        } else {
            log.warn("No active transaction to roll back");
        }
    }


    public void closeTransactionAndSession() {
        log.info("Closing transaction: {}", currentTransaction);

        if (currentTransaction != null) {
            currentTransaction.close();
            currentTransaction = null;
        }
        if (currentSession != null) {
            currentSession.close();
            currentSession = null;
        }
    }

    public Transaction getCurrentTransaction() {
        if (currentTransaction == null || !currentTransaction.isOpen()) {
            throw new IllegalStateException("Transaction has not been started or already closed");
        }
        return currentTransaction;
    }

    public void runInTransaction(Consumer<Transaction> transactionConsumer) {
        try (Session session = driver.session(); Transaction tx = session.beginTransaction()) {
            transactionConsumer.accept(tx);
            tx.commit();
        } catch (Exception e) {
            throw new RuntimeException("Transaction failed", e);
        }
    }
}

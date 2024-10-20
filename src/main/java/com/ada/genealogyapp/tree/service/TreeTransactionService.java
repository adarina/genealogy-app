package com.ada.genealogyapp.tree.service;

import org.neo4j.driver.Driver;

import org.neo4j.driver.Session;
import org.neo4j.driver.SessionConfig;
import org.neo4j.driver.Transaction;
import org.springframework.stereotype.Service;

@Service
public class TreeTransactionService {

    private final Driver driver;
    private final ThreadLocal<Session> currentSession = new ThreadLocal<>();
    private final ThreadLocal<Transaction> currentTransaction = new ThreadLocal<>();

    public TreeTransactionService(Driver driver) {
        this.driver = driver;
    }

    public void startTransactionAndSession() {
        Session session = driver.session(SessionConfig.builder().withDatabase("neo4j").build());
        Transaction transaction = session.beginTransaction();

        currentSession.set(session);
        currentTransaction.set(transaction);
    }

    public void commitChanges() {
        Transaction tx = currentTransaction.get();
        if (tx != null) {
            try {
                tx.commit();
            } finally {
                closeTransactionAndSession();
            }
        }
    }

    public void rollbackChanges() {
        Transaction tx = currentTransaction.get();
        if (tx != null) {
            try {
                tx.rollback();
            } finally {
                closeTransactionAndSession();
            }
        }
    }

    private void closeTransactionAndSession() {
        Transaction tx = currentTransaction.get();
        Session session = currentSession.get();
        if (tx != null) {
            tx.close();
        }
        if (session != null) {
            session.close();
        }
        currentTransaction.remove();
        currentSession.remove();
    }

    public Transaction getCurrentTransaction() {
        Transaction tx = currentTransaction.get();
        if (tx == null) {
            throw new IllegalStateException("Transaction has not been started");
        }
        return tx;
    }
}
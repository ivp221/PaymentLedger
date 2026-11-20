package com.example.ledger.platform.db;

import java.sql.SQLException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

public final class TransientDbRetry {
    public static final int MAX_ATTEMPS = 3;
    public static final long BASE_DELAY_MS = 20;
    private TransientDbRetry(){}
    public static <T> T execute(Supplier<T> action){
        for (int attempt = 1; ; attempt++){
            try{
                return action.get();
            }catch(RuntimeException e){
                if (attempt > MAX_ATTEMPS || !isTransient(e)){
                    throw e;
                }
                sleep(attempt);
            }
        }
    }

    public static void run(Runnable action){
        execute(() ->{
            action.run();
            return null;
        });
    }

    static boolean isTransient(Throwable t){
        for (Throwable c = t; c != null; c = c.getCause()){
            if(c instanceof SQLException sql && ("40001".equals(sql.getSQLState()) || "40P01".equals(sql.getSQLState()))) return true;
            if(c.getCause() == c) break;
        }
        return false;
    }

    private static void sleep(int attempt){
        long ceiling = BASE_DELAY_MS << (attempt-1);
        long delay = ceiling + ThreadLocalRandom.current().nextLong(ceiling+1);
        try{
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Interrupted while retrying",e);
        }

    }



}

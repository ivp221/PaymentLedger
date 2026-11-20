package com.example.ledger.platform.id;

import java.security.SecureRandom;
import java.util.UUID;

public final class UUIDv7 {
    private static final SecureRandom RANDOM = new SecureRandom();
    private static long lastMillis = -1;
    private static int counter;

    private UUIDv7(){}
    public static synchronized UUID next(){
        long now = System.currentTimeMillis();
        if (now > lastMillis){
            lastMillis = now;
            counter = RANDOM.nextInt(0x800);
        }else {
            counter ++;
            if (counter > 0xFFF) {
                lastMillis++;
                counter = RANDOM.nextInt(0x800);
            }
        }
        long msb = lastMillis<<16 |(0x7L << 12)| counter;
        long lsb =(RANDOM.nextLong() & 0x3FFF_FFFF_FFFF_FFFFL) | 0x8000_0000_0000_0000L;
        return new UUID(msb,lsb);
    }

}

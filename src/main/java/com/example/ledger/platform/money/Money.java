package com.example.ledger.platform.money;

import java.util.Currency;
import java.util.Objects;

public record Money(long minorUnits, Currency currency) {

    public Money{
        if (currency.getDefaultFractionDigits() < 0){
            throw new IllegalArgumentException("");
        }
    }
    public static Money of(long minorUnits, String ISOCode){
        return new Money(minorUnits, Currency.getInstance(Objects.requireNonNull(ISOCode)));
    }

    public static Money zero(String ISOCode){
        return Money.of(0,ISOCode);
    }

    public void requiresSameCurrency(Money other){
        if (!currency.equals(other.currency())){
            throw new IllegalArgumentException("Currency mismatch");
        }
    }
    public Money plus(Money other){
        requiresSameCurrency(other);
        return new Money(Math.addExact(minorUnits, other.minorUnits()), currency);
    }

    public Money minus(Money other){
        requiresSameCurrency(other);
        return new Money(Math.subtractExact(minorUnits, other.minorUnits()), currency);
    }

    public Money negate(){
        return new Money(Math.negateExact(minorUnits),currency);
    }
    public boolean isPositive(){
        return minorUnits > 0;
    }
    public boolean isZero(){
        return minorUnits == 0;
    }
    public int fractionDigits(){
        return currency.getDefaultFractionDigits();
    }


}
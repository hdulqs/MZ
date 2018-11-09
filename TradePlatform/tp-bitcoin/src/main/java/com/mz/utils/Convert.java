package com.mz.utils;

import java.math.BigDecimal;

/**
 * <p> TODO</p>
 * @author:         shangxl
 * @Date :          2017年3月13日 下午7:38:15
 */
public final class Convert {
    private Convert() { }

    public static BigDecimal fromWei(String number, Unit unit) {
        return fromWei(new BigDecimal(number), unit);
    }

    public static BigDecimal fromWei(BigDecimal number, Unit unit) {
        return number.divide(unit.getWeiFactor());
    }

    public static BigDecimal toWei(String number, Unit unit) {
        return toWei(new BigDecimal(number), unit);
    }

    public static BigDecimal toWei(BigDecimal number, Unit unit) {
        return number.multiply(unit.getWeiFactor());
    }

    public enum Unit {
    	//精度0位
    	WEI("wei", 0),
    	//精度0位
        KWEI("kwei", 3),
        //精度0位
        MWEI("mwei", 6),
        //精度0位
        GWEI("gwei", 9),
        //精度0位
        SZABO("szabo", 12),
        //精度0位
        FINNEY("finney", 15),
        //精度0位
        ETHER("ether", 18),
        //精度0位
        KETHER("kether", 21),
        //精度0位
        METHER("mether", 24),
        //精度0位
        GETHER("gether", 27),
        //精度0位
    	ONE("one",1),
    	//精度0位
    	TWO("two",2),
    	//精度0位
    	FOUR("four",4),
    	//精度0位
    	FIVER("fiver",5),
    	//精度0位
    	SEVEN("seven",7),
    	//精度0位
    	EIGHT("eight",8),
    	//精度0位
    	TEN("ten",10),
    	//精度0位
    	ELEVEN("eleven",11),
    	//精度0位
    	THIRTEEN("thirteen",13);
    	
        private String name;
        private BigDecimal weiFactor;

        Unit(String name, int factor) {
            this.name = name;
            this.weiFactor = BigDecimal.TEN.pow(factor);
        }

        public BigDecimal getWeiFactor() {
            return weiFactor;
        }

        @Override
        public String toString() {
            return name;
        }

        public static Unit fromString(String name) {
            if (name != null) {
                for (Unit unit : Unit.values()) {
                    if (name.equalsIgnoreCase(unit.name)) {
                        return unit;
                    }
                }
            }
            return Unit.valueOf(name);
        }
    }
}

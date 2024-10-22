package org.algorithmtools.ad4j.enumtype;

public enum LogicType {
    AND,OR
    ;

    public static LogicType parse(String signal){
        if("and".equalsIgnoreCase(signal) || "&&".equals(signal)){
            return AND;
        }
        else if("or".equalsIgnoreCase(signal) || "||".equals(signal)){
            return OR;
        }
        throw new IllegalArgumentException("["+signal+"] signal illegal! Please input and,&&,or,||.");
    }

}

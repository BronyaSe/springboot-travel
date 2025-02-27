package com.bronya.travel.Utils;

import java.util.Random;

public class VerifyCodeGenUtils {
    public static String gencode(){
        int code = new Random().nextInt(999999);;
        if(code<100000){
            code+=100000;
        }
        return String.valueOf(code);
    }
}

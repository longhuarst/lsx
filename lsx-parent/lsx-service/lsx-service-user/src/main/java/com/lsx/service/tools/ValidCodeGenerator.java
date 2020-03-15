package com.lsx.service.tools;

import java.util.Random;

public class ValidCodeGenerator {


    public static String generate(){

        String str="ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb=new StringBuilder(4);
        for(int i=0;i<4;i++)
        {
            char ch=str.charAt(new Random().nextInt(str.length()));
            sb.append(ch);
        }
        System.out.println(sb.toString());

        return  sb.toString();
    }
}

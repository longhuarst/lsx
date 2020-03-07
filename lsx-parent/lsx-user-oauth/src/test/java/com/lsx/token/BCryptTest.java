package com.lsx.token;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCryptTest {



    @Test
    void testEncode(){

        System.out.println(new BCryptPasswordEncoder().encode("longhua").toString());


        System.out.println(new BCryptPasswordEncoder().encode("testpasswordx").toString());




    }
}

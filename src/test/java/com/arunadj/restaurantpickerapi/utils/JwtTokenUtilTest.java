package com.arunadj.restaurantpickerapi.utils;

import com.arunadj.restaurantpickerapi.ReflectionUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class JwtTokenUtilTest {

    private JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();

    @BeforeEach
    public void setup() throws NoSuchFieldException, IllegalAccessException {
        ReflectionUtils.setFieldValue(jwtTokenUtil, "secret", "4325dssg");
    }
    @Test
    public void testGenerationAndValidation() {
        User user = new User("aruna", "", List.of());
        String token = this.jwtTokenUtil.generateToken(user);
        Boolean validated = this.jwtTokenUtil.validateToken(token, user);
        Assertions.assertThat(validated).isTrue();
    }

    @Test
    public void testGetExpirationDateFromToken() {
        User user = new User("aruna", "", List.of());
        String token = this.jwtTokenUtil.generateToken(user);
        Date currentDate = new Date();

        Date expirationDateFromToken = this.jwtTokenUtil.getExpirationDateFromToken(token);
        Assertions.assertThat(new SimpleDateFormat("YYYYMMdd hhmm").format(expirationDateFromToken).toString()).isEqualTo(new SimpleDateFormat("YYYYMMdd hhmm").format(new Date(currentDate.getTime() + 5*3600*1000)));
    }

    @Test
    public void testGetUsernameFromToken() {
        User user = new User("aruna", "", List.of());
        String token = this.jwtTokenUtil.generateToken(user);

        String usernameFromToken = this.jwtTokenUtil.getUsernameFromToken(token);

        Assertions.assertThat(usernameFromToken).isEqualTo("aruna");
    }
}

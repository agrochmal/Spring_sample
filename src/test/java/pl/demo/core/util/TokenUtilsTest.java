package pl.demo.core.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import pl.demo.core.model.entity.Role;
import pl.demo.core.model.entity.RoleName;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class TokenUtilsTest {

    private final static String FAKE_TOKEN="admin@admin.pl:1431784298750:4b4227fc85c4a08ef6f648b234a2c8f5";

    private UserDetails userDetails = new UserDetails() {
        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {

            return Arrays.asList();
        }

        @Override
        public String getPassword() {
            return "76702638de8b35a1609f9cc19b744a3f726078a280b993fbf5fe4937985fcbbcbcc8b7099b448f91";
        }

        @Override
        public String getUsername() {
            return "admin@admin.pl";
        }

        @Override
        public boolean isAccountNonExpired() {
            return false;
        }

        @Override
        public boolean isAccountNonLocked() {
            return false;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return false;
        }

        @Override
        public boolean isEnabled() {
            return false;
        }
    };

    @Test
    public void testGetUsernameFromToken() throws Exception {
        //given
        String result = TokenUtils.getUsernameFromToken(null);
        assertNull(result);
        //when
        result = TokenUtils.getUsernameFromToken(FAKE_TOKEN);
        //then
        assertEquals("admin@admin.pl", result);
    }

    @Test
    public void testValidateToken() throws Exception {
        //given


        String realToken = TokenUtils.createToken(userDetails);

        //success
        //when
        boolean result = TokenUtils.validateToken(realToken, userDetails);
        //then
        assertTrue(result);

        //expired time czas cofnac o 6 min

       // result = TokenUtils.validateToken(newToken, userDetails);
       // assertFalse(result);

        //failed
        result = TokenUtils.validateToken(realToken + "extra", userDetails);
        assertFalse(result);
    }

    @Test
    public void testCreateToken() throws Exception {
       // ArgumentCaptor<Integer> cap; cap.capture();

        //given
        //when
        String result = TokenUtils.createToken(userDetails);
        //then
    }
}
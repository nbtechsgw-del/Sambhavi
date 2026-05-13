package com.hospital.cprms.config;

import com.hospital.cprms.security.Role;
import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.security")
public class AppSecurityProperties {

    private List<UserCredential> users = new ArrayList<>();

    public List<UserCredential> getUsers() {
        return users;
    }

    public void setUsers(List<UserCredential> users) {
        this.users = users;
    }

    public static class UserCredential {
        private String username;
        private String password;
        private Role role;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public Role getRole() {
            return role;
        }

        public void setRole(Role role) {
            this.role = role;
        }
    }
}

package codesquad.domain.user;

import support.domain.AbstractEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class User extends AbstractEntity {
    @Column(nullable = false, length = 20, unique = true)
    private String userId;

    @JsonIgnore
    @Column(nullable = false, length = 20)
    private String password;

    @Column(nullable = false, length = 12)
    private String name;

    @Column(nullable = false, length = 30)
    private String email;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void update(User updatedUser) {
        if (this.password.equals(updatedUser.password)) {
            setName(updatedUser.name);
            setEmail(updatedUser.email);
        }
    }

    public boolean matchId(Long inputId) {
        if (inputId == null) {
            return false;
        }
        return this.getId().equals(inputId);
    }

    public boolean matchPassword(User inputUser) {
        if (inputUser == null) {
            return false;
        }
        return this.password.equals(inputUser.password);
    }

    public boolean isSameUser(User inputUser) {
        return matchId(inputUser.getId());
    }

    @Override
    public String toString() {
        return "User{" +
                super.toString() +
                "userId='" + userId + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}

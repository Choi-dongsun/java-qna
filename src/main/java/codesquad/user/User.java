package codesquad.user;

import javax.persistence.*;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false, length=20, unique=true)
    private String userId;
    @Column(nullable=false, length=12)
    private String password;
    @Column(nullable=false, length=12)
    private String name;
    @Column(nullable=false, length=50)
    private String email;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
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
        setPassword(updatedUser.getPassword());
        setName(updatedUser.getName());
        setEmail(updatedUser.getEmail());
    }

    public boolean matchPassword(String password) {
        if (password == null) return false;
        return this.password.equals(password);
    }

    public boolean matchId(Long id) {
        if (id == null) return false;
        return this.id.equals(id);
    }

    public boolean matchUserId(String userId) {
        if (userId == null) return false;
        return this.userId.equals(userId);
    }
}

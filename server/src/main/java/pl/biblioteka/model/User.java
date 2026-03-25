package pl.biblioteka.model;

import java.util.Objects;

public class User {
    private long id;
    private String name;
    private String surname;
    private String email;
    private UserRole status;

    public User() {
    }

    public User(long id, String name, String surname, String email, UserRole status) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.status = status;
    }

    public void changeUser(long id, String name, String surname, String email, UserRole status) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserRole getStatus() {
        return status;
    }

    public void setStatus(UserRole status) {
        this.status = status;
    }

    public boolean isAdmin() {
        return status == UserRole.ADMIN;
    }

    @Override
    public String toString() {
        return "User{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", surname='" + surname + '\'' +
            ", email='" + email + '\'' +
            ", status=" + status +
            '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof User user)) {
            return false;
        }
        return id == user.id
            && Objects.equals(name, user.name)
            && Objects.equals(surname, user.surname)
            && Objects.equals(email, user.email)
            && status == user.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, surname, email, status);
    }
}
package nic.testprojet.AccountingSystem.models;


import javax.validation.constraints.Size;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.UniqueElements;

@Entity
@Table(name ="user")
public class Person {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty(message = "Имя не должно быть пустым")
    @Column(name = "full_name")
    @Size(min = 5, max =100, message = "ФИО должно быть от 5 до 100 символов")
    private String fullName;

    @UniqueElements
    private String login;

    @Column(name = "password")
    private String password;

    @Column(name = "expire_date")
    private String expireDate;

    public Person(){

    }

    public Person(String fullName, String login) {
        this.fullName = fullName;
        this.login = login;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", expireDate='" + expireDate + '\'' +
                '}';
    }
}

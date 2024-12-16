package kz.dos.libraryService.models;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class User {
    private int id;

    @NotEmpty(message = "Name should not be empty!")
    @Size(min = 2, max = 100, message = "Name length should be between 2 and 100 characters")
    private String fullName;

    @Min(value = 1900, message = "Birth year should be higher that 1900")
    private int birthYear;

    public User(int id, String fullName, int birthYear) {
        this.id = id;
        this.fullName = fullName;
        this.birthYear = birthYear;
    }

    public User(){};

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

    public int getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(int birthYear) {
        this.birthYear = birthYear;
    }
}

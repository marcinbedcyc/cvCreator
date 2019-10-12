package cvObjects;

import cvObjects.Address;
import cvObjects.LifeEvent;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class Person {
    private String name;
    private String surname;
    private String emailAddress;
    private String shortInfo;
    private LocalDate dateOfBirth;
    private int phoneNumber;
    private Address address;
    private List<String> interest;
    private List<String> skills;
    private List<LifeEvent> experience;
    private List<LifeEvent>  courses;
    private List<LifeEvent>  education;
    private String imageFile;
}

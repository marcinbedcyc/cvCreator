import lombok.Data;

import java.time.LocalDate;

@Data
public class Person {
    private String name;
    private String surname;
    private String emailAddress;
    private String shortInfo;
    private LocalDate dateOfBirth;
    private int phoneNumber;
    private Address address;
    private String [] interest;
    private String [] skills;
    private LifeEvent [] experience;
    private LifeEvent [] courses;
    private LifeEvent [] education;
}

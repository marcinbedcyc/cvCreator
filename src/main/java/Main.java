import java.time.LocalDate;
import java.time.Month;

public class Main {
    public static void main(String[] args) {
        Person cvMaker = new Person();
        cvMaker.setName("Marcin");
        cvMaker.setSurname("Kowalski");
        cvMaker.setPhoneNumber(123456789);
        cvMaker.setEmailAddress("komarcki@gmail.com");
        String [] interests = {"Piłka nożna", "Programowanie", "Java", "Gotowanie"};
        cvMaker.setInterest(interests);
        String [] skills = {"Umiejętność1", "Umiejętność2", "Umiejętność3", "Umiejętność4"};
        cvMaker.setSkills(skills);
        cvMaker.setShortInfo("Jestem studentem, poszukuję stażu, aby móc stale się rozwijać.");
        cvMaker.setDateOfBirth(LocalDate.of(1998, Month.JANUARY, 2));
        cvMaker.setAddress(new Address("Polna", 2, 3,"87-114", "Warszawa"));
    }
}

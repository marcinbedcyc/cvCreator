import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Address {
    private String street;
    private int houseNumber;
    private int flatNumber;
    private String postCode;
    private String city;
}

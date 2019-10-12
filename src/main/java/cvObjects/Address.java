package cvObjects;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Address {
    private String street;
    private int houseNumber;
    private int flatNumber;
    private Character houseLetter;
    private String postCode;
    private String city;

    @Override
    public String toString() {
        String result = new String();
        result += "ul." + street + " " + houseNumber;
        if(houseLetter != null)
            result += houseLetter;
        if(flatNumber >= 0)
            result += "/" + flatNumber;
        result +="\n" + postCode + " " + city;
        return  result;
    }
}

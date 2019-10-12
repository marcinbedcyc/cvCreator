package cvObjects;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class LifeEvent {
    private LocalDate begin;
    private LocalDate end;
    private String title;
    private String description;
}

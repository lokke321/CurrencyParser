package project;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Currency {
    private String numCode;
    private String charCode;
    private String nominal;
    private String name;
    private String value;
    private Double valuePerOne;
}

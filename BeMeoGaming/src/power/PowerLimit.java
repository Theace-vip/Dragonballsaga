package power;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 *

 */

@Setter
@Getter
@AllArgsConstructor
@Builder
public class PowerLimit {

    private int id;
    private double power;
    private double hp;
    private double mp;
    private double damage;
    private double defense;
    private int critical;
}

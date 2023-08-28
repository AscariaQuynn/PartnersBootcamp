package cz.devforce.partnersbootcamp.configuration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class MeaningOfLifeProperties {

    @Value("${the.meaning.of.life.the.universe.and.everything}")
    private int meaningOfLife;
}

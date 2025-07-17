package cat.itacademy.s05.t01.n01.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import lombok.EqualsAndHashCode;


@Table(name = "players")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Player {
    @Id
    private int id;
    @NotNull(message = "You need a name")
    @Size(min = 3, max = 50, message = "The name must be between 3 and 50 characters")
    private String name;
    private long totalScore;



}

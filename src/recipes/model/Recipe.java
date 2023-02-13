package recipes.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.hypersistence.utils.hibernate.type.array.ListArrayType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.List;



@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "recipes")
@TypeDef(name = "list-array", typeClass = ListArrayType.class)
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private long id;

    @NotBlank
    private String name;
    @NotBlank
    private String description;

    @NotBlank
    @NonNull
    private String category;

    @UpdateTimestamp
    LocalDateTime date;

    @NotEmpty
    @Type(type = "list-array")
    @Column(name = "ingredients", columnDefinition = "text[]")
    private List<String> ingredients;
    @NotEmpty
    @Type(type = "list-array")
    @Column(name = "directions", columnDefinition = "text[]")
    private List<String> directions;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

}

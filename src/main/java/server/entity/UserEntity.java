package server.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "Users")
@Getter
@Setter
@ToString
@AllArgsConstructor
public class UserEntity {
    @NotNull
    @Id
    @GeneratedValue()
    @Column(nullable = false, updatable = false)
    private Integer id;
    @NotBlank
    @Size(min = 3, max = 255)
    private String nome;
    @Column(unique = true)
    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String senha;
    @NotNull
    private Boolean isAdmin;

    public UserEntity() {
    }

}

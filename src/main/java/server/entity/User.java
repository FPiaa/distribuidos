package server.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.NaturalId;
import server.dto.CreateUser;
import server.dto.UpdateUser;

@Entity
@Table(name = "Users")
@Getter
@Setter
@ToString
@AllArgsConstructor
public class User {
    @NotNull
    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Size(min = 3, max = 255)
    private String nome;
    @NotNull
    @Email
    @NaturalId
    private String email;

    @NotNull
    private String senha;
    @NotNull
    private Boolean isAdmin;

    public User() {
    }

    public static User of(CreateUser user) {
        var entity = new User();
        entity.setEmail(user.email());
        entity.setSenha(user.senha());
        entity.setIsAdmin(user.tipo());
        entity.setNome(user.nome());
        return entity;
    }

    public static User of(UpdateUser user) {
        var entity = new User();
        entity.setId(user.registro());
        entity.setEmail(user.email());
        entity.setSenha(user.senha());
        entity.setIsAdmin(user.tipo());
        entity.setNome(user.nome());
        return entity;
    }

    public static User clone(User old, User info) {
        var user = new User();
        if(info.getEmail() != null) {
            user.setEmail(info.getEmail());
        } else {
            user.setEmail(old.getEmail());
        }

        if (info.getSenha() != null) {
            user.setSenha(info.getSenha());
        }else {
            user.setSenha(old.getSenha());
        }

        if (info.getIsAdmin() != null) {
            user.setIsAdmin(info.getIsAdmin());
        } else {
            user.setIsAdmin(old.getIsAdmin());
        }

        if (info.getNome() != null) {
            user.setNome(info.getNome());
        } else {
            user.setNome(old.getNome());
        }
        return user;
    }

}

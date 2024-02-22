package com.management.management.domain.user;


import com.management.management.domain.project.Project;
import com.management.management.domain.task.Task;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String surname;

    private String username;

    private String email;

    private String password;

    private UserRole role;

    @ManyToMany(mappedBy = "employees", cascade = CascadeType.ALL)
    private List<Project> projectsWorked = new ArrayList<>();

    @ManyToMany(mappedBy = "managers", cascade = CascadeType.ALL)
    private List<Project> projectsManaged = new ArrayList<>();

    @OneToMany(mappedBy = "responsible", cascade = CascadeType.ALL)
    private List<Task> tasksResponsibleFor = new ArrayList<>();

    public User(Long id, String name, String surname, String username, String email, String password, UserRole role) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();

        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        if (this.role == UserRole.ADMIN) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }

        return authorities;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getGeneratedUsername(){
        return this.username;
    }
}

package com.github.prajjwal.hospitalmanagement.model;

import com.github.prajjwal.hospitalmanagement.model.type.AuthProviderType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@Builder
// index for optimization to fast query execution
@Table(name = "user_details", indexes = {
        @Index(name = "idx_provider_id_provider_type", columnList = "providerId, providerType")
})
@AllArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    @NotNull
    private String password;

    private String providerId;

    @Enumerated(EnumType.STRING)
    private AuthProviderType providerType;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ADMIN"),  new SimpleGrantedAuthority("PATIENT"),
                new SimpleGrantedAuthority("DOCTOR"));
    }
}
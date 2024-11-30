package com.sml.smartledger.Model;

import com.sml.smartledger.Model.business.Business;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Entity(name = "user")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseModel implements UserDetails {


    @Column(name = "user_name", nullable = false)
    private String name;
    @Column(unique = true, nullable = false)
    private String email;

    private String password;
    @Column(length = 1000)
    private String about;
    @Column(length = 1000)
    private String profilePic;
    private String phoneNumber;


    // information
    private boolean enabled = true; // active or not

    private boolean emailVerified = false;
    private boolean phoneVerified = false;

    //self, google, facebook, GitHub, twitter etc
    @Enumerated(EnumType.STRING)
    private Providers provider = Providers.SELF;
    private String providerId;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role_list", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role_list")
    List<String> roleList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    List<Business> businessList = new ArrayList<>();

    @OneToOne
    Business selectedBusiness;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roleList.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());  // or use .collect(Collectors.toCollection(ArrayList::new))
    }

    @Override
    public String getUsername() {
        return this.email;
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
}

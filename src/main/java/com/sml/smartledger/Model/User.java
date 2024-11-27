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


@Entity(name = "user")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User implements UserDetails {

    @Id
    private String id;

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
    List<String> roleList = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    List<Business> businessList;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<SimpleGrantedAuthority> simpleGrantedAuthorities = roleList.stream().map(SimpleGrantedAuthority::new).toList();
        return simpleGrantedAuthorities;
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

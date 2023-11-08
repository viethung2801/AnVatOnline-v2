package com.viethung.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Collection;
import java.util.UUID;

@Table(name = "users")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id ;

    @Column(name = "code", columnDefinition = "nvarchar(20)", unique = true, nullable = false)
    private String code; // Customer start with CUS ,ADMIN start with ADM

    @Column(name = "image_url", columnDefinition = "nvarchar(max)")
    private String imageUrl;

    @Column(name = "first_name", columnDefinition = "nvarchar(50)",nullable = false)
    private String firstName;

    @Column(name = "last_name", columnDefinition = "nvarchar(100)")
    private String lastName;

    @Column
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate dateBirth;

    @Column(name = "address", columnDefinition = "nvarchar(max)")
    private String address;

    @Column(name = "email", columnDefinition = "nvarchar(max)",unique = true,nullable = false)
    private String email;

    @Column(name = "phone_number", columnDefinition = "nvarchar(max)",unique = true,nullable = false)
    private String phoneNumber;

    @Column
    private Boolean gender;

    @Column
    private String password;

    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Collection<Role> roles;
}

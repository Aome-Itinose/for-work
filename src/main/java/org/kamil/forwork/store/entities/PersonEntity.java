package org.kamil.forwork.store.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "Person")
public class PersonEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(unique = true, nullable = false)
    String username;

    @Column(nullable = false)
    String password;

    String fullName;

    Date bornAt;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    List<PhoneNumberEntity> phoneNumbers = new ArrayList<>();

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    List<EmailEntity> emails = new ArrayList<>();

    @Column(nullable = false)
    Long amountOfMoney;

    Long startAmountOfMoney;

}
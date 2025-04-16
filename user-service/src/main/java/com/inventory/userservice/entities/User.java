package com.inventory.userservice.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "inventory_user")
public class User {

    @Id
    @Column(name = "userid")
    private String userId;

    @Column(name = "username", length = 10, nullable = false, unique = true)
    private String userName;

    @Column(name = "firstname", length = 50, nullable = false)
    private String firstName;

    @Column(name = "lastname", length = 50)
    private String lastName;

    @Column(name = "email", length = 50, nullable = false, unique = true)
    private String email;

    @Column(name = "phonenumber", nullable = false, length = 10)
    private int phoneNumber;

}

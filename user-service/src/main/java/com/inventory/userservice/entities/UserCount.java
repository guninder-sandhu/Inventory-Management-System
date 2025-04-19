package com.inventory.userservice.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Entity
@Getter
@Service
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "usercount")
public class UserCount {

    @Id
    private int id;
    @Column(name = "usercount")
    private int userCount;
}

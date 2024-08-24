package com.example.security.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Builder
@Table(name = "USER")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //MYSQL AUTO_INCREMENT
    @Column(name = "user_id")
    private Long id; // 데이터는 숨긴다.

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true, length = 30)
    //Column의 unique는 데이텉 무결성, 비즈니스 규칙으로 중복된 값이 아닌 유일한 값을 필요로 할 때 사용. 성능과 예외 처리 고민
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String phoneNum;

    private String imgUrl;

    @JsonIgnore
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

    private LocalDate createdAt;
}

package com.example.security.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterDto {

    private String name;
    private String email;
    private String password;
    private String phoneNum;
    private String imgUrl;

    public static class Builder {
        private String name;
        private String email;
        private String password;
        private String phoneNum;
        private String imgUrl;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder phoneNum(String phoneNum) {
            this.phoneNum = phoneNum;
            return this;
        }

        public Builder imgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
            return this;
        }

        public UserRegisterDto build() {
            UserRegisterDto dto = new UserRegisterDto();
            dto.name = this.name;
            dto.email = this.email;
            dto.password = this.password;
            dto.phoneNum = this.phoneNum;
            dto.imgUrl = this.imgUrl;
            return dto;
        }

        public static Builder builder() {
            return new Builder();
        }
    }
}

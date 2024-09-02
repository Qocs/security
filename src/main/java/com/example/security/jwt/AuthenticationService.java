package com.example.security.jwt;

import com.example.security.domain.User;
import com.example.security.domain.dto.AuthenticationRequest;
import com.example.security.domain.dto.AuthenticationResponse;
import com.example.security.domain.dto.UserRegisterDto;
import com.example.security.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(UserRegisterDto request) {
        var user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNum(request.getPhoneNum())
                .imgUrl(request.getImgUrl())
                .build();
        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse login(AuthenticationRequest request) {

        try {
            //UsernamePasswordAuthenticationFilter가 사용자 정보를 추출해서 인증 때 사용할 토큰을 생성한다.
            //이 토큰을 인증 처리 과정에 맡기기 위해 authenticationManager에게 넘긴다.
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

            //인증 처리 과정을 거쳐 인증 결과가 성공 시 authentication 구현체에서 Principal을 추출해 userDetails를 만든다.
            //만든 userDetails로 jwtToken을 생성한다.
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String jwtToken = jwtService.generateToken(userDetails);

            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("유효하지 않은 비밀번호입니다.");
        }

    }
}

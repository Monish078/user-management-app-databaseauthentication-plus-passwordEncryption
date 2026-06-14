package com.project_intermediate.UserManagement_System_App.service;

import com.project_intermediate.UserManagement_System_App.dto.LoginRequest;
import com.project_intermediate.UserManagement_System_App.dto.RegisterRequest;
import com.project_intermediate.UserManagement_System_App.dto.UserRequest;
import com.project_intermediate.UserManagement_System_App.entity.Role;
import com.project_intermediate.UserManagement_System_App.entity.User;
import com.project_intermediate.UserManagement_System_App.exception.EmailAlreadyExistException;
import com.project_intermediate.UserManagement_System_App.repository.UserRepository;
import lombok.RequiredArgsConstructor;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

@Service
@RequiredArgsConstructor
public class AuthService {   //---> phase 3 ke liye jissme db se user verify hota hai (authenticate hota hai )
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;  //
    private final AuthenticationManager authenticationManager;



    public String register(RegisterRequest request) {  // new user ko register karne ke liye
        // pehle check karo ki duplicate email pass to nhi kari
        if(userRepository.findByEmail(request.getEmail())
                .isPresent()) {

            throw new EmailAlreadyExistException("Email already exists");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(
                        passwordEncoder.encode(
                                request.getPassword()  // encrypted password save karo ab , phase 3
                        )
                )
                .role(Role.USER)  // role user set karo
                .build();

        userRepository.save(user);  //  save the user

        return "User Registered Successfully";
    }



    // login service
    public String login(LoginRequest request) {

        authenticationManager.authenticate(

                new UsernamePasswordAuthenticationToken(

                        request.getEmail(),
                        request.getPassword()
                )
        );

        return "Login Successful";
    }
}

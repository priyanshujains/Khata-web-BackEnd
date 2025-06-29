package com.example.KhataWeb.Contoller;

import com.example.KhataWeb.Dtos.AuthRequest;
import com.example.KhataWeb.Dtos.SignupDto;
import com.example.KhataWeb.Security.JwtUtil;
import com.example.KhataWeb.Service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authManager;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthController(
                          UserService userService,
                          JwtUtil jwtUtil){
        this.jwtUtil=jwtUtil;
        this.userService=userService;
    }


    @PostMapping("/register")
    public String register(@RequestBody SignupDto request, @RequestParam(defaultValue = "USER") String role) {
        return userService.register(request, role);
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request, HttpServletResponse response) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String token = jwtUtil.generateToken(userDetails.getUsername(),
                userDetails.getAuthorities().stream()
                        .map(a -> a.getAuthority()).collect(Collectors.toSet())
        );


//        String cookieValue = "jwt=" + token + "; HttpOnly; Path=/; Max-Age=86400; SameSite=None"; // 👈 Secure only on HTTPS
//        response.setHeader("Set-Cookie", cookieValue);

        Cookie cookie = new Cookie("jwt", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // ✅ false for localhost, true for production HTTPS
        cookie.setPath("/");
        cookie.setMaxAge(24 * 60 * 60); // 1 day
        cookie.setDomain("localhost"); // optional, makes it explicit
//        cookie.setAttribute("SameSite", "None"); // 🚨 THIS REQUIRES Java 11+ and Servlet 6
        cookie.setAttribute("SameSite", "Lax");

        response.addCookie(cookie);

        return ResponseEntity.ok("Login successful. Token set in cookie.");
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("jwt", null);
        cookie.setMaxAge(0); // delete cookie
        cookie.setPath("/");
        response.addCookie(cookie);
        return ResponseEntity.ok("Logged out successfully.");
    }
}

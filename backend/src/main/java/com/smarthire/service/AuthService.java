package com.smarthire.service;

import com.smarthire.dto.*;
import com.smarthire.entity.*;
import com.smarthire.exception.BadRequestException;
import com.smarthire.repository.CandidateProfileRepository;
import com.smarthire.repository.CompanyRepository;
import com.smarthire.repository.RecruiterProfileRepository;
import com.smarthire.repository.UserRepository;
import com.smarthire.security.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final CandidateProfileRepository candidateProfileRepository;
    private final RecruiterProfileRepository recruiterProfileRepository;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    public AuthService(UserRepository userRepository, CandidateProfileRepository candidateProfileRepository, RecruiterProfileRepository recruiterProfileRepository, CompanyRepository companyRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.candidateProfileRepository = candidateProfileRepository;
        this.recruiterProfileRepository = recruiterProfileRepository;
        this.companyRepository = companyRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail().trim().toLowerCase())) {
            throw new BadRequestException("Email is already registered: " + request.getEmail());
        }

        User user = User.builder()
                .email(request.getEmail().trim().toLowerCase())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName().trim())
                .role(request.getRole())
                .enabled(true)
                .build();

        user = userRepository.save(user);

        // Initialize role-specific profile
        if (user.getRole() == Role.ROLE_CANDIDATE) {
            CandidateProfile candidateProfile = CandidateProfile.builder()
                    .user(user)
                    .build();
            candidateProfileRepository.save(candidateProfile);
        } else if (user.getRole() == Role.ROLE_RECRUITER) {
            Company company = null;
            if (StringUtils.hasText(request.getCompanyName())) {
                company = companyRepository.findByNameIgnoreCase(request.getCompanyName().trim())
                        .orElseGet(() -> companyRepository.save(Company.builder()
                                .name(request.getCompanyName().trim())
                                .verified(false) // requires admin approval
                                .build()));
            }

            RecruiterProfile recruiterProfile = RecruiterProfile.builder()
                    .user(user)
                    .company(company)
                    .build();
            recruiterProfileRepository.save(recruiterProfile);
        }

        String jwt = tokenProvider.generateTokenFromUsername(user.getEmail(), user.getId(), user.getRole().name());
        return AuthResponse.builder()
                .token(jwt)
                .user(mapToUserDTO(user))
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail().trim().toLowerCase(),
                        request.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);

        User user = userRepository.findByEmail(request.getEmail().trim().toLowerCase())
                .orElseThrow(() -> new BadRequestException("User not found"));

        if (!user.isEnabled()) {
            throw new BadRequestException("Your account has been deactivated. Please contact administrator.");
        }

        return AuthResponse.builder()
                .token(jwt)
                .user(mapToUserDTO(user))
                .build();
    }

    public UserDTO getCurrentUser(User user) {
        return mapToUserDTO(user);
    }

    public UserDTO mapToUserDTO(User user) {
        Long companyId = null;
        String companyName = null;
        Boolean companyVerified = null;

        if (user.getRole() == Role.ROLE_RECRUITER) {
            var recruiterOpt = recruiterProfileRepository.findByUserId(user.getId());
            if (recruiterOpt.isPresent() && recruiterOpt.get().getCompany() != null) {
                Company company = recruiterOpt.get().getCompany();
                companyId = company.getId();
                companyName = company.getName();
                companyVerified = company.isVerified();
            }
        }

        return UserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(user.getRole())
                .enabled(user.isEnabled())
                .companyId(companyId)
                .companyName(companyName)
                .companyVerified(companyVerified)
                .createdAt(user.getCreatedAt())
                .build();
    }
}

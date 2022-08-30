package uz.hu.my_project_trello.services;

import lombok.NonNull;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.hu.my_project_trello.config.security.UserDetails;
import uz.hu.my_project_trello.domains.auth.ActivationCode;
import uz.hu.my_project_trello.domains.auth.AuthRole;
import uz.hu.my_project_trello.domains.auth.AuthUser;
import uz.hu.my_project_trello.dtos.JwtResponse;
import uz.hu.my_project_trello.dtos.LoginRequest;
import uz.hu.my_project_trello.dtos.RefreshTokenRequest;
import uz.hu.my_project_trello.dtos.UserRegisterDTO;
import uz.hu.my_project_trello.dtos.auth.AuthUserDTO;
import uz.hu.my_project_trello.exceptions.GenericInvalidTokenException;
import uz.hu.my_project_trello.exceptions.GenericNotFoundException;
import uz.hu.my_project_trello.exceptions.GenericRuntimeException;
import uz.hu.my_project_trello.mappers.AuthUserMapper;
import uz.hu.my_project_trello.repository.AuthUserRepository;
import uz.hu.my_project_trello.repository.RoleRepository;
import uz.hu.my_project_trello.services.auth.ActivationCodeService;
import uz.hu.my_project_trello.services.mail.MailService;
import uz.hu.my_project_trello.utils.jwt.TokenService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Supplier;


@Service
public class AuthUserService implements UserDetailsService {
    private final AuthenticationManager authenticationManager;
    private final AuthUserRepository authUserRepository;
    private final TokenService accessTokenService;
    private final TokenService refreshTokenService;
    private final AuthUserMapper authUserMapper;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    private final ActivationCodeService activationCodeService;
    private final RoleRepository roleRepository;

    @Value("${activation.link.base.path}")
    private String basePath;

    public AuthUserService(@Lazy AuthenticationManager authenticationManager,
                           AuthUserRepository authUserRepository,
                           @Qualifier("accessTokenService") TokenService accessTokenService,
                           @Qualifier("refreshTokenService") TokenService refreshTokenService,
                           AuthUserMapper authUserMapper,
                           PasswordEncoder passwordEncoder,
                           MailService mailService,
                           RoleRepository roleRepository,
                           ActivationCodeService activationCodeService) {
        this.authenticationManager = authenticationManager;
        this.authUserRepository = authUserRepository;
        this.accessTokenService = accessTokenService;
        this.refreshTokenService = refreshTokenService;
        this.authUserMapper = authUserMapper;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
        this.activationCodeService = activationCodeService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Supplier<UsernameNotFoundException> exception = () ->
                new UsernameNotFoundException("Bad credentials");
        AuthUser authUser =  authUserRepository.findByUsername(username).orElseThrow(exception);
        return new UserDetails(authUser);
    }

    public JwtResponse login(LoginRequest request) {
        Collection<AuthRole> authRoles = new ArrayList<>();
        authRoles.add(roleRepository.save(AuthRole.builder()
                .name("user")
                .code("USER")
                .build()));
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.username(), request.password()));
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String accessToken = accessTokenService.generateToken(userDetails);
        String refreshToken = refreshTokenService.generateToken(userDetails);
        AuthUser authUser = userDetails.authUser();
        authUser.setLastLoginTime(LocalDateTime.now());
        authUser.setRoles(authRoles);
        authUserRepository.save(authUser);
        return new JwtResponse(accessToken, refreshToken, "Bearer");
    }

    public JwtResponse refreshToken(@NonNull RefreshTokenRequest request) {
        String token = request.token();
        if (accessTokenService.isValid(token)) {
            throw new GenericInvalidTokenException("Refresh Token invalid", 401);
        }
        String subject = accessTokenService.getSubject(token);
        UserDetails userDetails = loadUserByUsername(subject);
        String accessToken = accessTokenService.generateToken(userDetails);
        return new JwtResponse(accessToken, request.token(), "Bearer");
    }

    @SneakyThrows
    @Transactional
    public AuthUser register(UserRegisterDTO dto) {
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        AuthUser authUser = authUserMapper.fromRegisterDTO(dto);
        authUserRepository.save(authUser);
        AuthUserDTO authUserDTO = authUserMapper.toDTO(authUser);
        ActivationCode activationCode = activationCodeService.generateCode(authUserDTO);
        String link = basePath.formatted(activationCode.getActivationLink());
        mailService.sendEmail(authUserDTO, link);
        return authUser;
    }

    @Transactional(noRollbackFor = GenericRuntimeException.class)
    public Boolean activateUser(String activationCode) {
        ActivationCode activationLink = activationCodeService.findByActivationLink(activationCode);
        if (activationLink.getValidTill().isBefore(LocalDateTime.now())) {
            activationCodeService.delete(activationLink.getId());
            throw new GenericRuntimeException("Activation Code is not active", 400);
        }

        AuthUser authUser = authUserRepository.findById(activationLink.getUserId()).orElseThrow(() -> {
            throw new GenericNotFoundException("User not found", 404);
        });

        authUser.setStatus(AuthUser.Status.ACTIVE);
        authUserRepository.save(authUser);
        return true;
    }
}

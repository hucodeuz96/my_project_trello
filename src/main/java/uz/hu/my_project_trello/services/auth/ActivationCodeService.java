package uz.hu.my_project_trello.services.auth;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uz.hu.my_project_trello.domains.auth.ActivationCode;
import uz.hu.my_project_trello.dtos.auth.AuthUserDTO;
import uz.hu.my_project_trello.exceptions.GenericNotFoundException;
import uz.hu.my_project_trello.repository.ActivationCodeRepository;
import uz.hu.my_project_trello.utils.BaseUtils;

import java.time.LocalDateTime;
import java.util.UUID;



@Service
@RequiredArgsConstructor
public class ActivationCodeService {
    private final BaseUtils baseUtils;
    private final ActivationCodeRepository repository;

    @Value("${activation.link.expiry.in.minutes}")
    private long activationLinkValidTillInMinutes;

    public ActivationCode generateCode(@NonNull AuthUserDTO authUserDTO) {
        String codeForEncoding = "" + UUID.randomUUID() + System.currentTimeMillis();
        String encodedActivationCode = baseUtils.encode(codeForEncoding);
        ActivationCode activationCode = ActivationCode.builder()
                .activationLink(encodedActivationCode)
                .userId(authUserDTO.getId())
                .validTill(LocalDateTime.now().plusMinutes(activationLinkValidTillInMinutes))
                .build();
        return repository.save(activationCode);
    }

    public ActivationCode findByActivationLink(@NonNull String activationLink) {
        return repository.findByActivationLink(activationLink).orElseThrow(() ->
        {
            throw new GenericNotFoundException("Activation Link Not Found", 404);
        });
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}

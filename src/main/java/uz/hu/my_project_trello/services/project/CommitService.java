package uz.hu.my_project_trello.services.project;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.hu.my_project_trello.repository.CardRepository;
import uz.hu.my_project_trello.repository.CommitRepository;

/**
 * @author "Husniddin Ulachov"
 * @created 2:59 PM on 9/2/2022
 * @project my_project_trello
 */
@Service
@RequiredArgsConstructor
public class CommitService {
    private final CommitRepository commitRepository;
    private final CardRepository cardRepository;



}

package uz.hu.my_project_trello.utils.comparator;

import org.springframework.stereotype.Service;
import uz.hu.my_project_trello.dtos.project.columin.ColuminResDTO;

import java.util.Comparator;

/**
 * @author "Husniddin Ulachov"
 * @created 6:31 AM on 9/5/2022
 * @project my_project_trello
 */
@Service
public class ColuminComparator implements Comparator<ColuminResDTO> {

    @Override
    public int compare(ColuminResDTO o1, ColuminResDTO o2) {
        return o1.getOrderNumber().compareTo(o2.getOrderNumber());
    }
}

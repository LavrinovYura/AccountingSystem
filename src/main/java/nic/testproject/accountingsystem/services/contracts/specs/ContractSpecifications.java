package nic.testproject.accountingsystem.services.contracts.specs;

import nic.testproject.accountingsystem.dto.contracts.ContractCriteriaDTO;
import nic.testproject.accountingsystem.models.contracts.Contract;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class ContractSpecifications {

    public static Specification<Contract> searchContracts(ContractCriteriaDTO searchCriteria) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (searchCriteria.getName() != null) {
                predicates.add(criteriaBuilder.like(root.get("name"), searchCriteria.getName() + "%"));
            }

            if (searchCriteria.getType() != null) {
                predicates.add(criteriaBuilder.equal(root.get("type"), searchCriteria.getType()));
            }

            if (searchCriteria.getPlannedStartDate() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("plannedStartDate"), searchCriteria.getPlannedStartDate()));
            }

            if (searchCriteria.getPlannedEndDate() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("plannedEndDate"), searchCriteria.getPlannedEndDate()));
            }

            if (searchCriteria.getActualStartDate() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("actualStartDate"), searchCriteria.getActualStartDate()));
            }

            if (searchCriteria.getActualEndDate() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("actualEndDate"), searchCriteria.getActualEndDate()));
            }

            if (searchCriteria.getAmount() != null) {
                predicates.add(criteriaBuilder.equal(root.get("amount"), searchCriteria.getAmount()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}



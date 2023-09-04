package nic.testproject.accountingsystem.services.contracts.specs;

import nic.testproject.accountingsystem.dtos.contracts.CounterpartyDTO;
import nic.testproject.accountingsystem.models.contracts.details.Counterparty;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class CounterpartySpecifications {

    public static Specification<Counterparty> searchCounterparties(CounterpartyDTO searchCriteria) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (searchCriteria.getName() != null) {
                predicates.add(criteriaBuilder.like(root.get("name"), searchCriteria.getName() + "%"));
            }

            if (searchCriteria.getAddress() != null) {
                predicates.add(criteriaBuilder.like(root.get("address"), searchCriteria.getAddress() + "%"));
            }

            if (searchCriteria.getInn() != null) {
                predicates.add(criteriaBuilder.equal(root.get("inn"), searchCriteria.getInn()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}

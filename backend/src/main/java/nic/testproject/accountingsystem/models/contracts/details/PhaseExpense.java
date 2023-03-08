package nic.testproject.accountingsystem.models.contracts.details;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "phase_expenses")
@Data
@NoArgsConstructor
public class PhaseExpense {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false)
        private String name;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private ExpenseType type;

        @Column(nullable = false)
        private BigDecimal amount;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "contact_phase_id")
        private ContractPhase contractPhase;

        public enum ExpenseType {
            PLAN, ACTUAL
        }
}

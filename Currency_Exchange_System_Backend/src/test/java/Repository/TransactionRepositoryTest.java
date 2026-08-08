package Repository;


import org.example.App;
import org.example.enums.TxStatus;
import org.example.enums.TxType;
import org.example.model.Transaction;
import org.example.repository.interfaces.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = App.class)
public class TransactionRepositoryTest {

    @Autowired
    private TransactionRepository transactionRepository;

    // first senario : customer hozoori oomade ---> performedByUserId = 12, approvedByUserId = null, requestedByCustomer = false, status = COMPLETED
    // second senario : customer khodesh request mide gheire hozoori ---> performedByUserId = null, approvedByUserId = 8, requestedByCustomer = true, status = PENDING : bad az tayid COMPLETE

    @Test
    public void transactionSaveTest() {

        BigDecimal amoutncurrency = new BigDecimal(100);
        BigDecimal amountToman = new BigDecimal(2000000);
        BigDecimal rateUsed = new BigDecimal(1200);
        BigDecimal rateRequest = new BigDecimal(1000);

        Transaction transaction = new Transaction(
                amoutncurrency,
                amountToman,
                null,
                null,
                LocalDateTime.now(),
                1,
                1,
                null,
                true,
                rateUsed,
                rateRequest,
                TxStatus.PENDING,
                TxType.BUY
        );

        Transaction saved = transactionRepository.save(transaction);
        assertThat(transaction).isNotNull();
        assertThat(transaction.getAmountCurrency()).isEqualTo(amoutncurrency);
        assertThat(transaction.getAmountToman()).isEqualTo(amountToman);
        assertThat(transaction.getApprovedAt()).isNull();
        assertThat(transaction.getApprovedByUserId()).isNull();
        assertThat(transaction.getCreatedAt()).isNotNull();
        assertThat(transaction.getCurrencyId()).isEqualTo(1);
        assertThat(transaction.getCustomerId()).isEqualTo(1);
        assertThat(transaction.getPerformedByUserId()).isNull();
        assertThat(transaction.isRequestedByCustomer()).isEqualTo(true);
        assertThat(transaction.getRateUsed()).isEqualTo(rateUsed);
        assertThat(transaction.getRequestedRate()).isEqualTo(rateRequest);
        assertThat(transaction.getStatus()).isEqualTo(TxStatus.PENDING);
        assertThat(transaction.getTxType()).isEqualTo(TxType.BUY);
    }
}

package service;

import org.example.App;
import org.example.enums.TxStatus;
import org.example.enums.TxType;
import org.example.model.Transaction;
import org.example.service.interfaces.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = App.class)
public class TransactionServiceTest {

    @Autowired
    private TransactionService transactionService;


    @Test
    public void saveTransactionTest() {

        BigDecimal amoutncurrency = new BigDecimal(200);
        BigDecimal amountToman = new BigDecimal(210000);
        BigDecimal rateUsed = new BigDecimal(1500);
        BigDecimal rateRequest = new BigDecimal(1200);

        Transaction transaction = new Transaction(
                amoutncurrency,
                amountToman,
                null,
                null,
                LocalDateTime.now(),
                1,
                1,
                null,
                false,
                rateUsed,
                rateRequest,
                TxStatus.PENDING,
                TxType.BUY
        );

        transactionService.save(transaction);

//        assertThat(saved).isNotNull();
//        assertThat(saved.getAmountCurrency()).isEqualTo(amoutncurrency);
//        assertThat(saved.getAmountToman()).isEqualTo(amountToman);
//        assertThat(saved.getApprovedAt()).isNull();
//        assertThat(saved.getApprovedByUserId()).isNull();
//        assertThat(saved.getCreatedAt()).isNotNull();
//        assertThat(saved.getCurrencyId()).isEqualTo(1);
//        assertThat(saved.getCustomerId()).isEqualTo(2);
//        assertThat(saved.getPerformedByUserId()).isNull();
//        assertThat(saved.isRequestedByCustomer()).isEqualTo(false);
//        assertThat(saved.getRateUsed()).isEqualTo(rateUsed);
//        assertThat(saved.getRequestedRate()).isEqualTo(rateRequest);
//        assertThat(saved.getStatus()).isEqualTo(TxStatus.PENDING);
//        assertThat(saved.getTxType()).isEqualTo(TxType.BUY);
//        System.out.println(saved.getId());
    }
}

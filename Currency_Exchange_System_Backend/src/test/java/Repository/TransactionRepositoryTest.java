package Repository;


import org.example.App;
import org.example.database.DatabaseManager;
import org.example.enums.TxStatus;
import org.example.enums.TxType;
import org.example.model.Transaction;
import org.example.repository.interfaces.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = App.class)
public class TransactionRepositoryTest {

    @Autowired
    private TransactionRepository transactionRepository;

    // first senario : customer hozoori oomade ---> performedByUserId = 12, approvedByUserId = null, requestedByCustomer = false, status = COMPLETED
    // second senario : customer khodesh request mide gheire hozoori ---> performedByUserId = null, approvedByUserId = 8, requestedByCustomer = true, status = PENDING : bad az tayid COMPLETE

    @Test
    public void transactionSaveTest() {

        BigDecimal amoutncurrency = new BigDecimal(200);
        BigDecimal amountToman = new BigDecimal(210000);
        BigDecimal rateUsed = new BigDecimal(1500);
        BigDecimal rateRequest = new BigDecimal(1200);

        Transaction transaction = new Transaction(
                amoutncurrency,
                amountToman,
                null,
                1L,
                LocalDateTime.now(),
                1,
                2,
                3L,
                false,
                rateUsed,
                rateRequest,
                TxStatus.COMPLETED,
                TxType.BUY
        );

        Transaction saved = transactionRepository.save(transaction);
//        assertThat(transaction).isNotNull();
//        assertThat(transaction.getAmountCurrency()).isEqualTo(amoutncurrency);
//        assertThat(transaction.getAmountToman()).isEqualTo(amountToman);
//        assertThat(transaction.getApprovedAt()).isNull();
//        assertThat(transaction.getApprovedByUserId()).isNull();
//        assertThat(transaction.getCreatedAt()).isNotNull();
//        assertThat(transaction.getCurrencyId()).isEqualTo(1);
//        assertThat(transaction.getCustomerId()).isEqualTo(1);
//        assertThat(transaction.getPerformedByUserId()).isNull();
//        assertThat(transaction.isRequestedByCustomer()).isEqualTo(true);
//        assertThat(transaction.getRateUsed()).isEqualTo(rateUsed);
//        assertThat(transaction.getRequestedRate()).isEqualTo(rateRequest);
//        assertThat(transaction.getStatus()).isEqualTo(TxStatus.PENDING);
//        assertThat(transaction.getTxType()).isEqualTo(TxType.BUY);
        System.out.println(saved.getId());
    }

    @Test
    public void findAllTransactionsTest() {

        List<Transaction> allTransactions = transactionRepository.findAll();
        for(Transaction transaction : allTransactions) {
            System.out.println(transaction.getId() + " - " +
                    transaction.getAmountCurrency().toString() + " - " +
                    transaction.getAmountToman().toString() + " - " +
                    transaction.getApprovedAt() + " - " +
                    transaction.getApprovedByUserId() + " - " +
                    transaction.getCreatedAt() + " - " +
                    transaction.getCurrencyId() + " - " +
                    transaction.getCustomerId() + " - " +
                    transaction.getPerformedByUserId() + " - " +
                    transaction.isRequestedByCustomer() + " - " +
                    transaction.getRateUsed() + " - " +
                    transaction.getRequestedRate() + " - " +
                    transaction.getStatus().name() + " - " +
                    transaction.getTxType().name());
        }
    }

    @Test
    public void findingTransactionByIdTest() {

        Transaction transaction = transactionRepository.findById(2);

        BigDecimal amoutncurrency = new BigDecimal(100);
        BigDecimal amountToman = new BigDecimal(2000000);
        BigDecimal rateUsed = new BigDecimal(1200);
        BigDecimal rateRequest = new BigDecimal(1000);

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

    @Test
    public void testFindAllTranactionsOrderByDesc() {

        List<Transaction> allTransactions = transactionRepository.findAllByOrderByCreatedAtDesc();
        for(Transaction transaction : allTransactions) {
            System.out.println(transaction.getId() + " - " +
                    transaction.getAmountCurrency().toString() + " - " +
                    transaction.getAmountToman().toString() + " - " +
                    transaction.getApprovedAt() + " - " +
                    transaction.getApprovedByUserId() + " - " +
                    transaction.getCreatedAt() + " - " +
                    transaction.getCurrencyId() + " - " +
                    transaction.getCustomerId() + " - " +
                    transaction.getPerformedByUserId() + " - " +
                    transaction.isRequestedByCustomer() + " - " +
                    transaction.getRateUsed() + " - " +
                    transaction.getRequestedRate() + " - " +
                    transaction.getStatus().name() + " - " +
                    transaction.getTxType().name());
        }
    }


    @Test
    public void findByCustomerIdOrderByCreatedAtDescTest() {

        List<Transaction> allTrans = transactionRepository.findByCustomerIdOrderByCreatedAtDesc(1);
        for(Transaction transaction : allTrans) {
            System.out.println(transaction.getId() + " - " +
                    transaction.getAmountCurrency().toString() + " - " +
                    transaction.getAmountToman().toString() + " - " +
                    transaction.getApprovedAt() + " - " +
                    transaction.getApprovedByUserId() + " - " +
                    transaction.getCreatedAt() + " - " +
                    transaction.getCurrencyId() + " - " +
                    transaction.getCustomerId() + " - " +
                    transaction.getPerformedByUserId() + " - " +
                    transaction.isRequestedByCustomer() + " - " +
                    transaction.getRateUsed() + " - " +
                    transaction.getRequestedRate() + " - " +
                    transaction.getStatus().name() + " - " +
                    transaction.getTxType().name());
        }
    }

    @Test
    public void findByStatusOrderByCreatedAtDescTest() {

        List<Transaction> foundTransactions = transactionRepository.findByStatusOrderByCreatedAtDesc(TxStatus.COMPLETED);
        for(Transaction transaction : foundTransactions) {
            System.out.println(transaction.getId() + " - " +
                    transaction.getAmountCurrency().toString() + " - " +
                    transaction.getAmountToman().toString() + " - " +
                    transaction.getApprovedAt() + " - " +
                    transaction.getApprovedByUserId() + " - " +
                    transaction.getCreatedAt() + " - " +
                    transaction.getCurrencyId() + " - " +
                    transaction.getCustomerId() + " - " +
                    transaction.getPerformedByUserId() + " - " +
                    transaction.isRequestedByCustomer() + " - " +
                    transaction.getRateUsed() + " - " +
                    transaction.getRequestedRate() + " - " +
                    transaction.getStatus().name() + " - " +
                    transaction.getTxType().name());
        }
    }

    @Test
    public void findByPreformedByUserIdTest() {

        List<Transaction> foundTransactions = transactionRepository.findByPreformedByUserId(3);
        for(Transaction transaction : foundTransactions) {
            System.out.println(transaction.getId() + " - " +
                    transaction.getAmountCurrency().toString() + " - " +
                    transaction.getAmountToman().toString() + " - " +
                    transaction.getApprovedAt() + " - " +
                    transaction.getApprovedByUserId() + " - " +
                    transaction.getCreatedAt() + " - " +
                    transaction.getCurrencyId() + " - " +
                    transaction.getCustomerId() + " - " +
                    transaction.getPerformedByUserId() + " - " +
                    transaction.isRequestedByCustomer() + " - " +
                    transaction.getRateUsed() + " - " +
                    transaction.getRequestedRate() + " - " +
                    transaction.getStatus().name() + " - " +
                    transaction.getTxType().name());
        }
    }

    @Test
    public void findByApprovedByUserIdTest() {

        List<Transaction> trans = transactionRepository.findByApprovedByUserId(2);
        for(Transaction transaction : trans) {
            System.out.println(transaction.getId() + " - " +
                    transaction.getAmountCurrency().toString() + " - " +
                    transaction.getAmountToman().toString() + " - " +
                    transaction.getApprovedAt() + " - " +
                    transaction.getApprovedByUserId() + " - " +
                    transaction.getCreatedAt() + " - " +
                    transaction.getCurrencyId() + " - " +
                    transaction.getCustomerId() + " - " +
                    transaction.getPerformedByUserId() + " - " +
                    transaction.isRequestedByCustomer() + " - " +
                    transaction.getRateUsed() + " - " +
                    transaction.getRequestedRate() + " - " +
                    transaction.getStatus().name() + " - " +
                    transaction.getTxType().name());
        }

    }

    @Test
    public void findByCurrencyIdAndCreatedAtBetweenTest() {

        LocalDateTime start = LocalDateTime.of(2025, 8, 1, 0, 0, 0);
        LocalDateTime finish = LocalDateTime.of(2027, 8, 7, 23, 59, 59);
        List<Transaction> trans = transactionRepository.findByCurrencyIdAndCreatedAtBetween(1, start, finish);

        for(Transaction transaction : trans) {
            System.out.println(transaction.getId() + " - " +
                    transaction.getAmountCurrency().toString() + " - " +
                    transaction.getAmountToman().toString() + " - " +
                    transaction.getApprovedAt() + " - " +
                    transaction.getApprovedByUserId() + " - " +
                    transaction.getCreatedAt() + " - " +
                    transaction.getCurrencyId() + " - " +
                    transaction.getCustomerId() + " - " +
                    transaction.getPerformedByUserId() + " - " +
                    transaction.isRequestedByCustomer() + " - " +
                    transaction.getRateUsed() + " - " +
                    transaction.getRequestedRate() + " - " +
                    transaction.getStatus().name() + " - " +
                    transaction.getTxType().name());
        }
    }

    @Test
    public void findByStatusAndCreatedAtBetweenTest() {

        LocalDateTime start = LocalDateTime.of(2025, 8, 1, 0, 0, 0);
        LocalDateTime finish = LocalDateTime.of(2027, 8, 7, 23, 59, 59);
        List<Transaction> trans = transactionRepository.findByStatusAndCreatedAtBetween(TxStatus.COMPLETED, start, finish);

        for(Transaction transaction : trans) {
            System.out.println(transaction.getId() + " - " +
                    transaction.getAmountCurrency().toString() + " - " +
                    transaction.getAmountToman().toString() + " - " +
                    transaction.getApprovedAt() + " - " +
                    transaction.getApprovedByUserId() + " - " +
                    transaction.getCreatedAt() + " - " +
                    transaction.getCurrencyId() + " - " +
                    transaction.getCustomerId() + " - " +
                    transaction.getPerformedByUserId() + " - " +
                    transaction.isRequestedByCustomer() + " - " +
                    transaction.getRateUsed() + " - " +
                    transaction.getRequestedRate() + " - " +
                    transaction.getStatus().name() + " - " +
                    transaction.getTxType().name());
        }
    }

    @Test
    public void existsByCustomerIdAndCurrencyIdAndStatusTest() {

        System.out.println(transactionRepository.existsByCustomerIdAndCurrencyIdAndStatus(1, 1, TxStatus.PENDING));
        System.out.println(transactionRepository.existsByCustomerIdAndCurrencyIdAndStatus(2, 1, TxStatus.COMPLETED));
    }

}

package Repository;

import org.example.App;
import org.example.model.Teller;
import org.example.repository.interfaces.TellerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(classes = App.class)
public class TellerRepositoryTest {

    @Autowired
    private TellerRepository tellerRepository;

    @Test
    public void savingTellerTest() {
        Teller teller = new Teller("Akbar asghari", "094948484", 7, "09234566857");
        Teller savedTeller = tellerRepository.save(teller);
        System.out.println(savedTeller.getId());
    }

    @Test
    public void findAllTellersTest() {
        List<Teller> allTellers = tellerRepository.findAll();
        for(Teller teller : allTellers) {
            System.out.println(teller.getId() + " - " +
                    teller.getFullname() + " - " +
                    teller.getUserId() + " - " +
                    teller.getNationalId() + " - " +
                    teller.getPhoneNumber()
                    );
        }
    }

    @Test
    public void findByTellerIdTest() {
        Teller teller = tellerRepository.findById(2);
        assertThat(teller.getId()).isEqualTo(2);
        assertThat(teller.getNationalId()).isEqualTo("094948484");
        assertThat(teller.getUserId()).isEqualTo(7);
        assertThat(teller.getPhoneNumber()).isEqualTo("09234566857");
        assertThat(teller.getFullname()).isEqualTo("Akbar asghari");
    }

    @Test
    public void findingTellerByUserIdTest() {
        Teller teller = tellerRepository.findByUserId(7);
        assertThat(teller.getId()).isEqualTo(2);
        assertThat(teller.getNationalId()).isEqualTo("094948484");
        assertThat(teller.getUserId()).isEqualTo(7);
        assertThat(teller.getPhoneNumber()).isEqualTo("09234566857");
        assertThat(teller.getFullname()).isEqualTo("Akbar asghari");
    }
}

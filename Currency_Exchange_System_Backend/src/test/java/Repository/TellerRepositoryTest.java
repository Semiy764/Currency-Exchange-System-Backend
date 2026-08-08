package Repository;

import org.example.App;
import org.example.model.Teller;
import org.example.repository.interfaces.TellerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(classes = App.class)
public class TellerRepositoryTest {

    @Autowired
    private TellerRepository tellerRepository;

    @Test
    public void savingTellerTest() {
        Teller teller = new Teller("Akbar asghari", "094948484", 3, "09234566857");
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

    @Test
    public void existingByUserIdTest() {
        System.out.println(tellerRepository.existsByUserId(6));
    }

    @Test
    public void existsByIdTest() {
        System.out.println(tellerRepository.existsById(3));
    }

    @Test
    public void existingByPhoneNumberTest() {
        System.out.println(tellerRepository.existsByPhone("09234566856"));
    }

    @Test
    public void deleteTellerTest() {
        tellerRepository.delete(2);
    }

    @Test
    public void deleteByUserIdTest() {
        tellerRepository.deleteByUserId(6);
    }

    @Test
    public void updateTellerTest() {
        Teller teller = tellerRepository.findById(3);
        teller.setFullname("Erfan Simiyari");
        teller.setNationalId("0313464200");
        teller.setPhoneNumber("09107855814");

        tellerRepository.update(teller);

        Teller updatedTeller = tellerRepository.findById(3);
        assertThat(updatedTeller.getId()).isEqualTo(3);
        assertThat(updatedTeller.getUserId()).isEqualTo(7);
        assertThat(updatedTeller.getFullname()).isEqualTo("Erfan Simiyari");
        assertThat(updatedTeller.getNationalId()).isEqualTo("0313464200");
        assertThat(updatedTeller.getPhoneNumber()).isEqualTo("09107855814");
    }
}

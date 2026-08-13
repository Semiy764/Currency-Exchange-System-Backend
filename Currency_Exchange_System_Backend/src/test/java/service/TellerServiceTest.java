package service;

import org.example.App;
import org.example.model.Teller;
import org.example.service.interfaces.TellerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(classes = App.class)
public class TellerServiceTest {

    @Autowired
    private TellerService tellerService;

    @Test
    public void findTellerByIdTest() {
        Teller teller = tellerService.findById(1);
        System.out.println(
                teller.getId() + " - " +
                teller.getUserId() + " - " +
                teller.getPhoneNumber() + " - " +
                teller.getFullname() + " - " +
                teller.getNationalId()
                );
    }

    @Test
    public void findingAllTellersTest() {
        List<Teller> tellerList = tellerService.findAll();
        for(Teller teller : tellerList) {
            System.out.println(
                    teller.getId() + " - " +
                            teller.getUserId() + " - " +
                            teller.getPhoneNumber() + " - " +
                            teller.getFullname() + " - " +
                            teller.getNationalId()
            );
        }
    }

    @Test
    public void existingByTellerIdTest() {
        System.out.println(tellerService.existsById(1));
    }

    @Test
    public void updatingTellerTest() {

        Teller teller = tellerService.findById(2);
        teller.setFullname("fatemeh amookht");
        teller.setPhoneNumber("09380301761");
        teller.setNationalId("4899005989");

        Teller updatedTeller = tellerService.updateTeller(2,teller);

        Teller foundTeller = tellerService.findById(2);
        assertThat(foundTeller.getFullname()).isEqualTo("fatemeh amookht");
        assertThat(foundTeller.getPhoneNumber()).isEqualTo("09380301761");
        assertThat(foundTeller.getNationalId()).isEqualTo("4899005989");
        assertThat(foundTeller.getId()).isEqualTo(2);
        assertThat(foundTeller.getUserId()).isEqualTo(3);
    }

    @Test
    public void findingTellerByUserIdTest() {

        Teller foundTeller = tellerService.findByUserId(3);
        assertThat(foundTeller.getFullname()).isEqualTo("fatemeh amookht");
        assertThat(foundTeller.getPhoneNumber()).isEqualTo("09380301761");
        assertThat(foundTeller.getNationalId()).isEqualTo("4899005989");
        assertThat(foundTeller.getId()).isEqualTo(2);
        assertThat(foundTeller.getUserId()).isEqualTo(3);

    }
}

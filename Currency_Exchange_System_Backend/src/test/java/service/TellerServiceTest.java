package service;

import org.example.App;
import org.example.model.Teller;
import org.example.service.interfaces.TellerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

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
        System.out.println(tellerService.existsById(3));
    }
}

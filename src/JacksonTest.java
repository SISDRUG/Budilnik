import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class JacksonTest {

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void pojoToJsonString() throws IOException {
        Budilnik budilnik = new Budilnik(12, 21, true);
        BudilnikRepository budilnikRepository = new BudilnikRepository();
        budilnikRepository.addBudilnik(budilnik);
        budilnikRepository.addBudilnik(budilnik);
        budilnikRepository.addBudilnik(budilnik);
        String json = objectMapper.writeValueAsString(budilnikRepository);

        System.out.println(json);
    }

    @Test
    public void jsonFileToPojo() throws IOException {
        File file = new File("budilnik.json");

        BudilnikRepository budilnik = objectMapper.readValue(file, BudilnikRepository.class);

        System.out.println(budilnik.getBudilniks());
        System.out.println(budilnik.find(0).isStatus());
        budilnik.showBudilniks();


    }
}
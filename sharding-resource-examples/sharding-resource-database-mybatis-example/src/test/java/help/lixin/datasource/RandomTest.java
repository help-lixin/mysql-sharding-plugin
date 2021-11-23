package help.lixin.datasource;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomTest {

    @Test
    public void test() {
        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");

        Random random = new Random();
        int index = random.nextInt(list.size());
        System.out.println(index);
    }

}

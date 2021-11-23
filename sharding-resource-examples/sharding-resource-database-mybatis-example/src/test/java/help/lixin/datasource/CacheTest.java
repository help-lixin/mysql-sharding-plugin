package help.lixin.datasource;

import com.google.common.base.Equivalence;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import org.junit.Test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

public class CacheTest {

    @Test
    public void testCache() throws ExecutionException {
        String key = "helo";
        Cache<String, List<String>> cache3 = CacheBuilder
                .newBuilder()
                .build();
        List<String> list = cache3.getIfPresent(key);
        System.out.println(list);
        if (null == list) {
            list = new ArrayList<>();
            list.add("0");
            cache3.put(key, list);
        }
        List<String> list2 = cache3.getIfPresent(key);
        System.out.println(list2);

        list2.add("1");
        list2.add("2");
        list2.add("3");
        list2.add("4");
        list2.add("5");
        List<String> list3 = cache3.getIfPresent(key);
        System.out.println(list3);


        Map<String, String> map1 = new HashMap<>();
        map1.put("hello", "world");


        Map<String, String> map2 = new HashMap<>();
        map1.put("hello", "world");

        Cache<Map<String, String>, String> cache = CacheBuilder
                .newBuilder()
                .build();

        String hello = cache.getIfPresent(map1);
//        System.out.println(hello);
//        hello = cache.getIfPresent(map1);
        System.out.println(hello);

        hello = cache.get(map1, new Callable<String>() {
            @Override
            public String call() throws Exception {
                System.out.println("============================");
                return null;
            }
        });
        System.out.println(hello);

        hello = cache.get(map1, new Callable<String>() {
            @Override
            public String call() throws Exception {
                System.out.println("***************************");
                return "world";
            }
        });
        System.out.println(hello);

        Context context1 = new Context();
        context1.setName("test");
        context1.setValue("test");
//        context1.setOthers(map1);

        Context context2 = new Context();
        context2.setName("test");
        context2.setValue("test");
//        context2.setOthers(map1);

        Cache<Context, String> cache2 = CacheBuilder
                .newBuilder()
                .build();

        String value = cache2.get(context1, new Callable<String>() {
            @Override
            public String call() throws Exception {
                System.out.println("=======================================");
                return "hello";
            }
        });

        System.out.println(value);
        value = cache2.get(context2, new Callable<String>() {
            @Override
            public String call() throws Exception {
                System.out.println("===================***====================");
                return "world";
            }
        });
        System.out.println(value);
        System.out.println();
    }
}

class Context implements Serializable {
    private String name;
    private String value;
    private Map<String, String> others = new HashMap<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Map<String, String> getOthers() {
        return others;
    }

    public void setOthers(Map<String, String> others) {
        this.others = others;
    }
}


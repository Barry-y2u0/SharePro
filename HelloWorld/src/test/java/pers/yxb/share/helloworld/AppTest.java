package pers.yxb.share.helloworld;



import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import pers.yxb.share.helloworld.entity.Customer;
import pers.yxb.share.helloworld.repository.CustomerRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = HelloWorldApplication.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AppTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void test() throws Exception {
        customerRepository.save(new Customer("yxnb"));
        System.out.println(customerRepository.findByName("yxnb").getName());
        customerRepository.delete(customerRepository.findByName("yxnb"));
        stringRedisTemplate.opsForValue().set("aaa", "111");
        Assert.assertEquals("111", stringRedisTemplate.opsForValue().get("aaa"));
        System.out.println(stringRedisTemplate.hasKey("aaaa"));
    }
}

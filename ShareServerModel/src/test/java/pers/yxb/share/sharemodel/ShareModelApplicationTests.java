package pers.yxb.share.sharemodel;

import org.hibernate.annotations.Synchronize;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ShareModelApplicationTests {

	public static void main(String[] args) {
		synchronized (Synchronize.class){
			System.out.println("Synchronize");
		}
	}

}

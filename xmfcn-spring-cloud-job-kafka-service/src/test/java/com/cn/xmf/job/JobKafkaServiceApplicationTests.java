package com.cn.xmf.job;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JobKafkaServiceApplicationTests {


	@Autowired
	private Environment environment;

	@Test
	public void contextLoads() {
	}

}

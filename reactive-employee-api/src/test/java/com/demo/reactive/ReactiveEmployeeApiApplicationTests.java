package com.demo.reactive;

import com.demo.reactive.configuration.TestR2dbcConfiguration;
import io.r2dbc.spi.ConnectionFactory;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;
@SpringBootTest()

class ReactiveEmployeeApiApplicationTests {


	@Test
	void contextLoads() {
	}

}

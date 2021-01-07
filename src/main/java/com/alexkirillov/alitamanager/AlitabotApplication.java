package com.alexkirillov.alitamanager;

import com.alexkirillov.alitamanager.dao.WorkdayCleaner;
import com.alexkirillov.alitamanager.security.jwt.JwtConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({JwtConfig.class})
public class AlitabotApplication {

	public static void main(String[] args) {
		SpringApplication.run(AlitabotApplication.class, args);
		try{
			WorkdayCleaner scheduledCleaner = new WorkdayCleaner();
			scheduledCleaner.DayUpdate();
		}catch (Exception e){
			e.printStackTrace();
		}
	}

}

package nikonov.telegramgptbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Телеграм бот к GPR
 */
@EnableFeignClients
@SpringBootApplication
@ConfigurationPropertiesScan
public class TelegramGPTBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(TelegramGPTBotApplication.class, args);
	}
}

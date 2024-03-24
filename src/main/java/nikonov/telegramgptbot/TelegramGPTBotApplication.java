package nikonov.telegramgptbot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Телеграм бот к GPR
 */
@Slf4j
@EnableScheduling
@EnableFeignClients
@SpringBootApplication
public class TelegramGPTBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(TelegramGPTBotApplication.class, args);
	}
}

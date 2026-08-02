package the.aditya.live_chat_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LiveChatBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(LiveChatBackendApplication.class, args);
		System.out.println("Spring Boot Application Started");
	}
}

//cd /d "D:\Cloudflare Tunnel"
//cloudflared.exe tunnel --url http://localhost:8080
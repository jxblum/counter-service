package example.app.counters;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class CounterServiceApplication {

	public static void main(String[] args) {

		new SpringApplicationBuilder(CounterServiceApplication.class)
			.web(WebApplicationType.SERVLET)
			.build()
			.run(args);
	}
}

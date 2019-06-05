package example.app.counters;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * The CounterServiceApplication class...
 *
 * @author John Blum
 * @since 1.0.0
 */
@SpringBootApplication
public class CounterServiceApplication {

	public static void main(String[] args) {

		new SpringApplicationBuilder(CounterServiceApplication.class)
			.web(WebApplicationType.SERVLET)
			.build()
			.run(args);
	}
}

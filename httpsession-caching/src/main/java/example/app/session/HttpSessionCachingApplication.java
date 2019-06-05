package example.app.session;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * The HttpSessionCachingApplication class...
 *
 * @author John Blum
 * @since 1.0.0
 */
@SpringBootApplication
public class HttpSessionCachingApplication {

	public static void main(String[] args) {

		new SpringApplicationBuilder(HttpSessionCachingApplication.class)
			.web(WebApplicationType.SERVLET)
			.build()
			.run(args);
	}
}

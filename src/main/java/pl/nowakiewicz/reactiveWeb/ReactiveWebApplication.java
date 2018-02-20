package pl.nowakiewicz.reactiveWeb;

import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.ipc.netty.http.server.HttpServer;

import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

import static org.springframework.web.reactive.function.BodyInserters.fromObject;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

public class ReactiveWebApplication {
	static AtomicInteger counter = new AtomicInteger(0);

	public static void main(String[] args) {
		RouterFunction route = route( GET("/"),
				request -> {
					String welcomeHtml = "<h1>Welcome :)!</h1>";

					String visitsHtml = "<p>To są " + counter.incrementAndGet() + " odwiedziny</p>";

					LocalDateTime now = LocalDateTime.now();
					DateTimeFormatter myFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
					String timeHtml = "<p>Time: " + now.format(myFormatter) + "</p>";

					String inputHtml = "<input type='text' name='userName'>";
					String submitHtml = "<input type='submit' value='send'>";
					String formHtml = String.format("<form>%s %s</form>", inputHtml, submitHtml);

					return ServerResponse.ok()
							.contentType(new MediaType(MediaType.TEXT_HTML, Charset.forName("utf-8")))
							.body(fromObject("<body>" +
							welcomeHtml + visitsHtml + timeHtml + formHtml
							+ "</body>"
					));
				});


		HttpHandler httpHandler = RouterFunctions.toHttpHandler(route);
		HttpServer server = HttpServer.create("localhost", 8081);
		server.startAndAwait(new ReactorHttpHandlerAdapter(httpHandler));

	}
}
package moviechecker;

import org.springframework.boot.builder.SpringApplicationBuilder;

import moviechecker.app.CheckerApp;

public class CheckerMain {

	public static void main(String[] args) {
		new SpringApplicationBuilder(CheckerApp.class).headless(false).run(args);
	}

}

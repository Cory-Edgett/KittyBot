package core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import core.admin.app.controller.ApplicationController;
import javafx.application.Application;
import javafx.stage.Stage;
import core.utils.ExceptionHandler;
import core.utils.ResourceLoader;

@SpringBootApplication
public class Launcher extends Application {
	private static ApplicationContext ctx;
	public static void main(String[] args) {
		ctx = SpringApplication.run(Launcher.class, args);
		launch(args);
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		
		// try with resources: ctx is always closed
		try {
			KittyBot bot = ctx.getBean(KittyBot.class);
			ApplicationController controller = ctx.getBean(ApplicationController.class);
			controller.setStage(stage);
			controller.loadView(ResourceLoader.loadResource("MainActivity.fxml"));
			bot.printGuilds();
		} catch (InterruptedException e) {
			ExceptionHandler.printErr(e);
		}
	}
}

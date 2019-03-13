package core;

import javax.security.auth.login.LoginException;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import admin.app.controller.ApplicationController;
import core.utils.ExceptionHandler;
import javafx.application.Application;
import javafx.stage.Stage;

public class Launcher extends Application {
	public static void main(String[] args) {
		launch(args);
	}
	@Override
	public void start(Stage stage) {
		// try with resources: ctx is always closed
		try(final AbstractApplicationContext ctx = new ClassPathXmlApplicationContext("coreBeans.xml")) {
			KittyBot bot = ctx.getBean(KittyBot.class);
			//ApplicationController controller = ctx.getBean(ApplicationController.class);
			//bot.setupBot();
			bot.printGuilds();
		} catch (InterruptedException e) {
			ExceptionHandler.printErr(e);
		}
	}
}

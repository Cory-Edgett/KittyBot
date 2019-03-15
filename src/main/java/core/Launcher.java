package core;

import javax.security.auth.login.LoginException;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import admin.app.controller.ApplicationController;
import core.utils.ExceptionHandler;

public class Launcher {
	public static void main(String[] args) {
		try(final AbstractApplicationContext ctx = new ClassPathXmlApplicationContext("coreBeans.xml")) {
			KittyBot bot = ctx.getBean(KittyBot.class);
			bot.printGuilds();
		} catch (InterruptedException e) {
			ExceptionHandler.printErr(e);
		}
	}
}

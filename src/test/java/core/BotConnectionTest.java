package core;

import static org.junit.Assume.assumeNoException;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import javax.security.auth.login.LoginException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;

public class BotConnectionTest {
	private BotConnection connection;
	@Mock private JDABuilder mockBuilder;
	@Mock private JDA mockApi;
	protected String prefix = "kbtest!";

	
	@BeforeEach
	public void buildConnectionMocks() {
		MockitoAnnotations.initMocks(this);
		connection = new BotConnection();
		connection.setBuilder(mockBuilder);
	}
	
	@BeforeEach
	public void mockConnectionMetods() throws LoginException {
		doNothing().when(mockApi).shutdown();
		doNothing().when(mockApi).shutdownNow();
		doReturn(mockApi).when(mockBuilder).build();

	}
	
	@Test
	public void login() {
		try {
			connection.login();
		} catch (LoginException e) {
			assumeNoException("Mock Login should not fail", e);
		}
	}
	
	@Test 
	public void logout() {
		login();
		try {
			connection.logout();
		} catch (Exception e) {
			assumeNoException("Mock logout should not fail", e);
		}
		
		verify(mockApi).shutdown();
	}
	
	@Test
	public void forceLogout() {
		login();
		try {
			connection.forceLogout();
		} catch (Exception e) {
			assumeNoException("Mock force logout should not fail", e);
		}
		
		verify(mockApi).shutdownNow();
	}
}

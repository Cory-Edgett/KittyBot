package core.admin.app.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import core.KittyBot;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

@Component
public class ApplicationController extends ListenerAdapter implements Initializable  {

	@Autowired
	private KittyBot bot;
	private Stage stage;
	@Autowired
	private ChatLogManager chatLogManager;
	private ObservableList<Guild> guildsList;
	private ListProperty<Guild> guildsProperty;
	private ListProperty<String> chatProperty;
	
	private ListProperty<MessageChannel> channelsProperty;
	private Guild selectedGuild;
	private MessageChannel selectedChannel;
	
	@Value("classpath:MainActivity.fxml")
	private Resource fxmlResource;

    @FXML
    private ListView<Guild> guilds;

    @FXML
    private ListView<MessageChannel> channels;
    
    @FXML
    private ListView<String> chat;

    @FXML
    private TextArea messageBox;
    

    @FXML
    private Button send;
    
    public void setChatLogManager(ChatLogManager chatLogManager) {
    	this.chatLogManager = chatLogManager;
    }
    public void setBot(KittyBot bot) {
    	this.bot = bot;
    }
    
	public void setStage(Stage stage) {
		this.stage = stage;
	}
	
    public ApplicationController() throws IOException {
    	guildsList = FXCollections.observableArrayList();
    	
    	channelsProperty = new SimpleListProperty<>();
    	guildsProperty = new SimpleListProperty<>();
    	chatProperty = new SimpleListProperty<>();
    }

    
    @FXML
    public void sendMessage() {
    	if(selectedGuild == null || selectedChannel == null || messageBox.getText().isEmpty()) return;
    	selectedChannel.sendMessage(messageBox.getText()).complete();
    	messageBox.clear();
    }
    
    @FXML
    public void selectGuild() {
    	selectedGuild = guilds.getSelectionModel().getSelectedItem();
    	selectedChannel = null;
    	chatProperty.set(null);
		List<TextChannel> guildChannels = new ArrayList<>(bot.getTextChannels(selectedGuild));
		guildChannels.removeIf(c -> !c.canTalk());
    	channelsProperty.set(FXCollections.observableArrayList(guildChannels));

    }
    
    @FXML
    public void selectChannel() {
    	if(channels.getSelectionModel().getSelectedItem() == selectedChannel) return;
    	selectedChannel = channels.getSelectionModel().getSelectedItem();
    	chatProperty.set(chatLogManager.getChatLog(selectedGuild, selectedChannel));
    	
    }
	
	public void loadView(URL path) throws IOException {
		FXMLLoader loader = new FXMLLoader(path);
		loader.setController(this);
		Parent root = loader.load();
		Scene scene = new Scene(root);
		stage.setTitle("KittyBot v" + bot.getVersion() + " Message Sender");
		stage.setScene(scene);
		stage.setResizable(false);
		stage.setOnCloseRequest(e -> bot.shutdown(0));
		stage.show();
	}
	
	public void updateGuilds() {
		try {
			guildsList = FXCollections.observableArrayList(bot.getGuilds());
			guildsProperty.set(guildsList);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		guilds.itemsProperty().bindBidirectional(guildsProperty);
		channels.itemsProperty().bindBidirectional(channelsProperty);
		chat.itemsProperty().bindBidirectional(chatProperty);
		this.updateGuilds();

	}	
}

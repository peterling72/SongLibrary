/**
 * This is our controller for the GUI
 * The majority of the magic happens here.
 * @author Joseph Natale, Peter Ling
 */

package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Comparator;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedWriter;
import java.util.Scanner;

import app.Song;

public class SongLibController {

	private ObservableList<String> obsList;
	private ObservableList<String> attributeList;
	private ObservableList<String> attLabelList;
	private ArrayList<Song> playList;
	private String saveDataPath;
	
	
	
	 @FXML
	 private MenuItem addButton;
	 
	 @FXML
	 private MenuItem savePlaylistButton;
	 
	 @FXML
	 private Label commitText;

	 @FXML
	 private ListView<String> listView;
	 
	 @FXML
	 private ListView<String> detailList;

	 @FXML
	 private ListView<String> attributeLabels;
	 
	 @FXML
	 private TitledPane detailsTitle;
	 
	 @FXML
	 private TitledPane playlistTitle;
	 
	 @FXML
	 private MenuItem contextDelete;

	 @FXML
	 private SplitPane detailsPane;
	 
	 @FXML
	 private Button save_button;
	 
	 @FXML
	 private Button cancel_button;
	 
	 /*
	  * Called when the user hits the cancel button during new song addition
	  * If the song is new, removes the new song by forwarding event to deleteSong and alters text accordingly
	  * Else, calls editSong to reset the values of this existing song
	  */
	 @FXML
	 void cancelAddition(ActionEvent event) {
		int index = listView.getSelectionModel().getSelectedIndex();
		if (obsList.get(index).equals("New song")) {
			deleteSong(event);
			detailsTitle.setText("Song Details (Double-click to edit)");
			cancel_button.setVisible(false);
		}
		else {
			//Resets the detail values
			editSong(null);
		}
	 }
	 
	 /*
	  * Called when a user either adds through right click or the playlist dropdown
	  * Adds an entry called 'New song' if one doesn't already exist and selects it.
	  */
	 @FXML
	void addSong(ActionEvent event) {
	//Doesn't add another song if we're already trying to add a song
	if (obsList.size() > 0 && obsList.get(obsList.size()-1).equals("New song"))
		return;
	
	Song song = new Song();
	playList.add(song);
	obsList.add("New song");
	listView.getSelectionModel().select(obsList.size()-1);
	}

	
	@FXML
	void deleteSong(ActionEvent event) {
		int index = listView.getSelectionModel().getSelectedIndex();
		if (index < 0 || index >= obsList.size())
			return;
		
		//We only prompt the user if they are removing a saved song.
		if (!obsList.get(index).equals("New song")) {
			Alert alert = new Alert(AlertType.CONFIRMATION, "Delete song \"" + obsList.get(index) + "\"?" , ButtonType.YES, ButtonType.NO);
			alert.showAndWait();
			if (alert.getResult() == ButtonType.NO) {
				return;
			}
		}
		
		System.out.println("removing " + obsList.get(index));
		System.out.println("removing " + playList.get(index).toPlaylistString());
		obsList.remove(index);
		playList.remove(index);
		
		if (index < obsList.size()) {
			// select the next item
			listView.getSelectionModel().select(index);
			Song song = playList.get(index);
			attributeList.clear();
			attributeList.add(song.getTitle());
			attributeList.add(song.getArtist());
			attributeList.add(song.getAlbum());
			attributeList.add(song.getYear());
		}
		else if (obsList.size() > 0) {
			// select the previous item
			listView.getSelectionModel().select(index-1);
			Song song = playList.get(index-1);
			attributeList.clear();
			attributeList.add(song.getTitle());
			attributeList.add(song.getArtist());
			attributeList.add(song.getAlbum());
			attributeList.add(song.getYear());
		}
		else {
			//Empty the detail list
			attributeList.clear();
		}
		savePlaylist(null);
		if (playList.size() <= 0)
			playlistTitle.setText("Right click in empty space to add your first song!");
	}
	
	/*
	 * Called when a song is selected, thus executes on program start if a song exists in the save file
	 * Displays song details for the currently selected song, edits display if the selected song is 'new'
	 */
	@FXML
	void editSong(Stage mainStage) {
		//Hide our details helper text & save button
		commitText.setVisible(false);
		save_button.setVisible(false);
		
		
		
		int index = listView.getSelectionModel().getSelectedIndex();
		//System.out.println("index " + index);
		if (index < 0)
			return;
		
		if (index == obsList.size()-1 && obsList.get(index).equals("New song")) {
			detailsTitle.setText("Enter New Song Details (Double-click to edit)");
			cancel_button.setVisible(true);
		}
		else {
			detailsTitle.setText("Song Details (Double-click to edit)");
			cancel_button.setVisible(false);
		}
		
		//If we've selected an existing song, but we were previously trying to add a new song, delete that new song
		if (index != obsList.size()-1 && obsList.get(obsList.size()-1).equals("New song")){
			obsList.remove(obsList.size()-1);
			playList.remove(playList.size()-1);
		}
		
		//Get song object and populate attribute list
		Song song = playList.get(index);
		attributeList.clear();
		attributeList.add(song.getTitle());
		attributeList.add(song.getArtist());
		attributeList.add(song.getAlbum());
		attributeList.add(song.getYear());
	}
	
	//Shows helper text when user is editing an entry
	@FXML
	void startEdit(Event event) {
		 commitText.setVisible(true);
		 //Hide the save button while we're editing to avoid errors
		 save_button.setVisible(false);
	}
	
	/*
	 * Hides helper text and shows save/cancel buttons
	 * Called after user ends a cell edit
	 */
	@FXML
    void hideLabel(Event event) {
		commitText.setVisible(false);
		
		int index = listView.getSelectionModel().getSelectedIndex();
		//Prevents out of bounds errors
		if (index < 0 || index >= playList.size())
			return;
		
		Song song = playList.get(index);
		
		//If any of the attributes in the list are updated from our saved values, show the save/cancel buttons
		if (attributeList.size() > 0 && (!attributeList.get(0).equals(song.getTitle()) || !attributeList.get(1).equals(song.getArtist())
				|| !attributeList.get(2).equals(song.getAlbum()) || !attributeList.get(3).equals(song.getYear()))) {
			save_button.setVisible(true);
			cancel_button.setVisible(true);
		}
		else
			save_button.setVisible(false);
    }
	
	/*
	 * Triggered when user hits the save button.
	 * If the entered data is valid and has no conflicts, save the add/edit and update the playlist file
	 */
	@FXML
    void saveSongChanges(ActionEvent event) {

		//Hide our details helper text
		commitText.setVisible(false);
		
		int index = listView.getSelectionModel().getSelectedIndex();
		if (index < 0 || index > obsList.size())
			return;
		
		String desired_title = attributeList.get(0).trim();
		String desired_artist = attributeList.get(1).trim();
		String desired_album = attributeList.get(2).trim();
		String desired_year = attributeList.get(3).trim();
		
		Song song = new Song(desired_title, desired_artist);
		boolean commit = true;
		
		//Checks that the song name is valid
		if (desired_title.length() < 1 || desired_artist.length() < 1) {
			Alert alert = new Alert(AlertType.ERROR, "Title and artist cannot be empty! Changes will be erased.", ButtonType.OK);
			alert.setTitle("Error - Songlib");
			alert.setHeaderText("Error - Insufficient data");
			alert.showAndWait();
			
			if (alert.isShowing() == false) {
				song = playList.get(index);
				attributeList.clear();
				attributeList.add(song.getTitle());
				attributeList.add(song.getArtist());
				attributeList.add(song.getAlbum());
				attributeList.add(song.getYear());
				save_button.setVisible(false);
			}
			return;
		}
		
		for (int i = 0; i < playList.size(); i++) {
			if (i != index && playList.get(i).isEqual(song)) {
				commit = false;
			}
		}
		//If there is no conflicting entry, execute this
		if (commit) {
			Song old_song = playList.get(index);
			old_song.setTitle(desired_title);
			old_song.setArtist(desired_artist);
			old_song.setAlbum(desired_album);
			if (desired_year.length() > 0 && desired_year.chars().allMatch(Character::isDigit))
				old_song.setYear(Integer.parseInt(desired_year));
			else {
				attributeList.set(3, old_song.getYear());
			}
			
			obsList.set(index, old_song.toPlaylistString());
			sortPlaylist();
			
			for (int i = 0; i < playList.size(); i++) {
				if (old_song.isEqual(playList.get(i))){
					//Select the song at its new position
					listView.getSelectionModel().select(i);
					break;
				}
			}
			savePlaylist(null);
			playlistTitle.setText("Playlist View");
		}
		//Conflicting entry exists, alert the user and erase changes
		else {
			Alert alert = new Alert(AlertType.ERROR, "This song already exists! Changes will be erased.", ButtonType.OK);
			alert.setTitle("Error - Songlib");
			alert.setHeaderText("Error - Song Conflict");
			alert.showAndWait();
			
			if (alert.isShowing() == false) {
				song = playList.get(index);
				attributeList.clear();
				attributeList.add(song.getTitle());
				attributeList.add(song.getArtist());
				attributeList.add(song.getAlbum());
				attributeList.add(song.getYear());
				save_button.setVisible(false);
			}
			
		}
    }
	
	@FXML
	/**
	 * Saves the current playlist to a textfile for persistent sessions
	 * @param event
	 */
    void savePlaylist(ActionEvent event) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(saveDataPath));
			for (int i = 0; i < playList.size(); i++) {
				writer.write(playList.get(i).toString());
			}
			writer.close();
		} catch (IOException e){
			System.out.println("Writer error.");
		}
    }
	/*
	 * Reads the playlist file at the specified filepath
	 */
	void loadPlaylist(String filepath) {
		try {
			File file = new File(filepath);
			Scanner myReader = new Scanner(file);
			while (myReader.hasNextLine()) {
				String data = myReader.nextLine();
				if (data.equals("{")) {
					
					String title = myReader.nextLine().substring(1);
					String artist = myReader.nextLine().substring(1);
					String album = myReader.nextLine().substring(1);
					String year = myReader.nextLine().substring(1);
					Song song = new Song(title, artist, album, year);

					System.out.println(song.toPlaylistString());
					//Add song info to playlists
					playList.add(song);
					obsList.add(song.toPlaylistString());
					
				}
			}
			myReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("Playlist data not found, proceeding...");
		}
	}
	/**
	 * Sorts the internal and display lists
	 */
	private void sortPlaylist() {
		playList.sort(Comparator.comparing(Song::toPlaylistString, String.CASE_INSENSITIVE_ORDER));
		obsList.sort(Comparator.comparing(String::toString, String.CASE_INSENSITIVE_ORDER));

	}
	
	/*
	 * Called on program execution
	 */
    public void start(Stage mainStage) {
    
    //Initialize lists
    playList = new ArrayList<Song>();
    attributeList = FXCollections.observableArrayList();
    obsList = FXCollections.observableArrayList();
    attLabelList = FXCollections.observableArrayList();
    
    //Populate label list
    attLabelList.add("Title : ");
    attLabelList.add("Artist : ");
    attLabelList.add("Album : ");
    attLabelList.add("Year : ");
    
    //Adjust visibilities
    cancel_button.setVisible(false);
    commitText.setVisible(false);
    save_button.setVisible(false);
    
    //Load playlist file
    saveDataPath = "src\\app\\playlist.txt";
    loadPlaylist(saveDataPath);
    
    //Sorts the playlists just in case
    sortPlaylist();
    
    //Link GUI lists to coded lists
    listView.setItems(obsList);
    detailList.setItems(attributeList);
    attributeLabels.setItems(attLabelList);
    
    //Make our details editable
    detailList.setEditable(true);
    detailList.setCellFactory(TextFieldListCell.forListView());
    
    //Listen for selection change
    listView
	.getSelectionModel()
	.selectedIndexProperty()
	.addListener(
			(obs, oldVal, newVal) -> 
			editSong(mainStage));
    
    //If the list has songs, select the first one
    if (playList.size() > 0)
    	listView.getSelectionModel().select(0);
    else 
    	playlistTitle.setText("Right click in empty space to add your first song!");
    }
}

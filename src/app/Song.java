/**
 * This is the data structure we use for Song entries
 * Songs contain an artist, title, album, and year
 * @author Joseph Natale, Peter Ling
 */
package app;

public class Song {
	private String artist;
	private String title;
	private String album;
	private String year;
	
	public Song() {
		title = "";
		artist = "";
		album = "";
		year = "";
	}
	
	public Song(String title, String artist) {
		this.title = title;
		this.artist = artist;
		album = "";
		year = "";
	}
	
	public Song(String title, String artist, String album, String year) {
		this.title = title;
		this.artist = artist;
		this.album = album;
		this.year = year;
	}

	public String getArtist() {
		return artist;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getAlbum() {
		return album;
	}
	
	public String getYear() {
		return year;
	}
	
	/**
	 * Sets the artist name for a song
	 * Doesn't check if another song exists with this name, please check elsewhere
	 * @param s The desired artist name.
	 */
	public void setArtist(String desired_artist) {
		artist = desired_artist.trim();
	}
	
	/**
	 * Sets the title for a song
	 * Doesn't check if another song exists with this name, please check elsewhere
	 * @param s The desired song title.
	 */
	public void setTitle(String desired_title) {
		title = desired_title.trim();
	}
	
	public void setAlbum(String album_name) {
		album = album_name.trim();
	}
	
	public void setYear(int desired_year) {
		year = Integer.toString(desired_year);
	}
	
	public boolean isEqual(Song song) {
		if (song.getArtist().toLowerCase().equals(artist.toLowerCase()) && song.getTitle().toLowerCase().equals(title.toLowerCase()))
			return true;
		return false;
	}
	
	/**
	 * Returns a string used to display this song in the playlist view
	 * @return A string of the form "{SONG TITLE} - {ARTIST}"
	 */
	public String toPlaylistString() {
		return title + " - " + artist;
	}
	
	/**
	 * Returns a string used for persistent session file
	 * @return A string of the form:
	 * {
	 * 	title
	 * 	artist
	 * 	album
	 *  year
	 * }	
	 */
	public String toString() {
		String str = "{\n=" + title + "\n=" + artist + "\n=" + album + "\n=" + year + "\n}\n";
		return str;
	}
}

/*
 * This class manages, stores, and plays audio content such as songs, podcasts and audiobooks.
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Library
{
    private ArrayList<Song> 			songs;
    private ArrayList<AudioBook> 	audiobooks;
    private ArrayList<Playlist> 	playlists;

    // Library Constructor
    public Library()
    {
        songs 		= new ArrayList<Song>();
        audiobooks 	= new ArrayList<AudioBook>(); ;
        playlists   = new ArrayList<Playlist>();
    }
    /*
     * Download audio content from the store. Since we have decided (design decision) to keep 3 separate lists in our library
     * to store our songs, podcasts and audiobooks (we could have used one list) then we need to look at the type of
     * audio content (hint: use the getType() method and compare to Song.TYPENAME or AudioBook.TYPENAME etc)
     * to determine which list it belongs to above
     *
     * Make sure you do not add song/podcast/audiobook to a list if it is already there. Hint: use the equals() method
     * If it is already in a list, set the errorMsg string and return false. Otherwise add it to the list and return true
     * See the video
     */
    public void download(AudioContent content) {

        // If the audio content is of type Song, add it to the songs arraylist unless it is already in the list
        if (content.getType().equals(Song.TYPENAME)) {

            // If the song is already in the songs arraylist, throw a SongDownloadedException
            Song song = (Song) content;
            if (songs.contains(song)) {
                throw new SongDownloadedException("Song " + song.getTitle() + " already downloaded");
            }

            // Add the song to the songs arraylist
            songs.add(song);
            System.out.println("SONG " + song.getTitle() + " Added to Library");

            // If the audio content is of type AudioBook, add it to the audiobooks arraylist unless it is already in the list
        } else if (content.getType().equals(AudioBook.TYPENAME)) {

            // If the audiobook is already in the audiobook playlist, throw a AudioBookDownloadedException
            AudioBook aBook = (AudioBook) content;
            if (audiobooks.contains(aBook)) {
                throw new AudioBookDownloadedException("AudioBook " + aBook.getTitle() + " already downloaded");
            }

            // Add the audiobook to the audiobooks arraylist
            audiobooks.add(aBook);
            System.out.println("AUDIOBOOK " + aBook.getTitle() + " Added to Library");

        } else {
            // If the content does not exist, throw a ContentNotExistException
            throw new ContentNotExistException("Content does not exist");
        }
    }


    // Print Information (printInfo()) about all songs in the array list
    public void listAllSongs()
    {
        for (int i = 0; i < songs.size(); i++)
        {
            int index = i + 1;
            System.out.print("" + index + ". ");
            songs.get(i).printInfo();
            System.out.println();
        }
    }

    // Print Information (printInfo()) about all audiobooks in the array list
    public void listAllAudioBooks()
    {
        for (int i = 0; i < audiobooks.size(); i++) {
            int index = i + 1;
            System.out.print("" + index + ". ");
            audiobooks.get(i).printInfo();
            System.out.println();
        }
    }

    // Print the name of all playlists in the playlists array list
    // First print the index number as in listAllSongs() above
    public void listAllPlaylists()
    {
        for (int i = 0; i < playlists.size(); i++) {
            int index = i + 1;
            System.out.print("" + index + ". ");
            System.out.print(playlists.get(i).getTitle());
            System.out.println();
        }
    }

    // Print the name of all artists.
    public void listAllArtists()
    {
        // First create a new (empty) array list of string
        // Go through the songs array list and add the artist name to the new arraylist only if it is
        // not already there. Once the artist arraylist is complete, print the artists names
        ArrayList<String> artists = new ArrayList<String>();

        // Add the artist name to the new arraylist only if it is not already there
        for (int i = 0; i < songs.size(); i++) {
            Song currSong = songs.get(i);
            if (artists.contains(currSong.getArtist())) {
                continue;
            }
            artists.add(currSong.getArtist());
        }

        // Print the artists names
        for (int i = 0; i < artists.size(); i++) {
            int index = i + 1;
            System.out.print("" + index + ". " + artists.get(i));
            System.out.println();
        }

    }

    // Delete a song from the library (i.e. the songs list) -
    // also go through all playlists and remove it from any playlist as well if it is part of the playlist
    public void deleteSong(int index)
    {
        // If index is invalid, throw a SongNotFound exception
        if (index < 1 || index > songs.size())
        {
            throw new SongNotFoundException("Song Not Found");
        }

        // Remove song from all playlists
        for (int i = 0; i < playlists.size(); i++) {
            Playlist currPlaylist = playlists.get(i);

            // Remove song from current playlist
            for (int j = 0; j < currPlaylist.getContent().size(); j++) {
                AudioContent content = currPlaylist.getContent().get(j);

                if (content.getType() == Song.TYPENAME) {
                    if (content.equals(songs.get(index-1))) {
                        currPlaylist.getContent().remove(j);
                    }
                }
            }
        }

        // Remove song from songs arraylist and return true
        songs.remove(index-1);
    }

    //Sort songs in library by year
    public void sortSongsByYear()
    {
        // Use Collections.sort() to compare the years and sort from oldest to latest
        Collections.sort(songs, new SongYearComparator());

    }
    // Write a class SongYearComparator that implements
    // the Comparator interface and compare two songs based on year
    private class SongYearComparator implements Comparator<Song>
    {
        // Compare the years of both songs and return the appropriate value
        @Override
        public int compare(Song o1, Song o2) {

            if (o1.getYear() > o2.getYear()) {
                return 1;
            } else if (o1.getYear() < o2.getYear()) {
                return -1;
            } else {
                return 0;
            }

        }
    }

    // Sort songs by length
    public void sortSongsByLength()
    {
        // Use Collections.sort() to compare the lengths of the songs and sort from smallest to biggest length
        Collections.sort(songs, new SongLengthComparator());

    }
    // Write a class SongLengthComparator that implements
    // the Comparator interface and compare two songs based on length
    private class SongLengthComparator implements Comparator<Song>
    {
        // Compare the lengths of both songs and return the appropriate value
        @Override
        public int compare(Song o1, Song o2) {

            if (o1.getLength() > o2.getLength()) {
                return 1;
            } else if (o1.getLength() < o2.getLength()) {
                return -1;
            } else {
                return 0;
            }

        }
    }

    // Sort songs by title
    public void sortSongsByName()
    {
        // Use Collections.sort()
        // class Song should implement the Comparable interface
        // see class Song code
        Collections.sort(songs);

    }

    /*
     * Play Content
     */

    // Play song from songs list
    public void playSong(int index)
    {
        // If the index is invalid, throw a SongNotFoundException
        if (index < 1 || index > songs.size())
        {
            throw new SongNotFoundException("Song Not Found");
        }

        // Call the play() method using the song chosen
        songs.get(index-1).play();
    }

    // Play a chapter of an audiobook from list of audiobooks
    public void playAudioBook(int index, int chapter)
    {
        // If the index is invalid, throw a AudioBookNotFoundException
        if (index < 1 || index > audiobooks.size())
        {
            throw new AudioBookNotFoundException("AudioBook Not Found");
        }

        // If the chapter is invalid, throw a ChapterNotFoundException
        AudioBook currentBook = audiobooks.get(index-1);
        if (chapter < 1 || chapter > currentBook.getNumberOfChapters()) {
            throw new ChapterNotFoundException("Chapter Not Found");
        }

        // Select the chapter to be played and call the play() method in class AudioBook using the audiobook chosen
        currentBook.selectChapter(chapter);
        currentBook.play();
    }

    // Print the chapter titles (Table Of Contents) of an audiobook
    // see class AudioBook
    public void printAudioBookTOC(int index)
    {
        // If the index is invalid, throw a AudioBookNotFoundException
        if (index < 1 || index > audiobooks.size())
        {
            throw new AudioBookNotFoundException("AudioBook Not Found");

        }

        // Call the printTOC() method in class AudioBook using the audiobook chosen
        audiobooks.get(index-1).printTOC();
    }

    /*
     * Playlist Related Methods
     */

    // Make a new playlist and add to playlists array list
    // Make sure a playlist with the same title doesn't already exist
    public void makePlaylist(String title)
    {
        // Iterate through all the playlists and check if the inputted playlist title is valid
        for (Playlist currentPlaylist : playlists) {

            // If the inputted playlist title is equal to the current playlist title, throw a PlaylistExistsException
            if (currentPlaylist.getTitle().equals(title)) {
                throw new PlaylistExistsException("Playlist " + currentPlaylist.getTitle() + " Already Exists");
            }
        }

        // Create a new playlist with the given playlist title
        // Add it to the playlists array and return true
        Playlist playlist = new Playlist(title);
        playlists.add(playlist);
    }

    // Print list of content information (songs, audiobooks etc) in playlist named title from list of playlists
    public void printPlaylist(String title)
    {
        // Initialize boolean and Playlist variables
        boolean titleIn = false;
        Playlist currPlaylist = null;

        // Iterate through the playlists
        for (int i = 0; i < playlists.size(); i++) {

            // If the inputted playlist title is equal to the current playlist title
            // Set the current playlist and set the boolean variable to true
            if (playlists.get(i).getTitle().equals(title)) {
                currPlaylist = playlists.get(i);
                titleIn = true;
                break;
            }
        }

        // If the inputted playlist title is not valid, throw a PlaylistNotExistsException
        if (!titleIn) {
            throw new PlaylistNotExistsException("Invalid Playlist");
        }

        // Print the contents of the current playlist by calling printContents() in class Playlist
        currPlaylist.printContents();
    }

    // Play all content in a playlist
    public void playPlaylist(String playlistTitle)
    {
        // Initialize boolean and playlist variables
        boolean titleIn = false;
        Playlist currPlaylist = null;

        // Iterate through the playlists
        for (int i = 0; i < playlists.size(); i++) {

            // If the inputted playlist title is equal to the current playlist title
            // Set the current playlist and set the boolean variable to true
            if (playlists.get(i).getTitle().equals(playlistTitle)) {
                currPlaylist = playlists.get(i);
                titleIn = true;
                break;
            }
        }

        // If the inputted playlist title is not valid, throw a PlaylistNotExistsException
        if (!titleIn) {
            throw new PlaylistNotExistsException("Invalid Playlist");
        }

        // Play all the contents of the current playlist by calling playAll() in class Playlist
        currPlaylist.playAll();
    }

    // Play a specific audio content in a playlist
    public void playPlaylist(String playlistTitle, int indexInPL)
    {
        // Initialize boolean and playlist variables
        boolean titleIn = false;
        Playlist currPlaylist = null;

        // Check if the inputted playlist title is equal to any of the playlist titles
        for (int i = 0; i < playlists.size(); i++) {

            if (playlists.get(i).getTitle().equals(playlistTitle)) {
                currPlaylist = playlists.get(i);
                titleIn = true;
                break;
            }
        }

        // If the inputted playlist title is not valid, throw a PlaylistNotExistsException
        if (!titleIn) {
            throw new PlaylistNotExistsException("Invalid Playlist");
        }

        // If the index is invalid, throw a InvalidIndexException
        if (indexInPL < 1 || indexInPL > currPlaylist.getContent().size()) {
            throw new InvalidIndexException("Invalid Index");
        }

        // Print the title of the playlist
        System.out.println(currPlaylist.getTitle());
        currPlaylist.play(indexInPL);		// Play the content at given index using the play() method in class Playlist
    }

    // Add a song/audiobook/podcast from library lists at top to a playlist
    // Use the type parameter and compare to Song.TYPENAME etc
    // to determine which array list it comes from then use the given index
    // for that list
    public void addContentToPlaylist(String type, int index, String playlistTitle) {

        // Initialize boolean and playlist variables
        boolean titleIn = false;
        Playlist currPlaylist = null;

        // Check if the inputted playlist title is equal to any of the playlist titles
        for (int i = 0; i < playlists.size(); i++) {

            if (playlists.get(i).getTitle().equals(playlistTitle)) {
                titleIn = true;
                currPlaylist = playlists.get(i);
                break;
            }
        }

        // If the inputted playlist title is not valid, throw a PlaylistNotExistsException
        if (!titleIn) {
            throw new PlaylistNotExistsException("Invalid Playlist");

        }

        int contentIndex = index - 1;

        // Check the type of content inputted and check if given index is valid
        // Add the content the playlist
        if (type.equalsIgnoreCase(Song.TYPENAME)) {
            if (index > songs.size() || index < 1) {
                throw new InvalidIndexException("Invalid Index");

            }

            // Check if the song is already in the playlist, throw an exception if it is
            for (int i = 0; i < currPlaylist.getContent().size(); i++) {
                AudioContent audioContent = currPlaylist.getContent().get(i);

                if (audioContent.getType().equals(Song.TYPENAME)) {
                    if (audioContent.equals(songs.get(contentIndex))) {
                        throw new SongInPlaylistException("Song already in Playlist");
                    }
                }
            }
            currPlaylist.addContent(songs.get(contentIndex));

        } else if (type.equalsIgnoreCase(AudioBook.TYPENAME)) {
            if (index > audiobooks.size() || index < 1) {
                throw new InvalidIndexException("Invalid Index");
            }

            // Check if the audiobook is already in the playlist, throw an exception if it is
            for (int i = 0; i < currPlaylist.getContent().size(); i++) {
                AudioContent audioContent = currPlaylist.getContent().get(i);

                if (audioContent.getType().equals(AudioBook.TYPENAME)) {
                    if (audioContent.equals(audiobooks.get(contentIndex))) {
                        throw new AudioBookInPlaylistException("AudioBook already in Playlist");
                    }
                }
            }
            currPlaylist.addContent(audiobooks.get(contentIndex));

        } else {
            // Throw a PlaylistInvalidTypeException if the type inputted is invalid
            throw new PlaylistInvalidTypeException("Invalid Type");
        }

    }

    // Delete a song/audiobook/podcast from a playlist with the given title
    // Make sure the given index of the song/audiobook/podcast in the playlist is valid
    public void delContentFromPlaylist(int index, String title)
    {
        // Initialize boolean and playlist variables
        boolean titleIn = false;
        Playlist currPlaylist = null;

        // Check if the inputted playlist title is equal to any of the playlist titles
        for (int i = 0; i < playlists.size(); i++) {

            if (playlists.get(i).getTitle().equals(title)) {
                currPlaylist = playlists.get(i);
                titleIn = true;
                break;
            }
        }

        // If the inputted playlist title is not valid, throw a PlaylistNotExistsException
        if (!titleIn) {
            throw new PlaylistNotExistsException("Invalid Playlist");

        }

        // If the index is invalid, throw a InvalidIndexException
        if (index < 1 || index > currPlaylist.getContent().size()) {
            throw new InvalidIndexException("Invalid Index");
        }

        // Delete the specified content farom the playlist by calling deleteContent() in class Playlist
        currPlaylist.deleteContent(index);
    }

}

/*
 * Custom Exception Classes
 */
class SongDownloadedException extends RuntimeException {
    public SongDownloadedException(String message) {
        super(message);
    }
}

class AudioBookDownloadedException extends RuntimeException {
    public AudioBookDownloadedException(String message) {
        super(message);
    }
}

class ContentNotExistException extends RuntimeException {
    public ContentNotExistException(String message) {
        super(message);
    }
}
class SongNotFoundException extends RuntimeException {
    public SongNotFoundException(String message) {
        super(message);
    }
}

class AudioBookNotFoundException extends RuntimeException {
    public AudioBookNotFoundException(String message) {
        super(message);
    }
}

class ChapterNotFoundException extends RuntimeException {
    public ChapterNotFoundException(String message) {
        super(message);
    }
}

class PlaylistExistsException extends RuntimeException {
    public PlaylistExistsException(String message) {
        super(message);
    }
}

class PlaylistNotExistsException extends RuntimeException {
    public PlaylistNotExistsException(String message) {
        super(message);
    }
}

class InvalidIndexException extends RuntimeException {
    public InvalidIndexException(String message) {
        super(message);
    }
}

class SongInPlaylistException extends RuntimeException {
    public SongInPlaylistException(String message) {
        super(message);
    }
}

class AudioBookInPlaylistException extends RuntimeException {
    public AudioBookInPlaylistException(String message) {
        super(message);
    }
}

class PlaylistInvalidTypeException extends RuntimeException {
    public PlaylistInvalidTypeException(String message) {
        super(message);
    }
}

// Name: George Cui
// Simulation of a Simple Text-based Music App (like Apple Music)

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.StringTokenizer;

public class MyAudioUI
{
    public static void main(String[] args)
    {
        // Simulation of audio content in an online store
        // The songs, podcasts, audiobooks in the store can be downloaded to your mylibrary
        AudioContentStore store = new AudioContentStore();

        // Create my music mylibrary
        Library mylibrary = new Library();

        Scanner scanner = new Scanner(System.in);
        System.out.print(">");

        // Process keyboard actions
        while (scanner.hasNextLine())
        {
            try {

                String action = scanner.nextLine();

                if (action == null || action.equals("")) {
                    System.out.print("\n>");
                    continue;
                } else if (action.equalsIgnoreCase("Q") || action.equalsIgnoreCase("QUIT"))
                    return;

                else if (action.equalsIgnoreCase("STORE"))    // List all songs
                {
                    store.listAll();
                } else if (action.equalsIgnoreCase("SONGS"))    // List all songs
                {
                    mylibrary.listAllSongs();
                } else if (action.equalsIgnoreCase("BOOKS"))    // List all songs
                {
                    mylibrary.listAllAudioBooks();
                } else if (action.equalsIgnoreCase("ARTISTS"))    // List all songs
                {
                    mylibrary.listAllArtists();
                } else if (action.equalsIgnoreCase("PLAYLISTS"))    // List all play lists
                {
                    mylibrary.listAllPlaylists();
                }

                // Download audio content (song/audiobook/podcast) from the store
                // Specify the index of the content
                else if (action.equalsIgnoreCase("DOWNLOAD")) {

                    // Initialize index variables
                    int fromIndex = 0;
                    int toIndex = 0;

                    // Prompt user for the start index of the store content
                    System.out.print("From Store Content #: ");
                    if (scanner.hasNextInt()) {
                        fromIndex = scanner.nextInt();
                        scanner.nextLine(); // "consume" nl character (necessary when mixing nextLine() and nextInt())
                    }

                    // Prompt user t=for the end index of the store context
                    System.out.print("To Store Content #: ");
                    if (scanner.hasNextInt()) {
                        toIndex = scanner.nextInt();
                        scanner.nextLine(); // "consume" nl character (necessary when mixing nextLine() and nextInt())
                    }

                    // Create an arraylist of potential downloads
                    ArrayList<AudioContent> downloads = new ArrayList<AudioContent>();

                    // Add the specified store content into the downloads arraylist
                    for (int i = fromIndex; i < toIndex + 1; i++) {
                        downloads.add(store.getContent(i));
                    }

                    // Attempt to download the store content within the range
                    for (int i = 0; i < downloads.size(); i++) {

                        // Download the store content by adding it into the appropriate content arraylist
                        try {
                            mylibrary.download(downloads.get(i));

                        // If a NullPointerException occurred (User entered number that isnt a valid index), print an error message
                        } catch (NullPointerException e) {
                            System.out.println("Content Not Found in Store");

                        // If an exception occurred, print its error message
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                    }

                // Searches the store for an audio content with the specified title
                } else if (action.equalsIgnoreCase("SEARCH")) {

                    // Initialize title string variable
                    String title = "";

                    // Prompt user for the title and store the input
                    System.out.print("Title: ");
                    if (scanner.hasNextLine()) {
                        title = scanner.nextLine();
                    }

                    // Get the index of the specific title in the contents arraylist
                    int index = store.search(title);

                    // Print the info of the content with the specified title
                    System.out.print("" + (index+1) + ". ");
                    store.getContent(index+1).printInfo();
                    System.out.println();

                // Searches the store for a song with the specified artist
                } else if (action.equalsIgnoreCase("SEARCHA")) {

                    // Initialize artist string variable
                    String artist = "";

                    // Prompt user for the artist and store the input
                    System.out.print("Artist: ");
                    if (scanner.hasNextLine()) {
                        artist = scanner.nextLine();
                    }

                    // Get the indices of the content from the specified artist in the contents arraylist
                    ArrayList<Integer> indices = store.searchArtist(artist);

                    // Print the info of the content with the specified artist
                    for (int i = 0; i < indices.size(); i++) {
                        int index = indices.get(i);

                        System.out.print("" + (index+1) + ". ");
                        store.getContent(index + 1).printInfo();
                        System.out.println();
                    }


                // Searches the store for a song with the specified genre
                } else if (action.equalsIgnoreCase("SEARCHG")) {

                    // Initialize genre string variable
                    String genre = "";

                    // Prompt user for the genre and store the input
                    System.out.print("Genre [POP, ROCK, JAZZ, HIPHOP, RAP, CLASSICAL]: ");
                    if (scanner.hasNextLine()) {
                        genre = scanner.nextLine();
                    }

                    // Get the indices of the content from the specified genre in the contents arraylist
                    ArrayList<Integer> indices = store.searchGenre(genre);

                    // Print the info of the content with the specified genre
                    for (int i = 0; i < indices.size(); i++) {
                        int index = indices.get(i);

                        System.out.print("" + (index+1) + ". ");
                        store.getContent(index+1).printInfo();
                        System.out.println();
                    }

                    // Takes an artist string as parameter and downloads all audio content with this artist name from the store
                } else if (action.equalsIgnoreCase("DOWNLOADA")) {

                    // Initialize artist string variable
                    String artist = "";

                    // Prompt user for the artist and store the input
                    System.out.print("Artist: ");
                    if (scanner.hasNextLine()) {
                        artist = scanner.nextLine();
                    }

                    // Get the indices of the content from the specified artist in the contents arraylist
                    ArrayList<Integer> indices = store.searchArtist(artist);

                    // Download the content, printing an error message for each failed download
                    for (int i = 0; i < indices.size(); i++) {
                        try {
                            mylibrary.download(store.getContent(indices.get(i)+1));
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                    }

                // Takes a genre string as parameter and downloads all songs in this genre from the store
                } else if (action.equalsIgnoreCase("DOWNLOADG")) {

                    // Initialize genre string variable
                    String genre = "";

                    // Prompt user for the genre and store the input
                    System.out.print("Genre: ");
                    if (scanner.hasNextLine()) {
                        genre = scanner.nextLine();
                    }

                    // Get the indices of the content from the specified genre in the contents arraylist
                    ArrayList<Integer> indices = store.searchGenre(genre);

                    // Download the content, printing an error message for each failed download
                    for (int i = 0; i < indices.size(); i++) {
                        try {
                            mylibrary.download(store.getContent(indices.get(i) + 1));
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                    }

                }

                // Get the *library* index (index of a song based on the songs list)
                // of a song from the keyboard and play the song
                else if (action.equalsIgnoreCase("PLAYSONG")) {
                    // Initialize index variable
                    int index = 0;

                    // Prompt user for the song number and store the input
                    System.out.print("Song Number: ");
                    if (scanner.hasNextInt()) {
                        index = scanner.nextInt();
                        scanner.nextLine(); // "consume" nl character (necessary when mixing nextLine() and nextInt())
                    }

                    // Print error message if the song doesn't exist in the library
                    // Call playSong() method from class Library
                    mylibrary.playSong(index);

                }

                // Print the table of contents (TOC) of an audiobook that
                // has been downloaded to the library. Get the desired book index
                // from the keyboard - the index is based on the list of books in the library
                else if (action.equalsIgnoreCase("BOOKTOC")) {
                    // Initialize index variable
                    int index = 0;

                    // Prompt user for audiobook number and store the input
                    System.out.print("Audio Book Number: ");
                    if (scanner.hasNextInt()) {
                        index = scanner.nextInt();
                        scanner.nextLine(); // "consume" nl character (necessary when mixing nextLine() and nextInt())
                    }

                    // Print error message if the audiobook doesn't exist in the library
                    // Call printAudioBookTOC() from class Library
                    mylibrary.printAudioBookTOC(index);
                }

                // Similar to playsong above except for audio book
                // In addition to the book index, read the chapter
                // number from the keyboard - see class Library
                else if (action.equalsIgnoreCase("PLAYBOOK")) {
                    // Initialize index and chapter index variables
                    int index = 0;
                    int chapter = 0;

                    // Prompt user for audiobook number and store the input
                    System.out.print("Audio Book Number: ");
                    if (scanner.hasNextInt()) {
                        index = scanner.nextInt();
                        scanner.nextLine(); // "consume" nl character (necessary when mixing nextLine() and nextInt())

                    }

                    // Prompt user for the chapter and store the input
                    System.out.print("Chapter: ");
                    if (scanner.hasNextInt()) {
                        chapter = scanner.nextInt();
                        scanner.nextLine(); // "consume" nl character (necessary when mixing nextLine() and nextInt())
                    }

                    // Print error message if the song doesn't exist in the library
                    // Call playAudioBook() method in class Library
                    mylibrary.playAudioBook(index, chapter);
                }


                // Specify a playlist title (string)
                // Play all the audio content (songs, audiobooks, podcasts) of the playlist
                // see class Library for the method to call
                else if (action.equalsIgnoreCase("PLAYALLPL")) {
                    // Initialize playlist title variable
                    String title = "";

                    // Prompt user for the title of the playlist and store the input
                    System.out.print("Playlist Title: ");
                    if (scanner.hasNextLine()) {
                        title = scanner.nextLine();
                    }

                    // Print error message if the playlist does not exist
                    // Call playPlaylist() in class Library (One argument)
                    mylibrary.playPlaylist(title);

                }
                // Specify a playlist title (string)
                // Read the index of a song/audiobook/podcast in the playlist from the keyboard
                // Play all the audio content
                // see class Library for the method to call
                else if (action.equalsIgnoreCase("PLAYPL")) {
                    // Initialize playlist title variable
                    String title = "";
                    int index = 0;

                    // Prompt user for the title of the playlist and store the input
                    System.out.print("Playlist Title: ");
                    if (scanner.hasNextLine()) {
                        title = scanner.nextLine();
                    }

                    // Prompt user for the content in the playlist and store the input
                    System.out.print("Content Number: ");
                    if (scanner.hasNextInt()) {
                        index = scanner.nextInt();
                        scanner.nextLine();
                    }

                    // Print error message if the playlist does not exist
                    // Call playPlaylist() in class Library (two arguments)
                    mylibrary.playPlaylist(title, index);

                }
                // Delete a song from the list of songs in mylibrary and any playlists it belongs to
                // Read a song index from the keyboard
                // see class Library for the method to call
                else if (action.equalsIgnoreCase("DELSONG")) {
                    // Initialize song index variable
                    int index = 0;

                    // Prompt user for the song number and store the input
                    System.out.print("Library Song #: ");
                    if (scanner.hasNextInt()) {
                        index = scanner.nextInt();
                        scanner.nextLine(); // "consume" nl character (necessary when mixing nextLine() and nextInt())
                    }

                    // Print error message if the song does not exist in the library
                    // Call deleteSong() in class Library
                    mylibrary.deleteSong(index);
                }

                // Read a title string from the keyboard and make a playlist
                // see class Library for the method to call
                else if (action.equalsIgnoreCase("MAKEPL")) {
                    // Initialize playlist title variable
                    String title = "";

                    // Prompt user for title of the playlist and store the input
                    System.out.print("Playlist Title: ");
                    if (scanner.hasNextLine()) {
                        title = scanner.nextLine();
                    }

                    // Print error message if the playlist already exists
                    // Call makePlaylist() in class Library
                    mylibrary.makePlaylist(title);

                }

                // Print the content information (songs, audiobooks, podcasts) in the playlist
                // Read a playlist title string from the keyboard
                // see class Library for the method to call
                else if (action.equalsIgnoreCase("PRINTPL"))    // print playlist content
                {
                    // Initialize playlist title variable
                    String title = "";

                    // Prompt user for title of the playlist and store the input
                    System.out.print("Playlist Title: ");
                    if (scanner.hasNextLine()) {
                        title = scanner.nextLine();
                    }

                    // Print error message if the playlist does not exist
                    // Call printPlaylist() in class Library
                    mylibrary.printPlaylist(title);

                }

                // Add content (song, audiobook, podcast) from mylibrary (via index) to a playlist
                // Read the playlist title, the type of content ("song" "audiobook" "podcast")
                // and the index of the content (based on song list, audiobook list etc) from the keyboard
                // see class Library for the method to call
                else if (action.equalsIgnoreCase("ADDTOPL")) {
                    // Initialize playlist title variable, playlist index variable, and type of content variable
                    String title = "";
                    int index = 0;
                    String type = "";

                    // Prompt user for the title of the playlist and store the input
                    System.out.print("Playlist Title: ");
                    if (scanner.hasNextLine()) {
                        title = scanner.nextLine();
                    }

                    // Prompt user for the content type and store the input
                    System.out.print("Content Type [SONG, PODCAST, AUDIOBOOK]: ");
                    if (scanner.hasNextLine()) {
                        type = scanner.nextLine();
                    }

                    // Prompt user for the index of the library content and store the input
                    System.out.print("Library Content #: ");
                    if (scanner.hasNextInt()) {
                        index = scanner.nextInt();
                        scanner.nextLine(); // "consume" nl character (necessary when mixing nextLine() and nextInt())
                    }

                    // Print error message if playlist does not exist or library content does not exist
                    // Call addContentToPlaylist() in class Library
                    mylibrary.addContentToPlaylist(type, index, title);

                }

                // Delete content from play list based on index from the playlist
                // Read the playlist title string and the playlist index
                // see class Library for the method to call
                else if (action.equalsIgnoreCase("DELFROMPL")) {
                    // Initialize playlist title variable and playlist index variable
                    String title = "";
                    int index = 0;

                    // Prompt user for the title of the playlist and store the input
                    System.out.print("Playlist Title: ");
                    if (scanner.hasNextLine()) {
                        title = scanner.nextLine();
                    }

                    // Prompt user for the index of the content in the playlist and store the input
                    System.out.print("Playlist Content #: ");
                    if (scanner.hasNextInt()) {
                        index = scanner.nextInt();
                        scanner.nextLine(); // "consume" nl character (necessary when mixing nextLine() and nextInt())
                    }

                    // Print error message if playlist does not exist or playlist content does not exist
                    // Call delContentFromPlaylist() in class Library
                    mylibrary.delContentFromPlaylist(index, title);

                } else if (action.equalsIgnoreCase("SORTBYYEAR")) // sort songs by year
                {
                    mylibrary.sortSongsByYear();
                } else if (action.equalsIgnoreCase("SORTBYNAME")) // sort songs by name (alphabetic)
                {
                    mylibrary.sortSongsByName();
                } else if (action.equalsIgnoreCase("SORTBYLENGTH")) // sort songs by length
                {
                    mylibrary.sortSongsByLength();
                }

            // If an exception occurs in the Library class, print its error message
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            System.out.print("\n>");
        }
    }
}

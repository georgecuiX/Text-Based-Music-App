// Simulation of audio content in an online store
// The songs, podcasts, audiobooks listed here can be "downloaded" to your library

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class AudioContentStore
{
    private ArrayList<AudioContent> contents;

    // Initialize maps
    private HashMap<String, Integer> titleMap;
    private HashMap<String, ArrayList<Integer>> artistMap;
    private HashMap<String, ArrayList<Integer>> genreMap;

    // AudioContentStore constructor
    public AudioContentStore()
    {
        // Read the file using a private method and check if an IOException occurs
        try {
            contents = readFile("store.txt");

        // If an IOException occurs in the private method, print the error message and terminate the program
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }

        // Initialize all the maps
        titleMap = new HashMap<String, Integer>();
        artistMap = new HashMap<String, ArrayList<Integer>>();
        genreMap = new HashMap<String, ArrayList<Integer>>();

        // Fill in the maps with keys and appropriate values
        for (int i = 0; i < contents.size(); i++) {
            AudioContent content = contents.get(i);

            // Add titles and their associate content index into the title map
            titleMap.put(content.getTitle(), i);

            // If the audio content is a song, fill in the artist map and genre map accordingly
            if (content.getType().equals("SONG")) {

                // Get the artist of the specific song
                Song song = (Song) content;
                String artist = song.getArtist();

                // If the artist map does not contain the artist, add the artist as a key to the artist map and associate it with an arraylist of indices
                if (!artistMap.containsKey(artist)) {
                    artistMap.put(artist, new ArrayList<Integer>());
                }
                // Add the content index of the song to the index arraylist of the specific artist
                artistMap.get(artist).add(i);

                // If the genre map does not contain the genre, add the genre as a key to the artist map and associate it with an arraylist of indices
                String genre = song.getGenre().name();
                if (!genreMap.containsKey(genre)) {
                    genreMap.put(genre, new ArrayList<Integer>());
                }
                // Add the content index of the song to the index arraylist of the specific genre
                genreMap.get(genre).add(i);
            }

            // If the audio content is a audiobook, fill in the artist map accordingly
            if (content.getType().equals("AUDIOBOOK")) {

                // Get the author of the specific AudioBook
                AudioBook audioBook = (AudioBook) content;
                String author = audioBook.getAuthor();

                // If the artist map does not contain the author, add the author as a key to the artist map and associate it with an arraylist of indices
                if (!artistMap.containsKey(author)) {
                    artistMap.put(author, new ArrayList<Integer>());
                }
                // Add the content index of the audiobook to the index arraylist of the specific author
                artistMap.get(author).add(i);
            }

        }

    }

    // Private method to read the file, with the potential of throwing a FileNotFoundException
    private ArrayList<AudioContent> readFile(String fileContent) throws FileNotFoundException {

        // Create a scanner variable using the content of the file
        Scanner in = new Scanner(new File(fileContent));

        // Initialize the contents arraylist
        contents = new ArrayList<AudioContent>();

        // Go through the entire file, creating Song and AudioBook objects and adding them to the contents arraylist
        while (in.hasNextLine()) {

            // Get the type of the audiocontent
            String type = in.nextLine();

            // Song specific content
            if (type.equalsIgnoreCase("SONG")) {
                System.out.println("Loading SONG");

                // Store all the song properties
                String id = in.nextLine();
                String title = in.nextLine();
                String year = in.nextLine();
                String length = in.nextLine();
                String artist = in.nextLine();
                String composer = in.nextLine();
                Song.Genre genre = Song.Genre.valueOf(in.nextLine());

                // Create the lyrics of the song
                String numLines = in.nextLine();
                String lyrics = "";

                for (int i = 0; i < Integer.parseInt(numLines); i++) {
                    lyrics += in.nextLine() + "\n";
                }

                // Create a song object using the song properties and add the object to the contents arraylist
                contents.add(new Song(title, Integer.parseInt(year), id, Song.TYPENAME, lyrics, Integer.parseInt(length), artist, composer, genre, lyrics));
            }

            // Audiobook specific content
            if (type.equalsIgnoreCase("AUDIOBOOK")) {
                System.out.println("Loading AUDIOBOOK");

                // Store all the audiobook properties
                String id = in.nextLine();
                String title = in.nextLine();
                String year = in.nextLine();
                String length = in.nextLine();
                String author = in.nextLine();
                String narrator = in.nextLine();
                String numChapters = in.nextLine();
                int nChapters = Integer.parseInt(numChapters);

                // Create and fill an arraylist with chapter titles
                ArrayList<String> chapterTitles = new ArrayList<String>();
                for (int i = 0; i < nChapters; i++) {
                    chapterTitles.add(in.nextLine());
                }

                // Create and fill an arraylist with chapters
                ArrayList<String> chapters = new ArrayList<String>();
                for (int i = 0; i < nChapters; i++) {
                    String lines = in.nextLine();

                    String chapterContent = "";
                    for (int j = 0; j < Integer.parseInt(lines); j++) {
                        chapterContent += in.nextLine() + "\n";
                    }
                    chapters.add(chapterContent);
                }

                // Create an audiobook object using the audiobook properties and add the object to the contents arraylist
                contents.add(new AudioBook(title, Integer.parseInt(year), id, AudioBook.TYPENAME, "", Integer.parseInt(length), author, narrator, chapterTitles, chapters));
            }
        }

        // Close the scanner and return the contents arraylist
        in.close();
        return contents;
    }

    /**
     * Searches for a title in a map of titles and their corresponding indices. If the title is found, returns its index;
     * otherwise, throws a TitleNotFoundException.
     *
     * @param title the title to search for
     * @return the index of the title if found
     * @throws TitleNotFoundException if the title is not found in the map
     */
    public int search(String title) {

        // If the titleMap contains the title, return the index of that title in the contents arraylist
        if (titleMap.containsKey(title)) {

            // Return the index of the content with the specific title
            return titleMap.get(title);

        } else {
            // Throw a TitleNotFoundException if the title is not found in the map
            throw new TitleNotFoundException("No matches for " + title);
        }
    }

    /**
     * Searches for all content indices in the artistMap that have the specified artist. Returns a list of indices if
     * the artist is found; otherwise, throws an ArtistNotFoundException.
     *
     * @param artist the name of the artist to search for
     * @return an ArrayList of indices of all content in the artistMap with the specified artist
     * @throws ArtistNotFoundException if the artist is not found in the artistMap
     */
    public ArrayList<Integer> searchArtist(String artist) {

        // If the artistMap contains the artist, return the arraylist of indices of that artist's content in the contents arraylist
        if (artistMap.containsKey(artist)) {

            // Return the indices of the content with the specific artist
            return artistMap.get(artist);

        } else {
            // Throw a ArtistNotFoundException if the artist is not found in the map
            throw new ArtistNotFoundException("No matches for " + artist);
        }
    }

    /**
     * Searches for all content indices in the genreMap that have the specified genre. Returns a list of indices if
     * the genre is found; otherwise, throws an GenreNotFoundException.
     *
     * @param genre the name of the artist to search for
     * @return an ArrayList of indices of all content in the genreMap with the specified genre
     * @throws GenreNotFoundException if the genre is not found in the genreMap
     */
    public ArrayList<Integer> searchGenre(String genre) {

        // Check if the inputted genre is in the genre map
        if (genreMap.containsKey(genre)) {

            // Return the indices of the content with the specific genre
            return genreMap.get(genre);

        } else {
            // Throw a new GenreNotFoundException if the genre is not found in the map
            throw new GenreNotFoundException("No matches for " + genre);
        }
    }


    // Get the content at specific index
    public AudioContent getContent(int index)
    {
        // If the index is valid, return null
        if (index < 1 || index > contents.size())
        {
            return null;
        }
        // Return the content at specified index
        return contents.get(index-1);
    }

    // List all the store content
    public void listAll()
    {
        for (int i = 0; i < contents.size(); i++)
        {
            int index = i + 1;
            System.out.print("" + index + ". ");
            contents.get(i).printInfo();
            System.out.println();
        }
    }

}

/*
 * Custom exception classes
 */
class TitleNotFoundException extends RuntimeException {
    public TitleNotFoundException(String message) {
        super(message);
    }
}

class ArtistNotFoundException extends RuntimeException {
    public ArtistNotFoundException(String message) {
        super(message);
    }
}

class GenreNotFoundException extends RuntimeException {
    public GenreNotFoundException(String message) {
        super(message);
    }
}

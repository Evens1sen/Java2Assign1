import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MovieAnalyzer {

    static class Movie {
        String seriesTitle; // Name of the movie

        Integer releasedYear; // Year at which that movie released

        String certificate; // Certificate earned by that movie

        Integer runtime; // Total runtime of the movie

        String genre; // Genre of the movie

        Double imdbRating; // Rating of the movie at IMDB site

        String overview; // mini story/ summary

        Integer metaScore; // Score earned by the movie

        String director; // Name of the Director

        String star1; // Name of the Stars

        String star2;

        String star3;

        String star4;

        Integer noOfVotes; // Total number of votes

        Integer gross; // Money earned by that movie

        public Movie(String[] movieInfo) {
            this.seriesTitle = movieInfo[1];
            this.releasedYear = Integer.parseInt(movieInfo[2]);
            this.certificate = movieInfo[3];
            this.runtime = Integer.parseInt(movieInfo[4].split(" ")[0]);
            this.genre = movieInfo[5];
            this.imdbRating = Double.parseDouble(movieInfo[6]);
            this.overview = movieInfo[7];
            this.metaScore = movieInfo[8].equals("") ? null : Integer.parseInt(movieInfo[8]);
            this.director = movieInfo[9];
            this.star1 = movieInfo[10];
            this.star2 = movieInfo[11];
            this.star3 = movieInfo[12];
            this.star4 = movieInfo[13];
            this.noOfVotes = movieInfo[14].equals("") ? null : Integer.parseInt(movieInfo[14]);
            if (movieInfo[15].equals("")) {
                this.gross = null;
            } else {
                this.gross = Integer.parseInt(movieInfo[15].substring(1, movieInfo[15].length() - 1).replace(" ", ""));
            }
        }
    }

    List<Movie> movieList;

    public MovieAnalyzer(String dataset_path) {
        movieList = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(dataset_path));
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                movieList.add(parseLineToMovie(line));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    public Movie parseLineToMovie(String line) {
        List<Character> characters = new ArrayList<>(line.chars().mapToObj(e -> (char) e).toList());

        boolean inQuote = false;
        for (int i = 0; i < characters.size(); i++) {
            if (characters.get(i) == '"') {
                inQuote = !inQuote;
            }

            if (characters.get(i) == ',' && inQuote) {
                characters.set(i, ' ');
            }
        }

        if (characters.get(characters.size() - 1) == ',') {
            characters.add(' ');
        }

        String[] movieInfo = characters.stream().map(String::valueOf)
                .collect(Collectors.joining()).split(",");
        for (int i = 0; i < movieInfo.length; i++) {
            movieInfo[i] = movieInfo[i].trim();
        }

        return new Movie(movieInfo);
    }


    public static void main(String[] args) {
        MovieAnalyzer movieAnalyzer = new MovieAnalyzer("/Users/mhy/Code/javaworkspace/CS209/assignment/A1/resources/imdb_top_500.csv");
    }

}
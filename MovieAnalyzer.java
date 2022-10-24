import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MovieAnalyzer {

    static class Movie {
        String seriesTitle; // Name of the movie

        Integer releasedYear; // Year at which that movie released

        String certificate; // Certificate earned by that movie

        Integer runtime; // Total runtime of the movie

        List<String> genre; // Genre of the movie

        Float imdbRating; // Rating of the movie at IMDB site

        String overview; // mini story/ summary

        Integer metaScore; // Score earned by the movie

        String director; // Name of the Director

        String star1; // Name of the Stars

        String star2;

        String star3;

        String star4;

        Integer noOfVotes; // Total number of votes

        Long gross; // Money earned by that movie

        public Movie(String[] movieInfo) {
            this.seriesTitle = movieInfo[1].charAt(0) == '"'
                    ? movieInfo[1].substring(1, movieInfo[1].length() - 1) : movieInfo[1];
            this.releasedYear = Integer.parseInt(movieInfo[2]);
            this.certificate = movieInfo[3];
            this.runtime = Integer.parseInt(movieInfo[4].split(" ")[0]);

            this.genre = new ArrayList<>();
            if (movieInfo[5].charAt(0) != '"') {
                this.genre.add(movieInfo[5]);
            } else {
                this.genre = Arrays.asList(movieInfo[5]
                        .substring(1, movieInfo[5].length() - 1)
                        .split(", "));
            }

            this.imdbRating = Float.parseFloat(movieInfo[6]);
            this.overview = movieInfo[7].charAt(0) == '"'
                    ? movieInfo[7].substring(1, movieInfo[7].length() - 1) : movieInfo[7];

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
                this.gross = Long.parseLong(movieInfo[15]
                        .substring(1, movieInfo[15].length() - 1)
                        .replace(",", ""));
            }
        }

        public String getSeriesTitle() {
            return seriesTitle;
        }

        public Integer getReleasedYear() {
            return releasedYear;
        }

        public String getCertificate() {
            return certificate;
        }

        public Integer getRuntime() {
            return runtime;
        }

        public List<String> getGenre() {
            return genre;
        }

        public Float getImdbRating() {
            return imdbRating;
        }

        public String getOverview() {
            return overview;
        }

        public Integer getMetaScore() {
            return metaScore;
        }

        public String getDirector() {
            return director;
        }

        public String getStar1() {
            return star1;
        }

        public String getStar2() {
            return star2;
        }

        public String getStar3() {
            return star3;
        }

        public String getStar4() {
            return star4;
        }

        public Integer getNoOfVotes() {
            return noOfVotes;
        }

        public Long getGross() {
            return gross;
        }
    }

    static class Pair {
        String star;

        Double num;

        public Pair(String star, Double num) {
            this.star = star;
            this.num = num;
        }

        public String getStar() {
            return star;
        }

        public Double getNum() {
            return num;
        }
    }

    List<Movie> movieList;

    public MovieAnalyzer(String datasetPath) {
        movieList = new ArrayList<>();
        try {
            FileInputStream fis = new FileInputStream(datasetPath);
            InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
            BufferedReader br = new BufferedReader(isr);

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
        String[] movieInfo = line.split(",(?=([^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)", -1);
        return new Movie(movieInfo);
    }

    public Map<Integer, Integer> getMovieCountByYear() {
        Map<Integer, Integer> unsorted = movieList.stream()
                .collect(Collectors
                        .groupingBy(Movie::getReleasedYear, Collectors.summingInt(m -> 1)));

        LinkedHashMap<Integer, Integer> res = new LinkedHashMap<>();
        unsorted.entrySet().stream()
                .sorted(Map.Entry.comparingByKey(Comparator.reverseOrder()))
                .forEachOrdered(x -> res.put(x.getKey(), x.getValue()));
        return res;
    }

    public Map<String, Integer> getMovieCountByGenre() {
        Map<String, Integer> unsorted = new HashMap<>();
        for (Movie movie : movieList) {
            for (String genre : movie.getGenre()) {
                unsorted.put(genre, unsorted.getOrDefault(genre, 0) + 1);
            }
        }

        LinkedHashMap<String, Integer> res = new LinkedHashMap<>();
        unsorted.entrySet().stream()
                .sorted((o1, o2) -> {
                    if (o1.getValue().equals(o2.getValue())) {
                        return o1.getKey().compareTo(o2.getKey());
                    }

                    return -o1.getValue().compareTo(o2.getValue());
                })
                .forEachOrdered(x -> res.put(x.getKey(), x.getValue()));
        return res;
    }

    public Map<List<String>, Integer> getCoStarCount() {
        Map<List<String>, Integer> unsorted = new HashMap<>();
        for (Movie movie : movieList) {
            List<String> pair12 = Stream.of(movie.getStar1(),
                    movie.getStar2()).sorted().collect(Collectors.toList());
            unsorted.put(pair12, unsorted.getOrDefault(pair12, 0) + 1);

            List<String> pair13 = Stream.of(movie.getStar1(),
                    movie.getStar3()).sorted().collect(Collectors.toList());
            unsorted.put(pair13, unsorted.getOrDefault(pair13, 0) + 1);

            List<String> pair14 = Stream.of(movie.getStar1(),
                    movie.getStar4()).sorted().collect(Collectors.toList());
            unsorted.put(pair14, unsorted.getOrDefault(pair14, 0) + 1);

            List<String> pair23 = Stream.of(movie.getStar2(),
                    movie.getStar3()).sorted().collect(Collectors.toList());
            unsorted.put(pair23, unsorted.getOrDefault(pair23, 0) + 1);

            List<String> pair24 = Stream.of(movie.getStar2(),
                    movie.getStar4()).sorted().collect(Collectors.toList());
            unsorted.put(pair24, unsorted.getOrDefault(pair24, 0) + 1);

            List<String> pair34 = Stream.of(movie.getStar3(),
                    movie.getStar4()).sorted().collect(Collectors.toList());
            unsorted.put(pair34, unsorted.getOrDefault(pair34, 0) + 1);
        }

        return unsorted;
    }

    public List<String> getTopMovies(int topK, String by) {
        if (by.equals("runtime")) {
            return movieList.stream()
                    .sorted((o1, o2) -> {
                        if (Objects.equals(o2.getRuntime(), o1.getRuntime())) {
                            return o1.getSeriesTitle().compareTo(o2.getSeriesTitle());
                        }
                        return o2.getRuntime() - o1.getRuntime();
                    })
                    .map(Movie::getSeriesTitle)
                    .limit(topK)
                    .collect(Collectors.toList());
        } else if (by.equals("overview")) {
            return movieList.stream()
                    .sorted((o1, o2) -> {
                        if (o2.getOverview().length() == o1.getOverview().length()) {
                            return o1.getSeriesTitle().compareTo(o2.getSeriesTitle());
                        }
                        return o2.getOverview().length() - o1.getOverview().length();
                    })
                    .map(Movie::getSeriesTitle)
                    .limit(topK)
                    .collect(Collectors.toList());
        } else {
            return null;
        }
    }

    public List<String> getTopStars(int topK, String by) {
        Map<String, List<Movie>> starMovieMap = new HashMap<>();
        for (Movie movie : movieList) {
            if (starMovieMap.containsKey(movie.star1)) {
                starMovieMap.get(movie.star1).add(movie);
            } else {
                starMovieMap.put(movie.star1, new ArrayList<>(List.of(movie)));
            }

            if (starMovieMap.containsKey(movie.star2)) {
                starMovieMap.get(movie.star2).add(movie);
            } else {
                starMovieMap.put(movie.star2, new ArrayList<>(List.of(movie)));
            }

            if (starMovieMap.containsKey(movie.star3)) {
                starMovieMap.get(movie.star3).add(movie);
            } else {
                starMovieMap.put(movie.star3, new ArrayList<>(List.of(movie)));
            }

            if (starMovieMap.containsKey(movie.star4)) {
                starMovieMap.get(movie.star4).add(movie);
            } else {
                starMovieMap.put(movie.star4, new ArrayList<>(List.of(movie)));
            }
        }

        List<Pair> list = new ArrayList<>();
        if (by.equals("rating")) {
            starMovieMap.forEach(
                    (star, movies) -> {
                        list.add(new Pair(star, movies.stream()
                                .filter(movie -> movie.getImdbRating() != null)
                                .collect(Collectors.averagingDouble(Movie::getImdbRating))));
                    }
            );
        } else if (by.equals("gross")) {
            starMovieMap.forEach(
                    (star, movies) -> {
                        list.add(new Pair(star, movies.stream()
                                .filter(movie -> movie.getGross() != null)
                                .collect(Collectors.averagingDouble(Movie::getGross))));
                    }
            );
        } else {
            return null;
        }

        list.sort((o1, o2) -> {
            if (Objects.equals(o1.num, o2.num)) {
                return o1.star.compareTo(o2.star);
            }
            return o2.num.compareTo(o1.num);
        });
        return list.stream().map(Pair::getStar).limit(topK).collect(Collectors.toList());
    }

    public List<String> searchMovies(String genre, float minRating, int maxRuntime) {
        return movieList.stream()
                .filter(movie -> movie.genre.contains(genre)
                        && movie.imdbRating >= minRating
                        && movie.runtime <= maxRuntime)
                .map(Movie::getSeriesTitle)
                .sorted()
                .collect(Collectors.toList());
    }

    public static void main(String[] args) {
        MovieAnalyzer movieAnalyzer = new MovieAnalyzer("/Users/mhy/Code/javaworkspace/CS209/assignment/A1/resources/imdb_top_500.csv");
        movieAnalyzer.movieList.stream()
                .filter(movie -> movie.getGross() != null)
                .map(Movie::getSeriesTitle)
                .forEach(System.out::println);

        // Q1
        Map<Integer, Integer> movieCountByYear = movieAnalyzer.getMovieCountByYear();
        System.out.println(movieCountByYear);

        // Q2
        Map<String, Integer> movieCountByGenre = movieAnalyzer.getMovieCountByGenre();
        System.out.println(movieCountByGenre);

        // Q3
        Map<List<String>, Integer> coStarCount = movieAnalyzer.getCoStarCount();
        System.out.println(coStarCount);

        // Q4
        List<String> topkMovies = movieAnalyzer.getTopMovies(100, "overview");
        System.out.println(topkMovies);

        // Q5
        List<String> topkStars = movieAnalyzer.getTopStars(1, "rating");
        System.out.println(topkStars);

        // Q6
        List<String> searchMovies = movieAnalyzer.searchMovies("Adventure", 8.0f, 150);
        System.out.println(searchMovies);
    }

}

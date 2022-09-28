package practice.lab4;

import java.io.IOException;

import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class Practice4 {
    public static class City {
        private String name;
        private String state;
        private int population;

        public City(String name, String state, int population) {
            this.name = name;
            this.state = state;
            this.population = population;
        }

        public String getName() {
            return name;
        }

        public String getState() {
            return state;
        }

        public int getPopulation() {
            return population;
        }

        @Override
        public String toString() {
            return "City{" +
                    "name='" + name + '\'' +
                    ", state='" + state + '\'' +
                    ", population=" + population +
                    '}';
        }
    }

    public static Stream<City> readCities(String filename) throws IOException {
        return Files.lines(Paths.get(filename))
                .map(l -> l.split(", "))
                .map(a -> new City(a[0], a[1], Integer.parseInt(a[2])));
    }

    public static void main(String[] args) throws IOException, URISyntaxException {
        String filePath = "/Users/mhy/Code/javaworkspace/CS209/src/main/java/practice/lab4/cities.txt";
        Stream<City> cities = readCities(filePath);
        // Q1: count how many cities there are for each state
        Map<String, Long> cityCountPerState = cities.collect(Collectors.groupingBy(City::getState, Collectors.counting()));
        System.out.println("# of cities per state");
        System.out.println(cityCountPerState);

        cities = readCities(filePath);
        // Q2: count the total population for each state
        Map<String, Integer> statePopulation = cities.collect(Collectors.groupingBy(City::getState, Collectors.summingInt(City::getPopulation)));
        System.out.println("population per state");
        System.out.println(statePopulation);

        cities = readCities(filePath);
        // Q3: for each state, get the set of cities with >500,000 population
        Map<String, Set<City>> largeCitiesByState = cities.filter(city -> city.getPopulation() > 500000).collect(Collectors.groupingBy(City::getState, Collectors.toSet()));
        System.out.println("cities with > 500000 population per state");
        for (Map.Entry<String, Set<City>> entry : largeCitiesByState.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }
    }
}

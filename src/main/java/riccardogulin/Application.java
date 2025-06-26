package riccardogulin;

import com.github.javafaker.Faker;
import riccardogulin.entities.User;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Application {

	public static void main(String[] args) {

		Supplier<User> usersSupplier = () -> {
			Random rndm = new Random();
			Faker faker = new Faker(Locale.ITALY);

			return new User(faker.lordOfTheRings().character(), faker.name().lastName(), rndm.nextInt(1, 100), faker.lordOfTheRings().location());
		};

		List<User> users = new ArrayList<>();

		for (int i = 0; i < 100; i++) {
			users.add(usersSupplier.get());
		}

		users.forEach(System.out::println);

		// ************************************************************ COLLECTORS *****************************************************
		// 1. Raggruppiamo gli user maggiorenni per città
		Map<String, List<User>> usersByCity = users.stream().filter(user -> user.getAge() > 17).collect(Collectors.groupingBy(user -> user.getCity()));
		usersByCity.forEach((city, usersList) -> System.out.println("Città: " + city + ", " + usersList));

		// 2. Raggruppiamo gli user per età
		Map<Integer, List<User>> usersByAge = users.stream().collect(Collectors.groupingBy(user -> user.getAge()));
		usersByAge.forEach((age, usersList) -> System.out.println("Età: " + age + ", " + usersList));

		// 3. Concateniamo tutti i nomi e cognomi degli user, "Aldo Baglio, Giovanni Storti, Giacomo Poretti, ..."
		String namesAndSurnames = users.stream().map(user -> user.getName() + " " + user.getSurname()).collect(Collectors.joining(", "));
		System.out.println(namesAndSurnames);

		// 4. Concateniamo tutte le età
		String ages = users.stream().map(user -> "" + user.getAge()).collect(Collectors.joining(", "));
		System.out.println(ages);

		// 5. Calcolo la somma delle età
		int sum = users.stream().collect(Collectors.summingInt(user -> user.getAge()));
		System.out.println("La somma delle età è: " + sum);

		// 6. Calcoliamo la media delle età
		double average = users.stream().collect(Collectors.averagingInt(user -> user.getAge()));
		System.out.println("La media delle età é: " + average);

		// 7. Raggruppiamo gli user per città e calcoliamo la media delle età per ognuna di esse
		Map<String, Double> averageAgePerCity = users.stream().collect(Collectors.groupingBy(user -> user.getCity(), Collectors.averagingInt(user -> user.getAge())));
		averageAgePerCity.forEach((city, averageAge) -> System.out.println("Città: " + city + ", average age: " + averageAge));

		// 8. Raggruppiamo gli user per città e calcoliamo varie statistiche come media età, somma età, età minima, età massima...
		Map<String, IntSummaryStatistics> statsPerCity = users.stream().collect(Collectors.groupingBy(user -> user.getCity(), Collectors.summarizingInt(user -> user.getAge())));
		statsPerCity.forEach((city, stats) -> System.out.println("Città: " + city + ", stats: " + stats));

	}
}

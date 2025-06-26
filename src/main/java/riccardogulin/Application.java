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

		// **************************************************************** COMPARATORS **************************************************************
		System.out.println("**************************************************************** COMPARATORS **************************************************************");

		// 1. Ordiniamo gli utenti per età (Ordine Crescente)
		List<User> usersSortedByAge = users.stream().sorted(Comparator.comparing(user -> user.getAge())).toList();
		usersSortedByAge.forEach(user -> System.out.println(user));

		// 2. Ordiniamo gli utenti per età (Ordine Derescente)
		List<User> usersSortedByAgeDesc = users.stream().sorted(Comparator.comparing(User::getAge).reversed()).toList();
		usersSortedByAgeDesc.forEach(user -> System.out.println(user));

		// 3. Ordiniamo gli utenti per cognome
		List<User> usersSortedBySurname = users.stream().sorted(Comparator.comparing(user -> user.getSurname())).toList();
		usersSortedBySurname.forEach(user -> System.out.println(user));

		// **************************************************************** LIMIT **************************************************************
		System.out.println("**************************************************************** LIMIT **************************************************************");

		// 1. Otteniamo i 5 user più vecchi, tramite il sorted li ordino per età decrescente, poi tramite il limit tengo solo i primi 5
		List<User> fiveOldUsers = users.stream().sorted(Comparator.comparing(User::getAge).reversed()).skip(0).limit(10).toList();
		fiveOldUsers.forEach(user -> System.out.println(user));

		// **************************************************************** MAP TO **************************************************************
		System.out.println("**************************************************************** MAP TO **************************************************************");

		// 1. Calcolo della somma delle età tramite reduce
		System.out.println("1. Calcolo della somma delle età tramite reduce");
		int totalAge = users.stream().map(user -> user.getAge()).reduce(0, (partialSum, currentAge) -> partialSum + currentAge);
		System.out.println("Somma tramite reduce: " + totalAge);

		// 2. Calcolo della somma delle età tramite mapToInt
		System.out.println("1. Calcolo della somma delle età tramite mapToInt");
		int totalAge2 = users.stream().mapToInt(user -> user.getAge()).sum();
		System.out.println("Somma tramite mapToInt: " + totalAge2);

		// 3. Calcolo dell'età media tramite mapToInt
		System.out.println("3. Calcolo dell'età media tramite mapToInt");
		OptionalDouble average2 = users.stream().mapToInt(user -> user.getAge()).average();
		if (average2.isPresent()) System.out.println("La media è: " + average2.getAsDouble());
		else System.out.println("Non è possibile calcolare la media perché la lista è vuota");

		// 4. Calcolo dell'età maggiore tramite mapToInt
		System.out.println("4. Calcolo dell'età maggiore tramite mapToInt");
		OptionalInt maxAge = users.stream().mapToInt(user -> user.getAge()).max();
		if (maxAge.isPresent()) System.out.println("L'età massima è: " + maxAge.getAsInt());
		else System.out.println("Non è stato possibile determinare l'età massima perché la lista è vuota");

		// 5. Otteniamo statistiche sull'età tramite mapToInt
		IntSummaryStatistics stats = users.stream().mapToInt(user -> user.getAge()).summaryStatistics();
		System.out.println(stats);

	}
}

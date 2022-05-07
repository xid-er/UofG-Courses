package shapes;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Grades {
	public static void main(String[] args) {
		List<String> grades = Arrays.asList("*****", "*****", "", "**", "****", "*****", "*****", "***");
		double grade = grades.stream()
				// Count the number of "*" characters in each
				.map(s -> s.chars().filter(c -> c == '*').count())
				// Take the top 5 scores
				.sorted(Comparator.reverseOrder()).limit(5)
				// Sum them and scale to a mark out of 22
				.mapToLong(x -> x).sum() * (22.0 / 25.0);
		
		System.out.println(grade);
	}
}

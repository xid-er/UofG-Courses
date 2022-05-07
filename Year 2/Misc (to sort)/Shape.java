package shapes;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Random;

public class Shape {
	private Color color;
	private int weight;

	public Shape(Color color, int weight) {
		this.color = color;
		this.weight = weight;
	}

	/**
	 * @return the color
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * @param color the color to set
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * @return the weight
	 */
	public int getWeight() {
		return weight;
	}

	/**
	 * @param weight the weight to set
	 */
	public void setWeight(int weight) {
		this.weight = weight;
	}

	@Override
	public int hashCode() {
		return Objects.hash(color, weight);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Shape)) {
			return false;
		}
		Shape other = (Shape) obj;
		return Objects.equals(color, other.color) && weight == other.weight;
	}

	@Override
	public String toString() {
		return color + "(" + weight + ")";
	}

	public static void main(String[] args) {
		// Generate some random shapes
		Random rand = new Random();
		List<Shape> shapes = new ArrayList<>();
		Arrays.stream(new int[] { 1, 2, 3 });
		shapes.add(new Shape(Color.RED, rand.nextInt(100)));
		shapes.add(new Shape(Color.RED, rand.nextInt(100)));
		shapes.add(new Shape(Color.BLUE, rand.nextInt(100)));
		shapes.add(new Shape(Color.BLUE, rand.nextInt(100)));
		Collections.shuffle(shapes);
		System.out.println(shapes);
		
		Optional<Shape> firstMatch = shapes.stream()
			.filter(s -> s.getColor().equals(Color.RED))
			.findFirst();

		if (firstMatch.isPresent()) {
			Shape s = firstMatch.get();
		} else {
			// No shape found
		}
		
		OptionalInt minValue = shapes.stream()
				.filter(s -> s.getColor().equals(Color.YELLOW))
				.mapToInt(s -> s.getWeight())
				.min();
		if (minValue.isPresent()) {
			System.out.println("min weight: " + minValue.getAsInt());
		} else {
			System.out.println("No matching shapes");
		}
	}

}

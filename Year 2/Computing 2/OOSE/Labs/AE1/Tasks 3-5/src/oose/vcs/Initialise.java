package oose.vcs;

import vehicle.types.Airplane;
import vehicle.types.Bicycle;
import vehicle.types.Boat;
import vehicle.types.Bus;
import vehicle.types.Car;
import vehicle.types.Helicopter;
import vehicle.types.Motorcycle;
import vehicle.types.Ship;
import vehicle.types.Train;
import vehicle.types.Tram;
import vehicle.types.Truck;
import vehicle.types.Vehicle;

public class Initialise {
	Vehicle initialiseVehicle(String vehicleName) {
		if(vehicleName.equals("Boat")) {
			return new Boat("Apollo ");
		}
		else if(vehicleName.equals("Ship")) {
			return new Ship("Cruizz");
		}
		else if(vehicleName.equals("Truck")) {
			return new Truck("Ford F-650");
		}
		else if(vehicleName.equals("Motorcycle")) {
			return new Motorcycle("Suzuki");
		}
		else if(vehicleName.equals("Bus")) {
			return new Bus("Aero");
		}
		else if(vehicleName.equals("Car")) {
			return new Car("BMW");
		}
		else if(vehicleName.equals("Bicycle")) {
			return new Bicycle("A-bike");
		}
		else if(vehicleName.equals("Helicopter")) {
			return new Helicopter("Eurocopter");
		}
		else if(vehicleName.equals("Airplane")) {
			return new Airplane("BA");
		}
		else if(vehicleName.equals("Tram")) {
			return new Tram("EdinburghTram");
		}
		else if(vehicleName.equals("Train")) {
			return new Train("Virgin",4);
		}
		else return null;
	}
}

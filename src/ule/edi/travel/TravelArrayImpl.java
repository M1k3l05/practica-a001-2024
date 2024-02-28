package ule.edi.travel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import ule.edi.model.*;


public class TravelArrayImpl implements Travel {

	private static final Double DEFAULT_PRICE = 100.0;
	private static final Byte DEFAULT_DISCOUNT = 25;
	private static final Byte CHILDREN_EXMAX_AGE = 18;
	private Date travelDate;
	private int nSeats;

	private Double price; // precio de entradas
	private Byte discountAdvanceSale; // descuento en venta anticipada (0..100)

	private Seat[] seats;

	public TravelArrayImpl(Date date, int nSeats) {
		seats = new Seat[nSeats];
		this.travelDate = date;
		this.price = DEFAULT_PRICE;
		this.discountAdvanceSale = DEFAULT_DISCOUNT;
		this.nSeats = nSeats;

	}

	public TravelArrayImpl(Date date, int nSeats, Double price, Byte discount) {
		seats = new Seat[nSeats];
		this.travelDate = date;
		this.price = price;
		this.discountAdvanceSale = discount;
		this.nSeats = nSeats;

	}

	@Override
	public Byte getDiscountAdvanceSale() {
		return this.discountAdvanceSale;
	}

	@Override
	public int getNumberOfSoldSeats() {
		int soldSeats = 0;
		for (int i = 0; i < nSeats; i++) {
			if (seats[i] != null) {
				soldSeats += 1;
			}
		}
		return soldSeats;
	}

	@Override
	public int getNumberOfNormalSaleSeats() {
		int normalSeats = 0;
		for (int i = 0; i < nSeats; i++) {
			if (seats[i] != null && seats[i].getAdvanceSale() == false) {
				normalSeats++;
			}
		}
		return normalSeats;
	}

	@Override
	public int getNumberOfAdvanceSaleSeats() {
		int normalSeats = 0;
		for (int i = 0; i < nSeats; i++) {
			if (seats[i] != null && seats[i].getAdvanceSale() == true) {
				normalSeats++;
			}
		}
		return normalSeats;
	}

	@Override
	public int getNumberOfSeats() {
		return this.nSeats;
	}

	@Override
	public int getNumberOfAvailableSeats() {
		int availableSeats = 0;
		for (int i = 0; i < nSeats; i++) {
			if (seats[i] == null) {
				availableSeats++;
			}
		}
		return availableSeats;

	}

	@Override
	public Seat getSeat(int pos) {
		Seat asiento = null;
		if (pos > 0 && pos < nSeats) {
			asiento = seats[pos - 1];
		}
		return asiento;
	}

	@Override
	public Person refundSeat(int pos) {
		Person personOnSeat = null;
		if (pos > 0 && pos < nSeats + 1 && seats[pos - 1] != null) {
			personOnSeat = seats[pos - 1].getHolder();
			seats[pos - 1] = null;
		}
		return personOnSeat;
	}

	private boolean isChildren(int age) {
		boolean isChildren = false;
		if (age < CHILDREN_EXMAX_AGE) {
			isChildren = true;
		}
		return isChildren;
	}

	private boolean isAdult(int age) {
		boolean isChildren = false;
		if (age >= CHILDREN_EXMAX_AGE) {
			isChildren = true;
		}
		return isChildren;
	}

	@Override
	public List<Integer> getAvailableSeatsList() {
		List<Integer> lista = new ArrayList<Integer>(nSeats);
		for (int i = 0; i < nSeats; i++) {
			if (seats[i] == null) {
				lista.add(i + 1);
			}
		}
		return lista;
	}

	@Override
	public List<Integer> getAdvanceSaleSeatsList() {
		List<Integer> lista = new ArrayList<Integer>(nSeats);
		for (int i = 0; i < nSeats; i++) {
			if (seats[i] != null && seats[i].getAdvanceSale() == true) {
				lista.add(i + 1);
			}
		}

		return lista;
	}

	@Override
	public int getMaxNumberConsecutiveSeats() {
		int seatsNotTaken = 0;
		int consecutiveSeatsMax = 0;
		for (int i = 0; i < nSeats; i++) {
			if (seats[i] == null) {
				seatsNotTaken++;
			} else if (seatsNotTaken > consecutiveSeatsMax) {
				consecutiveSeatsMax = seatsNotTaken;
				seatsNotTaken = 0;
			} else {
				seatsNotTaken = 0;
			}
		}
		if (seatsNotTaken > consecutiveSeatsMax) {
			consecutiveSeatsMax = seatsNotTaken;
		}
		return consecutiveSeatsMax;
	}

	@Override
	public boolean isAdvanceSale(Person p) {
		boolean isAdvanceSale = false;
		for (int i = 0; i < nSeats; i++) {
			if (seats[i] != null && seats[i].getHolder().equals(p)) {
				isAdvanceSale = seats[i].getAdvanceSale();
			}
		}
		return isAdvanceSale;
	}

	@Override
	public Date getTravelDate() {
		return this.travelDate;
	}

	@Override
	public boolean sellSeatPos(int pos, String nif, String name, int edad, boolean isAdvanceSale) {
		boolean sellSeat = true;
		for (int i = 0; i < nSeats; i++) {
			if ((seats[i] != null && seats[i].getHolder().getNif() == nif)) {
				sellSeat = false;
			}
		}
		if (pos > 0 && pos < nSeats + 1 && seats[pos - 1] == null && sellSeat) {
			Person p = new Person(nif, name, edad);
			seats[pos - 1] = new Seat(isAdvanceSale, p);
		} else {
			sellSeat = false;
		}
		return sellSeat;
	}

	@Override
	public int getNumberOfChildren() {
		int childrenNumber = 0;
		for (int i = 0; i < nSeats; i++) {
			if (seats[i] != null && isChildren(seats[i].getHolder().getAge())) {
				childrenNumber++;
			}
		}

		return childrenNumber;
	}

	@Override
	public int getNumberOfAdults() {
		int adultsNumber = 0;
		for (int i = 0; i < nSeats; i++) {
			if (seats[i] != null && isAdult(seats[i].getHolder().getAge())) {
				adultsNumber++;
			}
		}

		return adultsNumber;
	}

	@Override
	public Double getCollectionTravel() {
		Double Collection = 0.0;
		for (int i = 0; i < nSeats; i++) {
			if (seats[i] != null) {
				Collection += getSeatPrice(seats[i]);
			}
		}
		return Collection;
	}

	@Override
	public int getPosPerson(String nif) {
		int personPos = -1;
		for (int i = 0; i < nSeats; i++) {
			if (seats[i] != null && seats[i].getHolder().getNif() == nif) {
				personPos = i + 1;
			}
		}
		return personPos;
	}

	@Override
	public int sellSeatFrontPos(String nif, String name, int edad, boolean isAdvanceSale) {
		boolean sellSeat = true;
		int seatPos = 0;
		if (this.getNumberOfAvailableSeats() == 0) {
			seatPos = -1;
		} else {
			for (int i = 0; i < nSeats; i++) {
				if ((seats[i] != null && seats[i].getHolder().getNif() == nif)) {
					sellSeat = false;
					seatPos = -1;
				}
			}
			while (sellSeat) {
				if (seats[seatPos] != null) {
					seatPos++;
				} else {
					Person p = new Person(nif, name, edad);
					seats[seatPos] = new Seat(isAdvanceSale, p);
					sellSeat = false;
					seatPos++;
				}
			}
		}
		return seatPos;
	}

	@Override
	public int sellSeatRearPos(String nif, String name, int edad, boolean isAdvanceSale) {
		boolean sellSeat = true;
		int seatPos = nSeats - 1;
		if (this.getNumberOfAvailableSeats() == 0) {
			seatPos = -1;
		} else {
			for (int i = 0; i < nSeats; i++) {
				if ((seats[i] != null && seats[i].getHolder().getNif() == nif)) {
					sellSeat = false;
					seatPos = -1;
				}
			}
			while (sellSeat) {
				if (seats[seatPos] != null) {
					seatPos--;
				} else {
					Person p = new Person(nif, name, edad);
					seats[seatPos] = new Seat(isAdvanceSale, p);
					sellSeat = false;
					seatPos++;
				}
			}
		}
		return seatPos;
	}

	@Override
	public Double getSeatPrice(Seat seat) {
		Double seatPrice = 0.0;
		if (seat.getAdvanceSale() == false) {
			seatPrice = this.price;
		} else {
			Double descuento = (100 - discountAdvanceSale) / 100.0;
			seatPrice = this.price * descuento;
		}
		return seatPrice;
	}

	@Override
	public double getPrice() {
		return this.price;
	}

}

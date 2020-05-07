package model;

import java.io.Serializable;

public class Timetable implements Serializable, Comparable<Timetable> {

	// ------------------------
	// ATRIBUTOS
	// ------------------------

	private int day;
	private int month;
	private int year;
	private int hours;
	private int minutes;
	private int seconds;

	// ------------------------
	// CONSTRUCTOR
	// ------------------------

	public Timetable(int year, int month, int day, int hours, int minutes, int seconds) {
		this.year = year;
		this.month = month;
		this.day = day;
		this.hours = hours;
		this.minutes = minutes;
		this.seconds = seconds;
	}

	// ------------------------
	// METODOS
	// ------------------------

	@Override
	public String toString() {
		String mes = "";
		String minutos = minutes + "";
		String segundos = seconds + "";
		String horas = hours + "";

		if (month == 1) {
			mes = "January";
		} else if (month == 2) {
			mes = "February";
		} else if (month == 3) {
			mes = "March";
		} else if (month == 4) {
			mes = "April";
		} else if (month == 5) {
			mes = "May";
		} else if (month == 6) {
			mes = "June";
		} else if (month == 7) {
			mes = "July";
		} else if (month == 8) {
			mes = "August";
		} else if (month == 9) {
			mes = "September";
		} else if (month == 10) {
			mes = "October";
		} else if (month == 11) {
			mes = "November";
		} else if (month == 12) {
			mes = "December";
		}

		if (minutes == 0) {
			minutos = 0 + "" + 0 + "";
		}
		if (minutes < 10) {
			minutos = 0 + "" + minutes;
		}
		if (seconds == 0) {
			segundos = 0 + "" + 0;
		}
		if (seconds < 10) {
			segundos = 0 + "" + seconds;
		}
		if (hours < 10) {
			horas = 0 + "" + horas;
		}
		if (hours == 0) {
			horas = 0 + "" + 0;
		}
		return mes + " " + day + ", " + year + " at " + horas + ":" + minutos + ":" + segundos;
	}

	// ------------------------
	// GETTER Y SETTER
	// ------------------------

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getHours() {
		return hours;
	}

	public void setHours(int hours) {
		this.hours = hours;
	}

	public int getMinutes() {
		return minutes;
	}

	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}

	public int getSeconds() {
		return seconds;
	}

	public void setSeconds(int seconds) {
		this.seconds = seconds;
	}

	// Compara una fecha, con la actual pasada por parametro (ya sea la actual que
	// tenga ya definida el sistema o la actual real).
	// Retorna -1 si la fecha pasada por parametro es menor que la actual
	// Retorna 1 si la fecha pasada por parametro es mayor que la actual
	// Retorna 0 si la fecha pasada por parametro es igual a la actual

	@Override
	public int compareTo(Timetable actualTime) {
		int outCome = 2;

//		Calendar calendar = Calendar.getInstance();
//		int actualDay = calendar.get(calendar.DATE);
//		int actualMonth = calendar.get(calendar.MONTH);
//		int actualYear = calendar.get(calendar.YEAR);
//		int actualHours = calendar.get(calendar.HOUR_OF_DAY);
//		int actualMinutes = calendar.get(calendar.MINUTE);
//		int actualSeconds = calendar.get(calendar.SECOND);

		if (this.year < actualTime.getYear()) {
			outCome = -1;
		} else if (this.year > actualTime.getYear()) {
			outCome = 1;
		} else if (this.year == actualTime.getYear()) {
			if (this.month < actualTime.getMonth()) {
				outCome = -1;
			} else if (this.month > actualTime.getMonth()) {
				outCome = 1;
			} else if (this.month == actualTime.getMonth()) {
				if (this.day < actualTime.getDay()) {
					outCome = -1;
				} else if (this.day > actualTime.getDay()) {
					outCome = 1;
				} else if (this.day == actualTime.getDay()) {
					if (this.hours < actualTime.getHours()) {
						outCome = -1;
					} else if (this.hours > actualTime.getHours()) {
						outCome = 1;
					} else if (this.hours == actualTime.getHours()) {
						if (this.minutes < actualTime.getMinutes()) {
							outCome = -1;
						} else if (this.minutes > actualTime.getMinutes()) {
							outCome = 1;
						} else if (this.minutes == actualTime.getMinutes()) {
							if (this.seconds < actualTime.getSeconds()) {
								outCome = -1;
							} else if (this.seconds > actualTime.getSeconds()) {
								outCome = 1;
							} else if (this.seconds == actualTime.getSeconds()) {
								outCome = 0;
							}
						}
					}
				}
			}
		}

		return outCome;
	}

}

package model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;

import exception.AlreadyHaveTurnException;
import exception.IncompleteInformationException;
import exception.IncorrectQuantityException;
import exception.LimitExceededException;
import exception.NotFoundException;
import exception.UserAlreadyExistsException;
import model.Turn.Typeturn;
import model.User.TypeDocument;

public class Business implements Serializable {

	// ------------------------
	// RELACIONES
	// ------------------------

	private ArrayList<Turn> turns;
	private ArrayList<Turn> turnsRecord;
	private ArrayList<User> users;
	private Timetable time;
	private Timetable actualTime;

	// ------------------------
	// CONSTRUCTOR
	// ------------------------

	public Business(Timetable time) {
		turns = new ArrayList<Turn>();
		turnsRecord = new ArrayList<Turn>();
		users = new ArrayList<User>();
		this.time = time;
	}

	// ------------------------
	// MÉTODOS
	// ------------------------

	/**
	 * Search an user by id number
	 * 
	 * @param idNumber the id number of user that we are looking for
	 * @return an object User, null if the user doesn't exist
	 * @throws NotFoundException when method can't found the user we are looking for
	 */

	public User searchUserByIdNumber(int idNumber) throws NotFoundException {
		long inicio = System.currentTimeMillis();
		long total = 0;
		boolean found = false;
		User userFound = null;
		for (int i = 0; i < users.size() && !found; i++) {
			if (users.get(i).getIdNumber() == idNumber) {
				found = true;
				userFound = users.get(i);
			}
		}
		if (found == false) {
			throw new NotFoundException(idNumber);
		}
		total = System.currentTimeMillis() - inicio;
		// System.out.println("Method duration: " + total);
		return userFound;
	}

	/**
	 * Comprobate user not exist in business
	 * 
	 * @param idUser the id number of user that we are looking for
	 * @return boolean, true if user exist and false if not
	 */

	public boolean comprobateUserNotExist(int idUser) {

		boolean found = false;
		for (int i = 0; i < users.size() && !found; i++) {
			if (users.get(i).getIdNumber() == idUser) {
				found = true;
			}
		}

		return found;
	}

	/**
	 * Check user don't have any active turn
	 * 
	 * @param id the id number of user that we are looking for
	 * @return boolean, true if user has a turn and false if not
	 */

	public boolean checkUserDontHaveturn(int id) {

		boolean found = false;
		for (int i = 0; i < turns.size(); i++) {
			if (turns.get(i).getUser().getIdNumber() == id) {
				found = true;
			}
		}

		return found;
	}

	/**
	 * Create a turn for a register user (not new user)
	 * 
	 * @param typeturn the type of the turn
	 * @param user     the user to whom the turn will be assigned
	 * @return turn, an object turn that is the new turn created
	 * @throws AlreadyHaveturnException when user already have a turn
	 */

	public Turn addturnRegisterUser(User user, Typeturn typeturn) throws AlreadyHaveTurnException {
		long inicio = System.currentTimeMillis();
		long total = 0;
		String numberLastturn = "";
		int idLastturn = 0;
		int number[] = new int[2];

		String numberNewturn = "";
		int idNewturn = 0;

		if (turns.size() == 0) {
			numberNewturn = 0 + "" + 0 + "";
			idNewturn = 'A';
		} else {
			Turn lastturn = turns.get(turns.size() - 1);
			if (checkUserDontHaveturn(user.getIdNumber()) == false
					&& checkUserNotSuspended(user.getIdNumber()) == false) {
				if (lastturn != null) {
					numberLastturn = lastturn.getNumber();
					idLastturn = lastturn.getId();

					number[0] = Integer.parseInt(numberLastturn.charAt(0) + "");
					number[1] = Integer.parseInt(numberLastturn.charAt(1) + "");

					if (number[1] == 9 && number[0] != 9) {
						number[0] = number[0] + 1;
						number[1] = 0;
					} else if (number[1] != 9) {
						number[1] = number[1] + 1;
						idNewturn = idLastturn;
					} else if (number[1] == 9 && number[0] == 9) {

						number[0] = 0;
						number[1] = 0;
						if (idLastturn == 'Z') {
							idLastturn = 'A';
						} else {
							idLastturn = idLastturn + 1;
						}
					}
					numberNewturn = (number[0] + "" + number[1] + "");
					idNewturn = idLastturn;
				}
			} else {
				throw new AlreadyHaveTurnException();
			}
		}
		Turn turnNew = new Turn(idNewturn, numberNewturn, typeturn);
		turnNew.setUser(user);
		Timetable timeCreation = actualTime();
		turnNew.setCreationTime(timeCreation);
		turns.add(turnNew);
		total = System.currentTimeMillis() - inicio;
		// System.out.println("Method duration: " + total);
		return turnNew;

	}

	public boolean checkUserNotSuspended(int id) {
		boolean suspended = false;
		for (int i = 0; i < users.size(); i++) {
			if (users.get(i).isSuspended() == true) {
				suspended = true;
			}
		}
		return suspended;
	}

	/**
	 * Create a turn for a new user (not register user)
	 * 
	 * @param idNumber     id number of the new user
	 * @param name         name of the new user
	 * @param lastName     last name of the new user
	 * @param phoneNumber  phone number of the new user
	 * @param address      address of the new user
	 * @param typeDocument type document of the new user
	 * @param typeturn     turn type
	 * @return turn, an object turn that is the new turn created
	 * @throws IncompleteInformationException when information is incomplete
	 * @throws NotFoundException              when method can't found user
	 * @throws UserAlreadyExistsException     when user already exist in system
	 */

	public Turn addturnNewUser(int idNumber, String name, String lastName, int phoneNumber, String address,
			TypeDocument typeDocument, Typeturn typeturn)
			throws IncompleteInformationException, NotFoundException, UserAlreadyExistsException {
		long inicio = System.currentTimeMillis();
		long total = 0;
		String numberLastturn;
		int idLastturn;
		int number[] = new int[2];

		String numberNewturn = "";
		int idNewturn = 0;

		if (turns.size() == 0) {
			numberNewturn = 0 + "" + 0 + "";
			idNewturn = 'A';
		} else {
			Turn lastturn = turns.get(turns.size() - 1);
			if (lastturn != null) {
				numberLastturn = lastturn.getNumber();
				idLastturn = lastturn.getId();

				number[0] = Integer.parseInt(numberLastturn.charAt(0) + "");
				number[1] = Integer.parseInt(numberLastturn.charAt(1) + "");

				if (number[1] == 9 && number[0] != 9) {
					number[0] = number[0] + 1;
					number[1] = 0;
				} else if (number[1] != 9) {
					number[1] = number[1] + 1;
					idNewturn = idLastturn;
				} else if (number[1] == 9 && number[0] == 9) {
					number[0] = 0;
					number[1] = 0;
					if (idLastturn == 'Z') {
						idLastturn = 'A';
					} else {
						idLastturn = idLastturn + 1;
					}
				}
				idNewturn = idLastturn;
				numberNewturn = (number[0] + "" + number[1] + "");
			}
		}

		Turn turnNew = new Turn(idNewturn, numberNewturn, typeturn);
		turnNew.setUser(new User(idNumber, name, lastName, phoneNumber, address, typeDocument));
		Timetable timeCreation = actualTime();
		turnNew.setCreationTime(timeCreation);
		addUser(idNumber, name, lastName, phoneNumber, address, typeDocument);
		turns.add(turnNew);
		total = System.currentTimeMillis() - inicio;
		// System.out.println("Method duration: " + total);
		return turnNew;
	}

	/**
	 * Add a new user to business
	 * 
	 * @param idNumber     id number of the new user
	 * @param name         name of the new user
	 * @param lastName     last name of the new user
	 * @param phoneNumber  phone number of the new user
	 * @param address      address of the new user
	 * @param typeDocument type document of the new user
	 * @throws IncompleteInformationException when information is incomplete
	 * @throws UserAlreadyExistsException     when user already exists
	 */

	public void addUser(int idNumber, String name, String lastName, int phoneNumber, String address,
			TypeDocument typeDocument) throws IncompleteInformationException, UserAlreadyExistsException {
		if (comprobateUserNotExist(idNumber) == true) {
			throw new UserAlreadyExistsException();
		}
		if (idNumber + "" != null && idNumber + "" != "") {
			if (typeDocument != null) {
				if (name != null && name != "") {
					if (lastName != null && lastName != "") {
						User newUser = new User(idNumber, name, lastName, phoneNumber, address, typeDocument);
						users.add(newUser);

					}
				}
			}
		} else {
			throw new IncompleteInformationException();
		}

	}

	/**
	 * Make turn record
	 * 
	 */

	public void maketurnRecord() {
		long inicio = System.currentTimeMillis();
		long total = 0;

		for (int i = 0; i < turns.size(); i++) {
			turnsRecord.add(turns.get(i));
		}
		total = System.currentTimeMillis() - inicio;
		// System.out.println("Method duration: " + total);
	}

	/**
	 * Show all turns of business
	 * 
	 * @return String, a message with the information of all actual turns in the
	 *         business
	 */

	public String showAllturns() {
		long inicio = System.currentTimeMillis();
		long total = 0;
		String msg = "                 ACTUAL turnS                  \n";
		String attended = "";
		String typeturn = "";
		for (int i = 0; i < turns.size(); i++) {
			if (turns.get(i).isAttended() == true && turns.get(i).isCalled() == true) {
				attended = " turn was called and user was attended";
			} else if (turns.get(i).isAttended() == false && turns.get(i).isCalled() == true) {
				attended = " turn was called and user wasn't attended because he was no longer in the place when he was called to be attended";
			} else {
				attended = "turn has not been called yet therefore it has not been attended";
			}

			if (turns.get(i).getTypeturn().equals(Typeturn.breakfastTurn)) {
				typeturn = "breakfast turn";
			} else if (turns.get(i).getTypeturn().equals(Typeturn.lunchTurn)) {
				typeturn = "lunch turn";
			} else if (turns.get(i).getTypeturn().equals(Typeturn.refreshmentTurn)) {
				typeturn = "refreshment turn";
			}
			char id = (char) (turns.get(i).getId());
			msg += "                 " + id + turns.get(i).getNumber() + "              " + "\n" + "TYPE turn: "
					+ typeturn + "\n" + "DURATION: " + turns.get(i).getDuration() + "\n" + "CREATED IN: "
					+ turns.get(i).getCreationTime().toString() + "\n" + "BELONGS TO: "
					+ turns.get(i).getUser().getName() + " " + turns.get(i).getUser().getLastName() + " with id number "
					+ turns.get(i).getUser().getIdNumber() + "\n" + "ATTENDED STATUS: " + attended + "\n\n";
		}
		total = System.currentTimeMillis() - inicio;
		// System.out.println("Method duration: " + total);
		return msg;
	}

	/**
	 * Show all record turns of business
	 * 
	 * @return String, a message with the information of all actual turns in the
	 *         business
	 */

	public String showAllRecordturns() {
		long inicio = System.currentTimeMillis();
		long total = 0;
		String msg = "                 RECORD turnS                  \n";
		String attended = "";
		String typeturn = "";
		for (int i = 0; i < turnsRecord.size(); i++) {
			if (turnsRecord.get(i).isAttended() == true && turnsRecord.get(i).isCalled() == true) {
				attended = " turn was called and user was attended";
			} else if (turnsRecord.get(i).isAttended() == false && turnsRecord.get(i).isCalled() == true) {
				attended = " turn was called and user wasn't attended because he was no longer in the place when he was called to be attended";
			} else {
				attended = "turn has not been called yet therefore it has not been attended";
			}

			if (turnsRecord.get(i).getTypeturn().equals(Typeturn.breakfastTurn)) {
				typeturn = "breakfast turn";
			} else if (turnsRecord.get(i).getTypeturn().equals(Typeturn.lunchTurn)) {
				typeturn = "lunch turn";
			} else if (turnsRecord.get(i).getTypeturn().equals(Typeturn.refreshmentTurn)) {
				typeturn = "refreshment turn";
			}
			char id = (char) (turnsRecord.get(i).getId());
			msg += "                 " + id + turnsRecord.get(i).getNumber() + "              " + "\n" + "TYPE turn: "
					+ typeturn + "\n" + "DURATION: " + turnsRecord.get(i).getDuration() + "\n" + "CREATED IN: "
					+ turnsRecord.get(i).getCreationTime().toString() + "\n" + "BELONGS TO: "
					+ turnsRecord.get(i).getUser().getName() + " " + turnsRecord.get(i).getUser().getLastName()
					+ " with id number " + turnsRecord.get(i).getUser().getIdNumber() + "\n" + "ATTENDED STATUS: "
					+ attended + "\n\n";
		}
		total = System.currentTimeMillis() - inicio;
		// System.out.println("Method duration: " + total);
		return msg;
	}

	/**
	 * Show all turns of business organized by duration
	 * 
	 * @return String, a message with the information of all actual turns in the
	 *         business organized by duration
	 */

	public String showAllturnsOrganizedDuration() {
		long inicio = System.currentTimeMillis();
		long total = 0;
		String msg = "                 ACTUAL turnS                  \n";
		String attended = "";
		String typeturn = "";
		Turn turn[] = new Turn[turns.size()];

		for (int j = 0; j < turn.length; j++) {
			turn[j] = turns.get(j);
		}
		Arrays.sort(turn);

		for (int i = 0; i < turn.length; i++) {
			if (turn[i].isAttended() == true && turn[i].isCalled() == true) {
				attended = " turn was called and user was attended";
			} else if (turn[i].isAttended() == false && turn[i].isCalled() == true) {
				attended = " turn was called and user wasn't attended because he was no longer in the place when he was called to be attended";
			} else {
				attended = "turn has not been called yet therefore it has not been attended";
			}

			if (turn[i].getTypeturn().equals(Typeturn.breakfastTurn)) {
				typeturn = "breakfast turn";
			} else if (turn[i].getTypeturn().equals(Typeturn.lunchTurn)) {
				typeturn = "lunch turn";
			} else if (turn[i].getTypeturn().equals(Typeturn.refreshmentTurn)) {
				typeturn = "refreshment turn";
			}
			char id = (char) (turn[i].getId());
			msg += "                 " + id + turn[i].getNumber() + "              " + "\n" + "TYPE turn: " + typeturn
					+ "\n" + "DURATION: " + turn[i].getDuration() + "\n" + "CREATED IN: "
					+ turn[i].getCreationTime().toString() + "\n" + "BELONGS TO: " + turn[i].getUser().getName() + " "
					+ turn[i].getUser().getLastName() + " with id number " + turn[i].getUser().getIdNumber() + "\n"
					+ "ATTENDED STATUS: " + attended + "\n\n";
		}
		total = System.currentTimeMillis() - inicio;
		// System.out.println("Method duration: " + total);
		return msg;
	}

	/**
	 * Show all turns of business organized by duration
	 * 
	 * @return String, a message with the information of all actual turns in the
	 *         business organized by duration
	 */

	public String showturnsRecordOrganizedDuration() {
		long inicio = System.currentTimeMillis();
		long total = 0;
		String msg = "                 RECORD turnS                  \n";
		String attended = "";
		String typeturn = "";
		Turn turn[] = new Turn[turnsRecord.size()];

		for (int j = 0; j < turn.length; j++) {
			turn[j] = turnsRecord.get(j);
		}
		Arrays.sort(turn);

		for (int i = 0; i < turn.length; i++) {
			if (turn[i].isAttended() == true && turn[i].isCalled() == true) {
				attended = " turn was called and user was attended";
			} else if (turn[i].isAttended() == false && turn[i].isCalled() == true) {
				attended = " turn was called and user wasn't attended because he was no longer in the place when he was called to be attended";
			} else {
				attended = "turn has not been called yet therefore it has not been attended";
			}

			if (turn[i].getTypeturn().equals(Typeturn.breakfastTurn)) {
				typeturn = "breakfast turn";
			} else if (turn[i].getTypeturn().equals(Typeturn.lunchTurn)) {
				typeturn = "lunch turn";
			} else if (turn[i].getTypeturn().equals(Typeturn.refreshmentTurn)) {
				typeturn = "refreshment turn";
			}
			char id = (char) (turn[i].getId());
			msg += "                 " + id + turn[i].getNumber() + "              " + "\n" + "TYPE turn: " + typeturn
					+ "\n" + "DURATION: " + turn[i].getDuration() + "\n" + "CREATED IN: "
					+ turn[i].getCreationTime().toString() + "\n" + "BELONGS TO: " + turn[i].getUser().getName() + " "
					+ turn[i].getUser().getLastName() + " with id number " + turn[i].getUser().getIdNumber() + "\n"
					+ "ATTENDED STATUS: " + attended + "\n\n";
		}
		total = System.currentTimeMillis() - inicio;
		// System.out.println("Method duration: " + total);
		return msg;
	}

	/**
	 * Show actual turns of business organized by creation date
	 * 
	 * @return String, a message with the information of all actual turns in the
	 *         business organized by creation date
	 */

	public String showAllturnsOrganizedCreationDate() {
		long inicio = System.currentTimeMillis();
		long total = 0;
		String msg = "                 ACTUAL turnS                  \n";
		String attended = "";
		String typeturn = "";
		Turn turn[] = new Turn[turns.size()];

		for (int j = 0; j < turn.length; j++) {
			turn[j] = turns.get(j);
		}
		TurnComparator turnC = new TurnComparator();
		Arrays.sort(turn, turnC);

		for (int i = 0; i < turn.length; i++) {
			if (turn[i].isAttended() == true && turn[i].isCalled() == true) {
				attended = " turn was called and user was attended";
			} else if (turn[i].isAttended() == false && turn[i].isCalled() == true) {
				attended = " turn was called and user wasn't attended because he was no longer in the place when he was called to be attended";
			} else {
				attended = "turn has not been called yet therefore it has not been attended";
			}

			if (turn[i].getTypeturn().equals(Typeturn.breakfastTurn)) {
				typeturn = "breakfast turn";
			} else if (turn[i].getTypeturn().equals(Typeturn.lunchTurn)) {
				typeturn = "lunch turn";
			} else if (turn[i].getTypeturn().equals(Typeturn.refreshmentTurn)) {
				typeturn = "refreshment turn";
			}
			char id = (char) (turn[i].getId());
			msg += "                 " + id + turn[i].getNumber() + "              " + "\n" + "TYPE turn: " + typeturn
					+ "\n" + "DURATION: " + turn[i].getDuration() + "\n" + "CREATED IN: "
					+ turn[i].getCreationTime().toString() + "\n" + "BELONGS TO: " + turn[i].getUser().getName() + " "
					+ turn[i].getUser().getLastName() + " with id number " + turn[i].getUser().getIdNumber() + "\n"
					+ "ATTENDED STATUS: " + attended + "\n\n";
		}
		total = System.currentTimeMillis() - inicio;
		// System.out.println("Method duration: " + total);
		return msg;
	}

	/**
	 * Sort actual turns of business organized by creation date
	 * 
	 * @return String, a message with the information of all actual turns in the
	 *         business organized by creation date
	 */

	public String showAllturnsOrganizedCreationDateInverse() {
		long inicio = System.currentTimeMillis();
		long total = 0;
		String msg = "                 ACTUAL turnS                  \n";
		String attended = "";
		String typeturn = "";
		Turn turn[] = new Turn[turns.size()];

		for (int j = 0; j < turn.length; j++) {
			turn[j] = turns.get(j);
		}
		TurnComparator turnC = new TurnComparator();
		turnC = (TurnComparator) Collections.reverseOrder(turnC);
		Arrays.sort(turn, turnC);

		for (int i = 0; i < turn.length; i++) {
			if (turn[i].isAttended() == true && turn[i].isCalled() == true) {
				attended = " turn was called and user was attended";
			} else if (turn[i].isAttended() == false && turn[i].isCalled() == true) {
				attended = " turn was called and user wasn't attended because he was no longer in the place when he was called to be attended";
			} else {
				attended = "turn has not been called yet therefore it has not been attended";
			}

			if (turn[i].getTypeturn().equals(Typeturn.breakfastTurn)) {
				typeturn = "breakfast turn";
			} else if (turn[i].getTypeturn().equals(Typeturn.lunchTurn)) {
				typeturn = "lunch turn";
			} else if (turn[i].getTypeturn().equals(Typeturn.refreshmentTurn)) {
				typeturn = "refreshment turn";
			}
			char id = (char) (turn[i].getId());
			msg += "                 " + id + turn[i].getNumber() + "              " + "\n" + "TYPE turn: " + typeturn
					+ "\n" + "DURATION: " + turn[i].getDuration() + "\n" + "CREATED IN: "
					+ turn[i].getCreationTime().toString() + "\n" + "BELONGS TO: " + turn[i].getUser().getName() + " "
					+ turn[i].getUser().getLastName() + " with id number " + turn[i].getUser().getIdNumber() + "\n"
					+ "ATTENDED STATUS: " + attended + "\n\n";
		}
		total = System.currentTimeMillis() - inicio;
		// System.out.println("Method duration: " + total);
		return msg;
	}

	/**
	 * Show actual turns of business organized by creation date
	 * 
	 * @return String, a message with the information of all actual turns in the
	 *         business organized by creation date
	 */

	public String showturnsRecordOrganizedCreationDate() {
		long inicio = System.currentTimeMillis();
		long total = 0;
		String msg = "                 RECORD turnS                  \n";
		String attended = "";
		String typeturn = "";
		Turn turn[] = new Turn[turnsRecord.size()];

		for (int j = 0; j < turn.length; j++) {
			turn[j] = turnsRecord.get(j);
		}
		TurnComparator turnC = new TurnComparator();
		Arrays.sort(turn, turnC);

		for (int i = 0; i < turn.length; i++) {
			if (turn[i].isAttended() == true && turn[i].isCalled() == true) {
				attended = " turn was called and user was attended";
			} else if (turn[i].isAttended() == false && turn[i].isCalled() == true) {
				attended = " turn was called and user wasn't attended because he was no longer in the place when he was called to be attended";
			} else {
				attended = "turn has not been called yet therefore it has not been attended";
			}

			if (turn[i].getTypeturn().equals(Typeturn.breakfastTurn)) {
				typeturn = "breakfast turn";
			} else if (turn[i].getTypeturn().equals(Typeturn.lunchTurn)) {
				typeturn = "lunch turn";
			} else if (turn[i].getTypeturn().equals(Typeturn.refreshmentTurn)) {
				typeturn = "refreshment turn";
			}
			char id = (char) (turn[i].getId());
			msg += "                 " + id + turn[i].getNumber() + "              " + "\n" + "TYPE turn: " + typeturn
					+ "\n" + "DURATION: " + turn[i].getDuration() + "\n" + "CREATED IN: "
					+ turn[i].getCreationTime().toString() + "\n" + "BELONGS TO: " + turn[i].getUser().getName() + " "
					+ turn[i].getUser().getLastName() + " with id number " + turn[i].getUser().getIdNumber() + "\n"
					+ "ATTENDED STATUS: " + attended + "\n\n";
		}
		total = System.currentTimeMillis() - inicio;
		// System.out.println("Method duration: " + total);
		return msg;
	}

	/**
	 * Restart actual turns in business
	 * 
	 */

	public void restartturn() {
		long inicio = System.currentTimeMillis();
		long total = 0;
		turns.clear();
		total = System.currentTimeMillis() - inicio;
		// System.out.println("Method duration: " + total);
	}

	/**
	 * Modify the attribute attended of turns
	 * 
	 * @param attended, that is the new attributed that we want to change
	 * @param turn,     that is the turn in which we want to change the attribute
	 */

	public void setAttendedturn(boolean attended, Turn turn) {
		for (int i = 0; i < turns.size(); i++) {
			if (turns.get(i).getId() == turn.getId() && turns.get(i).getNumber().equals(turn.getNumber())) {
				turns.get(i).setAttended(attended);
			}
		}

	}

	/**
	 * Make a turn report of all people who have ever had an specific turn
	 * 
	 * @param id,     is the id of the turn we are looking for
	 * @param number, is the number of the turn we are looking for
	 * @return String, returns the report of all people who have ever had an
	 *         specific turn
	 */

	public String maketurnReport(int id, String number) {
		long inicio = System.currentTimeMillis();
		long total = 0;
		Turn turnTemp = null;
		String msg = "All the people who have ever had the turn have been \n";
		for (int i = 0; i < turnsRecord.size(); i++) {

			if (turnsRecord.get(i).getId() == id && turnsRecord.get(i).getNumber().equals(number)) {
				turnTemp = turnsRecord.get(i);
				msg += turnTemp.getUser().toString() + "\n";
			}
		}
		total = System.currentTimeMillis() - inicio;
		// System.out.println("Method duration: " + total);
		return msg;
	}

	/**
	 * Make a turn report of all turns who have ever had an specific person
	 * 
	 * @param idNumber, is the id of the person we are looking for
	 * @return String, returns the report of all turns who have ever had an specific
	 *         person
	 */

	public String makeUserReport(int idNumber) {
		long inicio = System.currentTimeMillis();
		long total = 0;
		String msg = "All the turns who have ever had the person \n";
		String attended = "";
		String typeturn = "";
		for (int i = 0; i < turnsRecord.size(); i++) {
			if (turnsRecord.get(i).getUser().getIdNumber() == idNumber) {
				if (turnsRecord.get(i).isAttended() == true && turnsRecord.get(i).isCalled() == true) {
					attended = " turn was called and user was attended";
				} else if (turnsRecord.get(i).isAttended() == false && turnsRecord.get(i).isCalled() == true) {
					attended = " turn was called and user wasn't attended because he was no longer in the place when he was called to be attended";
				} else {
					attended = "turn has not been called yet therefore it has not been attended";
				}

				if (turnsRecord.get(i).getTypeturn().equals(Typeturn.breakfastTurn)) {
					typeturn = "breakfast turn";
				} else if (turnsRecord.get(i).getTypeturn().equals(Typeturn.lunchTurn)) {
					typeturn = "lunch turn";
				} else if (turnsRecord.get(i).getTypeturn().equals(Typeturn.refreshmentTurn)) {
					typeturn = "refreshment turn";
				}
				char id = (char) (turnsRecord.get(i).getId());
				msg += "                 " + id + turnsRecord.get(i).getNumber() + "              " + "\n"
						+ "TYPE turn: " + typeturn + "\n" + "DURATION: " + turnsRecord.get(i).getDuration() + "\n"
						+ "CREATED IN: " + turnsRecord.get(i).getCreationTime().toString() + "\n" + "BELONGS TO: "
						+ turnsRecord.get(i).getUser().getName() + " " + turnsRecord.get(i).getUser().getLastName()
						+ " with id number " + turnsRecord.get(i).getUser().getIdNumber() + "\n" + "ATTENDED STATUS: "
						+ attended + "\n\n";
			}
		}
		total = System.currentTimeMillis() - inicio;
		// System.out.println("Method duration: " + total);
		return msg;
	}

	/**
	 * Binary search of users by id number
	 * 
	 * @param id,      is the id of the turn we are looking for
	 * @param arreglo, is the array of users
	 * @return User, returns the user found or null if user don't exists
	 */

	public User binarySearchUser(int id, User[] arreglo) {
		long inicioTiempo = System.currentTimeMillis();
		long total = 0;
		User encontrado = null;
		boolean encontre = false;
		int inicio = 0;
		int fin = arreglo.length - 1;

		while (inicio <= fin && !encontre) {
			int medio = (inicio + fin) / 2;

			if (arreglo[medio].getIdNumber() == id) {
				encontrado = arreglo[medio];
				encontre = true;
			} else if (arreglo[medio].getIdNumber() > id) {
				fin = medio - 1;
			} else {
				inicio = medio + 1;
			}
		}
		total = System.currentTimeMillis() - inicioTiempo;
//		System.out.println("Method duration: " + total);

		return encontrado;
	}

	public User[] usersToArray() {
		User user[] = new User[users.size()];

		for (int j = 0; j < user.length; j++) {
			user[j] = users.get(j);
		}

		return user;
	}

	/**
	 * Make advance turns given duration and 15 seconds that takes time to attend
	 * one turn to another
	 * 
	 */

	public void advanceturns() {
		long inicio = System.currentTimeMillis();
		long total = 0;
		int attended = 0;
		advanceTime();
		Turn turnTemp;
		Timetable timeTemp;
		int turnDay;
		int turnMonth;
		int turnYear;
		int turnHour;
		int turnMinute;
		int turnSecond;

		int systemDay = this.time.getDay();
		int systemMonth = this.time.getMonth();
		int systemYear = this.time.getYear();
		int systemHour = this.time.getHours();
		int systemMinute = this.time.getMinutes();
		int systemSecond = this.time.getSeconds();

		for (int i = 0; i < turns.size(); i++) {
			attended = (int) (Math.random() * 2) + 1;
			if (turns.get(i).isCalled() == false) {
				turnTemp = turns.get(i);
				turnDay = turnTemp.getCreationTime().getDay();
				turnMonth = turnTemp.getCreationTime().getMonth();
				turnYear = turnTemp.getCreationTime().getYear();
				turnHour = turnTemp.getCreationTime().getHours();
				turnMinute = (int) (turnTemp.getCreationTime().getMinutes() + turnTemp.getDuration());
				turnSecond = (int) (turnTemp.getCreationTime().getSeconds() + turnTemp.ATTENTION_TIME);

				if (turnMinute > 60) {
					turnMinute = turnMinute - 60;
					turnHour = turnHour + 1;
				}
				if (turnHour >= 24 && turnMinute > 60) {
					turnDay = turnDay + 1;
					turnHour = turnHour - 24;
					turnMinute = turnMinute - 60;
				}
				if (turnSecond > 60) {
					turnSecond = turnSecond - 60;
					turnMinute = turnMinute + 1;
				}
				if (turnDay > 30 && turnHour > 24) {
					turnMonth = turnMonth + 1;
					turnDay = turnDay - 30;
					turnHour = turnHour - 24;
				}
				if (turnMonth > 12 && turnDay > 30) {
					turnMonth = turnMonth - 12;
					turnYear = turnYear + 1;
					turnDay = turnDay - 30;
				}

				timeTemp = new Timetable(turnYear, turnMonth, turnDay, turnHour, turnMinute, turnSecond);
				// Si el timeTemp es menor que el tiempo del sistema, quiere decir que ya fue
				// atendido
				if (timeTemp.compareTo(this.time) == -1) {
					turns.get(i).setCalled(true);
					if (attended == 1) {
						turns.get(i).setAttended(true);
					} else if (attended == 2) {
						turns.get(i).setAttended(false);
					}
				}
			}
		}
		total = System.currentTimeMillis() - inicio;
		// System.out.println("Method duration: " + total);
	}

	/**
	 * Make advance advance system time
	 * 
	 */

	public void advanceTime() {

		Timetable systemTime = this.time;
		Timetable actualTime = null;
		Calendar calendar = Calendar.getInstance();

		int actualDay = calendar.get(calendar.DATE);
		int actualMonth = calendar.get(calendar.MONTH) + 1;
		int actualYear = calendar.get(calendar.YEAR);
		int actualHours = calendar.get(calendar.HOUR_OF_DAY);
		int actualMinutes = calendar.get(calendar.MINUTE);
		int actualSeconds = calendar.get(calendar.SECOND);
		actualTime = new Timetable(actualYear, actualMonth, actualDay, actualHours, actualMinutes, actualSeconds);

		int differenceDay = 0;
		int differenceMonth = 0;
		int differenceYear = 0;
		int differenceHours = 0;
		int differenceMinutes = 0;
		int differenceSeconds = 0;

		int advancedDay = 0;
		int advancedMonth = 0;
		int advancedYear = 0;
		int advancedHour = 0;
		int advancedMinute = 0;
		int advancedSeconds = 0;

		// time es menor que actualTime
		if (systemTime.compareTo(actualTime) == -1) {
			differenceDay = actualDay - systemTime.getDay();
			differenceMonth = actualMonth - systemTime.getMonth();
			differenceYear = actualYear - systemTime.getYear();
			differenceHours = actualHours - systemTime.getHours();
			differenceMinutes = actualMinutes - systemTime.getMinutes();
			differenceSeconds = actualSeconds - systemTime.getSeconds();

			advancedDay = systemTime.getDay() + differenceDay;
			advancedMonth = systemTime.getMonth() + differenceMonth;
			advancedYear = systemTime.getYear() + differenceYear;
			advancedHour = systemTime.getHours() + differenceHours;
			advancedMinute = systemTime.getMinutes() + differenceMinutes;
			advancedSeconds = systemTime.getSeconds() + differenceSeconds;

			// time es mayor que actualTime
		} else if (systemTime.compareTo(actualTime) == 1 && this.actualTime != null) {

			differenceDay = actualDay - this.actualTime.getDay();
			differenceMonth = actualMonth - this.actualTime.getMonth();
			differenceYear = actualYear - this.actualTime.getYear();
			differenceHours = (actualHours - this.actualTime.getHours());
			differenceMinutes = (actualMinutes - this.actualTime.getMinutes())
					- (systemTime.getMinutes() - this.actualTime.getMinutes());
			differenceSeconds = (actualSeconds - this.actualTime.getSeconds())
					- (systemTime.getSeconds() - this.actualTime.getSeconds());

			advancedDay = systemTime.getDay() + differenceDay;
			advancedMonth = systemTime.getMonth() + differenceMonth;
			advancedYear = systemTime.getYear() + differenceYear;
			advancedHour = systemTime.getHours() + differenceHours;
			advancedMinute = systemTime.getMinutes() + differenceMinutes;
			advancedSeconds = systemTime.getSeconds() + differenceSeconds;
		}
		if (advancedSeconds < 0) {
			advancedSeconds = (-1) * (advancedSeconds);
		}
		if (advancedMinute < 0) {
			advancedMinute = (-1) * (advancedSeconds);
		}

		if (advancedMinute > 60) {
			advancedMinute = advancedMinute - 60;
			advancedHour = advancedHour + 1;
		}
		if (advancedHour >= 24 && advancedMinute > 60) {
			advancedDay = advancedDay + 1;
			advancedHour = advancedHour - 24;
			advancedMinute = advancedMinute - 60;
		}
		if (advancedSeconds > 60) {
			advancedSeconds = advancedSeconds - 60;
			advancedMinute = advancedMinute + 1;
		}
		if (advancedDay > 30 && advancedHour > 24) {
			advancedMonth = advancedMonth + 1;
			advancedDay = advancedDay - 30;
			advancedHour = advancedHour - 24;

		}
		if (advancedMonth > 12 && advancedDay > 30) {
			advancedMonth = advancedMonth - 12;
			advancedYear = advancedYear + 1;
			advancedDay = advancedDay - 30;
		}
		time.setDay(advancedDay);
		time.setMonth(advancedMonth);
		time.setYear(advancedYear);
		time.setHours(advancedHour);
		time.setMinutes(advancedMinute);
		time.setSeconds(advancedSeconds);
	}

	/**
	 * Give the actual time of system
	 * 
	 * @return Timetable, the actual time of system
	 */

	public Timetable actualTime() {

		Timetable systemTime = this.time;
		Timetable actualTime = null;
		Calendar calendar = Calendar.getInstance();

		int actualDay = calendar.get(calendar.DATE);
		int actualMonth = calendar.get(calendar.MONTH);
		int actualYear = calendar.get(calendar.YEAR);
		int actualHours = calendar.get(calendar.HOUR_OF_DAY);
		int actualMinutes = calendar.get(calendar.MINUTE);
		int actualSeconds = calendar.get(calendar.SECOND);
		actualTime = new Timetable(actualYear, actualMonth, actualDay, actualHours, actualMinutes, actualSeconds);

		int differenceDay = 0;
		int differenceMonth = 0;
		int differenceYear = 0;
		int differenceHours = 0;
		int differenceMinutes = 0;
		int differenceSeconds = 0;

		int advancedDay;
		int advancedMonth;
		int advancedYear;
		int advancedHour;
		int advancedMinute;
		int advancedSeconds;

		// time es menor que actualTime
		if (systemTime.compareTo(actualTime) == -1) {
			differenceDay = actualTime.getDay() - systemTime.getDay();
			differenceMonth = actualTime.getMonth() - systemTime.getMonth();
			differenceYear = actualTime.getYear() - systemTime.getYear();
			differenceHours = actualTime.getHours() - systemTime.getHours();
			differenceMinutes = actualTime.getMinutes() - systemTime.getMinutes();
			differenceSeconds = actualTime.getSeconds() - systemTime.getSeconds();
			// time es mayor que actualTime
		} else if (systemTime.compareTo(actualTime) == 1) {
			differenceDay = systemTime.getDay() - actualTime.getDay();
			differenceMonth = systemTime.getMonth() - actualTime.getMonth();
			differenceYear = systemTime.getYear() - actualTime.getYear();
			differenceHours = systemTime.getHours() - actualTime.getHours();
			differenceMinutes = systemTime.getMinutes() - actualTime.getMinutes();
			differenceSeconds = systemTime.getSeconds() - actualTime.getSeconds();
		}

		advancedDay = systemTime.getDay() + differenceDay;
		advancedMonth = systemTime.getMonth() + differenceMonth;
		advancedYear = systemTime.getYear() + differenceYear;
		advancedHour = systemTime.getHours() + differenceHours;
		advancedMinute = systemTime.getMinutes() + differenceMinutes;
		advancedSeconds = systemTime.getSeconds() + differenceSeconds;

		if (advancedDay > 60) {
			advancedMinute = advancedMinute - 60;
			advancedHour = advancedHour + 1;
		}
		if (advancedHour >= 24 && advancedMinute > 60) {
			advancedDay = advancedDay + 1;
			advancedHour = advancedHour - 24;
			advancedMinute = advancedMinute - 60;
		}
		if (advancedSeconds > 60) {
			advancedSeconds = advancedSeconds - 60;
			advancedMinute = advancedMinute + 1;
		}
		if (advancedDay > 30 && advancedHour > 24) {
			advancedMonth = advancedMonth + 1;
			advancedDay = advancedDay - 30;
			advancedHour = advancedHour - 24;

		}
		if (advancedMonth > 12 && advancedDay > 30) {
			advancedMonth = advancedMonth - 12;
			advancedYear = advancedYear + 1;
			advancedDay = advancedDay - 30;
		}
		Timetable currentTime = new Timetable(advancedYear, advancedMonth, advancedDay, advancedHour, advancedMinute,
				advancedSeconds);
		return currentTime;
	}

	/**
	 * Bubble sort of users by id number
	 * 
	 * @return String, a message with users information
	 */

	public String bubbleSortUsers() {
		long inicio = System.currentTimeMillis();
		long total = 0;
		String msg = "";
		User[] user = new User[users.size()];

		for (int m = 0; m < user.length; m++) {
			user[m] = users.get(m);
		}

		for (int i = user.length; i > 0; i--) {
			for (int j = 0; j < i - 1; j++) {
				if (user[j].compareTo(user[j + 1]) == 1) {
					User temp = user[j];
					user[j] = user[j + 1];
					user[j + 1] = temp;
				}
			}
		}
		msg = showUsers(user);
		total = System.currentTimeMillis() - inicio;
		// System.out.println("Method duration: " + total);
		return msg;
	}

	public String showUser(User user) {
		String msg = "        USERS INFORMATION SORT         \n";
		String typeDocument = "";
		if (user.getTypeDocument() == TypeDocument.civilRegistration) {
			typeDocument = " civil registration";
		} else if (user.getTypeDocument() == TypeDocument.foreignCard) {
			typeDocument = " foreign card";
		} else if (user.getTypeDocument() == TypeDocument.identificationCard) {
			typeDocument = " identification card";
		} else if (user.getTypeDocument() == TypeDocument.identityCard) {
			typeDocument = " identity card";
		} else if (user.getTypeDocument() == TypeDocument.passport) {
			typeDocument = " passport";
		}

		msg += "NAME: " + user.getName() + " " + user.getLastName() + "\n" + "ID NUMBER AND DOCUMENT TYPE: "
				+ user.getIdNumber() + typeDocument + "\n" + "ADDRESS: " + user.getAddress() + "\n" + "PHONE NUMBER: "
				+ user.getPhoneNumber() + "\n" + "\n";

		return msg;
	}

	/**
	 * Selection sort of users by name
	 * 
	 * @return String, a message with users information
	 */

	public String selectionSortUsers() {
		long inicio = System.currentTimeMillis();
		long total = 0;
		String msg = "";
		User[] user = new User[users.size()];

		for (int m = 0; m < user.length; m++) {
			user[m] = users.get(m);
		}

		for (int i = 0; i < user.length - 1; i++) {
			User menor = user[i];
			int cual = i;
			for (int j = i + 1; j < user.length; j++) {
				if (user[j].getName().compareTo(menor.getName()) < 0) {
					menor = user[j];
					cual = j;
				}
			}
			User temp = user[i];
			user[i] = menor;
			user[cual] = temp;
		}
		total = System.currentTimeMillis() - inicio;
		// System.out.println("Method duration: " + total);
		msg = showUsers(user);

		return msg;
	}

	/**
	 * Insertion sort of users by last name
	 * 
	 * @return String, a message with users information
	 */

	public String insertionSortUsers() {
		long inicio = System.currentTimeMillis();
		long total = 0;
		String msg = "";
		User[] user = new User[users.size()];

		for (int m = 0; m < user.length; m++) {
			user[m] = users.get(m);
		}

		for (int i = 1; i < user.length; i++) {
			for (int j = i; j > 0 && user[j - 1].getLastName().compareTo(user[j].getLastName()) > 0; j--) {
				User temp = user[j];
				user[j] = user[j - 1];
				user[j - 1] = temp;
			}
		}
		msg = showUsers(user);
		total = System.currentTimeMillis() - inicio;
		// System.out.println("Method duration: " + total);
		return msg;
	}

	/**
	 * Show users information
	 * 
	 * @param users, that is the arrays of users
	 * @return String, a message with users information
	 */

	public String showUsers(User[] users) {
		String msg = "        USERS INFORMATION SORT         \n";
		String typeDocument = "";
		for (int i = 0; i < users.length; i++) {
			if (users[i].getTypeDocument() == TypeDocument.civilRegistration) {
				typeDocument = " civil registration";
			} else if (users[i].getTypeDocument() == TypeDocument.foreignCard) {
				typeDocument = " foreign card";
			} else if (users[i].getTypeDocument() == TypeDocument.identificationCard) {
				typeDocument = " identification card";
			} else if (users[i].getTypeDocument() == TypeDocument.identityCard) {
				typeDocument = " identity card";
			} else if (users[i].getTypeDocument() == TypeDocument.passport) {
				typeDocument = " passport";
			}

			msg += "NAME: " + users[i].getName() + " " + users[i].getLastName() + "\n" + "ID NUMBER AND DOCUMENT TYPE: "
					+ users[i].getIdNumber() + typeDocument + "\n" + "ADDRESS: " + users[i].getAddress() + "\n"
					+ "PHONE NUMBER: " + users[i].getPhoneNumber() + "\n" + "\n";
		}
		return msg;
	}

	public void changeAllturnsAttended() {
		int attended = 0;
		for (int i = 0; i < turns.size(); i++) {
			attended = (int) (Math.random() * 2) + 1;
			turns.get(i).setCalled(true);
			if (attended == 1) {
				turns.get(i).setAttended(true);
			} else if (attended == 2) {
				turns.get(i).setAttended(false);
			}
		}
	}

	public void randomNewUsers(int quantity) throws IncompleteInformationException, IOException {
		long inicio = System.currentTimeMillis();
		long total = 0;
		int contador = 0;
		FileReader archivo = new FileReader("data/randomData.csv");
		BufferedReader reader = new BufferedReader(archivo);
		String mensaje = reader.readLine();
		mensaje = reader.readLine();
		while (contador != quantity && mensaje != null) {
			boolean added = true;
			String[] datos = mensaje.split(",");
			int id = Integer.parseInt(datos[0]);
			int phoneNumber = Integer.parseInt(datos[3]);
			int typeD = Integer.parseInt(datos[5]);
			TypeDocument type = null;
			if (typeD == 1) {
				type = TypeDocument.civilRegistration;
			} else if (typeD == 2) {
				type = TypeDocument.foreignCard;
			} else if (typeD == 3) {
				type = TypeDocument.identificationCard;
			} else if (typeD == 4) {
				type = TypeDocument.identityCard;
			} else if (typeD == 5) {
				type = TypeDocument.passport;
			}
			User user = new User(id, datos[1], datos[2], phoneNumber, datos[4], type);
			if (user != null) {

				if (comprobateUserNotExist(id) == false) {
					users.add(user);
					contador++;
				}
			}

			mensaje = reader.readLine();
		}
		reader.close();
		total = System.currentTimeMillis() - inicio;
		// System.out.println("Method duration: " + total);
	}

	public void randomturns(int quantity, int howMany) throws AlreadyHaveTurnException, IncompleteInformationException,
			IOException, LimitExceededException, IncorrectQuantityException {
		int contador = 0;
		Typeturn type = null;
		int randomType = 0;
		if (quantity < 0)
			throw new IncorrectQuantityException();
		if (quantity > 100000)
			throw new LimitExceededException();
		while (contador != quantity) {

			randomNewUsers(quantity);
			for (int i = 0; i < users.size(); i++) {
				if (checkUserDontHaveturn(users.get(i).getIdNumber()) == false) {
					randomType = (int) (Math.random() * 3) + 1;
					if (randomType == 1) {
						type = Typeturn.breakfastTurn;
					} else if (randomType == 2) {
						type = Typeturn.lunchTurn;
					} else if (randomType == 3) {
						type = Typeturn.refreshmentTurn;
					}
					addturnRegisterUser(users.get(i), type);
					contador++;
				}
			}
		}

	}

	public void layOffSpecificUser(int id) {
		int contador = 0;
		for (int i = 0; i < turns.size(); i++) {
			if (turns.get(i).getUser().getIdNumber() == id) {
				if (turns.get(i).isCalled() == true && turns.get(i).isAttended() == false) {
					contador++;
				}
			}
		}
		if (contador >= 2) {
			User user = binarySearchUser(id, usersToArray());
			user.setSuspended(true);
		}
	}

	// ------------------------
	// GETTER Y SETTER
	// ------------------------

	public ArrayList<Turn> getturns() {
		return turns;
	}

	public void setturns(ArrayList<Turn> turns) {
		this.turns = turns;
	}

	public ArrayList<Turn> getturnsRecord() {
		return turnsRecord;
	}

	public void setturnsRecord(ArrayList<Turn> turnsRecord) {
		this.turnsRecord = turnsRecord;
	}

	public ArrayList<User> getUsers() {
		return users;
	}

	public void setUsers(ArrayList<User> users) {
		this.users = users;
	}

	public Timetable getTime() {
		return time;
	}

	public void setTime(Timetable time) {
		this.time = time;
	}

	public Timetable getActualTime() {
		return actualTime;
	}

	public void setActualTime(Timetable actualTime) {
		this.actualTime = actualTime;
	}

}

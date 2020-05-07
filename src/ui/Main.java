package ui;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Calendar;
import java.util.Scanner;

import exception.AlreadyHaveTurnException;
import exception.IncompleteInformationException;
import exception.IncorrectQuantityException;
import exception.LimitExceededException;
import exception.NotFoundException;
import exception.UserAlreadyExistsException;
import model.Business;
import model.Timetable;
import model.Turn;
import model.Turn.Typeturn;
import model.User;
import model.User.TypeDocument;

public class Main {

	// ------------------------
	// RELACIONES
	// ------------------------

	// Relación con el modelo
	private Business business;
	private Scanner reader;

	// ------------------------
	// MÉTODOS
	// ------------------------

	public Main() {
		reader = new Scanner(System.in);
		business = new Business(null);
		deserialization();
	}

	public void showMenu() {
		long inicio = 0;
		long total = 0;
		String nombreArchivo = "";
		BufferedWriter fileWriter;

		Calendar calendar = Calendar.getInstance();

		System.out.println("Before starting system, please select the way you want to have date system");
		System.out.println(
				"1. Using the current date and time of the computer system \n2. Manually, through indicated values");
		int timeSelection = reader.nextInt();

		Timetable time = null;
		Timetable actualSystemTime = null;
		if (timeSelection == 1) {

			int actualDay = calendar.get(calendar.DATE);
			int actualMonth = calendar.get(calendar.MONTH) + 1;
			int actualYear = calendar.get(calendar.YEAR);
			int actualHours = calendar.get(calendar.HOUR_OF_DAY);
			int actualMinutes = calendar.get(calendar.MINUTE);
			int actualSeconds = calendar.get(calendar.SECOND);
			time = new Timetable(actualYear, actualMonth, actualDay, actualHours, actualMinutes, actualSeconds);
			business.setActualTime(null);
		} else if (timeSelection == 2) {
			System.out.println("Enter day");
			int day = reader.nextInt();
			reader.nextLine();

			System.out.println("Enter month");
			int month = reader.nextInt();
			reader.nextLine();

			System.out.println("Enter year");
			int year = reader.nextInt();
			reader.nextLine();

			System.out.println(
					"Enter hour (system only works in 24 hours format for example if is 4 pm you should enter 16)");
			int hours = reader.nextInt();
			reader.nextLine();

			System.out.println("Enter minutes");
			int minutes = reader.nextInt();
			reader.nextLine();

			System.out.println("Enter seconds");
			int seconds = reader.nextInt();
			reader.nextLine();

			time = new Timetable(year, month, day, hours, minutes, seconds);
			Timetable actualST = new Timetable(calendar.get(calendar.YEAR), calendar.get(calendar.MONTH) + 1,
					calendar.get(calendar.DATE), calendar.get(calendar.HOUR_OF_DAY), calendar.get(calendar.MINUTE),
					calendar.get(calendar.SECOND));
			business.setActualTime(actualST);
		}
		business.setTime(time);
		System.out.println(business.getTime().toString());

		int userInput = 0;

		while (userInput != 17) {
			showOptions();
			userInput = reader.nextInt();
			reader.nextLine();

			switch (userInput) {

			case 1:
				System.out.println("Enter id number of the user who will have the turn");
				int idNumber = reader.nextInt();
				Turn turn = null;

				System.out.println("Select turn type");
				System.out.println("1. Breakftast turn \n2. Lunch turn \n3. Refreshment turn");
				int typeturnSelection = reader.nextInt();
				reader.nextLine();

				Typeturn typeturn = null;
				if (typeturnSelection == 1) {
					typeturn = Typeturn.breakfastTurn;
				} else if (typeturnSelection == 2) {
					typeturn = typeturn.lunchTurn;
				} else if (typeturnSelection == 3) {
					typeturn = Typeturn.refreshmentTurn;
				}

				try {
					inicio = System.currentTimeMillis();
					total = 0;
					User turnUser = business.searchUserByIdNumber(idNumber);
					turn = business.addturnRegisterUser(turnUser, typeturn);

					System.out.println("         USER INFORMATION       ");
					System.out.println("Complete name: " + turnUser.getName() + " " + turnUser.getLastName());
					System.out.println("Id number " + turnUser.getIdNumber());
					char id = (char) (turn.getId());
					System.out.println("The assigned turn for " + turnUser.getName() + " " + turnUser.getLastName()
							+ " is " + id + turn.getNumber());
				} catch (NotFoundException e) {
					System.err.println(e.getMessage());

					System.out.println("You want to register user?");
					System.out.println("1. Yes \n2. No");
					int decision = reader.nextInt();

					if (decision == 1) {

						reader.nextLine();
						System.out.println("Enter user name");
						String name = reader.nextLine();

						System.out.println("Enter last name");
						String lastName = reader.nextLine();

						System.out.println("Enter phone number");
						int phoneNumber = reader.nextInt();
						reader.nextLine();

						System.out.println("Enter address");
						String address = reader.nextLine();

						System.out.println("Select type document");
						System.out.println(
								"1. identification card \n2. identity card \n3. civil registration \n4. passport \n5. foreign card");
						int typeDocumentSelection = reader.nextInt();
						reader.nextLine();

						TypeDocument typeDocument = null;
						if (typeDocumentSelection == 1) {
							typeDocument = TypeDocument.identificationCard;
						} else if (typeDocumentSelection == 2) {
							typeDocument = TypeDocument.identityCard;
						} else if (typeDocumentSelection == 3) {
							typeDocument = TypeDocument.civilRegistration;
						} else if (typeDocumentSelection == 4) {
							typeDocument = TypeDocument.passport;
						} else if (typeDocumentSelection == 5) {
							typeDocument = TypeDocument.foreignCard;
						}

						try {
							inicio = System.currentTimeMillis();
							total = 0;

							turn = business.addturnNewUser(idNumber, name, lastName, phoneNumber, address, typeDocument,
									typeturn);

							System.out.println("         USER INFORMATION       ");
							System.out.println("Complete name: " + name + " " + lastName);
							System.out.println("Id number " + idNumber);
							char id = (char) (turn.getId());
							System.out.println(
									"The assigned turn for " + name + " " + lastName + " is " + id + turn.getNumber());
						} catch (IncompleteInformationException | NotFoundException | UserAlreadyExistsException e1) {
							System.err.println(e1.getMessage());
						}
					}
				} catch (AlreadyHaveTurnException e) {
					System.err.println(e.getMessage());
				}
				total = System.currentTimeMillis() - inicio;
				System.out.println("Method duration: " + total + " millis");
				break;

			case 2:
				System.out.println("Would you like to save the turns that currently exist before restarting them?");
				System.out.println("1. Yes \n2. No");
				int recordDecision = reader.nextInt();
				reader.nextLine();
				inicio = System.currentTimeMillis();
				total = 0;
				if (recordDecision == 1) {
					business.maketurnRecord();
				}
				business.restartturn();
				System.out.println("Turns were restared");
				total = System.currentTimeMillis() - inicio;
				System.out.println("Method duration: " + total + " millis");
				break;

			case 3:
				inicio = System.currentTimeMillis();
				total = 0;
				business.advanceturns();
				total = System.currentTimeMillis() - inicio;
				System.out.println("Method duration: " + total + " millis");
				break;

			case 4:

				System.out.println("Want to see organized turns?");
				System.out.println("1. Yes \n2. No");
				int organizedSelection = reader.nextInt();
				reader.nextLine();
				if (organizedSelection == 1) {
					System.out.println("Select how you want to sort turns");
					System.out.println("1. By duration \n2. By creation date");
					int sortSelection = reader.nextInt();
					reader.nextLine();
					inicio = System.currentTimeMillis();
					total = 0;
					if (sortSelection == 1) {
						System.out.println(business.showAllturnsOrganizedDuration());
					} else if (sortSelection == 2) {
						System.out.println("Select the way you want to sort creation date");
						System.out.println("1. Ascending \n2.Descending");
						int wayDuration = reader.nextInt();
						reader.nextLine();
						if (wayDuration == 1) {
							System.out.println(business.showAllturnsOrganizedCreationDate());
						} else if (wayDuration == 2) {
							System.out.println(business.showAllturnsOrganizedCreationDateInverse());
						}
					}
				} else if (organizedSelection == 2) {
					inicio = System.currentTimeMillis();
					total = 0;
					System.out.println(business.showAllturns());
				}
				total = System.currentTimeMillis() - inicio;
				System.out.println("Method duration: " + total + " millis");
				break;
			case 5:
				System.out.println("Want to see organized turns record?");
				System.out.println("1. Yes \n2. No");
				int turnsRecordSelection = reader.nextInt();
				reader.nextLine();
				if (turnsRecordSelection == 1) {
					System.out.println("Select how you want to sort turns");
					System.out.println("1. By duration \n2. By creation date");
					int sortSelection = reader.nextInt();
					reader.nextLine();
					inicio = System.currentTimeMillis();
					total = 0;
					if (sortSelection == 1) {
						System.out.println(business.showturnsRecordOrganizedDuration());
					} else if (sortSelection == 2) {
						System.out.println(business.showturnsRecordOrganizedCreationDate());
					}
				} else if (turnsRecordSelection == 2) {
					inicio = System.currentTimeMillis();
					total = 0;
					System.out.println(business.showAllRecordturns());
				}
				total = System.currentTimeMillis() - inicio;
				System.out.println("Method duration: " + total + " millis");
				break;

			case 6:
				System.out.println("Choose the way you want to visualize users");
				System.out.println("1. Organize by id number \n2. Organize by first name \n3. Organize by last name");
				int userVisualizationSelection = reader.nextInt();
				reader.nextLine();
				inicio = System.currentTimeMillis();
				total = 0;

				if (userVisualizationSelection == 1) {
					System.out.println(business.bubbleSortUsers());
				} else if (userVisualizationSelection == 2) {
					System.out.println(business.selectionSortUsers());
				} else if (userVisualizationSelection == 3) {
					System.out.println(business.insertionSortUsers());
				}
				total = System.currentTimeMillis() - inicio;
				System.out.println("Method duration: " + total + " millis");
				break;

			case 7:

				System.out.println("Enter id number");
				int idN = reader.nextInt();
				reader.nextLine();

				System.out.println("Enter user name");
				String name = reader.nextLine();

				System.out.println("Enter last name");
				String lastName = reader.nextLine();

				System.out.println("Enter phone number");
				int phoneNumber = reader.nextInt();
				reader.nextLine();

				System.out.println("Enter address");
				String address = reader.nextLine();

				System.out.println("Select type document");
				System.out.println(
						"1. identification card \n2. identity card \n3. civil registration \n4. passport \n5. foreign card");
				int typeDocumentSelection = reader.nextInt();
				reader.nextLine();

				TypeDocument typeDocument = null;
				if (typeDocumentSelection == 1) {
					typeDocument = TypeDocument.identificationCard;
				} else if (typeDocumentSelection == 2) {
					typeDocument = TypeDocument.identityCard;
				} else if (typeDocumentSelection == 3) {
					typeDocument = TypeDocument.civilRegistration;
				} else if (typeDocumentSelection == 4) {
					typeDocument = TypeDocument.passport;
				} else if (typeDocumentSelection == 5) {
					typeDocument = TypeDocument.foreignCard;
				}
				inicio = System.currentTimeMillis();
				total = 0;
				try {
					business.addUser(idN, name, lastName, phoneNumber, address, typeDocument);
				} catch (IncompleteInformationException e) {
					System.err.println(e.getMessage());
				} catch (UserAlreadyExistsException e) {
					System.err.println(e.getMessage());
				}
				total = System.currentTimeMillis() - inicio;
				System.out.println("Method duration: " + total + " millis");
				break;

			case 8:
				inicio = System.currentTimeMillis();
				total = 0;
				business.advanceTime();
				System.out.println(business.getTime().toString());
				total = System.currentTimeMillis() - inicio;
				System.out.println("Method duration: " + total + " millis");
				break;
			case 9:
				System.out.println("Enter day");
				int day = reader.nextInt();
				reader.nextLine();

				System.out.println("Enter month");
				int month = reader.nextInt();
				reader.nextLine();

				System.out.println("Enter year");
				int year = reader.nextInt();
				reader.nextLine();

				System.out.println("Enter hour");
				int hours = reader.nextInt();
				reader.nextLine();

				System.out.println("Enter minutes");
				int minutes = reader.nextInt();
				reader.nextLine();

				System.out.println("Enter seconds");
				int seconds = reader.nextInt();
				reader.nextLine();

				time = new Timetable(year, month, day, hours, minutes, seconds);
				inicio = System.currentTimeMillis();
				total = 0;
				if (time.compareTo(business.getTime()) == 1) {
					business.setTime(time);
					business.changeAllturnsAttended();
					Timetable actualST = new Timetable(calendar.get(calendar.YEAR), calendar.get(calendar.MONTH) + 1,
							calendar.get(calendar.DATE), calendar.get(calendar.HOUR_OF_DAY),
							calendar.get(calendar.MINUTE), calendar.get(calendar.SECOND));
					business.setActualTime(actualST);
				} else {
					System.out.println(
							"Time entered is less than the current time, it was not possible to make the change");
				}
				total = System.currentTimeMillis() - inicio;
				System.out.println("Method duration: " + total + " millis");
				break;
			case 10:
				String ruta = "data/";
				System.out.println("Select what type of report you want to make");
				System.out.println(
						"1. Report of all users who have had a certain turn \n2. Report with all turns that a certain person has ever requested");
				int reportSelection = reader.nextInt();
				reader.nextLine();

				System.out.println("How do you want to see the report?");
				System.out.println("1. Console \n2. Generate a file");
				int wayToReport = reader.nextInt();
				reader.nextLine();

				inicio = System.currentTimeMillis();
				total = 0;
				if (wayToReport == 1) {

					if (reportSelection == 1) {
						System.out.println("Enter the id of the turn you want to report to");
						String id = reader.nextLine();
						int idturn = id.charAt(0);
						System.out.println("Enter the number of the turn you want to report to");
						String number = reader.nextLine();

						System.out.println(business.maketurnReport(idturn, number));

					} else if (reportSelection == 2) {
						System.out.println("Enter the id of the person");
						int idReport = reader.nextInt();
						reader.nextLine();
						System.out.println(business.makeUserReport(idReport));
					}

				} else if (wayToReport == 2) {
					System.out.println("Enter the name of file (remember to add extension, for example Reporte.txt)");
					nombreArchivo = reader.nextLine();
					System.out.println("All files are created in the data folder");

					try {
						fileWriter = new BufferedWriter(new FileWriter(ruta + nombreArchivo));

						if (reportSelection == 1) {

							System.out.println("Enter the id of the turn you want to report to");
							String id = reader.nextLine();
							int idturn = id.charAt(0);
							System.out.println("Enter the number of the turn you want to report to");
							String number = reader.nextLine();

							System.out.println("You want to organize the report?");
							// si la respuesta es si, utilizamos comparable y hacemos sort

							fileWriter.write(business.maketurnReport(idturn, number));
						} else if (reportSelection == 2) {
							System.out.println("Enter the id of the person");
							int idReport = reader.nextInt();
							reader.nextLine();
							fileWriter.write(business.makeUserReport(idReport));
						}
						fileWriter.close();
					} catch (IOException e) {
						System.err.println(e.getMessage());

					}
				}
				total = System.currentTimeMillis() - inicio;
				System.out.println("Method duration: " + total + " millis");
				break;

			case 11:
				System.out.println("Enter how much users you want to generate");
				int randomUsers = reader.nextInt();
				reader.nextLine();
				inicio = System.currentTimeMillis();
				total = 0;
				try {
					business.randomNewUsers(randomUsers);
				} catch (IncompleteInformationException | IOException e1) {
					System.err.println(e1.getMessage());
				}
				total = System.currentTimeMillis() - inicio;
				System.out.println("Method duration: " + total + " millis");
				break;
			case 12:
				System.out.println("Enter how much turns you want to generate");
				int randomturns = reader.nextInt();
				reader.nextLine();

				System.out.println("how many turns will be generated per day");
				int howMany = reader.nextInt();
				reader.nextLine();

				inicio = System.currentTimeMillis();
				total = 0;
				try {
					business.randomturns(randomturns, howMany);
				} catch (AlreadyHaveTurnException | IncompleteInformationException | IOException
						| LimitExceededException | IncorrectQuantityException e1) {
					System.err.println(e1.getMessage());
				}
				total = System.currentTimeMillis() - inicio;
				System.out.println("Method duration: " + total + " millis");
				break;
			case 13:
				System.out.println("Users are suspended if they have not been present in the last two turns");
				inicio = System.currentTimeMillis();
				total = 0;
				System.out.println("Enter id number of user you want to suspend");
				int idLay = reader.nextInt();
				reader.nextLine();
				inicio = System.currentTimeMillis();
				total = 0;
				business.layOffSpecificUser(idLay);
				total = System.currentTimeMillis() - inicio;
				System.out.println("Method duration: " + total + " millis");
				break;
			case 14:
				System.out.println("Enter id number of user you are looking for");
				int idNumberBS = reader.nextInt();
				reader.nextLine();
				inicio = System.currentTimeMillis();
				total = 0;
				User found = business.binarySearchUser(idNumberBS, business.usersToArray());
				System.out.println(business.showUser(found));
				total = System.currentTimeMillis() - inicio;
				System.out.println("Method duration: " + total + " millis");
				break;
			case 15:
				inicio = System.currentTimeMillis();
				total = 0;
				String rutaArchivo = "data/BusinessInformation.txt";
				File archivo = new File(rutaArchivo);
				try {
					ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivo));
					oos.writeObject(business);
					oos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				total = System.currentTimeMillis() - inicio;
				System.out.println("Method duration: " + total + " millis");
				break;

			}
		}

	}

	public void showOptions() {
		System.out.println("--------------------------------------------------------------------");
		System.out.println("                     WELCOME TO turn CONTROL                       ");
		System.out.println("                        Choose an option                            ");
		System.out.println("--------------------------------------------------------------------");
		System.out.println("1. Assign turn");
		System.out.println("2. Restart turns");
		System.out.println("3. Advance turn");
		System.out.println("4. Show all turn");
		System.out.println("5. Show turns record");
		System.out.println("6. Show users");
		System.out.println("7. Add new user");
		System.out.println("8. Show date");
		System.out.println("9. Update system date");
		System.out.println("10. Make a report");
		System.out.println("11. Randomly generate new users");
		System.out.println("12. Randomly generate new turns");
		System.out.println("13. Suspend an user");
		System.out.println("14. Search an specific user");
		System.out.println("15. Save information");
		System.out.println("16. Exit");

	}

	public static void main(String[] args) {
		Main main = new Main();
		main.showMenu();
	}

	public void deserialization() {
		try {
			String rutaArchivo = "data/BusinessInformation.txt";
			File archivo = new File(rutaArchivo);
			if (archivo.exists()) {
				ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo));
				business = (Business) ois.readObject();
				ois.close();
			} else {
				archivo.createNewFile();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

}

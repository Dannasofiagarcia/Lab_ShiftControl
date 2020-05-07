package model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import exception.IncompleteInformationException;
import exception.NotFoundException;
import exception.UserAlreadyExistsException;
import model.User.TypeDocument;

public class BusinessTest {
	private Business business;

	public void setupStage1() {
		business = new Business(null);
	}

	public void setupStage2() {
		business = new Business(null);
		try {
			business.addUser(123, "Danna", "Garcia", 3293841, "Street 234", TypeDocument.identificationCard);
		} catch (IncompleteInformationException | UserAlreadyExistsException e) {
			e.printStackTrace();
		}
	}

	public void setupStage3() {
		business = new Business(null);
		try {
			business.addUser(123, "Danna", "Garcia", 3293841, "Street 234", TypeDocument.identificationCard);
			business.addUser(124, "Daniela", "Garcia", 343121, "Street 432", TypeDocument.identificationCard);
		} catch (IncompleteInformationException | UserAlreadyExistsException e) {
			e.printStackTrace();
		}
	}

	public void setupStage4() {
		business = new Business(new Timetable(2020, 3, 14, 17, 50, 0));
		business.setActualTime(new Timetable(2020, 3, 13, 17, 50, 20));
		business.advanceTime();
		assertEquals(53, business.getTime().getMinutes());
	}

	@Test
	public void addUserTest() {
		setupStage1();
		try {
			business.addUser(123, "Danna", "Garcia", 3293841, "Street 234", TypeDocument.identificationCard);
		} catch (IncompleteInformationException e) {
			e.printStackTrace();
		} catch (UserAlreadyExistsException e) {
			e.printStackTrace();
		}
		assertEquals(1, business.getUsers().size());
	}

	@Test

	public void addUserTest1() {
		setupStage2();
		try {
			business.addUser(123, "Danna", "Garcia", 3293841, "Street 234", TypeDocument.identificationCard);
		} catch (IncompleteInformationException | UserAlreadyExistsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(1, business.getUsers().size());
	}

	@Test

	public void addUserTest2() {
		setupStage1();
		try {
			business.addUser(123, "Danna", "Garcia", 3293841, "Street 234", TypeDocument.identificationCard);
		} catch (IncompleteInformationException | UserAlreadyExistsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(1, business.getUsers().size());
	}

	@Test

	public void addUserTest3() {
		setupStage2();
		try {
			business.addUser(124, "Danna", "Garcia", 3293841, "Street 234", TypeDocument.identificationCard);
		} catch (IncompleteInformationException | UserAlreadyExistsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(2, business.getUsers().size());
	}

	@Test

	public void searchUserTest() {
		setupStage2();

		User user = null;
		boolean objectNull = false;

		try {
			user = business.searchUserByIdNumber(123);
		} catch (NotFoundException e) {
			e.printStackTrace();
		}

		if (user == null) {
			objectNull = true;
		}
		assertEquals(false, objectNull);
	}

	@Test

	public void searchUserTest1() {
		setupStage1();

		User user = null;
		boolean objectNull = false;

		try {
			user = business.searchUserByIdNumber(123);
		} catch (NotFoundException e) {
			e.printStackTrace();
		}

		if (user == null) {
			objectNull = true;
		}
		assertEquals(true, objectNull);
	}

	@Test

	public void searchUserTest2() {
		setupStage1();

		User user = null;
		boolean objectNull = false;

		try {
			user = business.searchUserByIdNumber(123);
		} catch (NotFoundException e) {
			e.printStackTrace();
		}

		if (user == null) {
			objectNull = true;
		}
		assertEquals(true, objectNull);
	}

	@Test

	public void searchUserTest3() {
		setupStage2();

		User user = null;
		boolean objectNull = false;

		try {
			user = business.searchUserByIdNumber(123);
		} catch (NotFoundException e) {
			e.printStackTrace();
		}

		if (user == null) {
			objectNull = true;
		}
		assertEquals(false, objectNull);
	}

//	public void addturnTest() {
//		setupStage2();
//		try {
//			business.addturnRegisterUser(business.getUsers().get(0));
//			business.addturnRegisterUser(business.getUsers().get(0));
//		} catch (AlreadyHaveturnException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		assertEquals(1, business.getturns().size());
//	}
//
//	@Test
//
//	public void addturnTest1() {
//		setupStage2();
//		try {
//			business.addturnRegisterUser(business.getUsers().get(0));
//		} catch (AlreadyHaveturnException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		assertEquals(1, business.getturns().size());
//	}

}

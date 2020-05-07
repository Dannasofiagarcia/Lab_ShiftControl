package model;

import java.util.Comparator;

public class TurnComparator implements Comparator<Turn> {

	@Override
	public int compare(Turn one, Turn two) {
		return one.getCreationTime().compareTo(two.getCreationTime());
	}

}

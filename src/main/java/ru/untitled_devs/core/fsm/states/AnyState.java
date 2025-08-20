package ru.untitled_devs.core.fsm.states;

public class AnyState extends State {

	public AnyState() {
		super("ANYSTATE");
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof State;
	}

	@Override
	public int hashCode() {
		return 0;
	}
}

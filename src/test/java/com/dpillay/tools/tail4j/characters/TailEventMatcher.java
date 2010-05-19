package com.dpillay.tools.tail4j.characters;

import org.easymock.IArgumentMatcher;

public class TailEventMatcher implements IArgumentMatcher {

	@Override
	public void appendTo(StringBuffer buffer) {
		buffer.append("TailEventMatcher matches on equals");
	}

	@Override
	public boolean matches(Object argument) {
		// TODO Auto-generated method stub
		return false;
	}

}

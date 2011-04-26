package org.paramecium.aop.cglib;

import org.paramecium.aop.asm.Type;

public class Local {
	private Type type;
	private int index;

	public Local(int index, Type type) {
		this.type = type;
		this.index = index;
	}

	public int getIndex() {
		return index;
	}

	public Type getType() {
		return type;
	}
}

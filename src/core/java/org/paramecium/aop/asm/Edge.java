package org.paramecium.aop.asm;

class Edge {

	static final int NORMAL = 0;

	static final int EXCEPTION = 0x7FFFFFFF;

	int info;

	Label successor;

	Edge next;
}

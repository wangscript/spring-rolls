package org.cy.core.aop.asm;

public interface FieldVisitor {

	AnnotationVisitor visitAnnotation(String desc, boolean visible);

	void visitAttribute(Attribute attr);

	void visitEnd();
}

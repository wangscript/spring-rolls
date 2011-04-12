package org.cy.core.aop.cglib;

import java.util.Iterator;
import java.util.List;

import org.cy.core.aop.asm.Type;

@SuppressWarnings("unchecked")
class InvocationHandlerGenerator implements CallbackGenerator {
	public static final InvocationHandlerGenerator INSTANCE = new InvocationHandlerGenerator();

	private static final Type INVOCATION_HANDLER = TypeUtils
			.parseType("net.sf.cglib.proxy.InvocationHandler");
	private static final Type UNDECLARED_THROWABLE_EXCEPTION = TypeUtils
			.parseType("net.sf.cglib.proxy.UndeclaredThrowableException");
	private static final Type METHOD = TypeUtils
			.parseType("java.lang.reflect.Method");
	private static final Signature INVOKE = TypeUtils
			.parseSignature("Object invoke(Object, java.lang.reflect.Method, Object[])");

	public void generate(ClassEmitter ce, Context context, List methods) {
		for (Iterator it = methods.iterator(); it.hasNext();) {
			MethodInfo method = (MethodInfo) it.next();
			Signature impl = context.getImplSignature(method);
			ce.declare_field(Constants.PRIVATE_FINAL_STATIC, impl.getName(),
					METHOD, null);

			CodeEmitter e = context.beginMethod(ce, method);
			Block handler = e.begin_block();
			context.emitCallback(e, context.getIndex(method));
			e.load_this();
			e.getfield(impl.getName());
			e.create_arg_array();
			e.invoke_interface(INVOCATION_HANDLER, INVOKE);
			e.unbox(method.getSignature().getReturnType());
			e.return_value();
			handler.end();
			EmitUtils.wrap_undeclared_throwable(e, handler, method
					.getExceptionTypes(), UNDECLARED_THROWABLE_EXCEPTION);
			e.end_method();
		}
	}

	public void generateStatic(CodeEmitter e, Context context, List methods) {
		for (Iterator it = methods.iterator(); it.hasNext();) {
			MethodInfo method = (MethodInfo) it.next();
			EmitUtils.load_method(e, method);
			e.putfield(context.getImplSignature(method).getName());
		}
	}
}

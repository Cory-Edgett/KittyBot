package core.utils;

public class ExceptionHandler {
	public static void printErr(Exception e) {
		System.err.printf("[Method: %s (line %d)] %s: %s\n"
				, e.getStackTrace()[0].getMethodName()
				, e.getStackTrace()[0].getLineNumber()
				, e.getClass().getName()
				, e.getMessage());
	}
}

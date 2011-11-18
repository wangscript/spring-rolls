package org.paramecium.log;

import java.util.HashMap;
import java.util.Map;

import org.paramecium.commons.DateUtils;
import org.paramecium.commons.ExceptionUtils;
import org.paramecium.commons.PropertiesUitls;
import org.paramecium.log.handler.ConsoleLogHandler;
import org.paramecium.log.handler.DataBaseLogHandler;
import org.paramecium.log.handler.FileLogHandler;
import org.paramecium.log.system.CollectorFactory;

/**
 * 功 能 描 述:<br>
 * 日志创建
 * <br>代 码 作 者:曹阳(CaoYang)
 * <br>开 发 日 期:2011-4-2下午03:07:28
 * <br>项 目 信 息:paramecium:org.paramecium.log.LoggerFactory.java
 */
public class LoggerFactory {
	private static LogHandler consoleLogHandler;
	private static LogHandler fileLogHandler;
	private static LogHandler dbLogHandler;
	private static int consoleLoggerLevel = Log.LEVEL_DEBUG;
	private static int fileLoggerLevel = Log.LEVEL_WARN;
	private static int dbLoggerLevel = Log.LEVEL_ERROR;
	
	private final static Map<String,Integer> levelMap = new HashMap<String,Integer>();
	private static String loggerFileName;
	public static int loggerFileMax = 10;
	public static boolean sqlIsFormat = false;
	public static boolean beanLogCollector = false;
	public static boolean jdbcLogCollector = false;
	public static boolean webLogCollector = false;
	
	static{
		levelMap.put("trace", Log.LEVEL_TRACE);
		levelMap.put("debug", Log.LEVEL_DEBUG);
		levelMap.put("info", Log.LEVEL_INFO);
		levelMap.put("warn", Log.LEVEL_WARN);
		levelMap.put("error", Log.LEVEL_ERROR);
		levelMap.put("fatal", Log.LEVEL_FATAL);
		Map<String,String> properties = PropertiesUitls.get("/logger.properties");
		{//控制台输出模式
			String loggerLevel = properties.get("consoleLoggerLevel");
			if(loggerLevel!=null){
				consoleLoggerLevel = levelMap.get(loggerLevel.trim().toLowerCase());
				consoleLogHandler = new ConsoleLogHandler();
			}
		}
		{//文件输出模式
			String loggerLevel = properties.get("fileLoggerLevel");
			if(loggerLevel!=null){
				fileLoggerLevel = levelMap.get(loggerLevel.toLowerCase());
				loggerFileName = properties.get("loggerFileName");
				if(loggerFileName==null){
					System.err.println("文件输出日志没有制定loggerFileName属性值!");
					System.exit(0);
				}
				String loggerFileMaxStr = properties.get("loggerFileMax");
				loggerFileMax = loggerFileMaxStr!=null?Integer.parseInt(loggerFileMaxStr):loggerFileMax;
				fileLogHandler = new FileLogHandler();
				
			}
		}
		{//数据库输出模式
			String loggerLevel = properties.get("dbLoggerLevel");
			if(loggerLevel!=null){
				dbLoggerLevel = levelMap.get(loggerLevel.trim().toLowerCase());
				String loggerDbInterface = properties.get("loggerDbInterface");
				if(loggerDbInterface==null){
					System.err.println("数据库输出日志没有制定loggerDbInterface属性值!");
					System.exit(0);
				}
				DataBaseLogHandler.setDbInterface(loggerDbInterface);
				dbLogHandler = new DataBaseLogHandler();
			}
		}
		String sqlIsFormatStr = properties.get("sqlIsFormat");
		String beanLogCollectorStr = properties.get("beanLogCollector");
		String jdbcLogCollectorStr = properties.get("jdbcLogCollector");
		String webLogCollectorStr = properties.get("webLogCollector");
		sqlIsFormat = sqlIsFormatStr != null ? Boolean.valueOf(sqlIsFormatStr) : false;
		beanLogCollector = beanLogCollectorStr != null ? Boolean.valueOf(beanLogCollectorStr) : false;
		jdbcLogCollector = jdbcLogCollectorStr != null ? Boolean.valueOf(jdbcLogCollectorStr) : false;
		webLogCollector = webLogCollectorStr != null ? Boolean.valueOf(webLogCollectorStr) : false;
		CollectorFactory.setLogCollector(properties.get("logCollectorInterface"));
	}
	
	/**
	 * 获得文件名
	 * @return
	 */
	public static String getLoggerFileName(){
		return loggerFileName;
	}
	
	/**
	 * 获取打印日志用的信息
	 * @param message
	 * @param level
	 * @param className
	 * @return
	 */
	private static String getMessage(String message,String level,String className,String methodName){
		StringBuffer sb = new StringBuffer();
		sb.append(DateUtils.getCurrentDateTimeStr()).append(' ');
		sb.append(className).append('.').append(methodName);
		sb.append("\r\n");
		sb.append(level).append(":");
		sb.append(message);
		sb.append("\r\n");
		return sb.toString();
	}

	/**
	 * 获取打印日志用的信息
	 * @param throwable
	 * @param level
	 * @param className
	 * @return
	 */
	private static String getMessage(Throwable throwable,String level,String className,String methodName){
		StringBuffer sb = new StringBuffer();
		sb.append(DateUtils.getCurrentDateTimeStr()).append(' ');
		sb.append(className).append('.').append(methodName);
		sb.append("\r\n");
		sb.append(level).append(":");
		sb.append(ExceptionUtils.getExceptionString(throwable));
		sb.append("\r\n");
		return sb.toString();
	}
	
	public static Log getLogger(){
		return (new LoggerFactory()).new Logger();
	}

	/**
	 * 功 能 描 述:<br>
	 * 日志实现类
	 * <br>代 码 作 者:曹阳(CaoYang)
	 * <br>开 发 日 期:2011-4-2下午04:07:51
	 * <br>项 目 信 息:paramecium:org.paramecium.log.LoggerFactory.java
	 */
	private final class Logger implements Log{
		
		private String className;
		
		//以下为异常级别===============
		
		private String getMethodNameSpecial(){
			Exception exception = new Exception();
			String methodName = exception.getStackTrace()[1].getMethodName();// 获得调用者的方法名
			methodName = methodName+"("+exception.getStackTrace()[1].getFileName()+":"+exception.getStackTrace()[1].getLineNumber()+")";// 获得调用者的文件名和行号
			if(this.className==null){
				className = exception.getStackTrace()[1].getClassName();// 获得调用者的类名
			}
			return methodName;
		}

		private String getMethodNameSpecial(Throwable throwable){
			String methodName = throwable.getStackTrace()[1].getMethodName();// 获得调用者的方法名
			methodName = methodName+"("+throwable.getStackTrace()[1].getFileName()+":"+throwable.getStackTrace()[1].getLineNumber()+")";// 获得调用者的文件名和行号
			if(this.className==null){
				className = throwable.getStackTrace()[1].getClassName();// 获得调用者的类名
			}
			return methodName;
		}
		
		public void error(String message) {
			String methodName = getMethodNameSpecial();
			if(consoleLogHandler!=null && consoleLoggerLevel <= Log.LEVEL_ERROR){
				consoleLogHandler.log(getMessage(message, "ERROR", className,methodName),true);
			}
			if(fileLogHandler!=null && fileLoggerLevel <= Log.LEVEL_ERROR){
				fileLogHandler.log(getMessage(message, "ERROR", className,methodName),true);
			}
			if(dbLogHandler!=null && dbLoggerLevel <= Log.LEVEL_ERROR){
				dbLogHandler.log(getMessage(message, "ERROR", className,methodName),true);
			}
		}

		
		public void error(Throwable throwable) {
			String methodName = getMethodNameSpecial(throwable);
			if(consoleLogHandler!=null && consoleLoggerLevel <= Log.LEVEL_ERROR){
				consoleLogHandler.log(getMessage(throwable, "ERROR", className,methodName),true);
			}
			if(fileLogHandler!=null && fileLoggerLevel <= Log.LEVEL_ERROR){
				fileLogHandler.log(getMessage(throwable, "ERROR", className,methodName),true);
			}
			if(dbLogHandler!=null && dbLoggerLevel <= Log.LEVEL_ERROR){
				dbLogHandler.log(getMessage(throwable, "ERROR", className,methodName),true);
			}
		}

		
		public void fatal(String message) {
			String methodName = getMethodNameSpecial();
			if(consoleLogHandler!=null && consoleLoggerLevel <= Log.LEVEL_FATAL){
				consoleLogHandler.log(getMessage(message, "FATAL", className,methodName),true);
			}
			if(fileLogHandler!=null && fileLoggerLevel <= Log.LEVEL_FATAL){
				fileLogHandler.log(getMessage(message, "FATAL", className,methodName),true);
			}
			if(dbLogHandler!=null && dbLoggerLevel <= Log.LEVEL_FATAL){
				dbLogHandler.log(getMessage(message, "FATAL", className,methodName),true);
			}
		}

		public void fatal(Throwable throwable) {
			String methodName = getMethodNameSpecial(throwable);
			if(consoleLogHandler!=null && consoleLoggerLevel <= Log.LEVEL_FATAL){
				consoleLogHandler.log(getMessage(throwable, "FATAL", className,methodName),true);
			}
			if(fileLogHandler!=null && fileLoggerLevel <= Log.LEVEL_FATAL){
				fileLogHandler.log(getMessage(throwable, "FATAL", className,methodName),true);
			}
			if(dbLogHandler!=null && dbLoggerLevel <= Log.LEVEL_FATAL){
				dbLogHandler.log(getMessage(throwable, "FATAL", className,methodName),true);
			}
		}
		
		//以下为常规级别===============
		
		private String getMethodNameGeneral(){
			Exception exception = new Exception();
			String methodName = exception.getStackTrace()[1].getMethodName();// 获得调用者的方法名
			if(this.className==null){
				className = exception.getStackTrace()[1].getClassName();// 获得调用者的类名
			}
			return methodName;
		}

		private String getMethodNameGeneral(Throwable throwable){
			String methodName = throwable.getStackTrace()[1].getMethodName();// 获得调用者的方法名
			if(this.className==null){
				className = throwable.getStackTrace()[1].getClassName();// 获得调用者的类名
			}
			return methodName;
		}

		public void info(String message) {
			String methodName = getMethodNameGeneral();
			if(consoleLogHandler!=null && consoleLoggerLevel <= Log.LEVEL_INFO){
				consoleLogHandler.log(getMessage(message, "INFO", className,methodName),false);
			}
			if(fileLogHandler!=null && fileLoggerLevel <= Log.LEVEL_INFO){
				fileLogHandler.log(getMessage(message, "INFO", className,methodName),false);
			}
			if(dbLogHandler!=null && dbLoggerLevel <= Log.LEVEL_INFO){
				dbLogHandler.log(getMessage(message, "INFO", className,methodName),false);
			}
		}
		
		public void info(Throwable throwable) {
			String methodName = getMethodNameGeneral(throwable);
			if(consoleLogHandler!=null && consoleLoggerLevel <= Log.LEVEL_INFO){
				consoleLogHandler.log(getMessage(throwable, "INFO", className,methodName),false);
			}
			if(fileLogHandler!=null && fileLoggerLevel <= Log.LEVEL_INFO){
				fileLogHandler.log(getMessage(throwable, "INFO", className,methodName),false);
			}
			if(dbLogHandler!=null && dbLoggerLevel <= Log.LEVEL_INFO){
				dbLogHandler.log(getMessage(throwable, "INFO", className,methodName),false);
			}
		}

		public void trace(String message) {
			String methodName = getMethodNameGeneral();
			if(consoleLogHandler!=null && consoleLoggerLevel <= Log.LEVEL_TRACE){
				consoleLogHandler.log(getMessage(message, "TRACE", className,methodName),false);
			}
			if(fileLogHandler!=null && fileLoggerLevel <= Log.LEVEL_TRACE){
				fileLogHandler.log(getMessage(message, "TRACE", className,methodName),false);
			}
			if(dbLogHandler!=null && dbLoggerLevel <= Log.LEVEL_TRACE){
				dbLogHandler.log(getMessage(message, "TRACE", className,methodName),false);
			}
		}

		public void trace(Throwable throwable) {
			String methodName = getMethodNameGeneral(throwable);
			if(consoleLogHandler!=null && consoleLoggerLevel <= Log.LEVEL_TRACE){
				consoleLogHandler.log(getMessage(throwable, "TRACE", className,methodName),false);
			}
			if(fileLogHandler!=null && fileLoggerLevel <= Log.LEVEL_TRACE){
				fileLogHandler.log(getMessage(throwable, "TRACE", className,methodName),false);
			}
			if(dbLogHandler!=null && dbLoggerLevel <= Log.LEVEL_TRACE){
				dbLogHandler.log(getMessage(throwable, "TRACE", className,methodName),false);
			}
		}
		
		public void debug(String message) {
			String methodName = getMethodNameGeneral();
			if(consoleLogHandler!=null && consoleLoggerLevel <= Log.LEVEL_DEBUG){
				consoleLogHandler.log(getMessage(message, "DEBUG", className,methodName),false);
			}
			if(fileLogHandler!=null && fileLoggerLevel <= Log.LEVEL_DEBUG){
				fileLogHandler.log(getMessage(message, "DEBUG", className,methodName),false);
			}
			if(dbLogHandler!=null && dbLoggerLevel <= Log.LEVEL_DEBUG){
				dbLogHandler.log(getMessage(message, "DEBUG", className,methodName),false);
			}
		}

		
		public void debug(Throwable throwable) {
			String methodName = getMethodNameGeneral(throwable);
			if(consoleLogHandler!=null && consoleLoggerLevel <= Log.LEVEL_DEBUG){
				consoleLogHandler.log(getMessage(throwable, "DEBUG", className,methodName),false);
			}
			if(fileLogHandler!=null && fileLoggerLevel <= Log.LEVEL_DEBUG){
				fileLogHandler.log(getMessage(throwable, "DEBUG", className,methodName),false);
			}
			if(dbLogHandler!=null && dbLoggerLevel <= Log.LEVEL_DEBUG){
				dbLogHandler.log(getMessage(throwable, "DEBUG", className,methodName),false);
			}
		}
		
		public void warn(String message) {
			String methodName = getMethodNameGeneral();
			if(consoleLogHandler!=null && consoleLoggerLevel <= Log.LEVEL_WARN){
				consoleLogHandler.log(getMessage(message, "WARN", className,methodName),true);
			}
			if(fileLogHandler!=null && fileLoggerLevel <= Log.LEVEL_WARN){
				fileLogHandler.log(getMessage(message, "WARN", className,methodName),true);
			}
			if(dbLogHandler!=null && dbLoggerLevel <= Log.LEVEL_WARN){
				dbLogHandler.log(getMessage(message, "WARN", className,methodName),true);
			}
		}

		public void warn(Throwable throwable) {
			String methodName = getMethodNameGeneral(throwable);
			if(consoleLogHandler!=null && consoleLoggerLevel <= Log.LEVEL_WARN){
				consoleLogHandler.log(getMessage(throwable, "WARN", className,methodName),true);
			}
			if(fileLogHandler!=null && fileLoggerLevel <= Log.LEVEL_WARN){
				fileLogHandler.log(getMessage(throwable, "WARN", className,methodName),true);
			}
			if(dbLogHandler!=null && dbLoggerLevel <= Log.LEVEL_WARN){
				dbLogHandler.log(getMessage(throwable, "WARN", className,methodName),true);
			}
		}
		
	}
	
}

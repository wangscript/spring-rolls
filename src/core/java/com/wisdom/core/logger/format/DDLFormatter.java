package com.wisdom.core.logger.format;

import java.util.StringTokenizer;

/**
 * 功 能 描 述:<br>
 * 带有DDL语句格式化
 * <br>代 码 作 者:曹阳(CaoYang)
 * <br>开 发 日 期:2010-11-18上午10:24:56
 * <br>项 目 信 息:wisdom.3.0:com.wisdom.core.logger.format.DDLFormatter.java
 */
public class DDLFormatter{
	
	public static String format(String sql) {
		if ( sql.toLowerCase().startsWith( "create table" ) ) {
			return formatCreateTable( sql );
		}
		else if ( sql.toLowerCase().startsWith( "alter table" ) ) {
			return formatAlterTable( sql );
		}
		else if ( sql.toLowerCase().startsWith( "comment on" ) ) {
			return formatCommentOn( sql );
		}
		else {
			return "\n    " + sql;
		}
	}

	private static String formatCommentOn(String sql) {
		StringBuffer result = new StringBuffer( 60 ).append( "\n    " );
		StringTokenizer tokens = new StringTokenizer( sql, " '[]\"", true );

		boolean quoted = false;
		while ( tokens.hasMoreTokens() ) {
			String token = tokens.nextToken();
			result.append( token );
			if ( isQuote( token ) ) {
				quoted = !quoted;
			}
			else if ( !quoted ) {
				if ( "is".equals( token ) ) {
					result.append( "\n       " );
				}
			}
		}

		return result.toString();
	}

	private static String formatAlterTable(String sql) {
		StringBuffer result = new StringBuffer( 60 ).append( "\n    " );
		StringTokenizer tokens = new StringTokenizer( sql, " (,)'[]\"", true );

		boolean quoted = false;
		while ( tokens.hasMoreTokens() ) {
			String token = tokens.nextToken();
			if ( isQuote( token ) ) {
				quoted = !quoted;
			}
			else if ( !quoted ) {
				if ( isBreak( token ) ) {
					result.append( "\n        " );
				}
			}
			result.append( token );
		}

		return result.toString();
	}

	private static String formatCreateTable(String sql) {
		StringBuffer result = new StringBuffer( 60 ).append( "\n    " );
		StringTokenizer tokens = new StringTokenizer( sql, "(,)'[]\"", true );

		int depth = 0;
		boolean quoted = false;
		while ( tokens.hasMoreTokens() ) {
			String token = tokens.nextToken();
			if ( isQuote( token ) ) {
				quoted = !quoted;
				result.append( token );
			}
			else if ( quoted ) {
				result.append( token );
			}
			else {
				if ( ")".equals( token ) ) {
					depth--;
					if ( depth == 0 ) {
						result.append( "\n    " );
					}
				}
				result.append( token );
				if ( ",".equals( token ) && depth == 1 ) {
					result.append( "\n       " );
				}
				if ( "(".equals( token ) ) {
					depth++;
					if ( depth == 1 ) {
						result.append( "\n        " );
					}
				}
			}
		}

		return result.toString();
	}

	private static boolean isBreak(String token) {
		return "drop".equals( token ) ||
				"add".equals( token ) ||
				"references".equals( token ) ||
				"foreign".equals( token ) ||
				"on".equals( token );
	}

	private static boolean isQuote(String tok) {
		return "\"".equals( tok ) ||
				"`".equals( tok ) ||
				"]".equals( tok ) ||
				"[".equals( tok ) ||
				"'".equals( tok );
	}

}
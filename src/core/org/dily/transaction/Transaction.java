package org.dily.transaction;

import java.sql.Connection;
import java.sql.SQLException;

import org.dily.jdbc.datasource.DefaultDataSource;
import org.dily.jdbc.datasource.MultiDataSourceFactory;
import org.dily.log.Log;
import org.dily.log.LoggerFactory;
/**
 * 功 能 描 述:<br>
 * 数据库事务处理
 * <br>代 码 作 者:曹阳(CaoYang)
 * <br>开 发 日 期:2011-4-1下午05:45:03
 * <br>项 目 信 息:dily:org.dily.transaction.Transaction.java
 */
public class Transaction {

	private final static Log logger = LoggerFactory.getLogger();
	
	private DefaultDataSource dataSource;
	
	private Connection connection;
	
	private boolean isException = false;
	
	public Transaction(final String dataSourceName) throws SQLException{
		this.dataSource = MultiDataSourceFactory.getDataSource(dataSourceName);
		if(this.dataSource==null){
			throw new SQLException("EN:connection fail!CN:数据库连接错误!");
		}
		this.connection = this.dataSource.getConnection();
		if(this.connection==null){
			throw new SQLException("EN:connection fail!CN:数据库连接错误!");
		}
		logger.debug("TRANSACTION#"+this.hashCode()+" CONNECTION#"+this.connection.hashCode()+" IS OPEN!");
	}
	
	/**
	 * 获得当前连接
	 * @return
	 * @throws SQLException
	 */
	public synchronized Connection getCurrentConnection() throws SQLException {
		return this.connection;
	}
	
	/**
	 * Connection.TRANSACTION_READ_COMMITTED:默认,可以防止脏读<br>
	 * Connection.TRANSACTION_READ_UNCOMMITTED:只保证不会读到非法数据，有可能出现脏读取、重复读取、虚读<br>
	 * Connection.TRANSACTION_REPEATABLE_READ:可以防止脏读和不可重复读取<br>
	 * Connection.TRANSACTION_READ_SERIALIZABLE防止出现脏读取、重复读取、虚读。<br>
	 * Connection.TRANSACTION_NONE<br>
	 * @param level
	 * @throws SQLException
	 */
	public synchronized void setTransactionIsolation(int level) throws SQLException{
		connection.setTransactionIsolation(level);
	}
	
	public synchronized void setReadOnly(boolean readOnly) throws SQLException{
		connection.setReadOnly(readOnly);
	}

	/**
	 * 提交事务
	 * @throws SQLException
	 */
	public synchronized void commit() throws SQLException{
		connection.commit();
		logger.debug("TRANSACTION#"+this.hashCode()+" CONNECTION#"+this.connection.hashCode()+" IS COMMIT!");
	}

	/**
	 * 回滚事务
	 * @throws SQLException
	 */
	public synchronized void rollback() throws SQLException{
		connection.rollback();
		logger.debug("TRANSACTION#"+this.hashCode()+" CONNECTION#"+this.connection.hashCode()+" IS ROOLBACK!");
	}
	
	/**
	 * 连接结束
	 * @throws SQLException
	 */
	public synchronized void over() throws SQLException{
		dataSource.replace(connection);
		logger.debug("TRANSACTION#"+this.hashCode()+" CONNECTION#"+this.connection.hashCode()+" IS OVER!");
	}


	public synchronized boolean isException() {
		return isException;
	}

	public synchronized void setException() {
		if(this.isException == false){
			this.isException = true;
		}
	}

	public synchronized void setConnection(Connection connection) {
		this.connection = connection;
	}
	
}

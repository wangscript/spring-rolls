package org.cy.core.test;

import java.util.Date;

import org.cy.core.jdbc.GenericJdbcDao;
import org.cy.core.transaction.TransactionManager;

public class Test{
	
	private int id;
	
	private String logInfo;
	
	private Date logDate;
	
	public static void main(String[] args) throws Exception {
		String sql = "insert into t_logger_info(id,log_info,log_date) values(:id,:logInfo,:logDate)";
		TransactionManager.before();
		GenericJdbcDao dao = new GenericJdbcDao();
		GenericJdbcDao dao2 = new GenericJdbcDao();
		Test test = new Test();
		//test.setId(10021);
		test.setLogInfo("哈哈神马都是浮云");
		test.setLogDate(new Date());
		int id = dao.insertGetGeneratedKeyByBean(sql,test).intValue();
		short id2 = dao.getAutoGeneratedKey().shortValue();
		System.out.println(id);
		System.out.println(id2);
		int id3 = dao2.insertGetGeneratedKeyByBean("errorsql",test).intValue();
		short id4 = dao.getAutoGeneratedKey().shortValue();
		System.out.println(id3);
		System.out.println(id4);
		TransactionManager.end();
	}

	public String getLogInfo() {
		return logInfo;
	}

	public void setLogInfo(String logInfo) {
		this.logInfo = logInfo;
	}

	public Date getLogDate() {
		return logDate;
	}

	public void setLogDate(Date logDate) {
		this.logDate = logDate;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
}

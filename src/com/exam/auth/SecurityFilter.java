package com.exam.auth;

import java.io.File;
import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.paramecium.commons.CommandUtils;
import org.paramecium.commons.EncodeUtils;



public class SecurityFilter implements Filter{
	
	private static boolean isSecurity = false;
	private static String cpuId = null;
	private static String currentSN = null;
	
	
	public void destroy() {
		
	}

	public void doFilter(ServletRequest request, ServletResponse response,FilterChain chain) throws IOException, ServletException {
		if(isSecurity){
			chain.doFilter(request, response);
			return;
		}
		if(currentSN == null){//读取上传来的sn
			String sn = request.getParameter("AUTH_SN");
			if(sn!=null){
				sn = sn.trim().toUpperCase();
				if(sn.equals(getSN(getCPUID()))){//如果读出的sn合法
					isSecurity = true;
					String fileName = ((HttpServletRequest)request).getSession().getServletContext().getRealPath("/WEB-INF");
					File file = new File(fileName.concat("/"+getSN(getCPUID())));
					if(!file.createNewFile()){
						throw new RuntimeException("该操作系统没有为此系统授权，无法创建授权文件!");
					}
					success((HttpServletResponse)response);
					return;
				}
			}
		}
		error((HttpServletResponse)response);
	}
	
	private void error(HttpServletResponse response) throws IOException, ServletException {
		response.setCharacterEncoding("UTF-8");
		ServletOutputStream out = response.getOutputStream();
		out.print("<html><meta content='text/html; charset=UTF-8' http-equiv='content-type'><head><title>请输入授权SN码!</title></head>");
		out.print("<body>");
		out.print("<font size='10' color='red'><b>请输入授权SN码!</b></font>");
		out.print("<form action='/authorize.auth' method='post'>");
		out.print("<b>CPUID:"+getCPUID()+"</b><br>");
		out.print("请输入SN:<input name='AUTH_SN'><br>");
		out.print("<button type='submit'>提交</button>");
		out.print("</form>");
		out.print("</body>");
		out.print("</html>");
		out.flush();
	}
	
	private void success(HttpServletResponse response) throws IOException, ServletException {
		response.setCharacterEncoding("UTF-8");
		ServletOutputStream out = response.getOutputStream();
		out.print("<html><head><meta content='text/html; charset=UTF-8' http-equiv='content-type'><title>授权正确!</title></head>");
		out.print("<body>");
		out.print("<font size='10' color='red'><b>授权正确!</b></font>");
		out.print("</body>");
		out.print("</html>");
		out.flush();
	}

	public void init(FilterConfig config) throws ServletException {
		String fileName = config.getServletContext().getRealPath("/WEB-INF");
		File file = new File(fileName.concat("/"+getSN(getCPUID())));
		if(file.canRead()){
			isSecurity = true;
		}
	}
	
	public static void main(String[] args) {
		System.out.println(getCPUID());
		System.out.println(getSN(getCPUID()));
	}
	
	/**
	 * 获得CPUID
	 * @return
	 */
	private static String getCPUID(){
		if(cpuId==null){
			cpuId = CommandUtils.getRunResult("wmic cpu get processorid");
			cpuId = new StringBuffer(cpuId).delete(0, 11).toString().trim().toUpperCase();
		}
		return cpuId;
	}
	
	private static String getSN(String cpuId){
		String str = EncodeUtils.encryptMD5(cpuId.toUpperCase()).toUpperCase();
		StringBuffer sb = new StringBuffer();
		sb.append(str.substring(14, 18)).append("-");
		sb.append(str.substring(5, 9)).append("-");
		sb.append(str.substring(9, 13)).append("-");
		sb.append(str.substring(1, 5));
		return sb.toString();
	}

}

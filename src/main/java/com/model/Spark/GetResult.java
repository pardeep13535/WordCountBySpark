package com.model.Spark;


/**
 * @author Pardeep Kumar
 * @FBID www.facebook.com/pradeep13535
 */
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class GetResult extends HttpServlet {
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
	{
		WordCount wc = new WordCount();
		String directory = req.getParameter("dir");
		String query = req.getParameter("query");
		List<String> name = wc.search(query, directory);
		String lines = null;
		if(name != null){
			lines = name.toString();
		}
		resp.getOutputStream().print(lines);
	}

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

}
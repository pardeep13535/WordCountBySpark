
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author" content="">
<%@ page import="com.model.Spark.WordCount" %>
<title>Jumbotron Template for Bootstrap</title>
<%@ page import=" java.util.List" %>
<!-- Bootstrap core CSS -->
<link href="css/bootstrap.min.css" rel="stylesheet">

<!-- Custom styles for this template -->
<link href="css/jumbotron.css" rel="stylesheet">
	<!-- Bootstrap core JavaScript
    ================================================== -->
	<!-- Placed at the end of the document so the pages load faster -->
	<script src="js/jquery-2.1.1.min.js"></script>
	<script src="js/bootstrap.min.js"></script>
	    <style>
        .rma-loading {
        position: absolute; top: 50%; left: 50%; margin-left: -75px;
        margin-top: -75px; z-index: 9999;
        }
        #msg
        {
        	text-align: center;
        }
    </style>
</head>
<%
	String query=null;
	if(request.getParameter("query") != null)
		query=request.getParameter("query");
	String directory=null;
	if(request.getParameter("directory") != null)
		directory=request.getParameter("directory");
%>
<body>
	<div id="loadingDiv" class="rma-loading">
        <img src="images/images.jpg" alt="Loading..." />
    </div>
    
<script>
function showLoading()
{
    $('#loadingDiv').show();
}
function hideLoading()
{
    $('#loadingDiv').hide();
}
</script>

	<div class="navbar navbar-inverse navbar-fixed-top" role="navigation">
		<div class="container">
			<div class="navbar-header">
				<button type="button" class="navbar-toggle" data-toggle="collapse"
					data-target=".navbar-collapse">
					<span class="sr-only">Toggle navigation</span> <span
						class="icon-bar"></span> <span class="icon-bar"></span> <span
						class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="#">FF4 - Risk database analysis</a>
			</div>
			<div class="navbar-collapse collapse">
				<form class="navbar-form navbar-right" role="form">
					<div class="form-group">
						<input type="text" value="<%= directory!=null? directory:"" %>" placeholder="<%= directory!=null? directory:"Enter directory path" %>" class="form-control" name="directory">
						<input type="text" value="<%= query!=null? query:"" %>" placeholder="<%= query!=null? query:"Enter query" %>" class="form-control" name="query">
					</div>
					<button type="submit" class="btn btn-success" action="results.jsp">Search</button>
				</form>
			</div>
			<!--/.navbar-collapse -->
		</div>
	</div>
<% 
if(query!=null && directory!=null)
{
%>
<%	
WordCount hw =  new WordCount();
List<String> name = hw.search(query,directory);
%>
<script>
hideLoading();
</script>
	<br/><br/><br/><br/><br/>
	<div class="container">
		<table class="table table-hover">
			<thead>
				<tr>
					<th>Filename</th>
					<th>Matched String</th>
					<th>Count</th>
				</tr>
				<%
					for(int i=0;i<name.size();i++)
					{
						String line = (String)name.get(i);	
                        
                        String fileName = line.substring(line.lastIndexOf("|:|")+3,line.lastIndexOf("||:"));
                        String count = line.substring(line.lastIndexOf("||:")+3);
                        line = line.substring(0, line.lastIndexOf("|:|"));
                        if(line.length()>100)
                        {
                        	line=line.substring(0,99)+"...";
                        }
                 %>
                                         <tr>
                                         <td><%=fileName%></td>
                                         <td><%=line%></td>
                                         <td><%=count%></td>
                                         </tr>
                 
                 <%
					}
				%>

			</thead>
			<tbody>
					
			</tbody>
		</table>
	</div>
	<!-- /container -->
<%
}
else
{
%>
	<script>
	hideLoading();
	</script>
<%	
	out.println("<br/><br/><div id='msg'>");
	out.println("<h2>Enter Query and repository folder(local or spark cluster) in top right corner ...<h2>");
	out.println("</div>");
}
%>

</body>
</html>

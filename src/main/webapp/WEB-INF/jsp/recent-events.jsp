<jsp:include page="common/header.jsp"/>

<%@ page import="javabandsintown.entity.Event" %>
<%@ page import="java.util.List" %>
<html>
<head>
    <title>musiConcerts</title>
</head>
<!-- tag for search box -->
<meta name="ROBOTS" content="NOINDEX, NOFOLLOW" />

<!-- Navigation -->
<nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
    <div class="container-fluid">
        <!-- Brand and toggle get grouped for better mobile display -->
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse"
                    data-target="#bs-example-navbar-collapse-1">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="home">musiConcerts</a>
        </div>
        <div class="collapse navbar-collapse">
            <ul class="nav navbar-nav">
                <li><a href="gallery">Your Galleries</a></li>
                <li><a href="popular">Popular</a></li>
                <li class="active"><a href="recent?daysBefore=1">Recent Events</a></li>
<!--  SEARCH BAR -->
<!-- 				<li> -->
<!-- 					<form id="demo-2" class="form-wrapper" action="search" method="post" commnadName="tag"> -->
<!-- 					    <input type="text" id="search" name="tag" size="21" placeholder="Search for Artist, Concerts ..." maxlength="120" required /> -->
<!-- 					    <input type="submit" name="submit" value="Search" id="submit"> -->
<!-- 					    <input type="hidden" name="searchType" value="tag"/>					         -->
<!-- 					</form> -->
<!-- 					<div class="tfclear"></div> -->
<!-- 		        </li> -->
		        <li>
						<form id="demo-2" action="search" method="post" commnadName="tag">
							<input type="search" name="tag" required />
						</form>
		        </li> 
            </ul>
        </div>
    </div>
</nav>

<body>
	
    <div class="col-lg-10 col-lg-offset-1">
<!--         <form action="search" method="post" commnadName="tag"> -->
<!--         	<div class="input-group input-group-lg"  id="search-input"> -->
<!--             <input type="text" name="tag" class="form-control" placeholder="Search your artist .... enjoy the magic" required />  -->
<!--             <span class="input-group-btn"> -->
<!--                 <button class="btn btn-default" name="submit" value="Search" id="submit" type="submit">Go!</button> -->
<!--             </span> -->
<!--         </div> -->
<!--         </form> -->
        
        <table class="table table-fixed">
        	<thead>
		      <tr>
		        <th>Event</th>
		        <th>Date</th>
		      </tr>
		    </thead>
        	<tbody>
                
			<%
	        	List<Event> eventList = 
	        		(List<Event>)request.getAttribute("eventList");
	//			System.out.println("artist-events page with " + eventList.size() + " events");
				int position = 0;
	        	for(Event event : eventList){	
			 %>
		       				    
				<tr>
				  <td><a href="local?eventId=<%= event.getId()%>"><%= event.getTitle()%></a></td>
				  <td><%= event.getDatetime()%></td>
				</tr>
	
			<%
					position++;
	            }
	        %>
		    </tbody>
        </table>
	<% 
    	int daysBefore = (int) request.getAttribute("daysBefore");
		int previousDay = daysBefore + 1;
		int nextDay = daysBefore - 1;
    %>    
	<ul>
			<li>
				<a href="recent?daysBefore=<%= previousDay %>">
					previous day</a>
			</li>
		<% 	if(nextDay != 0){%>
			<li>
				<a href="recent?daysBefore=<%= nextDay %>">
					next day</a>
			</li> 
		<%	} %>		
	</ul>	
    </div>

</body>
</html>
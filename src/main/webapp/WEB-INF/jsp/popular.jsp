<jsp:include page="common/header.jsp"/>

<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.HashMap" %>

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
                <li class="active"><a href="popular">Popular</a></li>
                <li><a href="recent">Recent Events</a></li>
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

<!-- Page Content -->
<div class="container">

    <div class="row">

        <div class="col-lg-12">
            <h1 class="page-header">Popular Researches</h1>
        </div>
        
        <%
        	HashMap<String,String> map = 
	    		(HashMap<String,String>)request.getAttribute("map"); 
	        Iterator it = map.entrySet().iterator();
	        while (it.hasNext()) {
	            Map.Entry pair = (Map.Entry)it.next();
        %>
        <div class="col-lg-3 col-md-4 col-xs-6 thumb">
            <a class="thumbnail" href="search?tag=<%= pair.getKey() %>">
                            <img class="img-responsive"
                            	src="<%=pair.getValue()%>" alt="">
                <h1><%= pair.getKey()%></h1>
            </a>
        </div>
        <%
		        it.remove(); // avoids a ConcurrentModificationException
			}
        %>

        <%
	    	ArrayList<String> list = 
	    		(ArrayList<String>)request.getAttribute("list"); 
	    	for(String name : list){	
        %>
        <div class="col-lg-3 col-md-4 col-xs-6 thumb">
            <a class="thumbnail" href="search?tag=<%= name %>">
                <h1><%= name%></h1>
            </a>
        </div>
        <%
            }
        %>


    </div>

    <hr>


<jsp:include page="common/footer.jsp"/>
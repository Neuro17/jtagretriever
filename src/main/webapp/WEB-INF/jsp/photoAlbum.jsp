<jsp:include page="common/header.jsp"/>
<jsp:include page="common/stylesheet.jsp"/>

<%@ page import="java.util.ArrayList" %>

<!-- tag for search box -->
<meta name="ROBOTS" content="NOINDEX, NOFOLLOW" />

<!-- Navigation -->
<nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
    <div class="container">
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
                <li  class="active"><a href="#">PhotoAlbum</a></li>
                <li><a href="gallery">YourGalleries</a></li>
                <li><a href="popular">Popular</a></li>
                <li><a></a></li>
                <li><a></a></li>
                <li><a></a></li>
                <li><a></a></li>
                <li><a></a></li>
                <li><a></a></li>
                <li><a></a></li>
                <li><a></a></li>
                <li><a></a></li>
<!--  SEARCH BAR -->
				<li>
					<form class="form-wrapper" action="search" method="post" commnadName="tag">
					    <input type="text" id="search" name="tag" size="21" placeholder="Search for Artist, Concerts ..." maxlength="120" required />
					    <input type="submit" name="submit" value="Search" id="submit">
					    <input type="hidden" name="searchType" value="tag"/>					        
					</form>
					<div class="tfclear"></div>
		        </li>            
            </ul>
        </div>
    </div>
</nav>

<!-- Page Content -->
<div class="container">

    <div class="row">

        <div class="col-lg-12">
            <h1 class="page-header">Your Photo Album</h1>
        </div>

        <%
        	ArrayList<String> urlList = 
        		(ArrayList<String>)request.getAttribute("urlList"); 
        	for(String url : urlList){	
		 %>
        <div class="col-lg-3 col-md-4 col-xs-6 thumb">
            <a class="thumbnail" href="#">
                <img class="img-responsive" src="<%=url%>"
                     alt="">
            </a>
        </div>

        <%
            }
        %>


    </div>

    <hr>


<jsp:include page="common/footer.jsp"/>
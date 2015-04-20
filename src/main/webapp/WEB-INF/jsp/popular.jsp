<jsp:include page="common/header.jsp"/>

<%@ page import="org.jinstagram.Instagram" %>
<%@ page import="org.jinstagram.entity.users.feed.MediaFeed" %>
<%@ page import="org.jinstagram.entity.users.feed.MediaFeedData" %>
<%@ page import="java.util.List" %>

<%@ page import="app.instagram.PhotoRetriever" %>

<%
	PhotoRetriever pr = new PhotoRetriever();
%>

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
            <a class="navbar-brand" href="#">musiConcerts</a>
        </div>
        <div class="collapse navbar-collapse">
            <ul class="nav navbar-nav">
                <li><a href="gallery">Gallery</a></li>
                <li class="active"><a href="popular">Popular</a></li>
                <li><a href="search">Search</a></li>
            </ul>
        </div>
    </div>
    <!-- /.container -->
</nav>

<!-- Page Content -->
<div class="container">

    <div class="row">

        <div class="col-lg-12">
            <h1 class="page-header">Popular Media</h1>
        </div>
        <%

//            MediaFeed mediaFeed = instagram.getPopularMedia();


//            List<MediaFeedData> mediaList = mediaFeed.getData();
        List<MediaFeedData> mediaList = pr.getMediaByTag("likeforlike");

//           int mediaCount = mediaList.size();
			int mediaCount = 0;
			
        %>

        <h3>This page whould search latest concerts media</h3>
        <h3>	Media Count :  <%=mediaCount%></h3>

        <%
            for (MediaFeedData mediaFeedData : mediaList) {

        %>
        <div class="col-lg-3 col-md-4 col-xs-6 thumb">
            <a class="thumbnail" href="#">
                <img class="img-responsive" src="<%=mediaFeedData.getImages().getLowResolution().getImageUrl()%>"
                     alt="">
            </a>
        </div>

        <%
            }
        %>


    </div>

    <hr>


<jsp:include page="common/footer.jsp"/>
<jsp:include page="common/header.jsp"/>

<%@ page import="org.jinstagram.Instagram" %>
<%@ page import="org.jinstagram.entity.tags.TagMediaFeed" %>
<%@ page import="org.jinstagram.entity.users.feed.MediaFeed" %>
<%@ page import="org.jinstagram.entity.users.feed.MediaFeedData" %>
<%@ page import="java.util.List" %>

<%@ page import="app.instagram.PhotoRetriever" %>

<%
	PhotoRetriever pr = new PhotoRetriever();
%>
<!-- tag for search box -->
<meta name="ROBOTS" content="NOINDEX, NOFOLLOW" />
<!-- CSS styles for standard search box -->
<style type="text/css">
	body {
        background: #555 url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAUAAAAFCAYAAACNbyblAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAB9JREFUeNpi/P//PwM6YGLAAuCCmpqacC2MRGsHCDAA+fIHfeQbO8kAAAAASUVORK5CYII=);
		font: 13px 'Lucida sans', Arial, Helvetica;
        color: #eee;
        text-align: center;
    }
    #tfheader{
		background-color:#c3dfef;
	}
	#tfnewsearch{
		float:right;
		padding:10px;
	}
	.tftextinput{
		margin: 0;
		padding: 5px 15px;
		font-family: Arial, Helvetica, sans-serif;
		font-size:14px;
		border:1px solid #0076a3; 
		border-right:0px;
		border-top-left-radius: 5px 5px;
		border-bottom-left-radius: 5px 5px;
	}
	.tfbutton {
		margin: 0;
		padding: 5px 15px;
		font-family: Arial, Helvetica, sans-serif;
		font-size:14px;
		outline: none;
		cursor: pointer;
		text-align: center;
		text-decoration: none;
		color: #ffffff;
		border: solid 1px #0076a3; border-right:0px;
		background: #0095cd;
		background: -webkit-gradient(linear, left top, left bottom, from(#00adee), to(#0078a5));
		background: -moz-linear-gradient(top,  #00adee,  #0078a5);
		border-top-right-radius: 5px 5px;
		border-bottom-right-radius: 5px 5px;
	}
	.tfbutton:hover {
		text-decoration: none;
		background: #007ead;
		background: -webkit-gradient(linear, left top, left bottom, from(#0095cc), to(#00678e));
		background: -moz-linear-gradient(top,  #0095cc,  #00678e);
	}
	/* Fixes submit button height problem in Firefox */
	.tfbutton::-moz-focus-inner {
	  border: 0;
	}
	.tfclear{
		clear:both;
	}
</style>

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
                <li><a href="gallery">Gallery</a></li>
                <li><a href="popular">Popular</a></li>

<!--  SEARCH BAR -->
				<li><a href="#">Tag # </a></li>
				<li>
					<form id="tfnewsearch" action="search" method="post">
					        <input type="text" class="tftextinput" name="tag" size="21" maxlength="120"/>
					        &nbsp;
					        <input type="submit" name="submit" value="Search" class="tfbutton">
					        <input type="hidden" name="searchType" value="tag"/>					        
					</form>
					<div class="tfclear"></div>
		        </li>            
		        
            </ul>
        </div>
    </div>
    <!-- /.container -->
</nav>

<!-- Page Content -->
<div class="container">

    <div class="row">

        <div class="col-lg-12">
            <h1 class="page-header">Search</h1>
        </div>
<!-- 
        <p>

        <form action="search" method="post">
            Tag # <input type="text" name="tag"/> &nbsp; <input type="submit" name="submit" value="Submit">

            <input type="hidden" name="searchType" value="tag"/>

        </form>
        </p>
 -->

        <%
            List<MediaFeedData> mediaList = null;
            String errMessage = null;
            int mediaCount = 0;
            TagMediaFeed tagMediaFeed = null;
            if (request.getParameter("submit") != null) {
                if (request.getParameter("searchType").equals("tag")) {
                    String tag = request.getParameter("tag");
                    if (tag != null || tag.trim().length() != 0) {
                        try {
                            mediaList = pr.getMediaByTag(tag,4);
//                             tagMediaFeed = instagram.getRecentMediaTags(tag);
//                             mediaList = tagMediaFeed.getData();
//                             MediaFeed recentMediaNextPage = instagram.getRecentMediaNextPage(tagMediaFeed.getPagination());
//                             int counter = 0;
//                             while (recentMediaNextPage.getPagination() != null && counter < Constants.MAX_PAGE_SIZE) {
//                                 mediaList.addAll(recentMediaNextPage.getData());
//                                 recentMediaNextPage = instagram.getRecentMediaNextPage(recentMediaNextPage.getPagination());
//                                 counter++;
//                             }
                            mediaCount = mediaList.size();
                        } catch (Exception ex) {
                            errMessage = ex.getMessage();
                        }
                    }
                }
            }
        %>

        <% if (errMessage != null) { %>
        <div class="alert alert-danger" role="alert">
            <%= errMessage %>
        </div>
        <% } %>


        <%
            if (mediaList != null) {
        %>
        <h3>Media Count :  <%=mediaCount%>
        </h3>

        <%
            for (MediaFeedData mediaFeedData : mediaList) {
        %>
        <div class="col-lg-3 col-md-4 col-xs-6 thumb">
            <a class="thumbnail" href="#">
                <img class="img-responsive" src="<%=mediaFeedData.getImages().getLowResolution().getImageUrl()%>"
                     alt="">
            </a>
        </div>

        <% }
        }
        %>


    </div>
    
    <hr>


<jsp:include page="common/footer.jsp"/>
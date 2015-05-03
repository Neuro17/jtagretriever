<jsp:include page="common/header.jsp"/>

<%@ page import="org.jinstagram.Instagram" %>
<%@ page import="org.jinstagram.entity.users.feed.MediaFeedData" %>
<%@ page import="java.util.List" %>

<%@ page import="app.instagram.PhotoRetriever" %>

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
	    
		.form-wrapper {
		    width: 450px;
		    padding: 3px;
		    margin: 10px auto;
		    overflow: hidden;
		    border-width: 1px;
		    border-style: solid;
		    border-color: #dedede #bababa #aaa #bababa;
		    box-shadow: 0 3px 3px rgba(255,255,255,.1), 0 3px 0 #bbb, 0 4px 0 #aaa, 0 5px 5px #444;
		    border-radius: 10px;    
		    background-color: #f6f6f6;
		    background-image: linear-gradient(top, #f6f6f6, #eae8e8);
		}
	
		.form-wrapper #search {
		    width: 330px;
		    height: 20px;
		    padding: 10px 5px;
		    float: left;    
		    font: bold 16px 'lucida sans', 'trebuchet MS', 'Tahoma';
		    border: 1px solid #ccc;
		    box-shadow: 0 1px 1px #ddd inset, 0 1px 0 #fff;
		    border-radius: 3px;      
		}
		
		.form-wrapper #search:focus {
		    outline: 0; 
		    border-color: #aaa;
		    box-shadow: 0 1px 1px #bbb inset;  
		}
		
		.form-wrapper #search::-webkit-input-placeholder {
		   color: #000;
		   font-weight: normal;
		}
		
		.form-wrapper #search:-moz-placeholder {
		    color: #000;
		    font-weight: normal;
		}
		
		.form-wrapper #search:-ms-input-placeholder {
		    color: #000;
		    font-weight: normal;
		} 
		
		.form-wrapper #submit {
		    float: right;    
		    border: 1px solid #00748f;
		    height: 22px;
		    width: 100px;
		    padding: 0;
		    cursor: pointer;
		    font: bold 15px Arial, Helvetica;
		    color: #fafafa;
		    text-transform: uppercase;    
		    background-color: #0483a0;
		    background-image: linear-gradient(top, #31b2c3, #0483a0);
		    -moz-border-radius: 3px;
		    -webkit-border-radius: 3px;
		    border-radius: 3px;      
		    text-shadow: 0 1px 0 rgba(0, 0 ,0, .3);
		    box-shadow: 0 1px 0 rgba(255, 255, 255, 0.3) inset, 0 1px 0 #fff;
		}
		  
		.form-wrapper #submit:hover,
		.form-wrapper #submit:focus {       
		    background-color: #31b2c3;
		    background-image: linear-gradient(top, #0483a0, #31b2c3);
		}   
		  
		.form-wrapper #submit:active {
		    outline: 0;    
		    box-shadow: 0 1px 4px rgba(0, 0, 0, 0.5) inset;    
		}
		  
		.form-wrapper #submit::-moz-focus-inner {
		    border: 0;
		}
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
				<li><a></a></li>
                <li><a></a></li>
                <li><a></a></li>
                <li class="active"><a href="gallery">YourGalleries</a></li>
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
            <h1 class="page-header">YourGalleries should remember latest searches</h1>
        </div>
        <%
        	PhotoRetriever pr = new PhotoRetriever();
        
            List<MediaFeedData> mediaList = pr.getMediaByTag("red");

        %>

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

        <%
            }
        %>


    </div>

    <hr>


<jsp:include page="common/footer.jsp"/>
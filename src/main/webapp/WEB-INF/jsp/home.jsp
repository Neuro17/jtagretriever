<jsp:include page="common/header.jsp"/>
<jsp:include page="common/stylesheet.jsp"/>

<html>
<head>
    <title>musiConcerts</title>

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
                <li><a></a></li>
                <li><a></a></li>
                <li><a></a></li>
                <li><a href="gallery">YourGalleries</a></li>
                <li><a href="popular">Popular</a></li>
            </ul>
        </div>
    </div>
</nav>

<body>

	<form  class="form-wrapper" action="search" method="post" commnadName="tag">
	    <input type="text" id="search" name="tag" size="21" placeholder="Search for Artist, Concerts ..." maxlength="120" required />
<!-- 	&nbsp; 	-->
	    <input type="submit" name="submit" value="Search" id="submit">
	    <input type="hidden" name="searchType" value="tag"/>					        
	</form>

</body>
</html>
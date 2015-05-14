<jsp:include page="common/header.jsp"/>

<html>
<head>
    <title>musiConcerts</title>

</head>

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
	
    <div class="col-lg-8 col-lg-offset-2" id="search-bar">
        <form action="search" method="post" commnadName="tag">
        	<div class="input-group input-group-lg"  id="search-input">
            <input type="text" name="tag" class="form-control" placeholder="Search your artist .... enjoy the magic" required /> 
            <span class="input-group-btn">
                <button class="btn btn-default" name="submit" value="Search" id="submit" type="submit">Go!</button>
            </span>
        </div>
        </form>
        
    </div>

<!-- 	<form  class="form-wrapper" action="search" method="post" commnadName="tag"> -->
<!-- 	    <input type="text" id="search" name="tag" size="21" placeholder="Search for Artist, Concerts ..." maxlength="120" required /> -->
<!-- <!-- 	&nbsp; 	-->
<!-- 	    <input type="submit" name="submit" value="Search" id="submit"> -->
<!-- 	    <input type="hidden" name="searchType" value="tag"/>					         -->
<!-- 	</form> -->

</body>
</html>
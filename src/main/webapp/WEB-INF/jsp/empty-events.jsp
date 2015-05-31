<jsp:include page="common/header.jsp"/>

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
                <li><a href="gallery">Your Galleries</a></li>
                <li><a href="popular">Popular</a></li>
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


        <div class="col-lg-12">
            <h1 class="page-header">No events found for your artist</h1>
        </div>

    <hr>


<jsp:include page="common/footer.jsp"/>
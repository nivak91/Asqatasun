<%@ taglib uri="http://htmlcompressor.googlecode.com/taglib/compressor" prefix="compress" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://tagutils" prefix="tg" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<!-- external js-->
<c:set var="jqueryUrl">
    <c:url value="/External-Js/jquery-1.9.1.min.js"/>
</c:set>
<c:set var="d3JsUrl" scope="request">
    <c:url value="/External-Js/d3.v3.min.js"/>
</c:set>
<c:set var="r2d3JsUrl" scope="request">
    <c:url value="/External-Js/r2d3.v2.min.js"/>
</c:set>

<!-- internal js-->
<c:set var="WAQMscore" scope="page">
    <c:url value="/Js/score/WAQMscore-min.js"/>
</c:set>
<c:set var="WAQMpercentileRank" scope="page">
    <c:url value="/Js/score/WAQMpercentileRank-min.js"/>
</c:set>
<c:set var="EnhancedWAQMscore" scope="page">
    <c:url value="/Js/score/EnhancedWAQMscore-min.js"/>
</c:set>
<c:set var="EnhancedWAQMpercentileRank" scope="page">
    <c:url value="/Js/score/EnhancedWAQMpercentileRank-min.js"/>
</c:set>
<c:set var="metricsDonuts" scope="page">
    <c:url value="/Js/result-page/MetricsDonuts-min.js"/>
</c:set>


<html lang="${tg:lang(pageContext)}">
<%@include file="template/head.jsp" %>
<body id="my_page">
	<%@include file="template/header-utils.jsp" %>
	<div class="container">
	    <div class="row">
	      <div class = "span16">
	         <h1>Accessibility Metrics Results:</div>
	      </div>
	    </div>
	    <div id="WAQMResult" class="row">

            <div id="WAQMscore" class="span3">
            <div>${wqam}<span class="percent">%</span></div>
            </div>
            <div id="WAQMpercentileRank" class="span3">
            <div>${WQAMPercentileRank}<span class="percent">%</span></div>
            </div>
	    </div> <!-- class="row" -->
	    <div id="EnhancedWAQMResult" class="row">

            <div id="EnhancedWAQMscore" class="span3">
            <div>${enhanced_wqam}<span class="percent">%</span></div>
            </div>
            <div id="EnhancedWAQMpercentileRank" class="span3">
            <div>${enhancedWQAMPercentileRank}<span class="percent">%</span></div>
            </div>
        </div> <!-- class="row" -->
    </div><!-- class="container"-->
    <%@include file="template/footer.jsp" %>
    <script type="text/javascript" src="${jqueryUrl}"></script>

    <!--[if !IE]><!-->
    <script type="text/javascript" src="${d3JsUrl}"></script>
    <script type="text/javascript" src="${WAQMscore}"></script>
    <script type="text/javascript" src="${WAQMpercentileRank}"></script>
    <script type="text/javascript" src="${EnhancedWAQMscore}"></script>
    <script type="text/javascript" src="${EnhancedWAQMpercentileRank}"></script>
    <script type="text/javascript" src="${metricsDonuts}"></script>
</body>
</html>
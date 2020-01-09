<%@ page isErrorPage="true" contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<jsp:include page="fragments/headTag.jsp"/>
<body>
<jsp:include page="fragments/bodyHeader.jsp"/>

<div class="jumbotron">
    <div class="container text-center">
        <br>
        <h4 class="my-3">${param.status}</h4>
        <h2><spring:message code="common.appError"/></h2>
        <h4 class="my-5">${param.message}</h4>
    </div>
</div>

<c:forEach items="${param.exception.stackTrace}" var="stackTrace">
    ${stackTrace}
</c:forEach>

<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="propertiesBean" scope="request" type="jetbrains.buildServer.controllers.BasePropertiesBean"/>

<div class="parameter">
  Gerrit: <strong><props:displayValue name="gerritHost" /></strong> -p <strong><props:displayValue name="gerritPort"/></strong>
</div>

<div class="parameter">

</div>
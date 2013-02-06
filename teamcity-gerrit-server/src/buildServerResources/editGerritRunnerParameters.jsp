<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props" %>
<%@ taglib prefix="l" tagdir="/WEB-INF/tags/layout" %>
<%@ taglib prefix="bs" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="forms" tagdir="/WEB-INF/tags/forms" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="propertiesBean" scope="request" type="jetbrains.buildServer.controllers.BasePropertiesBean"/>

<l:settingsGroup title="Gerrit Verification Runner Parameters">
<tr>
    <th>
        <label for="gerritHost">Gerrit SSH Path: </label>
    </th>
    <td>
        <props:textProperty name="gerritHost" />
        <span class="error" id="error_gerritHost"></span>
        <span class="smallNote">Path to Gerrit including username (e.g harry.potter@gerrit.hogwarts.com)</span>
    </td>
</tr>
<tr>
    <th>
        <label for="gerritPort">Gerrit SSH Port: </label>
    </th>
    <td>
        <props:textProperty name="gerritPort" />
        <span class="error" id="error_gerritPort"></span>
        <span class="smallNote">Port for Gerrit SSH connector.</span>
    </td>
</tr>
</l:settingsGroup>
<html>
<head>
    <style>
    table, th, td {
        border: 1px solid black;
        border-collapse: collapse;
    }
    </style>
    <script src="http://ajax.aspnetcdn.com/ajax/jQuery/jquery-1.12.2.min.js">
    </script>
    <script>
        function download(filePath) {
            $.ajax
            ({
                url: 'download',
                data: "fileName=" + filePath,
                type: 'post',
                success: function () {
                    alert("file is Downloaded")
                }
            });
        }

        function deleteFile(filePath) {
            $.ajax
            ({
                url: 'delete',
                data: "fileName=" + filePath,
                type: 'post',
                success: function () {

                    window.location.href = "http://localhost:8080/azure/list"
                    alert('file is deleted')
                }
            });
        }
    </script>

</head>

<body style="background-color: lightskyblue">
<h3>Upload File</h3>
<table>
    <tr><td>
        <g:form enctype="multipart/form-data" action="upload" controller="azure">
            <input type="file" name="filePath"/>
            <input type="submit" value="Upload"/>
        </g:form>
    </td>

    </tr>
</table>
<br>
<h3>List ,Download and Delete the file</h3>
<br>
<table>

    <g:each in="${uriList}" var="filePath">
        <tr>
            <td>${filePath}</td>
            <td><button type="button" onclick="download(this.value)" value="${filePath}">Download</button></td>
            <td><button type="button" onclick="deleteFile(this.value)" value="${filePath}">Delete</button></td>
        </tr>

    </g:each>

</table>
</body>
</html>
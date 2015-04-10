function getContent(responseData) {
	if (responseData != "null") {
		responseData = responseData.substring(1, responseData.length - 1);
		if (responseData != "") {
			var name = responseData.split(",");
			var fileName = "", count = "";
			var table = document.getElementById('myTable').getElementsByTagName('tbody')[0];
			//table.find("tr").remove();
			for (var i = 0; i < name.length; i++) {
				var line = name[i];
				var row = table.insertRow(table.rows.length);

				var cell1 = row.insertCell(0);
				var cell2 = row.insertCell(1);
				var cell3 = row.insertCell(2);

				fileName = line.substring(line.lastIndexOf("|:|") + 3, line.lastIndexOf("||:"));
				count = line.substring(line.lastIndexOf("||:") + 3);
				line = line.substring(0, line.lastIndexOf("|:|"));
				if (line.length > 100) {
					line = line.substring(0, 99) + "...";
				}

				cell1.innerHTML = fileName;
				cell2.innerHTML = line;
				cell3.innerHTML = count;
			}
		}
	}
}

function getResult() {
	var directoryName = $('*[name="directory"]').val();
	var queryVal = $('*[name="query"]').val();

	if (directoryName == "") {
		$(".noDirectory").show();
	}
	if (queryVal == "") {
		$(".noQuery").show();
	}

	if (directoryName != "" && queryVal != "") {
		var xmlhttp = new XMLHttpRequest();
		$(".noDirectory").hide();
		$(".noQuery").hide();
		$('#tableData').hide();
		$('#loader').show();
		rotateLoader();

		xmlhttp.open("GET", "/WordCountBySpark/Result?dir=" + directoryName + "&query=" + queryVal, true);

		xmlhttp.onreadystatechange = function() {
			if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
				requestHandler(xmlhttp);
				$('#loader').hide();
				$('#tableData').show();
				window.history.replaceState({},"",'counts');
			}
		};
		xmlhttp.send();
	}
}

function requestHandler(request) {
	var responseData = request.responseText;
	getContent(responseData);
}

function rotateLoader() {
	$('.small, .small-shadow').velocity({
		rotateZ : [ 0, -360 ]
	}, {
		loop : true,
		duration : 2000
	});
	$('.medium, .medium-shadow').velocity({
		rotateZ : -240
	}, {
		loop : true,
		duration : 2000
	});
	$('.large, .large-shadow').velocity({
		rotateZ : 180
	}, {
		loop : true,
		duration : 2000
	});
}

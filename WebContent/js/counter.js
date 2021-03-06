var directoryName = null;
var queryVal = null;

window.onload = function() {
	$(document).ready(function() {
		$("body").keypress(function(event) {
			if (event.which == 13) {
				getResult();
			}
		});
		$('img').click(function() {
			$(".noDirectory").hide();
			$(".noQuery").hide();
			$('#tableData').hide();
			$("input").show();
			$("button").show();
			$('input').val('');
			window.history.replaceState({}, "", '/WordCountBySpark');
		});

	})
};

function getContent(responseData) {
	if (responseData != "null") {
		responseData = responseData.substring(1, responseData.length - 1);
		if (responseData != "") {
			var name = responseData.split(",");
			var fileName = "", count = "";
			var table = document.getElementById('myTable').getElementsByTagName('tbody')[0];

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

				var query = new RegExp("(\\b" + queryVal + "\\b)", "gim");
				var enew = line.replace(/(<span class='highlight'>|<\/span>)/igm, "");
				line = enew.replace(query, "<span class='highlight'>$1</span>");

				cell1.innerHTML = fileName;
				cell2.innerHTML = line;
				cell3.innerHTML = count;
			}

			$('#myTable').dataTable();
		}
	}
	$('input').val('');
}

function getResult() {
	$(".noDirectory").hide();
	$(".noQuery").hide();
	directoryName = $('*[name="directory"]').val();
	queryVal = $('*[name="query"]').val();

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
				$("input").hide();
				$("button").hide();
				window.history.replaceState({}, "", 'counts');
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

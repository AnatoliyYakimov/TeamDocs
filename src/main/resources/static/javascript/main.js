var address = document.querySelector('#context-path').textContent;

var myHeaders = new Headers();

myHeaders.append("content-type", "application/json");

var doc = {
	name: "Document",
	text: "Sometext"
}

console.log(doc);

console.log(JSON.stringify(doc));

function postDocument(url = "", doc = {}) {
	var headers = new Headers();
	headers.append("content-type", "application/json");

	var init = {
		method: 'POST',
		headers: myHeaders,
		body: JSON.stringify(doc)
	}
	return fetch(address + "documents/", init)
		.then(
			function (response) {
				return response;
			});
}

function getDocument(url = "") {
	var headers = new Headers();
	headers.append("content-type", "application/json");

	var init = {
		method: 'GET',
		headers: myHeaders,
	}

	return fetch(url, init).then(
	function (response) {
		return response.json();
		}).then(
			function (json) {
				return json;
			}).catch(
			function (e) {
				console.log(e);
			})
}

function getDocumentById(id){
	return getDocument(address + "/documents/" + id);
}

function checkForUpdates(document)

var currentDocument = null;
var editor = null;

updateEditor();

function updateEditor(text = "Enter text here") {
	//editor ? editor.editor.remove() : 0;
	editor = editormd("test-editor", {
		width  : "90%",
		height : "300px",
		markdown: text,
		path: "javascript/lib/"
	});
}

function newDocument(){
	updateEditor();
	document.querySelector('#document-name').value = "New Document";
	currentDocument = null;
}

function save() {
	var text = document.querySelector('#text-editor').textContent;
	var name = document.querySelector('#document-name').value;
	if (currentDocument == null) {
		var doc = {
			name: name,
			text: text
		}
		postDocument(doc).then(function (result) {
			currentDocument = result;
			console.log(currentDocument);
		});
	}
	else {
		currentDocument.name = name;
		currentDocument.text = text;
		console.log("Current doc:");
		console.log(currentDocument);
		putDocument(currentDocument);
	}
}


function load() {
	var id = document.querySelector('#document-id').value;
	getDocument(id).then(function (result) {
		console.log(result);
		currentDocument = result;
		updateEditor(currentDocument.text);
	});
}

function putDocument(doc = {}){
	var headers = new Headers();
	headers.append("content-type", "application/json");
	headers.append("connection", "keep-alive");

	var init = {
		method: 'PUT',
		headers: headers,
		body: JSON.stringify(doc)
	}

	return fetch("documents/" + doc.id, init)
		.then(
			function (response) {
				return response.json();
			}).then(function (json) {
				console.log(json);
				return json;
			});
}
function postDocument(doc = {}) {
	var headers = new Headers();
	headers.append("content-type", "application/json");
	headers.append("connection", "keep-alive");

	var init = {
		method: 'POST',
		headers: headers,
		body: JSON.stringify(doc)
	}
	return fetch("documents/", init)
		.then(
			function (response) {
				return response.json();
			}).then(function (json) {
				console.log(json);
				return json;
			});
}

function getDocument(id) {
	var headers = new Headers();
	headers.append("content-type", "application/json");
	headers.append("connection", "keep-alive");

	var init = {
		method: 'GET',
		headers: headers,
	}

	return fetch("documents/" + id, init).then(
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

function getDocumentById(id) {
	return getDocument(address + "/documents/" + id);
}


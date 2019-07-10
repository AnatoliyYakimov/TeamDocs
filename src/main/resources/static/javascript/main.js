"use strict"

class StompClient {
  constructor(url) {
    this.socket = new SockJS(url);
    this.client = Stomp.over(this.socket);
    this.subscription = null;
    this.connected = false;
  }
  connect(login, password, onconnect) {
    this.client.connect(login, password,
      () => {
        this.connected = true;
        console.log("Stomp connected");
        onconnect();
      });

  }

  disconnect(ondisconnect) {
    client.disconnect(() => {
      this.connected = false;
      console.log("Stomp disconnected");
      ondisconnect;
    });
  }

  subscribe(url, onmessage) {
    console.log("subscribed to " + url);
    this.unsub();
    this.subscription = this.client.subscribe(url, message => onmessage(message));
  }

  unsub() {
    if (this.subscribed()) {
      this.subscription.unsubscribe();
      this.subscription = null;
    }
  }

  subscribed() {
    return this.subscription != null;
  }
}

class EditorController {
  constructor() {
    this.editor = null;
  }
  update(text = "Enter text here") {
    editor = editormd("test-editor", {
      width: "90%",
      height: "300px",
      markdown: text,
      path: "javascript/lib/"
    });
  }
}

class ViewController {
  constructor(editor) {
    this.hashField = document.querySelector('#document-id');
    this.textField = document.querySelector('#text-editor');
    this.nameField = document.querySelector('#document-name');
    this.editor = editor;
    this.editor.update();
  }

  getHash() {
    return this.hashField.value;
  }

  getText() {
    return this.textField.textContent;
  }

  getName() {
    return this.nameField.value;
  }

  showDocument(document) {
    this.editor.update(document.text);
    this.nameField.value = document.name;
  }

}

class Controller {
  constructor(view, repository, stomp) {
    this.view = view;
    this.repository = repository;
    this.stomp = stomp;
    this.activeDocument = null;
    stomp.connect('guest', 'guest', () => "Conncted!");
  }


  newDocument() {
    this.stomp.unsub();
    this.activeDocument = {
      name: "New Document",
      text: "Enter text here"
    };
    this.view.showDocument(this.activeDocument);
  }

  load() {
    var hash = this.view.getHash();
    var self = this;
    this.repository.getLastVersionOfDocument(hash).then(function (result) {
      console.log(result);
      self.activeDocument = result;
      self.view.showDocument(self.activeDocument);
      self.subscribeToActive();
    });
  }
  save() {
    this.activeDocument.name = this.view.getName();
    this.activeDocument.text = this.view.getText();
    var self = this;
    this.repository.postDocument(this.activeDocument)
      .then(doc => {
        self.activeDocument = doc;
        self.subscribeToActive();
      });
  }

  subscribeToActive() {
    if (!this.stomp.subscribed()) {
      this.stomp.subscribe("/topic/document." + this.activeDocument.hash, this.getOnMessageFunction());
    }
  }

  getOnMessageFunction() {
    var self = this;
    return (message) => {
      console.log("Recieved:");
      console.log(message);
      self.view.showDocument(JSON.parse(message.body));
    }
  }
}

class DocumentRepository {
  constructor() {

  }

  putDocument(doc) {


    var init = {
      method: 'PUT',
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
  postDocument(doc) {
    console.log("Document to post: ");
    console.log(doc);

    var headers = {
      'Athorization': bearer,
      "content-type": "application/json"
    }

    console.log(headers);

    var init = {
      method: 'POST',
      headers: headers,
      body: JSON.stringify(doc)
    }

    console.log(init);
    return fetch("documents/", init)
      .then(
        function (response) {
          return response.json();
        }).then(function (json) {
          console.log(json);
          return json;
        });
  }

  getLastVersionOfDocument(hash) {
    var headers = new Headers();
    headers.append("Athorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTU2MzYyMjQ4N30.93JX03JmYCnsLb2gIZ-85ZBBHQuFeSg59f8GnP4i2MYfiFFfkXmWTdT_nuyhnaIOinhTiiN0ypIahL-xiczBtQ"); headers.append("content-type", "application/json");
    headers.append("connection", "keep-alive");
    var init = {
      method: 'GET',
      headers: headers,
    }

    return fetch("documents/last/" + hash, init).then(
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

}
var editor;
var view;
var repo;
var stomp;
var controller;
var bearer;
var adminToken = {
    "username": 'admin',
    "password": 'admin'
  };
  console.log(JSON.stringify(adminToken));
fetch("login/", {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json'
  },
  body: JSON.stringify(adminToken)
}).then((resp) => {
  bearer = resp.headers['Authentication'];
  editor = new EditorController();
  view = new ViewController(editor);
  repo = new DocumentRepository();
  stomp = new StompClient('/teamdocs');
  controller = new Controller(view, repo, stomp);
});



/**
 * Function to get all the users!
 */
function getAllUsers() {
  console.log("Getting all the users.");

  get("/api/users", function (returned_json) {
    document.getElementById('jsonDump').innerHTML = returned_json;
  });
}

function getAllUsersByAge() {
  console.log("Getting all the users.");

  get("/api/users?age=" + document.getElementById("age").value, function (returned_json) {
    document.getElementById('jsonDump').innerHTML = returned_json;
  });
}



// gets users from the api.
// It adds the values of the various inputs to the requested URl to filter and order the returned users.
function getFilteredUsers() {
  console.log("Getting all the users.");

  var url = "/api/users?";
  if(document.getElementById("age").value != "") {
    url = url + "&age=" + document.getElementById("age").value;
  }
  if(document.getElementById("company").value != "") {
    url = url + "&company=" + document.getElementById("company").value;
  }


  get(url, function(returned_json){
    document.getElementById('jsonDump').innerHTML = syntaxHighlight(JSON.stringify(JSON.parse(returned_json), null, 2));
  });
}

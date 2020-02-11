/**
 * Utility function to make generating http requests easier.
 * Sends a GET request to the URL described by 'aUrl' with
 * our 'aCallback' function to be executed when the server
 * sends a response.
 *
 * Based on: http://stackoverflow.com/a/22076667
 */
function get(aUrl, aCallback) {
  var anHttpRequest = new XMLHttpRequest();

  // Set a callback to be called when the ready state of our request changes.
  anHttpRequest.onreadystatechange = function () {
    /**
     * Only call our 'aCallback' function if the ready state is 'DONE' and
     * the request status is 200 ('OK')
     *
     * See https://httpstatuses.com/ for HTTP status codes
     * See https://developer.mozilla.org/en-US/docs/Web/API/XMLHttpRequest/readyState
     *  for XMLHttpRequest ready state documentation.
     *
     */
    if (anHttpRequest.readyState === 4 && anHttpRequest.status === 200)
      aCallback(anHttpRequest.responseText);
  };

  anHttpRequest.open("GET", aUrl, true);
  anHttpRequest.send(null);
}

// Syntax highlighting for JSON
// from https://stackoverflow.com/questions/4810841/how-can-i-pretty-print-json-using-javascript
function syntaxHighlight(json) {
  json = json.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;');
  return json.replace(/("(\\u[a-zA-Z0-9]{4}|\\[^u]|[^\\"])*"(\s*:)?|\b(true|false|null)\b|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?)/g, function (match) {
    var cls = 'number';
    if (/^"/.test(match)) {
      if (/:$/.test(match)) {
        cls = 'key';
      } else {
        cls = 'string';
      }
    } else if (/true|false/.test(match)) {
      cls = 'boolean';
    } else if (/null/.test(match)) {
      cls = 'null';
    }
    return '<span class="' + cls + '">' + match + '</span>';
  });
}

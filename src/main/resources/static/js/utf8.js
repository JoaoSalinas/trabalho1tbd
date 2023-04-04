function formatter(form) {
  let object = {};
  let formData = new FormData(form);
  for (let key of formData.keys()) {
    object[key] = formData.get(key);
  }
  return object;
}

window.addEventListener("load", function () {    
  if (document.querySelector("form") != null) {
    document.querySelectorAll("form").forEach(function (form) {
      
      form.addEventListener("submit", function (event) {
        console.log(form.action)
        event.preventDefault();
        
        fetch(`${form.action}`, {
          method: "POST",
          body: JSON.stringify(formatter(event.target)),
          headers: {
          "Content-type": "application/json; charset=UTF-8",
        },
      }).then(function (response) {
        if (response.ok) {            
          window.location.reload()     
        }
      }).catch(function error() {
          console.log("error" + error)          
        }) 
        
      })
    });
  }
});

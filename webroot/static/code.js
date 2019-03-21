const listContainer = document.querySelector('#service-list');
let servicesRequest = new Request('/service');
fetch(servicesRequest)
.then(function(response) { return response.json(); })
.then(function(serviceList) {
  serviceList.forEach(service => {
    var li = document.createElement("li");
    li.appendChild(document.createTextNode("URL:"+service.url + ' - NAME: ' + service.name + ' - STATUS: '+ service.name + ' - CREATED: ' + service.creationDate));
    listContainer.appendChild(li);
  });
});

const saveButton = document.querySelector('#post-service');
saveButton.onclick = evt => {
    let urlName = document.querySelector('#url-name').value;
    let serviceName = document.querySelector('#service-name').value;
    fetch('/service', {
    method: 'post',
    headers: {
    'Accept': 'application/json, text/plain, */*',
    'Content-Type': 'application/json'
    },
  body: JSON.stringify({url:urlName,
                        name:serviceName,
                        deleteService:false})
}).then(res=> location.reload());
}

const deleteButton = document.querySelector('#delete-service');
deleteButton.onclick = evt => {
    let urlName = document.querySelector('#url-name').value;
    let serviceName = document.querySelector('#service-name').value;
    fetch('/service', {
    method: 'post',
    headers: {
    'Accept': 'application/json, text/plain, */*',
    'Content-Type': 'application/json'
    },
  body: JSON.stringify({url:urlName,
                        name:serviceName,
                        deleteService:true})
}).then(res=> location.reload());
}
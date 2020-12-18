const client = filestack.init(stackKey);

const options = {
    onUploadDone: updateForm,
    maxSize: 10 * 1024 * 1024,
    accept: 'image/*',
    uploadInBackground: false

};
const picker = client.picker(options);

// Get references to the DOM elements

const fileInput = document.getElementById('fileupload');
const filePicker = document.getElementById('picker');


// Add our event listeners

filePicker.addEventListener('click', function (e) {
    e.preventDefault();
    picker.open();
});

// Helper to overwrite the field input value

function updateForm (result) {
    const fileData = result.filesUploaded[0];
    fileInput.value = fileData.url;
    document.querySelector("fileupload").value = fileData.url;

    // Some ugly DOM code to show some data.
    // const name = document.createTextNode('Selected: ' + fileData.filename);
    // const url = document.createElement('a');
    // url.href = fileData.url;
    // url.appendChild(document.createTextNode(fileData.url));
    // nameBox.appendChild(name);
    // urlBox.appendChild(document.createTextNode('Uploaded to: '));
    // urlBox.appendChild(url);
};
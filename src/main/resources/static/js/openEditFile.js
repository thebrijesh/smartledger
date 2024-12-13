// Should log the Axios object

function handleEdit(button) {
    console.log(button);
    // Get values from data attributes
    const amount = button.getAttribute('data-amount');
    console.log(amount);
    const description = button.getAttribute('data-description');
    console.log(description);
    const date = button.getAttribute('data-transactiondate');
    const party = button.getAttribute('data-party');
    const transactionType = button.getAttribute('data-transactionType');
    const id = button.getAttribute('data-id');
    console.log(date);


    // Add the specified number of days to the current date





    // Set values in the form
    document.getElementById('amount').value = amount;
    document.getElementById('message').value = description;
    document.getElementById('editTransactionType').value = transactionType;
    document.getElementById('partyId').value = party;
    document.getElementById('id').value = id;
    console.log(document.getElementById('id').value);

    // Convert the date string to a Date object
    const dateObj = new Date(date);

// Format the date as YYYY-MM-DD (for compatibility with HTML date input)
    const year = dateObj.getFullYear();
    const month = String(dateObj.getMonth() + 1).padStart(2, '0'); // Months are 0-based
    const day = String(dateObj.getDate()).padStart(2, '0');
    document.getElementById('datepicker-format').value = `${day}-${month}-${year}`;

}

function setDueDate(days) {

    const currentDate = new Date();

    // Add the specified number of days to the current date
    currentDate.setDate(currentDate.getDate() + days);

    // Format the date as YYYY-MM-DD (for compatibility with HTML date input)
    const year = currentDate.getFullYear();
    const month = String(currentDate.getMonth() + 1).padStart(2, '0'); // Months are 0-based
    const day = String(currentDate.getDate()).padStart(2, '0');

    // Return the formatted date
    callApi(`${day}-${month}-${year}`);
}

function callApi(selectedDate) {
    // Get the partyId from the data attribute
    const partyId = document.getElementById('partyContainer').getAttribute('data-partyId');
console.log(partyId);
    // Check if selectedDate is valid
    if (selectedDate) {
        // Make the API call using fetch
        fetch('/users/party/set-due-date', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',  // Specify the content type as JSON
            },
            body: JSON.stringify({
                date: selectedDate,  // Send the selected date
                partyId: partyId     // Send the partyId
            }),
        })
            .then(response => response.json())  // Parse the response as JSON
            .then(data => alert(data))
            .then(window.location.reload())// Handle success and alert the response data
            .catch(error => console.error('Error:', error));


    } else {
        alert("Please select a valid future date.");
    }
}


// Function to update the due date text after selecting a date
function updateDueDate() {
    const selectedDate = document.getElementById('datepicker-autohide').value;
    console.log(selectedDate);
    // if (selectedDate) {
    //     // Update the due date text
    //     document.getElementById('dueDateText').textContent = selectedDate;
    // }
    // Hide the date picker after setting the date
    callApi(selectedDate);
}

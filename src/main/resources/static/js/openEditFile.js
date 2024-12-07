function handleEdit(button) {
    console.log( button);
    const id = button.getAttribute('data-id');
    // Get values from data attributes
    const amount = button.getAttribute('data-amount');
    console.log(amount);
    const description = button.getAttribute('data-description');
    console.log(description);
    const date = button.getAttribute('data-transactiondate');

    // Set values in the form
    document.getElementById('amount').value = amount;
    document.getElementById('message').value = description;
    document.getElementById('datepicker-format').value = date;
    document.getElementById('transactionType').value = transactionType;
    document.getElementById('id').value = partyId;

}




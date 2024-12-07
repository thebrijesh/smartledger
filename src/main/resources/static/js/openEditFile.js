function handleEdit(button) {
    console.log( button);
    // Get values from data attributes
    const amount = button.getAttribute('data-amount');
    console.log(amount);
    const description = button.getAttribute('data-description');
    console.log(description);
    const date = button.getAttribute('data-transactiondate');
    const party = button.getAttribute('data-party');
    const transactionType = button.getAttribute('data-transactionType');
    const id = button.getAttribute('data-id');

    // Set values in the form
    document.getElementById('amount').value = amount;
    document.getElementById('message').value = description;
    document.getElementById('datepicker-format').value = date;
    document.getElementById('editTransactionType').value = transactionType;
    document.getElementById('partyId').value =  party;
    document.getElementById('id').value =  id;
}





document.addEventListener('DOMContentLoaded', function () {
    const amount = document.getElementById('amount');
    const submitBtn = document.getElementById('submitBtn');
    const amountError = document.getElementById('transactionAmountError');

    // Helper function to validate the form
    function validateForm() {
        let isValid = true;
        console.log('Validating form');


        // Validate Amount
        const value = amount.value.trim();
        if (value && parseFloat(value) < 0) {
            amountError.classList.remove('hidden');
            isValid = false;
        } else {
            amountError.classList.add('hidden');
        }

        // Enable or disable the submit button
        submitBtn.disabled = !isValid;
        submitBtn.classList.toggle('bg-blue-700', isValid);
        submitBtn.classList.toggle('hover:bg-blue-800', isValid);
        submitBtn.classList.toggle('bg-gray-400', !isValid);
        submitBtn.classList.toggle('cursor-not-allowed', !isValid);
    }

    // Attach event listeners to inputs
    amount.addEventListener('input', validateForm);
    amount.addEventListener('change', validateForm);
});

function setTransactionType(transactionType) {
    document.getElementById('transactionType').value = transactionType;
}



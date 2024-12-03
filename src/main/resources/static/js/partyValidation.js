

const button = document.getElementById('partymainlayout');
const variableValue = button.getAttribute('data-variable');

function loadParty(partyType){
    console.log(partyType);
}
console.log(variableValue);
document.addEventListener('DOMContentLoaded', function () {
    const phoneNumber = document.getElementById('phone-number');
    const openingBalance = document.getElementById('opening-balance');
    const submitBtn = document.getElementById('submitBtn');
    const mobileNumberError = document.getElementById('mobileNumberError');
    const amountError = document.getElementById('amountError');

    // Helper function to validate the form
    function validateForm() {
        let isValid = true;

        // Validate Phone Number
        const phoneValue = phoneNumber.value.trim();
        if (phoneValue && !/^\d{10}$/.test(phoneValue)) {
            mobileNumberError.classList.remove('hidden');
            isValid = false;
        } else {
            mobileNumberError.classList.add('hidden');
        }

        // Validate Opening Balance
        const balanceValue = openingBalance.value.trim();
        if (balanceValue && parseFloat(balanceValue) < 0) {
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
    phoneNumber.addEventListener('input', validateForm);
    openingBalance.addEventListener('input', validateForm);
});

document.addEventListener("DOMContentLoaded", () => {
    const tabs = document.querySelectorAll(".tab");

    tabs.forEach(tab => {
        tab.addEventListener("click", () => {
            // Remove the active class from all tabs
            tabs.forEach(t => t.classList.remove("active"));

            // Add the active class to the clicked tab
            tab.classList.add("active");

            // Redirect to the URL specified in the data-url attribute
            const redirectUrl = tab.getAttribute("data-url");
            if (redirectUrl) {
                window.location.href = redirectUrl;
            }
        });
    });
});



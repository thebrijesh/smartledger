

const button = document.getElementById('partymainlayout');
const variableValue = button.getAttribute('data-variable');

function loadParty(partyType){
    console.log(partyType);
}
console.log(variableValue);
document.addEventListener('DOMContentLoaded', function () {
    const phoneNumber = document.getElementById('phone-number');
    const openingBalance = document.getElementById('opening-balance');
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


    }

    // Attach event listeners to inputs
    openingBalance.addEventListener('input', validateForm);
});

// document.addEventListener("DOMContentLoaded", () => {
//     const tabs = document.querySelectorAll(".tab");
//
//     tabs.forEach(tab => {
//         tab.addEventListener("click", () => {
//             // Remove the active class from all tabs
//             tabs.forEach(t => t.classList.remove("active"));
//
//             // Add the active class to the clicked tab
//             tab.classList.add("active");
//
//             // Redirect to the URL specified in the data-url attribute
//             const redirectUrl = tab.getAttribute("data-url");
//             if (redirectUrl) {
//                 window.location.href = redirectUrl;
//             }
//         });
//     });
// });

function validateMobile(){
    const parties = document.getElementsByClassName('partyList');
    const phoneNumber = document.getElementById('phone-number').value;
    console.log(phoneNumber);
    const createPartyForm = document.getElementById('createPartyForm');
    console.log(parties);
    let show = true;
    Array.from(parties).forEach(party => {
        const mobile = party.querySelector('[data-mobile]').textContent;
        if(mobile === phoneNumber){
            show = false;
            window.location.href = '/users/party/view/'+party.getAttribute('data-partyid');
        }
        console.log(mobile + " " + phoneNumber.textContent);
    });
    if(show){
        createPartyForm.submit();
    }
}

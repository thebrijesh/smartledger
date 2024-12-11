sortCustomers();
filterCustomers();
function filterCustomers() {

    var searchQuery = document.getElementById('searchInput').value.toLowerCase();
    var filterType = document.getElementById('filterType').value;
    var customers = document.getElementsByClassName('partyList');


    Array.from(customers).forEach(customer => {
        var name = customer.querySelector('[data-name]').textContent.toLowerCase();
        var mobile = customer.querySelector('[data-mobile]').textContent.toLowerCase();
        var debit = parseFloat(customer.querySelector('[data-debit]')?.textContent.replace('₹', '') || 0);
        var credit = parseFloat(customer.querySelector('[data-credit]')?.textContent.replace('₹', '') || 0);
        var show = true;

        // Apply search filter
        if (!name.includes(searchQuery) && !mobile.includes(searchQuery)) {
            show = false;
        }

        // Apply type filter
        if (filterType !== 'all') {
            if (filterType === 'credit' && credit <= 0) {
                show = false;
            } else if (filterType === 'debit' && debit <= 0) {
                show = false;
            } else if (filterType === 'settle' && (credit !== 0 || debit !== 0)) {
                show = false;
            }
        }

        customer.style.display = show ? 'flex' : 'none';
    });
}

function sortCustomers() {

        var sortBy = document.getElementById('sortBy').value;
        var customersContainer = document.getElementsByClassName('partyView')[0]; // Select the first element

        var customers = Array.from(document.getElementsByClassName('partyList'));


        customers.sort(function(a, b) {
            var dateA = new Date(a.querySelector('[data-hiddenDate]').textContent);
            var dateB = new Date(b.querySelector('[data-hiddenDate]').textContent);
            var debitA = parseFloat(a.querySelector('[data-debit]')?.textContent.replace('₹', '') || 0);
            var debitB = parseFloat(b.querySelector('[data-debit]')?.textContent.replace('₹', '') || 0);
            var creditA = parseFloat(a.querySelector('[data-credit]')?.textContent.replace('₹', '') || 0);
            var creditB = parseFloat(b.querySelector('[data-credit]')?.textContent.replace('₹', '') || 0);

            if (sortBy === 'mostRecent') {
                return dateB - dateA;
            } else if (sortBy === 'highestAmount') {
                return (creditB + debitB) - (creditA + debitA);
            } else if (sortBy === 'leastAmount') {
                return (creditA + debitA) - (creditB + debitB);
            } else if (sortBy === 'byName') {
                return a.querySelector('[data-name]').textContent.localeCompare(b.querySelector('[data-name]').textContent);
            } else if (sortBy === 'oldest') {
                return dateA - dateB;
            }
        });

    // Append sorted customers back to the container
    customersContainer.innerHTML = '';
    customers.forEach(customer => {
        customersContainer.appendChild(customer);
    });
}
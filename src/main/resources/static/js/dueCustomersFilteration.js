document.addEventListener('DOMContentLoaded', () => {
    const searchQuery = document.getElementById('dueSearchInput');
    const filterType = document.getElementById('filterDueDate');
    const customers = document.querySelectorAll('.dueCustomersList');

    function filterCustomers() {
        const query = searchQuery.value.toLowerCase();
        const filter = filterType.value;
        const currDate = new Date().setHours(0, 0, 0, 0);

        customers.forEach(customer => {
            const name = customer.querySelector('[data-name]').textContent.toLowerCase();
            const [day, month, year] = customer.querySelector('[data-date]').textContent.split('-');

            const dueDate = new Date(`${month}-${day}-${year}`).setHours(0, 0, 0, 0);

            let show = true;

            // Apply search filter
            if (!name.includes(query)) {
                show = false;
            }


            // Apply type filter
            if (filter !== 'All') {
                if (filter === 'Today' && dueDate !== currDate) {
                    show = false;
                } else if (filter === 'Upcoming' && dueDate <= currDate) {
                    show = false;
                } else if (filter === 'Pending' && dueDate >= currDate) {
                    show = false;
                }
            }

            customer.style.display = show ? '' : 'none';
        });
    }

    searchQuery.addEventListener('input', filterCustomers);
    filterType.addEventListener('change', filterCustomers);

    filterCustomers();


});


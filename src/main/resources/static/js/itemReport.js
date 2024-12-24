document.addEventListener('DOMContentLoaded', () => {
    const filterPeriod = document.getElementById('ProductPeriod');
    const startDate = document.getElementById('startDate');
    const endDate = document.getElementById('endDate');
    const profit = document.querySelector('#profit');
    const qty = document.querySelector('#qty');
    const transactionList = document.querySelectorAll('.transactionList');
    const stoday = new Date();
    startDate.value = `${stoday.getFullYear()}-${String(stoday.getMonth() + 1).padStart(2, '0')}-${String(stoday.getDate()).padStart(2, '0')}`;
    endDate.value = `${stoday.getFullYear()}-${String(stoday.getMonth() + 1).padStart(2, '0')}-${String(stoday.getDate()).padStart(2, '0')}`;
    function filterTransactions() {
        const start = startDate.value ? new Date(startDate.value).setHours(0, 0, 0, 0) : null;
        const end = endDate.value ? new Date(endDate.value).setHours(23, 59, 59, 999) : null;

        let totalProfit = 0;
        let totalSold = 0;
        transactionList.forEach(transaction => {
            const unit = transaction.getAttribute('data-unit');
            const amount = transaction.getAttribute('data-amount');
            const type = transaction.getAttribute('data-type');
            const purchase = transaction.getAttribute('data-purchase');
            const dateStr = transaction.getAttribute('data-date');
            console.log(typeof dateStr);
            if (dateStr !== null) {
                const [day, month, year] = dateStr.split('-');
                const date = new Date(`${month}-${day}-${year}`);
                console.log("dateStr : " + date);
                if (type === 'OUT' && start && end && date >= start && date <= end) {
                    console.log(unit);
                    console.log(totalSold)
                    totalSold += parseInt(unit);

                    console.log(totalSold);
                    totalProfit += (amount - purchase) * unit;
                }
            }
        });

        profit.textContent = `₹${totalProfit.toLocaleString()}`;
        qty.textContent = `${totalSold.toLocaleString()}`;
    }

    startDate.addEventListener('change', filterTransactions);
    endDate.addEventListener('change', filterTransactions);


    filterPeriod.addEventListener('change', () => {
        const period = filterPeriod.value;
        const today = new Date();

        switch (period) {
            case 'Today' :
                startDate.value = `${today.getFullYear()}-${String(today.getMonth() + 1).padStart(2, '0')}-${String(today.getDate()).padStart(2, '0')}`;
                endDate.value = `${today.getFullYear()}-${String(today.getMonth() + 1).padStart(2, '0')}-${String(today.getDate()).padStart(2, '0')}`;
                break;
            case 'This Year':
                startDate.value = `${today.getFullYear()}-01-01`;
                endDate.value = `${today.getFullYear()}-12-31`;
                break;
            case 'This Month':
                startDate.value = `${today.getFullYear()}-${String(today.getMonth() + 1).padStart(2, '0')}-01`;
                endDate.value = new Date(today.getFullYear(), today.getMonth() + 1, 0).toISOString().split('T')[0];
                break;
            case 'This Week':
                const firstDayOfWeek = new Date(today);
                firstDayOfWeek.setDate(today.getDate() - today.getDay() + 1); // Monday as the first day of the week
                startDate.value = firstDayOfWeek.toISOString().split('T')[0];
                const lastDayOfWeek = new Date(today);
                lastDayOfWeek.setDate(today.getDate() - today.getDay() + 7); // Sunday as the last day of the week
                endDate.value = lastDayOfWeek.toISOString().split('T')[0];
                break;
            case 'Yesterday':
                const yesterday = new Date(today);
                yesterday.setDate(today.getDate() - 1);
                startDate.value = yesterday.toISOString().split('T')[0];
                endDate.value = yesterday.toISOString().split('T')[0];
                break;
            default:
                startDate.value = '';
                endDate.value = '';
        }

        filterTransactions();
    });

    filterTransactions();

    function showTransactionsByMostRecent() {
        const transactionList = filterPeriod.querySelectorAll('.transactionList');
        const container = document.querySelector('.container');

        // Sort the transactions by date in descending order
        const sortedTransactions = Array.from(transactionList).sort((a, b) => {
            const dateA = new Date(a.querySelector('[data-date]').textContent.split('-').reverse().join('-'));
            const dateB = new Date(b.querySelector('[data-date]').textContent.split('-').reverse().join('-'));
            return dateB - dateA;
        });

        // Clear the container and append sorted transactions back to the container
        container.innerHTML = '';
        sortedTransactions.forEach(transaction => container.appendChild(transaction));
    }
});

document.addEventListener('DOMContentLoaded', () => {
    const reportSearchInput = document.getElementById('reportSearchInput');
    const filterPeriod = document.getElementById('filterPeriod');
    const startDate = document.getElementById('startDate');
    const endDate = document.getElementById('endDate');
    const youGave = document.getElementById('youGave');
    const youGot = document.getElementById('youGot');
    const netBalance = document.querySelector('.netBalance');
    const partyList = document.querySelectorAll('.partyList');
    const stoday = new Date();
    startDate.value = `${stoday.getFullYear()}-01-01`;
    endDate.value = `${stoday.getFullYear()}-12-31`;
    function filterTransactions() {
        const searchValue = reportSearchInput.value.toLowerCase();
        const start = startDate.value ? new Date(startDate.value).setHours(0, 0, 0, 0) : null;
        const end = endDate.value ? new Date(endDate.value).setHours(23, 59, 59, 999) : null;

        let totalDebit = 0;
        let totalCredit = 0;
        let totalCount = 0;
        partyList.forEach(party => {
            const name = party.querySelector('[data-name]').textContent.toLowerCase();
            const [day, month, year] = party.querySelector('[data-date]').textContent.split('-');

            const date = new Date(`${month}-${day}-${year}`);

            let show = true;

            if (searchValue && !name.includes(searchValue)) {
                show = false;
            }

            if (start && date < start) {
                show = false;
            }

            if (end && date > end) {
                show = false;
            }


            party.style.display = show ? '' : 'none';

            if (show) {
                const debit = parseFloat(party.querySelector('[data-debit]').textContent.replace('₹', '') || 0);
                const credit = parseFloat(party.querySelector('[data-credit]').textContent.replace('₹', '') || 0);
                totalDebit += debit;
                totalCredit += credit;
                totalCount++;
            }

        });

        youGot.textContent = `₹${totalDebit.toLocaleString()}`;
        youGave.textContent = `₹${totalCredit.toLocaleString()}`;
        const net = totalDebit - totalCredit;
        netBalance.textContent = `₹${net.toLocaleString()}`;
        netBalance.classList.toggle('text-red-600', net < 0);
        netBalance.classList.toggle('text-green-600', net >= 0);
        const pdfBtn = document.getElementById('downloadPdfReportBtn');
        const excelBtn = document.getElementById('downloadExcelReportBtn');
        if (totalCount === 0) {
            //disable download and excel button
                pdfBtn.disabled = true;
                pdfBtn.style.cursor = "not-allowed";
                excelBtn.disabled = true;
                excelBtn.style.cursor = "not-allowed";
        }else {
            //enable download and excel button
            pdfBtn.disabled = false;
            pdfBtn.style.cursor = "pointer";
            excelBtn.disabled = false;
            excelBtn.style.cursor = "pointer";
        }
        showTransactionsByMostRecent();
    }

    reportSearchInput.addEventListener('input', filterTransactions);
    startDate.addEventListener('change', filterTransactions);
    endDate.addEventListener('change', filterTransactions);


    filterPeriod.addEventListener('change', () => {
        const period = filterPeriod.value;
        const today = new Date();

        switch (period) {
            case 'This Year':
                startDate.value = `${today.getFullYear()}-01-01`;
                endDate.value = `${today.getFullYear()}-12-31`;
                break;
            case 'This Quarter':
                const quarterStartMonth = Math.floor(today.getMonth() / 3) * 3;
                startDate.value = `${today.getFullYear()}-${String(quarterStartMonth + 1).padStart(2, '0')}-01`;
                endDate.value = new Date(today.getFullYear(), quarterStartMonth + 3, 0).toISOString().split('T')[0];
                break;
            case 'This Month':
                startDate.value = `${today.getFullYear()}-${String(today.getMonth() + 1).padStart(2, '0')}-01`;
                endDate.value = new Date(today.getFullYear(), today.getMonth() + 1, 0).toISOString().split('T')[0];
                break;
            case 'Last Month':
                const lastMonth = new Date(today.getFullYear(), today.getMonth() - 1, 1);
                startDate.value = `${lastMonth.getFullYear()}-${String(lastMonth.getMonth() + 1).padStart(2, '0')}-01`;
                endDate.value = new Date(lastMonth.getFullYear(), lastMonth.getMonth() + 1, 0).toISOString().split('T')[0];
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
        const partyList = document.querySelectorAll('.partyList');
        const container = document.querySelector('.reportTransactionList');

        // Sort the transactions by date in descending order
        const sortedTransactions = Array.from(partyList).sort((a, b) => {
            const dateA = new Date(a.querySelector('[data-date]').textContent.split('-').reverse().join('-'));
            const dateB = new Date(b.querySelector('[data-date]').textContent.split('-').reverse().join('-'));
            return dateB - dateA;
        });

        // Clear the container and append sorted transactions back to the container
        container.innerHTML = '';
        sortedTransactions.forEach(transaction => container.appendChild(transaction));
    }
});

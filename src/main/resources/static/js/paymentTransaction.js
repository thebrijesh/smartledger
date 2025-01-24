async function setPartyData(element) {

    const colorMap = {
        A: "bg-yellow-500",
        B: "bg-blue-500",
        C: "bg-green-500",
        D: "bg-yellow-500",
        E: "bg-indigo-500",
        F: "bg-pink-500",
        G: "bg-purple-500",
        H: "bg-gray-500",
        I: "bg-red-500",
        J: "bg-blue-500",
        K: "bg-green-500",
        L: "bg-yellow-500",
        M: "bg-indigo-500",
        N: "bg-pink-500",
        O: "bg-purple-500",
        P: "bg-gray-500",
        Q: "bg-red-500",
        R: "bg-blue-500",
        S: "bg-green-500",
        T: "bg-yellow-500",
        U: "bg-indigo-500",
        V: "bg-pink-500",
        W: "bg-purple-500",
        X: "bg-gray-500",
        Y: "bg-red-500",
        Z: "bg-blue-500",
    };
    const partyName = element.getAttribute('data-name');
    const partyId = element.getAttribute('data-id');
    const balance = element.getAttribute('data-balance');
    const firstChar = partyName.charAt(0).toUpperCase();
    const bgColor = colorMap[firstChar] || "bg-gray-500";
    const div = document.createElement("div");
    div.className = `w-12 h-12 text-black dark:text-white size-4 rounded-full flex justify-center items-center font-medium shadow-md transition-transform transform hover:scale-105 ${bgColor}`;
    div.innerHTML = `<span class="text-xl">${firstChar}</span>`;

    document.getElementById('firstChar').appendChild(div);
    document.getElementById('partyName').textContent = partyName;
    document.getElementById('balance').textContent = balance;
    document.getElementById('partyId').value = partyId;

    document.getElementById('drawer-right-select-party');
    document.getElementById('noUserSelectedLayout').style.display = 'none';
    document.getElementById('UserSelectedLayout').style.display = 'block';

    const container = document.getElementById('pending-invoices-container');
    container.innerHTML = ''; // Clear previous invoices

    try {
        const response = await fetch(`/users/party/getpendingbills/${partyId}`);
        const bills = await response.json();
        console.log(bills);
        if (bills.length === 0) {
            container.innerHTML = '<p class="text-sm text-gray-500 dark:text-gray-400">No pending invoices available.</p>';
            return;
        }

        bills.forEach(bill => {
            const billElement = `
                <div id="billRow" class="mt-2 bg-gray-100 dark:bg-gray-700 rounded-md p-4">
                    <p class="text-sm text-red-500 dark:text-red-400">₹ ${bill.amount} out of ₹ ${bill.amount} left to be settled</p>
                    <div class="mt-2">
                        <label class="flex items-center">
                            <input type="checkbox" class="text-green-500 focus:ring-green-300 dark:focus:ring-green-500">
                            <span class="ml-2 text-gray-900 dark:text-gray-100">#${bill.id} - ₹ ${bill.amount} (${bill.date})</span>
                        </label>
                    </div>
                </div>
            `;
            container.innerHTML += billElement;
        });
    } catch (error) {
        console.error('Error fetching pending bills:', error);
        container.innerHTML = '<p class="text-sm text-red-500 dark:text-red-400">Failed to load pending invoices.</p>';
    }

}

document.getElementById('party').addEventListener('change', async (event) => {

});


document.getElementById('amount').addEventListener('input', function (e) {
    const amount = e.target.value;
    if(amount > 0) {
        document.getElementById('PendingInvoice').style.display = 'block';
    }
});


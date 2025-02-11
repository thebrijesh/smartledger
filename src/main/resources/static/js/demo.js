function filterExpense() {
    let itemSearch = document.getElementById('searchInput').value.toLowerCase();
    const filterType = document.getElementById('filterCategorySelect').value;
    const items = document.querySelectorAll('.expenseRow');
    items.forEach(item => {
        const itemName = item.querySelector('.expenseName').textContent.toLowerCase();
        let show = true;
        if (!itemName.includes(itemSearch)) {
            show = false;
            console.log(itemName);
        }

        if (filterType !== 'All') {
            const itemCategory = item.querySelector('.expenseCategory').textContent.toLowerCase();
            console.log(itemCategory);
            if (filterType !== itemCategory) {
                show = false;
            }
        }
        //item style display
        item.style.display = show ? '' : 'none';
    });
}

function sortExpense() {
    const sortBy = document.getElementById('sortSelect').value;
    const items = document.getElementsByClassName('expenseRow');
    const productView = document.getElementsByClassName('expenseList')[0];
    const itemsArray = Array.from(items);
    console.log(itemsArray);

    // Sorting the itemsArray based on the sortBy value
    itemsArray.sort(function(a, b) {
        var dateA = new Date(a.querySelector('.expenseDate').textContent);
        var dateB = new Date(b.querySelector('.expenseDate').textContent);
        var amountA = parseFloat(a.querySelector('.expenseAmount').textContent);
        var amountB = parseFloat(b.querySelector('.expenseAmount').textContent);

        if (sortBy === 'letestFirst') {
            return dateB - dateA;
        } else if (sortBy === 'oldestFirst') {
            return dateA - dateB;
        } else if (sortBy === 'amountHighToLow') {
            return amountB - amountA;
        } else if (sortBy === 'amountLowToHigh') {
            return amountA - amountB;
        }
    });

    console.log(itemsArray);
    productView.innerHTML = '';
    itemsArray.forEach(i => {
        productView.appendChild(i);
    });


}
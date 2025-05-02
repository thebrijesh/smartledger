function filterBill() {
    let itemSearch = document.getElementById('searchName').value.toLowerCase();
    const filterType = document.getElementById('selectFilter').value;
    const items = document.querySelectorAll('.billRow');
    items.forEach(item => {
        const itemName = item.querySelector('.partyName').textContent.toLowerCase();
        let show = true;
        if (!itemName.includes(itemSearch) || (filterType !== item.querySelector('.paymentType').textContent.toLowerCase())) {
            show = false;
            console.log(itemName);
        }
        //item style display
        item.style.display = show ? '' : 'none';
    });
}

function sortBill() {
    const sortBy = document.getElementById('sortby').value;
    const items = document.getElementsByClassName('billRow');
    const productView = document.getElementsByClassName('billList')[0];
    const itemsArray = Array.from(items);
    console.log(itemsArray);

    // Sorting the itemsArray based on the sortBy value
    itemsArray.sort(function(a, b) {
        var dateA = new Date(a.querySelector('.billDate').textContent);
        var dateB = new Date(b.querySelector('.billDate').textContent);

        if (sortBy === 'mostRecent') {
            return dateB - dateA;
        } else if (sortBy === 'oldest') {
            return dateA - dateB;
        }
    });

    console.log(itemsArray);
    productView.innerHTML = '';
    itemsArray.forEach(i => {
        productView.appendChild(i);
    });


}
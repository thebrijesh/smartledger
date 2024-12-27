function filterService() {
    let itemSearch = document.getElementById('itemSearchInput').value.toLowerCase();
    const items = document.querySelectorAll('.serviceList');
    console.log(itemSearch);
    items.forEach(item => {
        const itemName = item.querySelector('.serviceName').textContent.toLowerCase();
        console.log(itemName);
        let show = true;
        if (!itemName.includes(itemSearch)) {
            show = false;
            console.log(itemName);
        }
        //item style display
        item.style.display = show ? '' : 'none';
    });
}


function sortService() {
    const sortBy = document.getElementById('itemSortBy').value;
    const items = document.getElementsByClassName('serviceList');
    const productView = document.getElementsByClassName('serviceView')[0];
    const itemsArray = Array.from(items);
    console.log(itemsArray);


    itemsArray.sort((a, b) => {

        const serviceTotalSoldUnits = (element) => parseInt(element.querySelector('.serviceTotalSoldUnits').textContent);
        console.log("stockQuantity : " + serviceTotalSoldUnits(a));
        const servicePrice = (element) => parseFloat(element.querySelector('.servicePrice').textContent.replace('₹', ''));
        console.log("salePrice : " + servicePrice(a));
        const serviceName = (element) => element.querySelector('.serviceName').textContent;
        console.log("productName : " + serviceName(a));

        switch (sortBy) {
            case 'mostRecent':
                return 0;
            case 'salesHighToLow':
                return serviceTotalSoldUnits(b) - serviceTotalSoldUnits(a);
            case 'salesLowToHigh':
                return serviceTotalSoldUnits(a) - serviceTotalSoldUnits(b);
            case 'salesPriceHighToLow':
                return servicePrice(b) - servicePrice(a);
            case 'salesPriceLowToHigh':
                return servicePrice(a) - servicePrice(b);
            case 'serviceNameAtoZ':
                return serviceName(a).localeCompare(serviceName(b));
        }
    });
    console.log(itemsArray);
    productView.innerHTML = '';
    itemsArray.forEach(i => {
        productView.appendChild(i);
    });


}

document
    .querySelector("#service_file_input")
    .addEventListener("change", function (event) {
        let file = event.target.files[0];
        let reader = new FileReader();
        reader.onload = function () {
            document
                .querySelector("#upload_image_preview")
                .setAttribute("src", reader.result);
        };
        reader.readAsDataURL(file);
    });


// function filterStock() {
//     let itemSearch = document.getElementById('itemSearchInput').value.toLowerCase();
//     const filterType = document.getElementById('itemFilterType').value;
//     const items = document.querySelectorAll('.ProductList');
//     items.forEach(item => {
//         const itemName = item.querySelector('.productName').textContent.toLowerCase();
//         let show = true;
//         if (!itemName.includes(itemSearch) || (filterType === 'lowStock' && !item.querySelector('.text-red-600'))) {
//             show = false;
//             console.log(itemName);
//         }
//         //item style display
//         item.style.display = show ? '' : 'none';
//     });
// }
//
//
// function sortStock() {
//     const sortBy = document.getElementById('itemSortBy').value;
//     const items = document.getElementsByClassName('ProductList');
//     const productView = document.getElementsByClassName('productView')[0];
//     const itemsArray = Array.from(items);
//     console.log(itemsArray);
//
//
//     itemsArray.sort((a, b) => {
//
//         const getStockQuantity = (element) => parseInt(element.querySelector('.stockQuantity').textContent);
//         console.log("stockQuantity : " + getStockQuantity(a));
//         const getSalePrice = (element) => parseFloat(element.querySelector('.stockSalePrice').textContent.replace('₹', ''));
//         console.log("salePrice : " + getSalePrice(a));
//         const getProductName = (element) => element.querySelector('.productName').textContent;
//         console.log("productName : " + getProductName(a));
//
//         switch (sortBy) {
//             case 'mostRecent':
//                 return 0;
//             case 'stockHighToLow':
//                 return getStockQuantity(b) - getStockQuantity(a);
//             case 'stockLowToHigh':
//                 return getStockQuantity(a) - getStockQuantity(b);
//             case 'stockPriceHighToLow':
//                 return getSalePrice(b) - getSalePrice(a);
//             case 'stockPriceLowToHigh':
//                 return getSalePrice(a) - getSalePrice(b);
//             case 'productNameAtoZ':
//                 return getProductName(a).localeCompare(getProductName(b));
//         }
//     });
//     console.log(itemsArray);
//     productView.innerHTML = '';
//     itemsArray.forEach(i => {
//         productView.appendChild(i);
//     });
//
//
// }

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


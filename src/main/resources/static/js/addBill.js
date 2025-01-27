const billForm = {
    partyId: -1,
    amount: 0,
    date: new Date(),
    paymentType: "CASH",
    dueAmount: 0,
    paidAmount: 0,
    totalDiscount: 0,
    billType: "SALE",
    additionalCharges: [],
    customFields: [],
    terms: [],
    products: [],
    services: [],               

};

document.addEventListener('DOMContentLoaded', (event) => {
    addItemsList();
    saveCustomField();
    saveCharge();
    saveTerms();
});

const handleResponse = async (response) => {
    if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || "Something went wrong");
    }
    return response.json();
};

document.addEventListener('DOMContentLoaded', (event) => {
    let newBill;
    let billId;
   if(document.getElementById('billId') != null) {
        billId = document.getElementById('billId').textContent;

       fetch('/users/transactions/get-bill/' + billId)
           .then(response => response.json())
           .then(data => {
               newBill = data;
               //print the data of the bill

               console.log(newBill);
               console.log(newBill.productTransactions);
               document.getElementById('totalDiscount').textContent = "0";
               setTotalBillAmount();

               const totalDiscount = document.getElementById('TotalDiscountFromDiscountModel');
               totalDiscount.value = newBill.totalDiscount;

               let discountAmount = newBill.discount;
               console.log(newBill.discount);
               let totalAmount = parseInt(document.getElementById('finalPayableBillAmount').textContent, 0);


               document.getElementById('totalDiscount').textContent = discountAmount;
               document.getElementById('DiscountLayout').style.display = 'none';
               document.getElementById('SelectDiscountLayout').style.display = '';
               billForm.totalDiscount = discountAmount;
               setTotalBillAmount();


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
               const partyName = newBill.party.name;
               const partyMobile = newBill.party.mobile;
               const partyId = newBill.party.id;
               const firstChar = partyName.charAt(0).toUpperCase();
               const bgColor = colorMap[firstChar] || "bg-gray-500";
               const div = document.createElement("div");
               div.className = `w-12 h-12 text-black dark:text-white size-4 rounded-full flex justify-center items-center font-medium shadow-md transition-transform transform hover:scale-105 ${bgColor}`;
               div.innerHTML = `<span class="text-xl">${firstChar}</span>`;
               document.getElementById('removePartyBtn').style.display = 'none';
               billForm.partyId = partyId;
               billForm.billType = newBill.billType;
               document.getElementById('firstChar').appendChild(div);
               document.getElementById('partyName').textContent = partyName;
               document.getElementById('partyMobile').textContent = partyMobile;
               document.getElementById('partyId').value = partyId;

               document.getElementById('drawer-right-select-party');
               document.getElementById('noPartySelectedLayout').style.display = 'none';
               document.getElementById('showSelectedPartyLayout').style.display = 'block';

               const paymentMethods = document.querySelectorAll('input[name="payment_method"]');
               const paymentType = newBill.paymentType;
               paymentMethods.forEach(radio => {
                   if (radio.value.toUpperCase() === paymentType) {
                       radio.checked = true;
                   }

                   const total = document.getElementById('finalBillAmount').textContent;

                   if (paymentType === 'UNPAID') {
                       console.log("Unpaid");
                       document.getElementById('dueDateLayout').style.display = '';
                       document.getElementById('receivedLayout').style.display = 'none';
                       console.log("total : " + total);
                       console.log(document.getElementById('balanceDue').textContent);
                       document.getElementById('balanceDue').textContent = '₹' + total;
                       billForm.paymentType = "UNPAID";
                       billForm.dueAmount = total;
                   } else if (paymentType === 'CASH') {
                       document.getElementById('dueDateLayout').style.display = 'none';
                       document.getElementById('receivedLayout').style.display = '';
                       billForm.paymentType = "CASH";
                   } else if (paymentType === 'ONLINE') {
                       document.getElementById('dueDateLayout').style.display = 'none';
                       document.getElementById('receivedLayout').style.display = '';
                       billForm.paymentType = "ONLINE";
                   }

               });

           });
   }
    console.log(billId);
    //get bill by id



});

document.getElementById('generateBillBtn').addEventListener('click', (event) => {
    console.log(billForm);
    billForm.date = document.getElementById('datepicker').value;
   let billId;
    if(document.getElementById('billId') == null){
        billId = null;
    }else{
         billId = document.getElementById('billId').textContent;
    }
    let url;
    if(billId === null){
        url = '/users/transactions/create-bill';
    }else{
        url = '/users/transactions/update-bill/' + billId;
    }
    console.log(url);
    console.log(billForm.date);
    fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(billForm),
    })
        .then(data => {
            console.log('Success:', data);
              const redirectValue = document.getElementById('billtype').textContent;
              console.log(redirectValue);
            window.location.href = ( redirectValue === 'Sales' ) ? '/users/transactions/sales' : '/users/transactions/purchases';
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Failed to submit the bill.');
        });

});

function setPartyData(element) {

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
    const partyMobile = element.getAttribute('data-mobile');
    const partyId = element.getAttribute('data-id');
    const firstChar = partyName.charAt(0).toUpperCase();
    const bgColor = colorMap[firstChar] || "bg-gray-500";
    const div = document.createElement("div");
    div.className = `w-12 h-12 text-black dark:text-white size-4 rounded-full flex justify-center items-center font-medium shadow-md transition-transform transform hover:scale-105 ${bgColor}`;
    div.innerHTML = `<span class="text-xl">${firstChar}</span>`;

    billForm.partyId = partyId;
    billForm.billType = (document.getElementById('billtype').textContent === 'Sales') ? 'SALE' : 'PURCHASE';
    document.getElementById('firstChar').appendChild(div);
    document.getElementById('partyName').textContent = partyName;
    document.getElementById('partyMobile').textContent = partyMobile;
    document.getElementById('partyId').value = partyId;

    document.getElementById('drawer-right-select-party');
    document.getElementById('noPartySelectedLayout').style.display = 'none';
    document.getElementById('showSelectedPartyLayout').style.display = 'block';
}


function removeParty() {
    document.getElementById('firstChar').innerHTML = '';
    document.getElementById('noPartySelectedLayout').style.display = 'block';
    document.getElementById('showSelectedPartyLayout').style.display = 'none';
    document.getElementById('partyId').value = '';
    billForm.partyId = -1;
}


//when user select payment method according change the layout
const paymentMethods = document.querySelectorAll('input[name="payment_method"]');
paymentMethods.forEach(radio => {
    radio.addEventListener('change', (event) => {
        const total = document.getElementById('finalBillAmount').textContent;

        if (event.target.checked) {
            const value = event.target.value;
            if (value === 'unpaid') {
                console.log("Unpaid");
                document.getElementById('dueDateLayout').style.display = '';
                document.getElementById('receivedLayout').style.display = 'none';
                console.log("total : " + total);
                console.log(document.getElementById('balanceDue').textContent);
                document.getElementById('balanceDue').textContent = total;
                billForm.paymentType = "UNPAID";
                billForm.dueAmount = total;


            } else if (value === 'cash') {
                document.getElementById('dueDateLayout').style.display = 'none';
                document.getElementById('receivedLayout').style.display = '';
                billForm.paymentType = "CASH";
                setReceivedAmount();
            } else if (value === 'online') {
                document.getElementById('dueDateLayout').style.display = 'none';
                document.getElementById('receivedLayout').style.display = '';
                billForm.paymentType = "ONLINE";
                setReceivedAmount();
            }
        }
    });
});

document.getElementById('received').addEventListener('input', setReceivedAmount);

    function setReceivedAmount() {
        console.log("Received");
        let received = (document.getElementById('received').value) ? document.getElementById('received').value : 0;
        const total = document.getElementById('finalBillAmount').textContent;
        let balance = parseFloat(total) - parseFloat(received);
        console.log(balance);
        if(balance < 0){ balance = 0};
        document.getElementById('balanceDue').textContent = balance;
        billForm.paidAmount = received;
        billForm.dueAmount = balance;
        if (balance > 0) {
            document.getElementById('dueDateLayout').style.display = '';
        }else{
            document.getElementById('dueDateLayout').style.display = 'none';

        }
    };
let terms = [];

function addNewTermRow() {
    terms = document.getElementsByClassName('term');
    if (terms.length < 3) {
        const termsRow = document.createElement('div');
        termsRow.className = 'termsRow flex flex-wrap items-center gap-4 mb-4';
        termsRow.innerHTML = `<input type="text" placeholder="Type here" class="term flex-1 min-w-[50%] px-3 py-2 border rounded focus:outline-none focus:ring-2 focus:ring-blue-500 dark:focus:ring-blue-400 hover:shadow-md transition-shadow duration-200 bg-white dark:bg-gray-700 text-gray-700 dark:text-gray-200 border-gray-300 dark:border-gray-600">`;
        document.getElementsByClassName('termAndConditionList')[0].appendChild(termsRow);

    }
    if (terms.length === 3) {
        document.getElementById('addNewTermBtn').style.display = 'none';
    }
}

function saveTerms(element) {
    terms = [];
    const list = document.getElementsByClassName('termsRow');

    //if checkbox is selected and name is not empty and value is not empty then save the field
    Array.from(list).forEach(term => {
        const name = term.querySelector('.term').value;
        if (name !== '') {
            terms.push(name);
            console.log(name);
        }
    });
    billForm.terms = terms;
    //append all the terms in the term view
    const termsView = document.getElementById('TermsAndConditionFieldsLayout');
    const termRow = document.getElementById('TermsAndConditionFieldsRow');
    termRow.innerHTML = '';
    termRow.classList.add('mb-4');
    terms.forEach(term => {
        const div = document.createElement('div');
        div.className = 'flex justify-between items-center gap-2';
        div.innerHTML = `<p class="text-gray-800 dark:text-gray-200">${term}</p>`;
        termRow.appendChild(div);
    });
    termsView.style.display = '';
}


let charges = [];

function addNewChargeRow() {
    charges = document.getElementsByClassName('chargeRow');
    if (charges.length < 3) {
        const chargeRow = document.createElement('div');
        chargeRow.className = 'chargeRow flex flex-wrap items-center gap-4 mb-4';
        chargeRow.innerHTML = `<div class="flex-shrink-0">
                <input type="checkbox" class="w-5 h-5 border-gray-300 dark:border-gray-600 rounded focus:ring-blue-500 dark:focus:ring-blue-400 hover:scale-105 transition-transform duration-200">
            </div>
            <input type="text" placeholder="Type here" min="0" class="flex-1 min-w-[50%] px-3 py-2 border rounded focus:outline-none focus:ring-2 focus:ring-blue-500 dark:focus:ring-blue-400 hover:shadow-md transition-shadow duration-200 bg-white dark:bg-gray-700 text-gray-700 dark:text-gray-200 border-gray-300 dark:border-gray-600">
            <input type="number" placeholder="999" class="w-20 px-3 py-2 border rounded focus:outline-none focus:ring-2 focus:ring-blue-500 dark:focus:ring-blue-400 hover:shadow-md transition-shadow duration-200 bg-white dark:bg-gray-700 text-gray-700 dark:text-gray-200 border-gray-300 dark:border-gray-600">`;
        document.getElementsByClassName('chargesList')[0].appendChild(chargeRow);
    }
    if (charges.length === 3) {
        document.getElementById('addNewChargeBtn').style.display = 'none';
    }
}


let customFields = [];

function addNewFieldRow() {
    customFields = document.getElementsByClassName('fieldRow');
    if (customFields.length < 5) {
        const fieldRow = document.createElement('div');
        fieldRow.className = 'fieldRow flex flex-wrap items-center gap-4 mb-4';
        fieldRow.innerHTML = `<div class="flex-shrink-0">
                <input type="checkbox" class="w-5 h-5 border-gray-300 dark:border-gray-600 rounded focus:ring-blue-500 dark:focus:ring-blue-400 hover:scale-105 transition-transform duration-200">
            </div>
            <input type="text" placeholder="Type here" min="0" class="nameField flex-1 min-w-[50%] px-3 py-2 border rounded focus:outline-none focus:ring-2 focus:ring-blue-500 dark:focus:ring-blue-400 hover:shadow-md transition-shadow duration-200 bg-white dark:bg-gray-700 text-gray-700 dark:text-gray-200 border-gray-300 dark:border-gray-600">
            <input type="text" placeholder="999" class=" valueField  w-20 px-3 py-2 border rounded focus:outline-none focus:ring-2 focus:ring-blue-500 dark:focus:ring-blue-400 hover:shadow-md transition-shadow duration-200 bg-white dark:bg-gray-700 text-gray-700 dark:text-gray-200 border-gray-300 dark:border-gray-600">`;
        document.getElementsByClassName('customFieldsList')[0].appendChild(fieldRow);
    }
    if (customFields.length === 5) {
        document.getElementById('addNewFieldBtn').style.display = 'none';
    }
}

function saveCustomField(element) {
    customFields = [];
    const list = document.getElementsByClassName('fieldRow');

    //if checkbox is selected and name is not empty and value is not empty then save the field
    Array.from(list).forEach(field => {
        const checkbox = field.querySelector('input[type="checkbox"]');
        const name = field.querySelector('.nameField').value;
        const value = field.querySelector('.valueField').value;
        if (checkbox.checked && name !== '' && value !== '') {
            customFields.push({name: name, value: value});
            console.log(name + " " + value);
        }
    });
    billForm.customFields = customFields;
    //append all the fields in the field view
    const fieldsView = document.getElementById('CustomFieldsLayout');
    const fieldRow = document.getElementById('CustomFieldsRow');
    fieldRow.innerHTML = '';
    fieldRow.classList.add('mb-4');
    customFields.forEach(field => {
        const div = document.createElement('div');
        div.className = 'flex justify-between items-center gap-2';
        div.innerHTML = `<p class="text-gray-800 dark:text-gray-200">${field.name}</p>
                                 <p class="text-gray-800 dark:text-gray-200 font-semibold">${field.value}</p>`;
        fieldRow.appendChild(div);
    });
    fieldsView.style.display = '';


}


let savedcharges = [];

function saveCharge() {
    savedcharges = [];
    const list = document.getElementsByClassName('chargeRow');
    let totalCharges = 0;
    Array.from(list).forEach(charge => {
        //if checkbox is selected and name is not empty and amount is not empty then save the charge
        const checkbox = charge.querySelector('input[type="checkbox"]');
        const name = charge.querySelector('input[type="text"]').value;
        const amount = charge.querySelector('input[type="number"]').value;
        if (checkbox.checked && name !== '' && amount !== '') {
            savedcharges.push({name: name, amount: amount});
            console.log(name + " " + amount);
            totalCharges += parseInt(amount, 0);
        }
    });
    billForm.additionalCharges = savedcharges;
    document.getElementById('totalCharges').textContent = totalCharges;
    setTotalBillAmount();
    //append all the charges in the charges view
    const chargesView = document.getElementById('AdditionalChargesLayout');
    const discountLayout = document.getElementById('DiscountLayout');
    const chargeRow = document.getElementById('AdditionalChargeRow');
    chargeRow.innerHTML = '';
    chargeRow.classList.add('mb-4');
    savedcharges.forEach(charge => {
        const div = document.createElement('div');
        div.className = 'flex justify-between items-center gap-2';
        div.innerHTML = `<p class="text-gray-800 dark:text-gray-200">${charge.name}</p>
                                 <p class="text-gray-800 dark:text-gray-200 font-semibold">&#8377; ${charge.amount}</p>`;
        chargeRow.appendChild(div);
    });
    chargesView.style.display = '';
    discountLayout.style.display = '';

    //hide the charges modal
    console.log(savedcharges);
}


let currItem;

function modifyItem(element) {
    currItem = element;
    const productId = element.getAttribute('data-product-id');
    const serviceId = element.getAttribute('data-service-id');
    const productName = element.getAttribute('data-item-name');
    const productSalePrice = element.getAttribute('data-item-sale-price');
    let discountType = element.getAttribute('data-discount-type');
    let discount = element.getAttribute('data-discount-amount');

    document.getElementById('itemName').textContent = productName;
    document.getElementById('itemSalePrice').value = parseInt(productSalePrice, 0);
    document.getElementById('amount').textContent = productSalePrice;
    document.getElementById('totalCalculatedAmount').textContent = `${productSalePrice - discount}`;
    document.getElementById('discountAmount').textContent = `${discount}`;
    if (discountType === 'PERCENTAGE') {
        discount = (discount * 100) / productSalePrice;
    }
    document.getElementById('itemDiscountFromDiscountModel').value = discount;
    console.log(discountType);

    if (discountType === null) {
        discountType = 'FLAT';
    }
    document.getElementById('discountType').value = discountType;
    console.log(discountType);
    const btnSave = document.getElementById('btnSave');
    btnSave.setAttribute('data-discount-type', discountType);
    btnSave.setAttribute('data-product-id', productId);
    btnSave.setAttribute('data-service-id', serviceId);
}

function finalPriceCalculation() {
    const itemPrice = parseInt(document.getElementById('itemSalePrice').value);
    let discount = parseInt(document.getElementById('itemDiscountFromDiscountModel').value);
    const discountType = document.getElementById('discountType').value;
    let finalPrice = 0, discountAmount = 0;
    document.getElementById('amount').textContent = `${itemPrice}`;
    if (discount === null) {
        discount = 0;
    }
    if (discountType === 'PERCENTAGE') {
        if (discount > 100) {
            discount = 100;
        }
        discountAmount = (itemPrice * discount) / 100;
    } else {
        if (discount > itemPrice) {
            discount = itemPrice;
        }
        discountAmount = discount;
    }
    document.getElementById('itemDiscount').value = discount;

    finalPrice = itemPrice - discountAmount;
    document.getElementById('discountAmount').textContent = `${discountAmount}`;
    document.getElementById('totalCalculatedAmount').textContent = `${finalPrice}` ? `${finalPrice}` : 0;
    console.log('' + (parseInt(itemPrice, 0) - parseInt(discount ? discount : 0)));

}


function editProductTransaction(element) {
    const productId = element.getAttribute('data-product-id');
    const serviceId = element.getAttribute('data-service-id');
    const itemSalePrice = document.getElementById('itemSalePrice').value;
    const itemDiscount = document.getElementById('discountAmount').textContent;
    const discountType = document.getElementById('discountType').value;
    currItem.setAttribute('data-item-sale-price', itemSalePrice);
    currItem.setAttribute('data-discount-amount', itemDiscount);
    currItem.setAttribute('data-discount-type', discountType);
    console.log("=========" + currItem.getAttribute('data-item-sale-price'));

    //if productId is not null then it is a product
    if (productId !== 'null') {
        const productTransactionsList = document.getElementsByClassName('productTransactionsList');
        Array.from(productTransactionsList).forEach(item => {
            console.log("item.getAttribute; " + item.getAttribute('data-product-id'));
            if (item.getAttribute('data-product-id') === productId) {
                const itemPrice = item.querySelector('#itemPrice');
                const discount = item.querySelector('#itemDiscount');
                const finalPrice = item.querySelector('#itemFinalPrice');
                const itemAddEditBtnFromDrawer = item.querySelector('#itemAddEditBtnFromDrawer');

                itemPrice.textContent = '₹ ' + currItem.getAttribute('data-item-sale-price');
                itemPrice.setAttribute('data-price', currItem.getAttribute('data-item-sale-price'));

                discount.textContent = '₹ ' + itemDiscount;
                discount.setAttribute('data-discount', itemDiscount);

                finalPrice.textContent = '₹ ' + (parseInt(itemPrice.getAttribute('data-price'), 0) - parseInt(itemDiscount, 0));
                finalPrice.setAttribute('data-final-price', '' + (parseInt(itemPrice.getAttribute('data-price'), 0) - parseInt(itemDiscount, 0)));


                itemAddEditBtnFromDrawer.setAttribute('data-discount-type', document.getElementById('discountType').value);
                console.log(itemAddEditBtnFromDrawer.getAttribute('data-discount-type'));

            }
        });
    } else {
        const serviceTransactionsList = document.getElementsByClassName('serviceTransactionsList');
        Array.from(serviceTransactionsList).forEach(item => {
            if (item.getAttribute('data-service-id') === serviceId) {
                const itemPrice = item.querySelector('#itemPriceService');
                const discount = item.querySelector('#itemDiscountService');
                const finalPrice = item.querySelector('#itemFinalPriceService');
                const itemAddEditBtnFromDrawer = item.querySelector('#itemAddEditBtnFromDrawer');

                itemPrice.textContent = '₹ ' + currItem.getAttribute('data-item-sale-price');
                itemPrice.setAttribute('data-price', currItem.getAttribute('data-item-sale-price'));

                discount.textContent = '₹ ' + itemDiscount;
                discount.setAttribute('data-discount', itemDiscount);

                finalPrice.textContent = '₹ ' + (parseInt(itemPrice.getAttribute('data-price'), 0) - parseInt(itemDiscount, 0));
                finalPrice.setAttribute('data-final-price', '' + (parseInt(itemPrice.getAttribute('data-price'), 0) - parseInt(itemDiscount, 0)));

                itemAddEditBtnFromDrawer.setAttribute('data-discount-type', document.getElementById('discountType').value);
                console.log(itemAddEditBtnFromDrawer.getAttribute('data-discount-type'));
            }
        });
    }
    element.setAttribute('data-discount-type', null);
    element.setAttribute('data-product-id', null);
    element.setAttribute('data-service-id', null);

    document.getElementById('itemSalePrice').value = 0;
    document.getElementById('discountAmount').textContent = '0';
    document.getElementById('discountType').value = null;

}


function setData() {
    const productTransactionsList = document.getElementsByClassName('productTransactionsList');
    Array.from(productTransactionsList).forEach(item => {

        const itemPrice = item.querySelector('#itemPrice');
        const discount = item.querySelector('#itemDiscount');
        const finalPrice = item.querySelector('#itemFinalPrice');
        itemPrice.textContent = '₹ ' + itemPrice.getAttribute('data-price');
        discount.textContent = '₹ ' + discount.getAttribute('data-discount');
        finalPrice.textContent = '₹ ' + finalPrice.getAttribute('data-final-price');
    });

    const serviceTransactionsList = document.getElementsByClassName('serviceTransactionsList');
    Array.from(serviceTransactionsList).forEach(item => {
        const itemPrice = item.querySelector('#itemPriceService');
        const discount = item.querySelector('#itemDiscountService');
        const finalPrice = item.querySelector('#itemFinalPriceService');
        itemPrice.textContent = '₹ ' + itemPrice.getAttribute('data-price');
        discount.textContent = '₹ ' + discount.getAttribute('data-discount');
        finalPrice.textContent = '₹ ' + finalPrice.getAttribute('data-final-price');
    });
}


//setTotalDiscount
function setTotalDiscount() {
    document.getElementById('totalDiscount').textContent = "0";
    setTotalBillAmount();
    const totalDiscount = document.getElementById('TotalDiscountFromDiscountModel');
    const discountType = document.getElementById('TotalDiscountType').value;
    let discountAmount = 0;
    let totalAmount = parseInt(document.getElementById('finalPayableBillAmount').textContent, 0);
    if (discountType === 'PERCENTAGE') {
        if (totalDiscount.value > 100) {
            totalDiscount.value = 100;
        }
        discountAmount = (totalAmount * totalDiscount.value) / 100;
    } else {
        if (totalDiscount.value > totalAmount) {
            totalDiscount.value = totalAmount;
        }
        discountAmount = totalDiscount.value;
    }

    document.getElementById('totalDiscount').textContent = discountAmount;
    document.getElementById('DiscountLayout').style.display = 'none';
    document.getElementById('SelectDiscountLayout').style.display = '';
    billForm.totalDiscount = discountAmount;
    setTotalBillAmount();

}


let productTransactions = [];
updateList('productsListLayout');

function updateList(layout) {
    if (layout === 'ServicesListLayout') {
        document.getElementById('servicesListLayout').style.display = '';
        document.getElementById('productsListLayout').style.display = 'none';
    } else {
        document.getElementById('servicesListLayout').style.display = 'none';
        document.getElementById('productsListLayout').style.display = '';
    }
}

function addItemsList() {
    transactions = [];
    let products = [];
    let services = [];
    let productTransactionsList = document.getElementsByClassName('productTransactionsList');
    Array.from(productTransactionsList).forEach(productTransaction => {
        let quantity = productTransaction.querySelector('.quantity').value;
        if (parseInt(quantity) > 0) {
            let product = {
                productId: productTransaction.getAttribute('data-product-id'),
                name: productTransaction.querySelector('#itemName').textContent,
                stockQuantity: quantity,
                amount: productTransaction.querySelector('#itemFinalPrice').textContent.split(' ')[1]
            };
            transactions.push(product);
            products.push(product);
        }
    });
    billForm.products = products;
    console.log(transactions);

    let serviceTransactionsList = document.getElementsByClassName('serviceTransactionsList');
    Array.from(serviceTransactionsList).forEach(productTransaction => {
        let quantity = productTransaction.querySelector('.quantity').value;
        if (parseInt(quantity) > 0) {
            let service = {
                serviceId: productTransaction.getAttribute('data-product-id'),
                name: productTransaction.querySelector('#itemNameService').textContent,
                stockQuantity: quantity,
                amount: productTransaction.querySelector('#itemFinalPriceService').textContent.split(' ')[1]
            };
            transactions.push(service);
            services.push(service);
        }
    });
    billForm.services = services;
    console.log(transactions);

    const itemView = document.getElementById('noItemSelectedLayout');
    const editOrAddItemLayout = document.getElementById('editOrAddItemLayout');
    const AdditionalChargesLayout = document.getElementById('AdditionalChargesLayout');
    const discountLayout = document.getElementById('DiscountLayout');

    itemView.style.display = (transactions.length > 0) ? 'none' : '';
    editOrAddItemLayout.style.display = (transactions.length < 0) ? 'none' : '';
    AdditionalChargesLayout.style.display = (transactions.length < 0) ? 'none' : '';
    discountLayout.style.display = (transactions.length < 0) ? 'none' : '';

    setProductTransactions();
}

// set the productTransactions data in this view :
// <div class="flex justify-between items-center mb-2">
// <p className="text-gray-800 dark:text-gray-200">mouse</p>
// <p className="text-gray-800 dark:text-gray-200 font-semibold">&#8377; 224</p>
// </div>
// <p className="text-sm text-gray-500 dark:text-gray-400">4.0 x &#8377; 56</p>
//and show this view in the selected item view
function setProductTransactions() {
    const selectedItemView = document.querySelector('.selectedItemView');
    selectedItemView.innerHTML = '';
    let totalAmount = 0;
    selectedItemView.classList.add('border-t', 'dark:border-gray-600');
    transactions.forEach(product => {
        const div = document.createElement('div');
        console.log("product " + product);
        div.className = 'flex justify-between items-center mt-2';
        div.innerHTML = `<p class="hidden text-gray-800 dark:text-gray-200">${product.productId}</p>
                             <p class="text-gray-800 dark:text-gray-200">${product.name}</p>
                             <p class="text-gray-800 dark:text-gray-200 font-semibold">&#8377; ${product.amount * product.stockQuantity}</p>`;
        selectedItemView.appendChild(div);
        const p = document.createElement('p');
        p.className = 'text-sm text-gray-500 dark:text-gray-400 mb-2';
        p.textContent = `${product.stockQuantity} x ${product.amount}`;
        console.log("amount" + product.amount);
        totalAmount += product.amount * product.stockQuantity;
        selectedItemView.appendChild(p);
    });
    document.getElementById('totalAmount').textContent = totalAmount;
    setTotalBillAmount();
    const editOrAddItemLayout = document.getElementById('editOrAddItemLayout');
    const AdditionalChargesLayout = document.getElementById('AdditionalChargesLayout');
    const discountLayout = document.getElementById('DiscountLayout');
    editOrAddItemLayout.style.display = (transactions.length > 0) ? '' : 'none';
    discountLayout.style.display = (transactions.length > 0) ? '' : 'none';
    document.getElementById('CustomFieldsLayout').style.display = (transactions.length > 0) ? '' : 'none';
    document.getElementById('TermsAndConditionFieldsLayout').style.display = (transactions.length > 0) ? '' : 'none';
}

function setTotalBillAmount() {
    const totalAmount = parseInt(document.getElementById('totalAmount').textContent, 0);
    console.log(totalAmount);
    const totalDiscount = parseInt(document.getElementById('totalDiscount').textContent, 0);
    console.log(totalDiscount);
    const totalCharges = parseInt(document.getElementById('totalCharges').textContent, 0);
    console.log(totalCharges);


    document.getElementById('finalPayableBillAmount').textContent = parseInt((totalAmount + totalCharges - totalDiscount), 0);
    billForm.amount = document.getElementById('finalPayableBillAmount').textContent;
    document.getElementById('finalBillAmount').textContent = parseInt((totalAmount + totalCharges - totalDiscount), 0);
    setReceivedAmount();
}



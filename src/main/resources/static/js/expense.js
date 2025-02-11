document.addEventListener("DOMContentLoaded", () => {
    // Helper function for handling Fetch API errors
    const handleResponse = async (response) => {
        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.message || "Something went wrong");
        }
        return response.json();
    };
    const categoriesTable = document.querySelector("#categoriesTable");
    const expensesTable = document.querySelector("#expensesTable");
    // Fetch categories and expenses on page load
    fetchCategories();
    fetchExpenses();


    // Fetch categories
    function fetchCategories() {
        fetch('/users/expense-category/all')
            .then(handleResponse)
            .then(categories => {
                categoriesTable.innerHTML = '';
                categories.forEach(category => {
                    categoriesTable.innerHTML += `
                        <tr>
                            <td id="categoryId" class="hidden border px-4 py-2">${category.id}</td>
                            <td id="categoryName" class="border px-4 py-2">${category.name}</td>
                            <td id="categoryDescription" class="border px-4 py-2">${category.description || '-'}</td>
                            <td class="border px-4 py-2">
                                <button class="editCategoryBtn text-blue-500" onclick="editCategory(this.parentElement)">Edit</button>
                                <button class="deleteCategoryBtn text-red-500 ml-2" onclick="deleteCategory(${category.id})">Delete</button>
                            </td>
                        </tr>
                    `;
                });
            })
            .catch(error => console.error(error));
    }

    // Fetch expenses
    function fetchExpenses() {
        fetch('/users/transaction/expense/all')
            .then(handleResponse)
            .then(expenses => {
                expensesTable.innerHTML = '';
                expenses.forEach(expense => {
                    expensesTable.innerHTML += `
                        <tr>
                            <td id="expenseId" class="hidden border px-4 py-2">${expense.id}</td>
                            <td id="expenseName" class="border px-4 py-2">${expense.name}</td>
                            <td id="expenseAmount" class="border px-4 py-2">${expense.amount}</td>
                            <td id="expenseDate" class="border px-4 py-2">${new Date(expense.date).toLocaleString('en-GB', {
                        day: '2-digit',
                        month: '2-digit',
                        year: 'numeric',
                        hour: '2-digit',
                        minute: '2-digit',
                        hour12: true
                    }).replace(',', '').replaceAll('/', '-')}</td>
                            <td id="expenseCategoryName" class="border px-4 py-2">${expense.expensesCategory.name}</td>
                            <td id="expenseCategoryId" class="hidden border px-4 py-2">${expense.expensesCategory.id}</td>
                            <td id="expenseDescription" class="border px-4 py-2">${expense.description || '-'}</td>
                            <td class="border px-4 py-2">
                                <button class="editExpenseBtn text-blue-500" onclick="editExpense(this.parentElement)">Edit</button>
                                <button class="deleteExpenseBtn text-red-500 ml-2" onclick="deleteExpense(${expense.id})">Delete</button>
                            </td>
                        </tr>
                    `;
                });
            })
            .catch(error => console.error(error));
    }

    // Add Category
    document.getElementById("categoryForm").addEventListener("submit", (e) => {
        e.preventDefault();
        const id = document.getElementById("selectedCategoryId").textContent;
        const name = document.getElementById("categoryNameInput").value;
        const description = document.getElementById("categoryDescriptionInput").value;
        if (!id || id === '') {
            fetch('/users/expense-category/add', {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({name, description})
            })
                .then(handleResponse)
                .then(() => {
                    alert('Category added!');
                    fetchCategories();
                    document.getElementById("categoryModal").classList.add("hidden");
                })
                .catch(error => console.error(error));
        } else {
            fetch(`/users/expense-category/update/` + id, {
                method: 'PUT',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({
                    id: id,
                    name: name,
                    description: description
                })
            })
                .then(handleResponse)
                .then(() => {
                    alert('Category updated!');
                    fetchCategories();
                    document.getElementById("categoryModal").classList.add("hidden");
                })
                .catch(error => console.error(error));
        }
        document.getElementById('categoryNameInput').value = "";
        document.getElementById('categoryDescriptionInput').value = "";
        document.getElementById("categoryId").textContent = "";
    });

    window.editCategory = (element) => {
        const categoryId = element.parentElement.querySelector("#categoryId").textContent;
        const categoryName = element.parentElement.querySelector("#categoryName").textContent;
        const categoryDescription = element.parentElement.querySelector("#categoryDescription").textContent;
        document.querySelector("#selectedCategoryId").textContent = categoryId;
        document.querySelector("#categoryNameInput").value = categoryName;
        document.querySelector("#categoryDescriptionInput").value = categoryDescription !== '-' ? categoryDescription : '';
        document.getElementById('categoryModal').classList.remove('hidden');


    }

    window.cancelBtnOfCategoryClickListener = () => {
        document.getElementById('categoryModal').classList.add('hidden');
        document.getElementById('categoryNameInput').value = "";
        document.getElementById('categoryDescriptionInput').value = "";
    }

    window.cancelBtnOfExpenseClickListener = () => {
        document.getElementById('expenseModal').classList.add('hidden');
        document.getElementById('selectedExpenseId').textContent = "";
        document.getElementById('expenseNameInput').value = "";
        document.getElementById('expenseAmountInput').value = "";
        document.getElementById('expenseDateInput').value = "";
        document.getElementById('expenseCategoryInput').value = "";
        document.getElementById('expenseDescriptionInput').value = "";
    }
    // // Edit Category (prefill form and update)
    // window.editCategory = (id) => {
    //     fetch(`/users/expense-category/update/${id}`)
    //         .then(handleResponse)
    //         .then(category => {
    //             document.getElementById("categoryName").value = category.name;
    //             document.getElementById("categoryDescription").value = category.description || '';
    //             document.getElementById("categoryModal").classList.remove("hidden");
    //
    //
    //         })
    //         .catch(error => console.error(error));
    // };

    // Add Expense
    document.getElementById("expenseForm").addEventListener("submit", (e) => {
        e.preventDefault();
        const id = document.getElementById("selectedExpenseId").textContent;
        const name = document.getElementById("expenseNameInput").value;
        const amount = document.getElementById("expenseAmountInput").value;
        const [year, month, day] = document.getElementById("expenseDateInput").value.toString().split('-');
            const expensesCategoryId = document.getElementById("expenseCategoryInput").value;
        const description = document.getElementById("expenseDescriptionInput").value;

        const date = `${day}-${month}-${year}`;
        console.log(date);
        console.log("id: " + id + " name: " + name + " amount: " + amount + " date: " + date + " categoryId: " + expensesCategoryId + " description: " + description);
        if (!id || id === '') {
            fetch('/users/transaction/expense/add', {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({name, date, expensesCategoryId, amount, description})
            })
                .then(handleResponse)
                .then(() => {
                    document.getElementById("expenseModal").classList.add("hidden");
                    alert('Expense added!');
                    fetchExpenses();
                })
                .catch(error => console.error(error));
        } else {
            fetch(`/users/transaction/expense/update/` + id, {
                method: 'PUT',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({
                    id: id,
                    name: name,
                    amount: amount,
                    date: date,
                    expensesCategoryId: expensesCategoryId,
                    description: description
                })
            })
                .then(handleResponse)
                .then(() => {
                    document.getElementById("expenseModal").classList.add("hidden");
                    alert('Expense updated!');
                    fetchExpenses();
                })
                .catch(error => console.error(error));
        }
        name.value = '';
        amount.value = '';
        expensesCategoryId.value = '';
        description.value = '';
        document.getElementById("expenseDateInput").value = '';
        document.getElementById("selectedExpenseId").textContent = '';
    });

    // Delete Category
    window.deleteCategory = (id) => {
        fetch(`/users/expense-category/delete/${id}`,
            {
                method: 'DELETE'
            })
            .then(handleDeleteResponse)
            .then(() => {
                alert('Category deleted!');
                fetchCategories();
            })
            .catch(error => console.error(error));


    };

    const handleDeleteResponse = async (response) => {
        if (!response.ok) {
            const errorText = await response.text();
            let errorData;
            try {
                errorData = JSON.parse(errorText);
            } catch (e) {
                throw new Error(errorText || "Something went wrong");
            }
            throw new Error(errorData.message || "Something went wrong");
        }
        const responseText = await response.text();
        return responseText ? JSON.parse(responseText) : {};
    };

    // Delete Expense
    window.deleteExpense = (id) => {
        fetch(`/users/expense/delete/${id}`, {method: 'DELETE'})
            .then(handleDeleteResponse)
            .then(() => {
                alert('Expense deleted!');
                fetchExpenses();
            })
            .catch(error => console.error(error));
    };

    // Edit Expense (prefill form and update)
    window.editExpense = (element) => {
        const expenseId = element.parentElement.querySelector("#expenseId").textContent;
        const expenseName = element.parentElement.querySelector("#expenseName").textContent;
        const expenseAmount = element.parentElement.querySelector("#expenseAmount").textContent;
        const expenseDate = element.parentElement.querySelector("#expenseDate").textContent;
        const expenseCategoryId = element.parentElement.querySelector("#expenseCategoryId").textContent;
        const expenseDescription = element.parentElement.querySelector("#expenseDescription").textContent;

        document.getElementById("selectedExpenseId").textContent = expenseId
        document.getElementById("expenseNameInput").value = expenseName
        document.getElementById("expenseAmountInput").value = expenseAmount
        document.getElementById("expenseDateInput").value = expenseDate
        document.getElementById("expenseCategoryInput").value = expenseCategoryId
        document.getElementById("expenseDescriptionInput").value = expenseDescription
        document.getElementById("expenseModal").classList.remove("hidden");


        //
        // fetch(`/expenses/${id}`)
        //     .then(handleResponse)
        //     .then(expense => {
        //         document.getElementById("expenseName").value = expense.name;
        //         document.getElementById("expenseAmount").value = expense.amount;
        //         document.getElementById("expenseDate").value = expense.date;
        //         document.getElementById("expenseCategory").value = expense.getExpenseCategory().id;
        //         document.getElementById("expenseDescription").value = expense.description || '';
        //         document.getElementById("expenseModal").classList.remove("hidden");
        //
        //         document.getElementById("expenseForm").onsubmit = (e) => {
        //             e.preventDefault();
        //             fetch(`/expenses/${id}`, {
        //                 method: 'PUT',
        //                 headers: {'Content-Type': 'application/json'},
        //                 body: JSON.stringify({
        //                     name: document.getElementById("expenseName").value,
        //                     amount: document.getElementById("expenseAmount").value,
        //                     date: document.getElementById("expenseDate").value,
        //                     categoryId: document.getElementById("expenseCategory").value,
        //                     description: document.getElementById("expenseDescription").value
        //                 })
        //             })
        //                 .then(handleResponse)
        //                 .then(() => {
        //                     alert('Expense updated!');
        //                     fetchExpenses();
        //                     document.getElementById("expenseModal").classList.add("hidden");
        //                 })
        //                 .catch(error => console.error(error));
        //         };
        //     })
        //     .catch(error => console.error(error));
    };
});

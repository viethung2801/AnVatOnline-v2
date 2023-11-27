document.addEventListener("DOMContentLoaded", function () {
    const searchProduct = document.getElementById("searchProduct");
    const tableContent = document.getElementById("table-content");
    const suggestionsProductContainer = document.querySelector(
        ".suggestions-product-container"
    );

    searchProduct.addEventListener("input", async function () {
        let inputValue = await this.value;
        let resp = await fetch("http://localhost:8080/api/products/top7?keys=" + inputValue);
        let products = await resp.json();
        suggestionsProductContainer.innerHTML = "";

        if (inputValue.length > 0) {
            products.forEach((product) => {
                const suggestionItem = newSuggestionItem(product);

                suggestionItem.addEventListener("click", async function () {
                    inputValue.value = "";
                    suggestionsProductContainer.style.display = "none";
                    //thêm vào table ở dưới
                    if (await newTableContent(product)) {
                        //create orderDetail
                        let resp = await handleCreateOrderDetail(product)
                        // console.log(resp)
                        await tableContent.appendChild(newTableContent(product,resp.id))
                    }
                    await getTotalPrice();
                });

                suggestionsProductContainer.appendChild(suggestionItem);
            });

            if (products.length > 0) {
                suggestionsProductContainer.style.display = "block";
            } else {
                suggestionsProductContainer.style.display = "none";
            }
        } else {
            suggestionsProductContainer.style.display = "none";
        }
    });

    // Close suggestions when click outside the search input
    document.addEventListener("click", function (event) {
        if (!searchProduct.contains(event.target)) {
            suggestionsProductContainer.style.display = "none";
        }
    });

});

async function update(evt) {
    const trEle = evt.target.parentNode.parentNode.parentNode
    let id = trEle.querySelector('.orderDetailId').value
    let price = trEle.childNodes[5].childNodes[1].childNodes[1].value
    let quantity = trEle.childNodes[7].childNodes[1].childNodes[1].value
    let total = trEle.childNodes[9]
    total.innerHTML = await formatCurrency(quantity * price);
    // await console.log(price)
    await handleUpdateOrderDetail(id,quantity,price,getTotalPrice())
}

function getTotalPrice() {
    const trs = document.getElementsByClassName("order-card")
    let total = 0;
    for (let tr of trs) {
        total += parseInt(tr.childNodes[9].textContent.replaceAll(",", ""))
    }
    document.getElementById("totalPrice").innerHTML = formatCurrency(total)
    document.getElementById("mustPay").innerHTML = formatCurrency(total)
    document.getElementById("giveMoney").value = total

    minusMoney();
}

function minusMoney() {
    let total = parseInt(document.getElementById("totalPrice").textContent.replaceAll(",", ""));
    let giveMoney = parseInt(document.getElementById("giveMoney").value);
    let lostMoney = document.getElementById("lostMoney");

    lostMoney.innerHTML = formatCurrency(giveMoney - total);
    if (giveMoney - total < 0) {
        //disable btn thanh toan
        const btnPay = document.getElementById("btn-pay");
        btnPay.classList.add("disabled")
    } else {
        const btnPay = document.getElementById("btn-pay");
        btnPay.classList.remove("disabled")
    }
}

async function remove(element) {
    const trEle = element.parentNode.parentNode;
    const tableEle = element.parentNode.parentNode.parentNode;
    const orderDetailId = trEle.querySelector(".orderDetailId").value;
    await handleDeleteOrderDetail(orderDetailId)
    await tableEle.removeChild(trEle)
    await getTotalPrice()
}

function newTableContent(product,orderDetailId) {
    const trs = document.getElementsByClassName("order-card")
    let isExists = false;
    for (let tr of trs) {
        if (tr.classList.contains(product.id)) {
            isExists = true;
        }
    }

    if (!isExists) {
        let quantity = 1;
        let content = `
        <td class="col-4 d-flex">
            <img src="/images/${product.imageUrl}" width="64px" alt="">
            <div>${product.name}</div>
        </td>
        <td class="col-1">${formatCurrency(product.price.substring(0, product.price.length - 3))}</td>
        <td class="col-2">
            <div>
                <input
                    type="number"
                    name="price"
                    class="form-control form-control-sm"
                    value="${product.price.substring(0, product.price.length - 3)}"
                    onchange="update(event)"
                />
            </div>
        </td>
        <td class="col-2">
            <div class="d-flex">
                <input type="number" class="col-6" name="quantity" min="1" value="${quantity}" onchange="update(event)">
            </div>
        </td>
        <td class="col-2">${formatCurrency(product.price.substring(0, product.price.length - 3))}</td>
        <td class="col-1">
            <button class="btn btn-outline-danger" onclick="remove(this)">
                <i class="bi bi-trash"></i>
            </button>
        </td>
        <input type="hidden" class="orderDetailId" value="${orderDetailId}">
`;

        const trElement = document.createElement("tr");
        trElement.classList.add("row", product.id, "order-card")
        trElement.innerHTML = content;
        return trElement;
    }
    return null;
}

function newSuggestionItem(product) {
    const suggestionItem = document.createElement("div");
    const suggestionItemImg = document.createElement("img");
    const suggestionItemContent = document.createElement("div");
    const suggestionItemContentName = document.createElement("div");
    const suggestionItemContentCode = document.createElement("div");
    const suggestionItemPrice = document.createElement("div");

    //item
    suggestionItem.classList.add("suggestion-item", "row");

    //IMAGE
    suggestionItemImg.classList.add("col-1");
    suggestionItemImg.src = "/images/" + product.imageUrl;

    //CONTENT
    suggestionItemContent.classList.add("col-9");

    suggestionItemContentName.classList.add("fw-bold");
    suggestionItemContentName.textContent = product.name;

    suggestionItemContentCode.classList.add("text-mute");
    suggestionItemContentCode.textContent = product.code;
    suggestionItemContent.appendChild(suggestionItemContentName);
    suggestionItemContent.appendChild(suggestionItemContentCode);

    //PRICE
    suggestionItemPrice.classList.add("col-2");
    suggestionItemPrice.textContent = formatCurrency(product.price.substring(0, product.price.length - 3));

    //merge
    suggestionItem.appendChild(suggestionItemImg);
    suggestionItem.appendChild(suggestionItemContent);
    suggestionItem.appendChild(suggestionItemPrice);

    return suggestionItem;
}

function formatCurrency(value) {
    value = parseInt(value)
    return value.toLocaleString('en-US');
}

async function handleCreateOrderDetail(product) {

    let data = {
        quantity: 1,
        price: parseInt(product.price),
        priceSale: parseInt(product.price),
        productID: product.id,
        orderID: document.getElementById("orderId").value
    };

    // console.log(data)
    const url = 'http://localhost:8080/api/order-details';
    let resp = await fetch(url, {
        method: 'POST',
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(data)
    });
    return await resp.json();
}

function handleUpdateOrderDetail(id,quantity, price, callback) {

    let data = {
        id,
        quantity:parseInt(quantity),
        priceSale: parseInt(price),
    };
    // console.log(data)
    const url = 'http://localhost:8080/api/order-details';
    fetch(url, {
        method: 'PUT',
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(data)
    }).then(callback)
        .catch(r => console.log(r))
}
function handleDeleteOrderDetail(orderDetailId) {
    const url = 'http://localhost:8080/api/order-details/'+orderDetailId;
    fetch(url, {
        method: 'DELETE',
        headers: {
            "Content-Type": "application/json",
        }
    }).then(resp=>{
        // console.log(resp)
    })
}

//btn
// <button class="btn"
//         onclick="this.parentNode.querySelector('input[type=number]').stepUp()">
//     <i class="bi bi-arrow-up"></i>
// </button>
// <button class="btn "
//         onclick="this.parentNode.querySelector('input[type=number]').stepDown()">
//     <i class="bi bi-arrow-down"></i>
// </button>
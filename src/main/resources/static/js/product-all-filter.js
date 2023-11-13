const productCards = Array.from(document.getElementsByClassName("product-card"));
const productAll = document.getElementById("productAll")

// console.log(productCards)
async function onFilter(value) {
    productAll.innerHTML = ''

    if (value == -1) {
        productCards.forEach(
            (productCard) => productAll.appendChild(productCard)
        )
    }

    if (value == 0) {
        let products = [...productCards];

        products.sort((el1, el2) => {
            const text1 = el1.firstElementChild.lastElementChild.children[0].firstElementChild.textContent.trim().substring(0, 1);
            const text2 = el2.firstElementChild.lastElementChild.children[0].firstElementChild.textContent.trim().substring(0, 1);

            return text1.localeCompare(text2);
        });

        products.forEach((productCard) => productAll.appendChild(productCard));

    }
    if (value == 1) {
        let products = [...productCards];

        products.sort((el1, el2) => {
            const text1 = el1.firstElementChild.lastElementChild.children[0].firstElementChild.textContent.trim().substring(0, 1);
            const text2 = el2.firstElementChild.lastElementChild.children[0].firstElementChild.textContent.trim().substring(0, 1);

            return text2.localeCompare(text1);
        });

        products.forEach((productCard) => productAll.appendChild(productCard));
    }
    if (value == 2) {
        let products = [...productCards];

        products.sort((el1, el2) => {
            const num1 = parseInt(el1.firstElementChild.lastElementChild.children[1].textContent.replaceAll(",", ""));
            const num2 = parseInt(el2.firstElementChild.lastElementChild.children[1].textContent.replaceAll(",", ""));
            return num1 - num2;
        });

        products.forEach((productCard) => productAll.appendChild(productCard));
    }
    if (value == 3) {
        let products = [...productCards];

        products.sort((el1, el2) => {
            const num1 = parseInt(el1.firstElementChild.lastElementChild.children[1].textContent.replaceAll(",", ""));
            const num2 = parseInt(el2.firstElementChild.lastElementChild.children[1].textContent.replaceAll(",", ""));
            return num2 - num1;
        });

        products.forEach((productCard) => productAll.appendChild(productCard));
    }


}

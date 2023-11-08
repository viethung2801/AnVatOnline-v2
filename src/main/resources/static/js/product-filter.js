const productElements = document.getElementsByClassName("product-detail")
const productDetails = Array.from(productElements)

function handleFilter(value) {
    let products = document.getElementById("products")
    products.innerHTML = ''
    if (value === "all"){
        for (const productDetail of productDetails) {
            products.appendChild(productDetail)
        }
    }

    if (value === "active"){
        for (const productDetail of productDetails) {
            if(productDetail.classList.contains("active")){
                products.appendChild(productDetail)
            }
        }
    }
    if (value === "inActive"){
        for (const productDetail of productDetails) {
            if(productDetail.classList.contains("inActive")){
                products.appendChild(productDetail)
            }
        }
    }
}
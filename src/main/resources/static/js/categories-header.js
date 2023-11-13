async function getCategory() {
    var categoryElement = document.getElementById("categoryElement")
    const response = (await fetch("http://localhost:8080/api/categories"))
    const categories = await response.json()
    await createElement(categories);
}

getCategory();

function createElement(categories) {
    categories.forEach((category) => {
        // <li><a className="dropdown-item" href="#">Another action</a></li>
        const liElement = document.createElement("li");
        const aElemet = document.createElement("a");
        aElemet.className = 'dropdown-item'
        aElemet.href = '/product-all?category=' + category.code
        aElemet.textContent = category.name
        liElement.appendChild(aElemet)

        categoryElement.appendChild(liElement)
    })
}

async function findFilter() {
    const response = (await fetch("http://localhost:8080/api/categories"))
    const categories = await response.json()
    await console.log(categories)
    await showFilter(categories);
}

findFilter();

function showFilter(categories) {
    let categoryElement = document.getElementById("categories")

    categories.forEach((category) => {
        const divElement = document.createElement("div");
        divElement.className='form-check'

        const inputElement = document.createElement("input");
        inputElement.className='form-check-input'
        inputElement.type='checkbox'
        inputElement.value=category.code
        inputElement.name = 'category'

        const labelElement = document.createElement("label");
        labelElement.className='form-check-label'
        labelElement.textContent=category.name

        divElement.appendChild(inputElement)
        divElement.appendChild(labelElement)

        categoryElement.appendChild(divElement)
    })
}
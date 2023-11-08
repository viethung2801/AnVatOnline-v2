const userElements = document.getElementsByClassName("user-detail")
const userDetails = Array.from(userElements)

function handleUserFilter(value) {
    let users = document.getElementById("users")
    users.innerHTML = ''
    if (value === 'all') {
        for (const userDetail of userDetails) {
            users.appendChild(userDetail)
        }
    }
    if (value === 'customer') {
        for (const userDetail of userDetails) {
            if (userDetail.classList.contains("customer")) {
                users.appendChild(userDetail)
            }
        }
    }
    if (value === 'admin') {
        for (const userDetail of userDetails) {
            if (userDetail.classList.contains("admin")) {
                users.appendChild(userDetail)
            }
        }
    }
}
document.addEventListener("DOMContentLoaded", function () {
    const searchUser = document.getElementById("searchUser");
    const suggestionsUserContainer = document.querySelector(
        ".suggestions-user-container"
    );


    searchUser.addEventListener("input", async function () {
        const inputValue = this.value;
        let response = await fetch("http://localhost:8080/api/users/top7?keys=" + inputValue);
        let users = await response.json();
        suggestionsUserContainer.innerHTML = "";

        if (inputValue.length > 0) {
            users.forEach((user) => {
                const suggestionItemContent = document.createElement("div");
                const suggestionItemContentName = document.createElement("div");
                const suggestionItemContentCode = document.createElement("div");

                //item
                suggestionItemContent.classList.add("suggestion-item");

                //CONTENT
                suggestionItemContentName.classList.add("fw-bold");
                suggestionItemContentName.textContent = user.lastName + ' ' + user.firstName;

                suggestionItemContentCode.classList.add("text-mute");
                suggestionItemContentCode.textContent = user.phoneNumber;
                suggestionItemContent.appendChild(suggestionItemContentName);
                suggestionItemContent.appendChild(suggestionItemContentCode);

                suggestionItemContent.addEventListener("click", async function () {
                    // searchUser.value = user.lastName + ' ' +user.firstName;
                    suggestionsUserContainer.style.display = "none";

                    //set value
                    const userId =await document.getElementById("userId")
                     userId.value = user.id

                    const userFullName =await document.getElementById("userFullName")
                    userFullName.textContent = user.lastName + ' ' + user.firstName

                    const userPhoneNumber =await document.getElementById("userPhoneNumber")
                    userPhoneNumber.textContent = user.phoneNumber

                    await handleChangeCustomer(user.id);
                });

                suggestionsUserContainer.appendChild(suggestionItemContent);
            });

            if (users.length > 0) {
                suggestionsUserContainer.style.display = "block";
            } else {
                suggestionsUserContainer.style.display = "none";
            }
        } else {
            suggestionsUserContainer.style.display = "none";
        }
    });

    // Close suggestions when clicking outside the search input
    document.addEventListener("click", function (event) {
        if (!searchUser.contains(event.target)) {
            suggestionsUserContainer.style.display = "none";
        }
    });
});

function clearUser() {
    const userId = document.getElementById("userId")
    const userPhoneNumber = document.getElementById("userPhoneNumber")
    const userFullName = document.getElementById("userFullName")
    const searchUser = document.getElementById("searchUser");

    handleChangeCustomer(null);

    userPhoneNumber.textContent = ''
    userFullName.textContent = 'Khách lẻ'
    userId.value = ''
    searchUser.value = ''

}

function handleChangeCustomer(userId) {
    let data = {
        orderId: document.getElementById("orderId").value,
        userId
    }
    const url = 'http://localhost:8080/api/orders';
    fetch(url, {
        method: 'PUT',
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(data)
    }).then()
}
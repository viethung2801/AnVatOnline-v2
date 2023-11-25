document.addEventListener("DOMContentLoaded", function () {
    const searchUser = document.getElementById("searchUser");
    const suggestionsUserContainer = document.querySelector(
        ".suggestions-user-container"
    );

    // Mock data for suggestions (replace with your own data)
    const suggestions = ["Apple", "Banana", "Orange", "Mango", "Grapes"];

    searchUser.addEventListener("input", function () {
        const inputValue = this.value.trim().toLowerCase();
        suggestionsUserContainer.innerHTML = "";

        if (inputValue.length > 0) {
            const matchingSuggestions = suggestions.filter((suggestion) =>
                suggestion.toLowerCase().includes(inputValue)
            );

            matchingSuggestions.forEach((suggestion) => {
                const suggestionItemContent = document.createElement("div");
                const suggestionItemContentName = document.createElement("div");
                const suggestionItemContentCode = document.createElement("div");

                //item
                suggestionItemContent.classList.add("suggestion-item");

                //CONTENT
                suggestionItemContentName.classList.add("fw-bold");
                suggestionItemContentName.textContent = suggestion;

                suggestionItemContentCode.classList.add("text-mute");
                suggestionItemContentCode.textContent = "SP100";
                suggestionItemContent.appendChild(suggestionItemContentName);
                suggestionItemContent.appendChild(suggestionItemContentCode);

                suggestionItemContent.addEventListener("click", function () {
                    searchUser.value = suggestion;
                    suggestionsUserContainer.style.display = "none";
                });

                suggestionsUserContainer.appendChild(suggestionItemContent);
            });

            if (matchingSuggestions.length > 0) {
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
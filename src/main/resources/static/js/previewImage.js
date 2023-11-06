var imageUrl = document.getElementById("imageUrl")

function displayImage(input) {
    if (input.files && input.files[0]) {
        var reader = new FileReader();
        reader.onload = (e) => {

            var imagePreview = document.getElementById("imagePreview")
            imagePreview.src = e.target.result
        }

        reader.readAsDataURL(input.files[0])
    }
}
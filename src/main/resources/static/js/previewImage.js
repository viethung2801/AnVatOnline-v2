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

function displayImageProduct(input) {
    var imagePreview = document.getElementById("imagePreview")
    imagePreview.innerHTML = ""
    for (let i = 0; i < 3; i++) {
        if (input.files && input.files[i]){
            var reader = new FileReader();
            reader.onload = (e)=>{
                var element = document.createElement("img");
                element.className = "col-2 p-1";
                element.src = e.target.result;
                element.alt = "";
                imagePreview.appendChild(element);
            }
            reader.readAsDataURL(event.target.files[i]);
        }
    }
}
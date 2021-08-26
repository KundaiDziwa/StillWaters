/* ----- Progress Circle ------ */
function showProgress(status) {
    var currentStatus = document.getElementById('info');
    currentStatus.innerHTML = status;
}
function progressReset() {
    var currentStatus = document.getElementById('info');
    currentStatus.innerHTML = "Progress";
}

/* -----Products cards ----- */
//Global variables
var cardIDs = ["BC_C", "BC_CP", "G_C", "G_CP", "C_PE", "A_CP"];
var brands = ["BellaCanvas", "BellaCanvas", "Gildan", "Gildan", "Cricut", "ATC"];
var materials = ["C100%", "C52%, P48%", "C100%", "C90%, C10%", "P95%, E5%", "C50%, P50%"];

/* ----- Dynamically creating the Product cards ----- */
function createCards() {
    var dynamic = document.querySelector('.products');
    for (var i = 0; i < cardIDs.length; i++) {
        var fetch = document.querySelector('.products').innerHTML;
        dynamic.innerHTML = `
        <div class="p-container">
            <div class="p-card" id="${cardIDs[i]}-card">
                <div class="imgBx">
                    <img src="/Images/Products/ran-white_shirt.png">
                    <h2 id="clothingInfo">${brands[i]} - ${materials[i]}</h2>
                </div>
                <div class="p-content">
                    <div class="p-size">
                        <h3>Size: </h3>
                        <div class="radio1">
                            <input class="p-size-input ${cardIDs[i]}-size-input" type="radio" value="xs" name="${cardIDs[i]}size" id="${cardIDs[i]}sizexs">
                            <label class="p-size-label ${cardIDs[i]}-size-label" for="${cardIDs[i]}sizexs">xs</label>
                            <input class="p-size-input ${cardIDs[i]}-size-input" type="radio" value="S" name="${cardIDs[i]}size" id="${cardIDs[i]}sizeS">
                            <label class="p-size-label ${cardIDs[i]}-size-label" for="${cardIDs[i]}sizeS">S</label>
                            <input class="p-size-input ${cardIDs[i]}-size-input" type="radio" value="M" name="${cardIDs[i]}size" id="${cardIDs[i]}sizeM">
                            <label class="p-size-label ${cardIDs[i]}-size-label" for="${cardIDs[i]}sizeM">M</label>
                            <input class="p-size-input ${cardIDs[i]}-size-input" type="radio" value="L" name="${cardIDs[i]}size" id="${cardIDs[i]}sizeL">
                            <label class="p-size-label ${cardIDs[i]}-size-label" for="${cardIDs[i]}sizeL">L</label>
                            <input class="p-size-input ${cardIDs[i]}-size-input" type="radio" value="XL" name="${cardIDs[i]}size" id="${cardIDs[i]}sizeXL">
                            <label class="p-size-label ${cardIDs[i]}-size-label" for="${cardIDs[i]}sizeXL">XL</label>
                            <input class="p-size-input ${cardIDs[i]}-size-input" type="radio" value="2XL" name="${cardIDs[i]}size" id="${cardIDs[i]}size2XL">
                            <label class="p-size-label ${cardIDs[i]}-size-label" for="${cardIDs[i]}size2XL">2XL</label>
                        </div>
                    </div>
                    <div class="p-color">
                        <h3>Colour: </h3>
                        <div class="radio2">
                            <input class="p-color-input color-input1 ${cardIDs[i]}color-input1" type="radio" value="White" name="${cardIDs[i]}color" id="${cardIDs[i]}colorW">
                            <label class="p-color-label color-label1 ${cardIDs[i]}color-label1" for="${cardIDs[i]}colorW"></label> 
                            <input class="p-color-input color-input2 ${cardIDs[i]}color-input2" type="radio" value="Grey" name="${cardIDs[i]}color" id="${cardIDs[i]}colorG">
                            <label class="p-color-label color-label2 ${cardIDs[i]}color-label2" for="${cardIDs[i]}colorG"></label> 
                            <input class="p-color-input color-input3 ${cardIDs[i]}color-input3" type="radio" value="CBlack" name="${cardIDs[i]}color" id="${cardIDs[i]}colorB">
                            <label class="p-color-label color-label3 ${cardIDs[i]}color-label3" for="${cardIDs[i]}colorB"></label> 
                            <input class="p-color-input color-input4 ${cardIDs[i]}color-input4" type="radio" value="Red" name="${cardIDs[i]}color" id="${cardIDs[i]}colorR">
                            <label class="p-color-label color-label4 ${cardIDs[i]}color-label4" for="${cardIDs[i]}colorR"></label> 
                        </div>
                    </div>
                    <label class="quantity-label" for="quantity"></label>
                    <input type="text" id="${cardIDs[i]}quantity" placeholder="Quantity" required>
                    <br>
                    <input type="submit" value="Add to Cart" onclick="collectData('${brands[i]}', '${materials[i]}', '${cardIDs[i]}');">
                </div>
            </div>
        </div>` + fetch;
    }
}
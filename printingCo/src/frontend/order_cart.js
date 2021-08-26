/* ----- Order Type ----- */
function storeType() {
    var tSelect = document.getElementById("orderType");
    var type = tSelect.value;
    sessionStorage.setItem("order_type", type);
}

/* ----- Files and Description ------ */
function storeDes() {
    var description = document.getElementByID("description").value;
    var filenameDes = document.getElementByID("designFile").value;
    sessionStorage.setItem("design_file", filenameDes);
    sessionStorage.setItem(filenameDes, description);
}


/* ------------------------------ */
/* ----- Cart Functionality ----- */
/* ------------------------------ */

/* ------ collecting data ------- */
// Global Variables

// ----- Order object ----- //
// Size
// Color
// Quantity
// Brand
// Material
function collectData(cardBrand, cardMaterial, partialID) { // Get info of clothing types as parameters
    let key = partialID + addNum(partialID);
    let order = {
        size: getSize(partialID),
        color: getColor(partialID),
        quantity: getQuantity(partialID),
        brand: cardBrand,
        material: getMaterial(cardMaterial),
        cost: "20"
    };
    // Adding order to localStorage
    fillStorage(key, order);
    storeKeyIDs(key);
    // Increment the cart value 
    updateCartSize();
    // makeCartOrder("order", partialID, getNum(partialID));
    addToCart();
}
function getSize(sizeID) {
    var fullID = sizeID + "size";
    var ele = document.getElementsByName(fullID);
    for (i = 0; i < ele.length; i++) {
        if(ele[i].checked)
        return ele[i].value;
    }
    return "N/A";
}
function getColor(colorID) {
    var fullID = colorID + "color";
    var ele = document.getElementsByName(fullID);
    for (i = 0; i < ele.length; i++) {
        if(ele[i].checked)
        return ele[i].value;
    }
    return "N/A";
}
function getMaterial(mtrl) {
    var pMaterial = mtrl.replace(/%/g, '');
    return pMaterial;
}
function getQuantity(qName) {
    var newQName = qName + "quantity"
    var qnty = document.getElementById(newQName).value;
    return qnty;
}

/* ------ Add to Cart pop up and display ------ */
function fillStorage(item, orderObj) {
    let myObj_serialized = JSON.stringify(orderObj);
    localStorage.setItem(item, myObj_serialized);
}
function getOrderItem(item) {
    let myObj_deserialized = JSON.parse(localStorage.getItem(item));
    return myObj_deserialized;
}
function makeCartOrder(titles, details, size, findIDs) {
    var dynamic = document.querySelector('.order-container');
    for (var i = 0; i < size; i++) {
        var fetch = document.querySelector('.order-container').innerHTML;
        dynamic.innerHTML = `
        <div class="order">
            <img src="/Images/Background/hotelT.jpg">
            <div class="order-content">
                <h2 id="order-title-${fullIDs[i]}">${titles[i]}</h2>
                <p id="order-details-${fullIDs[i]}">${details[i]}</p>
            </div>
            <button class="deleteBtn" id="deleteItem-${fullIDs[i]}" onclick="deleteItem('${findIDs[i]}');">Remove</button>
        </div>` + fetch;
    }
}
function addToCart() {
    var cartTitles = [];
    var cartDetails = [];
    var fullIDs = createIDArray();
    var divider = " - ";
    var cSize = fullIDs.length;
    var orders = [];
    // Populate the orders array //
    for (i = 0; i <= cSize; i++) {
        let order = localStorage.getItem(fullIDs[i]);
        orders.push(order);
    }
    for (i = 0; i < orders.length; i++) {
        cartTitles.push(orders[i].brand + divider + orders[i].material) + divider + "Price: " + orders[i].cost;
        cartDetails.push("Size: " + orders[i].size + divider + "Colour: " + orders[i].color + divider + "Quantity: " + orders[i].quantity);
    }
    makeCartOrder(cartTitles, cartDetails, orders.length, fullIDs);
}
function deleteItem(fullID) {
    localStorage.removeItem(fullID);
    delCartSize();
    cancelCartItem(fullID);
}
function cancelCartItem(identifier) {
    let fullTitleID = "order-title-" + identifier;
    let fullDetailID = "order-details-" + identifier;
    document.getElementById(fullTitleID).innerHTML = "XXXXXXXXXXXXXXXXXXXXXX";
    document.getElementById(fullDetailID).innerHTML = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";
}

/* ----- Helper functions ----- */
function updateCartSize() {
    let cartSize = localStorage.getItem("cart_size");
    cartSize++;
    localStorage.setItem("cart_size", cartSize);
    document.getElementById("cartSize").innerHTML = cartSize;
}
function delCartSize() {
    let cartSize = localStorage.getItem("cart_size");
    cartSize--;
    localStorage.setItem("cart_size", cartSize);
    document.getElementById("cartSize").innerHTML = cartSize;
}
function getNum(keyID) {
    let keyNum = keyID + "num";
    let num = localStorage.getItem(keyNum);
    return num;
}
function addNum(keyID) {
    let keyNum = keyID + "num";
    let num = localStorage.getItem(keyNum);
    if (num == null) {
        localStorage.setItem(keyNum, "0");
        return num;
    }
    num++;
    localStorage.setItem(keyNum, num);
    return num;
}
function storeKeyIDs(newKeyID) {
    let ids = localStorage.getItem("keyIDs");
    if (ids == null) {
        localStorage.setItem("keyIDs", newKeyID);
        return;
    }
    let finalInput = ids + "-" + newKeyID;
    localStorage.setItem("keyIDs", finalInput);
}
function createIDArray() {
    let idString = localStorage.getItem("keyIDs");
    let idArray = idString.split("-");
    return idArray;
}
function setUpPage() {
    let cSize = localStorage.getItem("cart_size");
    if (cSize == null) {
        localStorage.setItem("cart_size", "0");
        cSize = 0;
    }
    window.onload = (event) => {
        document.getElementById("cartSize").innerHTML = cartSize;
    }; 
}
function clearStorage() {
    localStorage.clear();
}

/* ----- Pricing ----- */
function updateTotal(cost) {
    let totalP = localStorage.getItem("Subtotal");
    if (totalP == null) {
        localStorage.setItem("Subtotal", cost);
        return;
    }
    totalP += cost;
    localStorage.setItem("Subtotal", totalP);
}


/* -------------------------------------- */
/* ----- Confirm Page Functionality ----- */
/* -------------------------------------- */

/* ----- Generating HTML ----- */
function createShipConfirm() {
    let shipObject = JSON.parse(sessionStorage.getItem("ShippingInfo"));
    if (shipObject == null) {return;}
    document.getElementById("name").innerHTML = "Name: " + shipObject.FName + " " + shipObject.LName;
    document.getElementById("address").innerHTML = "Address: " + shipObject.Address + ", " + shipObject.city + ", " + shipObject.Country + ", " + shipObject.PostalCode;
    document.getElementByID("pNumber").innerHTML = "Phone Number: " + shipObject.PhoneNumber;
    document.getElementById("email").innerHTML = "Email: " + shipObject.email;
}
function createPayConfirm() {
    let cardObject = JSON.parse(sessionStorage.getItem("CardInfo"));
    if (cardObject == null) {return;}
    // Useful Variable
    let cNum = cardObject.number.substring(12);

    document.getElementById("cNumber").innerHTML = "XXXX XXXX XXXX " + cNum;
    document.getElementById("exDate").innerHTML = "MM/YY" + `<br>` + cardObject.exMonth + "/" + cardObject.exYear;
    document.getElementById("cHName").innerHTML = cardObject.name;
    document.getElementById("ccv").innerHTML = cardObject.cvv;
}
function createOrderConfirm() {
    var clothing = [];
    var descriptions = [];
    var fullIDs = createIDArray();
    var divider = "/";
    var cSize = fullIDs.length;
    var orders = [];
    // Populate the orders array //
    for (i = 0; i <= cSize; i++) {
        let order = localStorage.getItem(fullIDs[i]);
        orders.push(order);
    }
    for (i = 0; i < orders.length; i++) {
        clothing.push(orders[i].brand + divider + orders[i].material + divider + "Size: " + orders[i].size + divider + "Colour: " + orders[i].color + divider + "Quantity: " + orders[i].quantity);
    }
    var dynamic = document.querySelector('.confirm-orders');
    for (var i = 0; i < cSize; i++) {
        var fetch = document.querySelector('.confirm-orders').innerHTML;
        dynamic.innerHTML = `
        <h3 id="order-${fullIDs[i]}">${clothing[i]}</h2>
        <br><br>` + fetch;
    }

    /* Order Description */
    var filename = sessionStorage.getItem("design_file");
    var orderDes = localStorage.getItem(filename);
    var dynamicDes = document.querySelector('.confirm-orders');
    dynamicDes.innerHTML = `
    <br><hr><br>
    <p class="order-description">Order Descrition: ${orderDes}</p>`;
}
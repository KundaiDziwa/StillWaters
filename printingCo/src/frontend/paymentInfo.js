/* ----- Website functionality ----- */
function payProgress(complete) {
    const progress = document.querySelector('.payment-progress-done');
    progress.setAttribute('data-done', complete);
    progress.style.width = complete + '%';
    progress.innerHTML = complete + '%';
    progress.style.opacity = 1;
}
function showBtn(btnID) {
    document.getElementById(btnID).style.display = "inline-block";
}

/* ----- Saving information ----- */
function saveShipData() {
    let Shipping = {
        FName: document.getElementById("sFirstNme").value,
        LName: document.getElementById("sLastName").value,
        Address: document.getElementById("sAddress").value,
        City: document.getElementById("sCity").value,
        Province: document.getElementById("sProvince").value,
        Country: document.getElementById("sCountry").value,
        PostalCode: document.getElementById("sPostalCode").value, 
        PhoneNumber: formatPNumber(document.getElementById("sPNumber").value),
        email: document.getElementById("sEmail").value
    };
    localStorage.setItem("ShippingInfo", JSON.stringify(Shipping));
}
function saveBillData() {
    let Billing = {
        FName: document.getElementById("bFirstNme").value,
        LName: document.getElementById("bLastName").value,
        Address: document.getElementById("bAddress").value,
        City: document.getElementById("bCity").value,
        Province: document.getElementById("bProvince").value,
        Country: document.getElementById("bCountry").value,
        PostalCode: document.getElementById("bPostalCode").value, 
        PhoneNumber: formatPNumber(document.getElementById("bPNumber").value),
        email: document.getElementById("bEmail").value
    };
    localStorage.setItem("BillingInfo", JSON.stringify(Billing));
}
function storePayData() {
    let Card = {
        name: document.getElementById("cardName").value,
        number: document.getElementById("CNumber").value,
        exMonth: document.getElementById("ExpiryMonth").value,
        exYear: document.getElementById("ExpiryYear").value,
        cvv: document.getElementById("code").value
    };
    sessionStorage.setItem("CardInfo", JSON.stringify(Card));
}
function storeShip() {
    let Shipping = {
        FName: document.getElementById("sFirstNme").value,
        LName: document.getElementById("sLastName").value,
        Address: document.getElementById("sAddress").value,
        City: document.getElementById("sCity").value,
        Province: document.getElementById("sProvince").value,
        Country: document.getElementById("sCountry").value,
        PostalCode: document.getElementById("sPostalCode").value, 
        PhoneNumber: formatPNumber(document.getElementById("sPNumber").value),
        email: document.getElementById("sEmail").value
    };
    sessionStorage.setItem("ShippingInfo", JSON.stringify(Shipping));
}
function storeBill() {
    let Billing = {
        FName: document.getElementById("bFirstNme").value,
        LName: document.getElementById("bLastName").value,
        Address: document.getElementById("bAddress").value,
        City: document.getElementById("bCity").value,
        Province: document.getElementById("bProvince").value,
        Country: document.getElementById("bCountry").value,
        PostalCode: document.getElementById("bPostalCode").value, 
        PhoneNumber: formatPNumber(document.getElementById("bPNumber").value),
        email: document.getElementById("bEmail").value
    };
    sessionStorage.setItem("BillingInfo", JSON.stringify(Billing));
}
function copyShip() {
    let shippingObj = JSON.parse(sessionStorage.getItem("shippingInfo"));
    sessionStorage.setItem("BillingInfo", JSON.stringify(shippingObj));
}
function removeShip() {
    localStorage.removeItem("shippingInfo");
}
function removeBill() {
    localStorage.removeItem("BillingInfo");
}

/* ----- Subtotal and Pricing ----- */
function getSubtotal() {
    let subTotal = localStorage.getItem("SUbtotal");
    return subTotal;
}
function showTotal() {
    document.getElementById("total").innerHTML = "Subtotal: CAD$ " + getSubtotal();
}

/* ----- Helper Functions ----- */
function formatPNumber(inNum) {
    matches = inNum.match(/\d+/g);
    var properNum = "";
    var formattedNum = "(";
    for (var i = 0; i < matches.length; i++) {
        properNum += matches[i];
    }
    if (properNum.length == 10) {
        formattedNum += properNum.substring(0, 3) + ")" + properNum.substring(3, 6) + "-" + properNum.substring(6);
        return formattedNum;
    }
    return properNum;
}
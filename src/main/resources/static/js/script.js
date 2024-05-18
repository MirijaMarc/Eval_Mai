function formatDate(dateString) {
    var dateString = dateString;

    var date = new Date(dateString);

    var options = { 
        year: 'numeric', 
        month: 'long', 
        day: 'numeric' 
    };

    var formatter = new Intl.DateTimeFormat('fr-FR', options);

    return formatter.format(date);
}

function formatNumber(number) {
    var string = numeral(parseFloat(number)).format('0,0.00');
    return string
}

const colDates = document.querySelectorAll(".colDate");
colDates.forEach(element => {
    element.innerHTML = formatDate(element.textContent)
});

const colNumber = document.querySelectorAll(".colNumber");
colNumber.forEach(element => {
    console.log("hello");
    element.innerHTML = formatNumber(element.textContent)
});